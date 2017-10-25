package com.iotai.alexaclient.http;

import android.os.AsyncTask;

import com.iotai.alexaclient.alexa.AlexaInterfaceManager;
import com.iotai.alexaclient.message.Event;
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
    private Call mDownChannelCall = null;

    private static final int HEART_BEAT_INTERVAL = 4*60*1000; //Heart Beat Interval, unit: milli-seconds
//    private static final int HEART_BEAT_INTERVAL = 1000; //Heart Beat Interval, unit: milli-seconds

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

    protected void fireOnDownChannelReadLineEvent(String line) {
        synchronized (mListeners) {
            for (DownChannelListener listener : mListeners) {
                listener.onDownChannelReadLine(line);
            }
        }
    }

    protected void fireOnDownChannelErrorEvent(String errorMessage) {
        synchronized (mListeners) {
            for (DownChannelListener listener : mListeners) {
                listener.onDownChannelError(errorMessage);
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

        OkHttpClient.Builder clientBuilder = new OkHttpClient().newBuilder()
                .connectTimeout(0, TimeUnit.MILLISECONDS)
                .readTimeout(0, TimeUnit.MILLISECONDS)
                .writeTimeout(0, TimeUnit.MILLISECONDS);

        mHttpClient = clientBuilder.build();

        final Request request = new Request.Builder()
                .url(URLConstants.ALEXA_DIRECTIVES_URL)
                .get()
                .addHeader("authorization", "Bearer "+accessToken)
                .build();

        mDownChannelCall = mHttpClient.newCall(request);
        mDownChannelCall.enqueue(new Callback() {
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
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... params) {
                            Event synchronizeStatesEvent = AlexaInterfaceManager.getInstance().getSynchronizeStatesEvent();
                            if (synchronizeStatesEvent != null)
                            {
                                GenericSender.getInstance().sendEvent(synchronizeStatesEvent, new Callback() {
                                    @Override
                                    public void onFailure(Call call, IOException e) {
                                        //                                call.cancel();
                                        fireOnDownChannelErrorEvent("Failed to send SynchronizeStates.");
                                    }

                                    @Override
                                    public void onResponse(Call call, Response response) throws IOException {

                                        mHeartBeatThread = new HeartBeatThread();
                                        mHeartBeatThread.start();

                                        mConnected = true;
                                        Logger.i("DownChannel Connected!");
                                        fireOnDownChannelConnectedEvent();
                                    }
                                });
                            }
                            return null;
                        }
                    }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }

                try {
                    BufferedSource bufferedSource = response.body().source();
                    while (!bufferedSource.exhausted()) {
                        String line = bufferedSource.readUtf8Line();
                        fireOnDownChannelReadLineEvent(line);
                    }
                } catch (Exception e)
                {
                    Logger.i(e.getMessage());
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

        if (mDownChannelCall != null)
        {
            mDownChannelCall.cancel();
            mDownChannelCall = null;
        }
    }

    class HeartBeatThread extends Thread {
        @Override
        public void run() {
            while ((mStarted) && (mConnected)) {
                try {
                    Thread.sleep(HEART_BEAT_INTERVAL);
                    sendHeartBeat();
                } catch (Exception e) {
                    Logger.i(e.getMessage());
                }
            }
        }
    }

    void sendHeartBeat()
    {
        OkHttpClient httpClient = OkHttpClientFactory.getOkHttpClient();
        final Request request = new Request.Builder()
                .url(URLConstants.ALEXA_PING_URL)
                .get()
                .addHeader("Authorization", "Bearer " + mAccessToken)
                .build();

        httpClient.newCall(request)
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
}
