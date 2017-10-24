package com.iotai.alexaclientsample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.iotai.alexaclient.alexa.AlexaClient;
import com.iotai.alexaclient.login.AuthorizationType;
import com.iotai.alexaclient.login.LoginEngine;
import com.iotai.alexaclient.login.LoginEngineFactory;
import com.iotai.alexaclient.login.LoginEngineListener;
import com.iotai.utils.Configuration;
import com.iotai.utils.Logger;
import com.iotai.utils.SerialNumberBuilder;

public class MainActivity extends AppCompatActivity {

    private String SERIAL_NUMBER = SerialNumberBuilder.build(this);
    private String mAccessToken = "";
    private LoginEngine mLoginEngine = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLoginEngine = LoginEngineFactory.getAmazonLoginEngine(AuthorizationType.LOCAL_AUTHORIZATION);

        mLoginEngine.initialize(this);
        mLoginEngine.addListener(mAmazaonEngineListener);

        Button buttonLogin = (Button) findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(mButtonLoginOnClickListener);

        Button buttonSignOut = (Button) findViewById(R.id.buttonSignOut);
        buttonSignOut.setOnClickListener(mButtonSignOutOnClickListener);

        Button buttonInitializeAlexa = (Button) findViewById(R.id.buttonInitializeAlexa);
        buttonInitializeAlexa.setOnClickListener(mButtonInitializeAlexaOnClickListener);

        Button buttonReleaseAlexa = (Button) findViewById(R.id.buttonReleaseAlexa);
        buttonReleaseAlexa.setOnClickListener(mButtonReleaseAlexaOnClickListener);

        Button buttonPressMe = (Button) findViewById(R.id.buttonPressMe);
        buttonPressMe.setOnClickListener(mButtonPressMeOnClickListener);

        Button buttonTest = (Button) findViewById(R.id.buttonTest);
        buttonTest.setOnClickListener(mButtonTestOnClickListener);
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        mLoginEngine.getAccessToken();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        mLoginEngine.release();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        mLoginEngine.onResume();
    }

    private View.OnClickListener mButtonLoginOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mLoginEngine.login(SERIAL_NUMBER, Constants.PRODUCT_ID);
        }
    };

    private View.OnClickListener mButtonSignOutOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mLoginEngine.signOut();
        }
    };

    private View.OnClickListener mButtonInitializeAlexaOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if ((mAccessToken == null)||(mAccessToken.length() == 0))
                return;
            Configuration configuration = new Configuration(MainActivity.this);
            configuration.setValue(Configuration.KEY_AMAZON_ACCESS_TOKEN, mAccessToken);
            AlexaClient.getInstance().initialize(MainActivity.this, configuration);
        }
    };

    private View.OnClickListener mButtonReleaseAlexaOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AlexaClient.getInstance().release();
        }
    };

    private View.OnClickListener mButtonPressMeOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    private View.OnClickListener mButtonTestOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, TestActivity.class);
            intent.putExtra("AccessToken", mAccessToken);
            startActivity(intent);
        }
    };

    private LoginEngineListener mAmazaonEngineListener = new LoginEngineListener() {
        @Override
        public void onLogin() {
            Logger.i("Amazon Account Login successfully.");
            mLoginEngine.getAccessToken();
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
            mAccessToken = token;
        }

        @Override
        public void onError(String errMessage) {
            Logger.i("Amazon Account Login Error: " + errMessage);
        }
    };
}
