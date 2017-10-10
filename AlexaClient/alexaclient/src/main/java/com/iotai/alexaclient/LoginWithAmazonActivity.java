package com.iotai.alexaclient;

import android.content.Intent;
import com.iotai.utils.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.amazon.identity.auth.device.AuthError;
import com.amazon.identity.auth.device.api.Listener;
import com.amazon.identity.auth.device.api.authorization.AuthCancellation;
import com.amazon.identity.auth.device.api.authorization.AuthorizationManager;
import com.amazon.identity.auth.device.api.authorization.AuthorizeListener;
import com.amazon.identity.auth.device.api.authorization.AuthorizeRequest;
import com.amazon.identity.auth.device.api.authorization.AuthorizeResult;
import com.amazon.identity.auth.device.api.authorization.Scope;
import com.amazon.identity.auth.device.api.authorization.ScopeFactory;
import com.amazon.identity.auth.device.api.workflow.RequestContext;
import com.iotai.utils.Logger;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * A login screen that offers login via email/password.
 */
public class LoginWithAmazonActivity extends AppCompatActivity {

    private RequestContext mRequestContext;
    private Configuration mConfiguration;


    private static final Scope ALEXA_ALL_SCOPE = ScopeFactory.scopeNamed("alexa:all");
    private static final String PRODUCT_ID = "productId";
    private static final String PRODUCT_INSTANCE_ATTRIBUTES = "productInstanceAttributes";
    private static final String DEVICE_SERIAL_NUMBER = "deviceSerialNumber";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_with_amazon);

        mRequestContext = RequestContext.create(this);
        mRequestContext.registerListener(new AuthorizeListenerImpl());

        Intent intent = getIntent();
        mConfiguration = (Configuration)intent.getSerializableExtra("Configuration");

        // Find the button with the login_with_amazon ID
        // and set up a click handler
        Button mLoginButton = (Button) findViewById(R.id.login_in_with_amazon_button);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final JSONObject scopeData = new JSONObject();
                final JSONObject productInstanceAttributes = new JSONObject();

                try {
                    productInstanceAttributes.put(DEVICE_SERIAL_NUMBER, mConfiguration.getValue(Configuration.KEY_DEVICE_SERIAL_NUMBER));
                    scopeData.put(PRODUCT_INSTANCE_ATTRIBUTES, productInstanceAttributes);
                    scopeData.put(PRODUCT_ID, mConfiguration.getValue(Configuration.KEY_PRODUCT_ID));

                    AuthorizationManager.authorize(new AuthorizeRequest.Builder(mRequestContext)
                            .addScope(ScopeFactory.scopeNamed("alexa:all", scopeData))
                            .forGrantType(AuthorizeRequest.GrantType.ACCESS_TOKEN)
                            .shouldReturnUserData(false)
                            .build());
                } catch (JSONException e) {
                    // handle exception here
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mRequestContext.onResume();
    }

    @Override
    protected void onStart(){
        super.onStart();
        AuthorizationManager.getToken(this, new Scope[] { ALEXA_ALL_SCOPE }, new TokenListener());
        Logger.i("onStart");
    }

    private class AuthorizeListenerImpl extends AuthorizeListener {

        /* Authorization was completed successfully. */
        @Override
        public void onSuccess(final AuthorizeResult authorizeResult) {
            AuthorizationManager.getToken(LoginWithAmazonActivity.this, new Scope[] { ALEXA_ALL_SCOPE }, new TokenListener());
        }

        /* There was an error during the attempt to authorize the application. */
        @Override
        public void onError(final AuthError authError) {
        }

        /* Authorization was cancelled before it could be completed. */
        @Override
        public void onCancel(final AuthCancellation authCancellation) {
        }
    }

    public class TokenListener implements Listener<AuthorizeResult, AuthError> {
        /* getToken completed successfully. */
        @Override
        public void onSuccess(AuthorizeResult authorizeResult) {
            String accessToken = authorizeResult.getAccessToken();
            Logger.i("accessToken: " + accessToken);

            mConfiguration.setValue(Configuration.KEY_AMAZON_ACCESS_TOKEN, accessToken);

            Intent intent = new Intent();
            intent.setClass(LoginWithAmazonActivity.this, LoginWithAmazonActivity.class);
            LoginWithAmazonActivity.this.startActivity(intent);
        }

        /* There was an error during the attempt to get the token. */
        @Override
        public void onError(AuthError authError) {
            Logger.i(authError.toString());
        }
    }

}


