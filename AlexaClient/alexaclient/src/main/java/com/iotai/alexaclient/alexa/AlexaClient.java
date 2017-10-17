package com.iotai.alexaclient.alexa;

import android.content.Context;

import com.iotai.alexaclient.http.DownChannel;
import com.iotai.alexaclient.http.GenericSender;
import com.iotai.utils.Configuration;

/**
 * Created by zhangjf9 on 2017/10/10.
 */

public class AlexaClient {
    private static volatile AlexaClient _instance;

    private Context mContext = null;
    private Configuration mConfiguration = null;

    public static AlexaClient getInstance()
    {
        if (_instance == null)
        {
            synchronized (AlexaClient.class)
            {
                if (_instance == null)
                {
                    _instance = new AlexaClient();
                }
            }
        }

        return _instance;
    }

    public boolean initialize(Context context, Configuration configuration) {
        mContext = context;
        mConfiguration = configuration;
        DownChannel.getInstance().start(mConfiguration.getValue(Configuration.KEY_AMAZON_ACCESS_TOKEN));
        GenericSender.getInstance().initialize(mConfiguration.getValue(Configuration.KEY_AMAZON_ACCESS_TOKEN));

        return true;
    }

    public void release() {
        DownChannel.getInstance().stop();
        GenericSender.getInstance().release();
    }
}
