package com.permoji.api;

/**
 * Created by michael on 22/05/18.
 */

public interface BaseCallback<T> {

    void onSuccess(T response);

    void onError();
}
