package com.iotai.alexaclient.login;

import android.support.v4.app.FragmentActivity;

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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangjf9 on 2017/10/10.
 */

public class AmazonLoginEngine {
    private static AmazonLoginEngine mInstance = null;
    private List<AmazonLoginEngineListener> mListeners = new ArrayList<AmazonLoginEngineListener>();

    private FragmentActivity mFragmentActivity = null;
    private RequestContext mRequestContext;
    private AuthorizeListener mAuthorizeListener;

    private static final String ALEXA_ALL = "alexa:all";
    private static final String PRODUCT_ID = "productId";
    private static final String PRODUCT_INSTANCE_ATTRIBUTES = "productInstanceAttributes";
    private static final String DEVICE_SERIAL_NUMBER = "deviceSerialNumber";
    private static final String CODE_CHALLENGE = "CodeChallenge";
    private static final String CODE_CHALLENGE_METHOD = "S256";

    private boolean mIsInitialized = false;
    private boolean mIsLoggedIn = false;


    public static AmazonLoginEngine getInstance() {
        synchronized (AmazonLoginEngine.class) {
            if (mInstance == null) {
                mInstance = new AmazonLoginEngine();
            }
        }

        return mInstance;
    }


    public void addListener(AmazonLoginEngineListener listener) {
        synchronized (mListeners) {
            if (!(mListeners.contains(listener))) {
                mListeners.add(listener);
            }
        }
    }

    public void removeListener(AmazonLoginEngineListener listener) {
        synchronized (mListeners) {
            if ((mListeners.contains(listener))) {
                mListeners.remove(listener);
            }
        }
    }

    protected void fireOnLoginEvent() {
        synchronized (mListeners) {
            for (AmazonLoginEngineListener listener : mListeners) {
                listener.onLogin();
            }
        }
    }

    protected void fireOnLoginCancelEvent() {
        synchronized (mListeners) {
            for (AmazonLoginEngineListener listener : mListeners) {
                listener.onLoginCancel();
            }
        }
    }

    protected void fireOnSignOutEvent() {
        synchronized (mListeners) {
            for (AmazonLoginEngineListener listener : mListeners) {
                listener.onSignOut();
            }
        }
    }

    protected void fireOnTokenReadyEvent(String token) {
        synchronized (mListeners) {
            for (AmazonLoginEngineListener listener : mListeners) {
                listener.onTokenReady(token);
            }
        }
    }

    protected void fireOnError(String errMsg) {
        synchronized (mListeners) {
            for (AmazonLoginEngineListener listener : mListeners) {
                listener.onError(errMsg);
            }
        }
    }

    private AmazonLoginEngine()
    {
    }

    public boolean initialize(FragmentActivity fragmentActivity)
    {
        if (mIsInitialized)
            return true;

        mFragmentActivity = fragmentActivity;
        mRequestContext = RequestContext.create(fragmentActivity);
        mAuthorizeListener =  new AuthorizeListenerImpl();
        mRequestContext.registerListener(mAuthorizeListener);
        mIsInitialized = true;

        return true;
    }

    public void release()
    {
        if (!mIsInitialized)
            return;

        mRequestContext.unregisterListener(mAuthorizeListener);
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
                    .forGrantType(AuthorizeRequest.GrantType.ACCESS_TOKEN)
                    .shouldReturnUserData(false)
                    .build());
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

        AuthorizationManager.getToken(mFragmentActivity, new Scope[] { ScopeFactory.scopeNamed(ALEXA_ALL) }, new TokenListener());

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

    private TokenListener mTokenListener = new TokenListener();

    private class TokenListener implements Listener<AuthorizeResult, AuthError> {
        /* getToken completed successfully. */
        @Override
        public void onSuccess(AuthorizeResult authorizeResult) {
            String accessToken = authorizeResult.getAccessToken();
            Logger.i("accessToken: " + accessToken);
            fireOnTokenReadyEvent(accessToken);
        }

        /* There was an error during the attempt to get the token. */
        @Override
        public void onError(AuthError authError) {
            fireOnError(authError.toString());
        }
    }
}
