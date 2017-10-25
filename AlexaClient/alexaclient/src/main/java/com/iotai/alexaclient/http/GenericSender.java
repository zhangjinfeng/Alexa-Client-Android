package com.iotai.alexaclient.http;

import com.iotai.alexaclient.message.Event;

import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by zhangjf9 on 2017/10/16.
 */

public class GenericSender {
    private static GenericSender mInstance = null;
    private String mAccessToken = "";

    private static final String BOUNDARY = "--1fadf9q3r023kfa0werfsdf--";
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

    public void sendAudioEvent(Event event, RequestBody audioRequestBody, Callback callback) {

        MultipartBody.Builder bodyBuilder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("metadata", "metadata", RequestBody.create(MediaType.parse("application/json; charset=UTF-8"), event.toString()));

        if (audioRequestBody != null)
            bodyBuilder.addFormDataPart("audio", "speech.wav", audioRequestBody);

        sendEvent(bodyBuilder, callback);
    }

    public void sendEvent(Event event, Callback callback) {

        MultipartBody.Builder bodyBuilder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("metadata", "metadata", RequestBody.create(MediaType.parse("application/json; charset=UTF-8"), event.toString()));


        sendEvent(bodyBuilder, callback);
    }

    private void sendEvent(MultipartBody.Builder bodyBuilder, Callback callback)
    {
        if (bodyBuilder == null)
            return;

        OkHttpClient httpClient = OkHttpClientFactory.getOkHttpClient();

        final Request request = new Request.Builder()
                .url(URLConstants.ALEXA_EVENTS_URL)
                .post(bodyBuilder.build())
                .addHeader("authorization", "Bearer "+ mAccessToken)
                .addHeader("content-type","multipart/form-data")
                .addHeader("boundary",BOUNDARY)
                .build();

        httpClient.newCall(request).enqueue(callback);
    }
}
