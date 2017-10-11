package com.iotai.alexaclientsample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.iotai.alexaclient.AmazonLoginEngine;
import com.iotai.alexaclient.AmazonLoginEngineListener;
import com.iotai.utils.Logger;
import com.iotai.utils.SerialNumberBuilder;

public class MainActivity extends AppCompatActivity {

    private String SERIAL_NUMBER = SerialNumberBuilder.build(this);
    private String mAccessToken = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AmazonLoginEngine.getInstance().initialize(this);
        AmazonLoginEngine.getInstance().addListener(mAmazaonEngineListener);

        Button buttonLogin = (Button) findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(mButtonLoginOnClickListener);

        Button buttonSignOut = (Button) findViewById(R.id.buttonSignOut);
        buttonSignOut.setOnClickListener(mButtonSignOutOnClickListener);

        Button buttonPressMe = (Button) findViewById(R.id.buttonPressMe);
        buttonPressMe.setOnClickListener(mButtonPressMeOnClickListener);
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        AmazonLoginEngine.getInstance().getAccessToken();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        AmazonLoginEngine.getInstance().release();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        AmazonLoginEngine.getInstance().onResume();
    }

    private View.OnClickListener mButtonLoginOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AmazonLoginEngine.getInstance().login(SERIAL_NUMBER, Constants.PRODUCT_ID);
        }
    };

    private View.OnClickListener mButtonSignOutOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AmazonLoginEngine.getInstance().signOut();
        }
    };

    private View.OnClickListener mButtonPressMeOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    private AmazonLoginEngineListener mAmazaonEngineListener = new AmazonLoginEngineListener() {
        @Override
        public void onLogin() {
            Logger.i("Amazon Account Login successfully.");
            AmazonLoginEngine.getInstance().getAccessToken();
        }

        @Override
        public void onLoginCancel() {
            Logger.i("Amazon Account Login is canceled.");
        }

        @Override
        public void onSignOut() {
            Logger.i("Amazon Account sign out successfully.");
        }

        @Override
        public void onTokenReady(String token) {
            Logger.i("AVS access token:" + token);
        }

        @Override
        public void onError(String errMessage) {
            Logger.i("Amazon Account Login Error: " + errMessage);
        }
    };
}
