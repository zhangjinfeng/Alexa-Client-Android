package com.iotai.alexaclient.login;

import com.amazon.identity.auth.device.api.authorization.AuthorizeResult;
import com.google.gson.Gson;
import com.iotai.alexaclient.http.OkHttpClientFactory;
import com.iotai.alexaclient.message.TokenResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by zhangjf9 on 2017/10/10.
 */

public class TokenManager {
    private static volatile TokenManager _instance;
    private List<TokenManagerListener> mListeners = new ArrayList<TokenManagerListener>();
    private String mClientId = "";

    private final static long REFRESH_TOKEN_EXPIRE_IN_THRESHOLD = 60; //UNIT: second

    public static TokenManager getInstance()
    {
        if (_instance == null)
        {
            synchronized (TokenManager.class)
            {
                if (_instance == null)
                {
                    _instance = new TokenManager();
                }
            }
        }

        return _instance;
    }

    public void addListener(TokenManagerListener listener) {
        synchronized (mListeners) {
            if (!(mListeners.contains(listener))) {
                mListeners.add(listener);
            }
        }
    }

    public void removeListener(TokenManagerListener listener) {
        synchronized (mListeners) {
            if ((mListeners.contains(listener))) {
                mListeners.remove(listener);
            }
        }
    }

    protected void fireOnSuccessEvent(String token) {
        synchronized (mListeners) {
            for (TokenManagerListener listener : mListeners) {
                listener.onSuccess(token);
            }
        }
    }

    protected void fireOnError(String errMsg) {
        synchronized (mListeners) {
            for (TokenManagerListener listener : mListeners) {
                listener.onError(errMsg);
            }
        }
    }

    private void getRefreshToken(String refreshToken, String clientId) {
        RequestBody formBody = new FormBody.Builder()
                .add("grant_type", "refresh_token")
                .add("refresh_token", refreshToken)
                .add("client_id", clientId)
                .build();
        doPostRequest(formBody);

    }

    public void doPostRequest(final RequestBody form) {

        OkHttpClient okclient = OkHttpClientFactory.getOkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.amazon.com/auth/O2/token")
                .post(form)
                .build();

        okclient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
                String errMsg = "";
                if (e != null)
                    errMsg = e.getMessage();

                fireOnError(errMsg);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String bodyString = response.body().string();
                TokenResponse tokenResponse = new Gson().fromJson(bodyString, TokenResponse.class);
                if (tokenResponse != null)
                {
                    if (tokenResponse.getExpiresIn() > REFRESH_TOKEN_EXPIRE_IN_THRESHOLD)
                    {
                        fireOnSuccessEvent(tokenResponse.getAccessToken());
                    }
                    else
                    {
                        getRefreshToken(tokenResponse.getRefreshToken(), mClientId);
                    }
                }
            }
        });
    }

    public boolean getToken(AuthorizeResult authorizeResult, String codeVerifier, TokenManagerListener tokenManagerListener) {
        if ((authorizeResult == null)||(tokenManagerListener == null))
            return false;

        if ((codeVerifier == null)||(codeVerifier.length() == 0))
            return false;

        final String authorizationCode = authorizeResult.getAuthorizationCode();
        final String redirectUri = authorizeResult.getRedirectURI();
        final String clientId = authorizeResult.getClientId();

        RequestBody formBody = new FormBody.Builder()
                .add("grant_type", "authorization_code")
                .add("code", authorizationCode)
                .add("redirect_uri", redirectUri)
                .add("client_id", clientId)
                .add("code_verifier", codeVerifier)
                .build();

        mClientId = clientId;

        doPostRequest(formBody);

        return true;
    }
}
