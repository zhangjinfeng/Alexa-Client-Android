package com.iotai.alexaclient.login;

/**
 * Created by zhangjf9 on 2017/10/10.
 */

public interface TokenManagerListener {
    void onSuccess(String token);
    void onError(String errMessage);
}
