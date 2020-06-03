package com.baronqi.libnetwork;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Joiner;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;

public class HttpWrapper {

    private static final OkHttpClient client;

    static {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        client = new OkHttpClient.Builder()
                .readTimeout(5, TimeUnit.SECONDS)
                .build();
        TrustManager[] manager = new TrustManager[]{
                new X509TrustManager() {

                    @Override
                    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                    }

                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }
                }
        };

        try {
            SSLContext ssl = SSLContext.getInstance("SSL");
            ssl.init(null, manager, new SecureRandom());

            HttpsURLConnection.setDefaultSSLSocketFactory(ssl.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }
    }

    public static Builder of(String url) {
        return new Builder(url);
    }

    public static class Builder {

        private String url;

        private Map<String, String> params;

        private List<MultipartBody.Part> uploadParts;

        Request.Builder reqBuilder;

        Builder(String url) {
            this.url = url;
            params = new HashMap<>();
            uploadParts = new ArrayList<>();

            reqBuilder = new Request.Builder();


            // 默认添加上user-agent
//            addHeader("User-Agent", DEFAULT_USER_AGENT);
        }


        // 添加参数
        public Builder addParam(String key, String value) {
            params.put(key, value);
            return this;
        }


        // 添加头
        public Builder addHeader(String key, String value) {
            reqBuilder.addHeader(key, value);
            return this;
        }


        public Builder file(String key, String fileName, String fileMime, byte[] bytes) {
            MultipartBody.Part part = MultipartBody.Part.createFormData(
                    key,
                    fileName,
                    RequestBody.create(MediaType.parse(fileMime), bytes));
            uploadParts.add(part);
            return this;
        }

        public Builder file(String key, String fileName, String fileMime, File file) {
            MultipartBody.Part part = MultipartBody.Part.createFormData(
                    key,
                    fileName,
                    RequestBody.create(MediaType.parse(fileMime), file));
            uploadParts.add(part);
            return this;
        }

        public Builder file(String key, String fileName, String fileMime, InputStream stream) throws IOException {
            int size = stream.available();
            byte[] bytes = new byte[size];
            stream.read(bytes);
            return file(key, fileName, fileMime, bytes);
        }

        /**
         * 发送get请求
         *
         * @return
         * @throws IOException
         */
        public <T> Result<T> get(Type type) throws IOException {
            StringBuilder urlBuilder = new StringBuilder(url);
            if (!params.isEmpty()) {
                urlBuilder.append("?").append(Joiner.on('&').withKeyValueSeparator('=').join(params));
            }
            Response response = client.newCall(reqBuilder.url(urlBuilder.toString()).build()).execute();

            return parseResponse(response, type);
        }

        /**
         * 发送get请求
         *
         * @return
         * @throws IOException
         */
        public <T> void get(Callback<T> callback) throws IOException {
            StringBuilder urlBuilder = new StringBuilder(url);
            if (!params.isEmpty()) {
                urlBuilder.append("?").append(Joiner.on('&').withKeyValueSeparator('=').join(params));
            }
            client.newCall(reqBuilder.url(urlBuilder.toString()).build()).enqueue(new okhttp3.Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {

                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    Result<T> result = parseResponse(response, callback);

                    if (result.success) {
                        callback.onSuccess(result);
                    } else {
                        callback.onFailure(100);
                    }
                }
            });
        }

        private <T> Result<T> parseResponse(Response response, Type type) {
            int status = response.code();
            boolean success = response.isSuccessful();
            String message = response.message();
            Result<T> result = new Result<>();

            try {
                String body = response.body().string();
                result.body = JSON.parseObject(body, type);
            } catch (IOException e) {
                message = e.getMessage();
                success = false;
                status = 0;
            }

            result.status = status;
            result.success = success;
            result.message = message;
            return result;

        }

        private <T> Result<T> parseResponse(Response response, Callback<T> callback) {
            ParameterizedType parameterizedType = (ParameterizedType) callback.getClass().getGenericSuperclass();
            Type actualTypeArguments = parameterizedType.getActualTypeArguments()[0];
            return parseResponse(response, actualTypeArguments);
        }

        /**
         * post表单数据
         *
         * @return
         */
        @RequiresApi(api = Build.VERSION_CODES.N)
        public Response post() throws IOException {
            // 创建表单
            FormBody.Builder formBodyBuilder = new FormBody.Builder();
            if (!params.isEmpty()) {
                params.forEach(formBodyBuilder::add);
            }

            return client.newCall(reqBuilder.url(url)
                    .post(formBodyBuilder.build())
                    .build())
                    .execute();
        }


        /**
         * 文件上传
         *
         * @return
         * @throws IOException
         */
        @RequiresApi(api = Build.VERSION_CODES.N)
        public Response upload() throws IOException {
            MultipartBody.Builder bodyBuilder = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM);
            uploadParts.forEach(bodyBuilder::addPart);

            // 添加参数
            params.forEach(bodyBuilder::addFormDataPart);

            return client.newCall(reqBuilder.url(url)
                    .post(bodyBuilder.build())
                    .build())
                    .execute();
        }
    }
}