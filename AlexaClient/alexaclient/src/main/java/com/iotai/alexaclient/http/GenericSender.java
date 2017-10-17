package com.iotai.alexaclient.http;

import com.iotai.alexaclient.message.Event;

import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by zhangjf9 on 2017/10/16.
 */

public class GenericSender {
    private static GenericSender mInstance = null;
    private String mAccessToken = "";

    public GenericSender()
    {

    }

    public static GenericSender getInstance() {
        synchronized (GenericSender.class) {
            if (mInstance == null) {
                mInstance = new GenericSender();
            }
        }

        return mInstance;
    }

    public boolean initialize(String accessToken)
    {
        mAccessToken = accessToken;
        return true;
    }

    public void release()
    {

    }

    public void sendEvent(Event event) {
        OkHttpClient httpClient = OkHttpClientFactory.getOkHttpClient().newBuilder()
                .connectTimeout(0, TimeUnit.MILLISECONDS)
                .readTimeout(0, TimeUnit.MILLISECONDS)
                .writeTimeout(0, TimeUnit.MILLISECONDS)
                .build();

        final Request request = new Request.Builder()
                .url(URLConstants.ALEXA_EVENTS_URL)
                .get()
                .addHeader("authorization", "Bearer "+ mAccessToken)
                .build();

        Call call = httpClient.newCall(request);
        try {
            Response response = call.execute();
        } catch (Exception e)
        {

        }
    }
}
