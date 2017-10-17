package com.iotai.alexaclientsample;

import android.app.Application;

/**
 * Created by zhangjf9 on 2017/10/10.
 */

public class AlexaClientApplication extends Application {
//    private AlexaClient mAlexaClient = null;
//    private Configuration mConfiguration = null;

    @Override
    public void onCreate() {
        super.onCreate();
//        mConfiguration = new Configuration(this);
//        fillConfiguration(mConfiguration);
//
//        mAlexaClient = new AlexaClient();
//        mAlexaClient.initialize(this, mConfiguration);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

//        mAlexaClient.release();
    }

//    public AlexaClient getAlexaClient() {
//        return mAlexaClient;
//    }
//
//    public Configuration getConfiguration() {
//        return mConfiguration;
//    }
//
//    private void fillConfiguration(Configuration configuration) {
//        mConfiguration.setValue(Configuration.KEY_PRODUCT_ID, Constants.PRODUCT_ID);
//    }
}
