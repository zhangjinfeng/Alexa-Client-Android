package com.iotai.alexaclient.login;

import android.support.v4.app.FragmentActivity;

import com.amazon.identity.auth.device.AuthError;
import com.amazon.identity.auth.device.api.Listener;
import com.amazon.identity.auth.device.api.authorization.AuthCancellation;
import com.amazon.identity.auth.device.api.authorization.AuthorizationManager;
import com.amazon.identity.auth.device.api.authorization.AuthorizeListener;
import com.amazon.identity.auth.device.api.authorization.AuthorizeRequest;
import com.amazon.identity.auth.device.api.authorization.AuthorizeResult;
import com.amazon.identity.auth.device.api.authorization.ScopeFactory;
import com.amazon.identity.auth.device.api.workflow.RequestContext;
import com.iotai.utils.Logger;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zhangjf9 on 2017/10/10.
 */

class RemoteAuthorizationLoginEngine extends LoginEngine {

    private FragmentActivity mFragmentActivity = null;
    private RequestContext mRequestContext;
    private AuthorizeListener mAuthorizeListener;

    private static final String ALEXA_ALL = "alexa:all";
    private static final String PRODUCT_ID = "productId";
    private static final String PRODUCT_INSTANCE_ATTRIBUTES = "productInstanceAttributes";
    private static final String DEVICE_SERIAL_NUMBER = "deviceSerialNumber";
    private static final String CODE_CHALLENGE_METHOD = "S256";

    private String mCodeVerifier;
    private String mCodeChallenge;
    private AuthorizeResult mAuthorizeResult;

    private boolean mIsInitialized = false;
    private boolean mIsLoggedIn = false;

    public boolean initialize(FragmentActivity fragmentActivity)
    {
        if (mIsInitialized)
            return true;

        mFragmentActivity = fragmentActivity;
        mRequestContext = RequestContext.create(fragmentActivity);
        mAuthorizeListener =  new AuthorizeListenerImpl();
        mRequestContext.registerListener(mAuthorizeListener);

        TokenManager.getInstance().addListener(mTokenManagerListener);

        try {
            mCodeVerifier = CodeVerifierUtils.generateCodeVerifier();
            mCodeChallenge = CodeVerifierUtils.generateCodeChallenge(mCodeVerifier, CODE_CHALLENGE_METHOD);
        } catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }

        mIsInitialized = true;

        return true;
    }

    public void release()
    {
        if (!mIsInitialized)
            return;

        mRequestContext.unregisterListener(mAuthorizeListener);
        TokenManager.getInstance().removeListener(mTokenManagerListener);
        mIsInitialized = false;
    }

    public boolean login(String serialNumber, String productId)
    {
        if (!mIsInitialized)
            return false;

        final JSONObject scopeData = new JSONObject();
        final JSONObject productInstanceAttributes = new JSONObject();

        try {
            productInstanceAttributes.put(DEVICE_SERIAL_NUMBER, serialNumber);
            scopeData.put(PRODUCT_INSTANCE_ATTRIBUTES, productInstanceAttributes);
            scopeData.put(PRODUCT_ID, productId);

            AuthorizationManager.authorize(new AuthorizeRequest.Builder(mRequestContext)
                    .addScope(ScopeFactory.scopeNamed(ALEXA_ALL, scopeData))
                    .forGrantType(AuthorizeRequest.GrantType.AUTHORIZATION_CODE)
                    .withProofKeyParameters(mCodeChallenge,CODE_CHALLENGE_METHOD)
                    .build());
//            AuthorizationManager.authorize(new AuthorizeRequest
//                    .Builder(mRequestContext)
//                    .addScopes(ProfileScope.profile(), ProfileScope.postalCode())
//                    .build());

        } catch (JSONException e) {
            return false;
        }
        return true;
    }

    public void signOut()
    {
        if (!mIsInitialized)
            return;

        AuthorizationManager.signOut(mFragmentActivity, new Listener<Void, AuthError>() {
            @Override
            public void onSuccess(Void aVoid) {
                mIsLoggedIn = false;
                fireOnSignOutEvent();
            }

            @Override
            public void onError(AuthError authError) {
                fireOnError(authError.toString());
            }
        });
    }

    public boolean getAccessToken()
    {
        if (!mIsInitialized)
            return false;

        if (!mIsLoggedIn)
            return false;

        if (mAuthorizeResult == null)
            return false;

        TokenManager.getInstance().getToken(mAuthorizeResult, mCodeVerifier, mTokenManagerListener);

        return true;
    }

    public void onResume()
    {
        if (!mIsInitialized)
            return;

        if (mRequestContext != null)
            mRequestContext.onResume();
    }

    private class AuthorizeListenerImpl extends AuthorizeListener {

        /* Authorization was completed successfully. */
        @Override
        public void onSuccess(final AuthorizeResult authorizeResult) {
            mAuthorizeResult = authorizeResult;
            mIsLoggedIn = true;
            fireOnLoginEvent();
        }

        /* There was an error during the attempt to authorize the application. */
        @Override
        public void onError(final AuthError authError) {
            fireOnError(authError.toString());
        }

        /* Authorization was cancelled before it could be completed. */
        @Override
        public void onCancel(final AuthCancellation authCancellation) {
            fireOnLoginCancelEvent();
        }
    }

    private TokenManagerListener mTokenManagerListener = new TokenManagerListener() {
        /* getToken completed successfully. */
        @Override
        public void onSuccess(String accessToken) {
            Logger.i("accessToken: " + accessToken);
            fireOnTokenReadyEvent(accessToken);
        }

        /* There was an error during the attempt to get the token. */
        @Override
        public void onError(String errMessage) {
            fireOnError(errMessage);
        }
    };
}
