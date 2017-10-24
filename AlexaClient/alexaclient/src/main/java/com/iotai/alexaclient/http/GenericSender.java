package com.iotai.alexaclient.http;

import com.iotai.alexaclient.message.Event;

import java.util.concurrent.TimeUnit;

import okhttp3.Call;
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

    public void sendEvent(Event event) {
        OkHttpClient httpClient = OkHttpClientFactory.getOkHttpClient().newBuilder()
                .connectTimeout(0, TimeUnit.MILLISECONDS)
                .readTimeout(0, TimeUnit.MILLISECONDS)
                .writeTimeout(0, TimeUnit.MILLISECONDS)
                .build();

        MultipartBody.Builder bodyBuilder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("metadata", "metadata", RequestBody.create(MediaType.parse("application/json; charset=UTF-8"), event.toString()));

        final Request request = new Request.Builder()
                .url(URLConstants.ALEXA_EVENTS_URL)
                .post(bodyBuilder.build())
                .addHeader("authorization", "Bearer "+ mAccessToken)
                .addHeader("content-type","multipart/form-data")
                .addHeader("boundary",BOUNDARY)
                .build();

        Call call = httpClient.newCall(request);
        try {
            Response response = call.execute();
        } catch (Exception e)
        {

        }
    }
}
