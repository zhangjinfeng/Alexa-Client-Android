package com.iotai.alexaclient.login;

/**
 * Created by zhangjf9 on 2017/10/10.
 */

public interface LoginEngineListener {
    void onLogin();
    void onLoginCancel();
    void onSignOut();
    void onTokenReady(String token);
    void onError(String errMessage);
}
