package com.iotai.alexaclient.http;

import java.util.concurrent.TimeUnit;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;

/**
 * Created by zhangjf9 on 2017/10/13.
 */

public class OkHttpClientFactory {
    private static OkHttpClient mOkHttpClient;

    private static final int CONNECTION_POOL_MAX_IDLE_CONNECTIONS = 5;
    private static final long CONNECTION_POOL_TIMEOUT = 60 * 60 * 1000; //Unit: milliseconds

    public static OkHttpClient getOkHttpClient() {
        if (mOkHttpClient == null) {
            ConnectionPool connectionPool = new ConnectionPool(
                    CONNECTION_POOL_MAX_IDLE_CONNECTIONS,
                    CONNECTION_POOL_TIMEOUT,
                    TimeUnit.MILLISECONDS);


            OkHttpClient.Builder clientBuilder = new OkHttpClient().newBuilder()
                    .connectTimeout(0, TimeUnit.MILLISECONDS)
                    .readTimeout(0, TimeUnit.MILLISECONDS)
                    .writeTimeout(0, TimeUnit.MILLISECONDS)
                    .connectionPool(connectionPool);

            mOkHttpClient = clientBuilder.build();
        }
        return mOkHttpClient;
    }
}
