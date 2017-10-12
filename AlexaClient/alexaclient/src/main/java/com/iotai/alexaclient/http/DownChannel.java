package com.iotai.alexaclient.http;

/**
 * Created by zhangjf9 on 2017/10/12.
 */

public class DownChannel implements  Runnable{
    private boolean mIsRunning = false;
    private Thread mThread = null;
    private String mAccessToken = "";

    private static final int HEART_BEAT_INTERVAL = 5*60*1000; //Heart Beat Interval, unit: milli-seconds

    public DownChannel(String accessToken)
    {
        mAccessToken = accessToken;
    }

    public boolean start()
    {
        if (mIsRunning)
            return true;

        mThread = new Thread(this);
        mThread.start();

        return true;
    }

    public void stop()
    {
        if (!mIsRunning)
            return;

        mIsRunning = false;
        try {
            mThread.join();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            // create http/2 connection
            createHTTP2Connection();

            synchronizeStates();

            while (mIsRunning) {
                Thread.sleep(HEART_BEAT_INTERVAL);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
