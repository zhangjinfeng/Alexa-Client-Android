package com.iotai.alexaclient;

import android.content.Context;

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
        return true;
    }

    public void release() {

    }
}
