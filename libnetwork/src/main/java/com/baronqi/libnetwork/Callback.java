package com.baronqi.libnetwork;

public interface Callback<T> {

    void onSuccess(Result<T> result);

    void onFailure(int code);

}
