package com.iotai.alexaclientsample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.iotai.alexaclient.R;

public class MainActivity extends AppCompatActivity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonInitialize = (Button) findViewById(R.id.buttonInitialize);
        buttonInitialize.setOnClickListener(mButtonInitializeOnClickListener);

        Button buttonRelease = (Button) findViewById(R.id.buttonRelease);
        buttonRelease.setOnClickListener(mButtonReleaseOnClickListener);

        Button buttonLogin = (Button) findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(mButtonLoginOnClickListener);

        Button buttonLogout = (Button) findViewById(R.id.buttonLogout);
        buttonLogout.setOnClickListener(mButtonLogoutOnClickListener);

        Button buttonCallAlexa = (Button) findViewById(R.id.buttonCallAlexa);
        buttonCallAlexa.setOnClickListener(mButtonCallAlexaOnClickListener);
    }

    private View.OnClickListener mButtonInitializeOnClickListener;
    private View.OnClickListener mButtonReleaseOnClickListener;
    private View.OnClickListener mButtonLoginOnClickListener;
    private View.OnClickListener mButtonLogoutOnClickListener;
    private View.OnClickListener mButtonCallAlexaOnClickListener;
}
