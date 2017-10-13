package com.iotai.alexaclient.http;

import okhttp3.OkHttpClient;

/**
 * Created by zhangjf9 on 2017/10/13.
 */

class OkHttpClientFactory {
    private static OkHttpClient mOkHttpClient;

    public static OkHttpClient getOkHttpClient() {
        if (mOkHttpClient == null)
            mOkHttpClient = new OkHttpClient();
        return mOkHttpClient;
    }
}
