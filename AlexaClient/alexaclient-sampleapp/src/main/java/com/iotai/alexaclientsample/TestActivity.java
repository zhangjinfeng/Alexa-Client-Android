package com.iotai.alexaclientsample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.iotai.alexaclient.http.DownChannel;

public class TestActivity extends AppCompatActivity {

    private String mAccessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        Intent intent = getIntent();
        mAccessToken = (String) intent.getStringExtra("AccessToken");

        Button buttonStartDownChannel = (Button) findViewById(com.iotai.alexaclientsample.R.id.buttonStartDownChannel);
        buttonStartDownChannel.setOnClickListener(mButtonStartDownChannelOnClickListener);

        Button buttonStopDownChannel = (Button) findViewById(com.iotai.alexaclientsample.R.id.buttonStopDownChannel);
        buttonStopDownChannel.setOnClickListener(mButtonStopDownChannelOnClickListener);
    }

    private View.OnClickListener mButtonStartDownChannelOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DownChannel.getInstance().start(mAccessToken);
        }
    };

    private View.OnClickListener mButtonStopDownChannelOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DownChannel.getInstance().stop();
        }
    };
}
