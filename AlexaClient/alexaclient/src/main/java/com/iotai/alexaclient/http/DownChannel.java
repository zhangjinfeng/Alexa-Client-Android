package com.iotai.alexaclient.http;

import com.iotai.alexaclient.message.Directive;
import com.iotai.alexaclient.message.ResponseParser;
import com.iotai.utils.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okio.BufferedSource;

/**
 * Created by zhangjf9 on 2017/10/12.
 */

public class DownChannel {
    private static DownChannel mInstance = null;
    private List<DownChannelListener> mListeners = new ArrayList<DownChannelListener>();

    private boolean mStarted = false;
    private boolean mConnected = false;
    private String mAccessToken = "";
    private OkHttpClient mHttpClient = null;
    private HeartBeatThread  mHeartBeatThread = null;

//    private static final int HEART_BEAT_INTERVAL = 4*60*1000; //Heart Beat Interval, unit: milli-seconds
    private static final int HEART_BEAT_INTERVAL = 1000; //Heart Beat Interval, unit: milli-seconds

    public DownChannel()
    {

    }

    public static DownChannel getInstance() {
        synchronized (DownChannel.class) {
            if (mInstance == null) {
                mInstance = new DownChannel();
            }
        }

        return mInstance;
    }


    public void addListener(DownChannelListener listener) {
        synchronized (mListeners) {
            if (!(mListeners.contains(listener))) {
                mListeners.add(listener);
            }
        }
    }

    public void removeListener(DownChannelListener listener) {
        synchronized (mListeners) {
            if ((mListeners.contains(listener))) {
                mListeners.remove(listener);
            }
        }
    }

    protected void fireOnDownChannelConnectedEvent() {
        synchronized (mListeners) {
            for (DownChannelListener listener : mListeners) {
                listener.onDownChannelConnected();
            }
        }
    }

    protected void fireOnDownChannelDisconnectedEvent() {
        synchronized (mListeners) {
            for (DownChannelListener listener : mListeners) {
                listener.onDownChannelDisconnected();
            }
        }
    }

    public boolean start(String accessToken)
    {
        if (mStarted)
            return true;
        mStarted = true;

        mAccessToken = accessToken;

        openConnection(mAccessToken);

        Logger.i("DownChannel started!");

        return true;
    }

    public void stop()
    {
        if (!mStarted)
            return;

        mStarted = false;
        closeConnection();

        Logger.i("DownChannel stopped!");
    }

    public boolean isRunning()
    {
        return mStarted;
    }

    public boolean isConnected()
    {
        return mConnected;
    }

    void openConnection(final String accessToken)
    {
        Logger.i("Open DownChannel Connection.");
        if (mConnected)
            return;

        mHttpClient = OkHttpClientFactory.getOkHttpClient().newBuilder()
                .connectTimeout(0, TimeUnit.MILLISECONDS)
                .readTimeout(0, TimeUnit.MILLISECONDS)
                .writeTimeout(0, TimeUnit.MILLISECONDS)
                .build();

        final Request request = new Request.Builder()
                .url(URLConstants.ALEXA_DIRECTIVES_URL)
                .get()
                .addHeader("authorization", "Bear "+accessToken)
                .build();

        mHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (mConnected)
                {
                    mConnected = false;
                    Logger.i("DownChannel Disconnected!");
                    fireOnDownChannelDisconnectedEvent();
                }

                try {
                    Thread.sleep(1000);
                } catch (Exception sleepException)
                {
                    sleepException.printStackTrace();
                }

                if (mStarted)
                {
                    openConnection(accessToken);
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!mConnected)
                {
                    mHeartBeatThread = new HeartBeatThread();
                    mHeartBeatThread.start();

                    mConnected = true;
                    Logger.i("DownChannel Connected!");
                    fireOnDownChannelConnectedEvent();
                }

                BufferedSource bufferedSource = response.body().source();

                while (!bufferedSource.exhausted()) {
                    String line = bufferedSource.readUtf8Line();
                    try {
                        Directive directive = ResponseParser.parseDirective(line);
                        if (directive != null)
                        {
                            dispatchDirective(directive);
                        }

                    } catch (Exception e) {
                        Logger.e("Bad line");
                    }
                }

            }
        });
    }

    void closeConnection()
    {
        Logger.i("Close DownChannel Connection.");
        if (!mConnected)
            return;
        mConnected = false;

        try {
            mHeartBeatThread.interrupt();
            mHeartBeatThread.join();
        } catch (Exception e)
        {

        }
        mHeartBeatThread = null;
    }

    class HeartBeatThread extends Thread {
        @Override
        public void run() {
            while ((mStarted) && (mConnected)) {
                try {
                    Thread.sleep(HEART_BEAT_INTERVAL);
                    sendHeartBeat();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    void sendHeartBeat()
    {
        final Request request = new Request.Builder()
                .url(URLConstants.ALEXA_PING_URL)
                .get()
                .addHeader("Authorization", "Bearer " + mAccessToken)
                .build();

        mHttpClient.newCall(request)
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {

                    }
                });
        Logger.i("Send AVS ping message: " + request.toString());
    }


    private void dispatchDirective(Directive directive) {

    }
}
