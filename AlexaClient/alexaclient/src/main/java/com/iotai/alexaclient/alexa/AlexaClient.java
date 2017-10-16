package com.iotai.alexaclient.alexa;

import android.content.Context;

import com.iotai.alexaclient.http.GenericSender;
import com.iotai.utils.Configuration;

/**
 * Created by zhangjf9 on 2017/10/10.
 */

public class AlexaClient {
    private Context mContext = null;
    private Configuration mConfiguration = null;
    public boolean initialize(Context context, Configuration configuration) {
        mContext = context;
        mConfiguration = configuration;
        GenericSender.getInstance().initialize(mConfiguration.getValue(Configuration.KEY_AMAZON_ACCESS_TOKEN));
        return true;
    }

    public void release() {
        GenericSender.getInstance().release();
    }
}
