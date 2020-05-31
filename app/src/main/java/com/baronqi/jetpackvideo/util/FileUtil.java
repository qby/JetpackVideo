package com.baronqi.jetpackvideo.util;

import android.content.res.AssetManager;

import com.baronqi.jetpackvideo.AppGlobals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FileUtil {

    public static String parseAssetsFile(String fileName) {

        AssetManager assets = AppGlobals.getApplication().getResources().getAssets();
        InputStream open = null;
        BufferedReader bufferedReader = null;
        StringBuilder sb = new StringBuilder();
        try {
            open = assets.open(fileName);

            bufferedReader = new BufferedReader(new InputStreamReader(open));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (open != null) {
                try {
                    open.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();
    }

}
