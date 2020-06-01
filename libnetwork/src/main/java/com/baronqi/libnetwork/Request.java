package com.baronqi.libnetwork;

public class Request<T, R extends Request> {

    private String mUrl;

    public Request(String url) {
        mUrl = url;
        getClass().getGenericSuperclass();
    }
}
