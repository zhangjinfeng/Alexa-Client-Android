package com.iotai.alexaclient;

/**
 * Created by zhangjf9 on 2017/10/10.
 */

public interface AmazonLoginEngineListener {
    void onLogin();
    void onLoginCancel();
    void onSignOut();
    void onTokenReady(String token);
    void onError(String errMessage);
}
