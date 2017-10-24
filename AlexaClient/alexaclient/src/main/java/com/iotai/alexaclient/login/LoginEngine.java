package com.iotai.alexaclient.login;

import android.support.v4.app.FragmentActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangjf9 on 2017/10/10.
 */

public abstract class LoginEngine {

    private List<LoginEngineListener> mListeners = new ArrayList<LoginEngineListener>();

    public void addListener(LoginEngineListener listener) {
        synchronized (mListeners) {
            if (!(mListeners.contains(listener))) {
                mListeners.add(listener);
            }
        }
    }

    public void removeListener(LoginEngineListener listener) {
        synchronized (mListeners) {
            if ((mListeners.contains(listener))) {
                mListeners.remove(listener);
            }
        }
    }

    protected void fireOnLoginEvent() {
        synchronized (mListeners) {
            for (LoginEngineListener listener : mListeners) {
                listener.onLogin();
            }
        }
    }

    protected void fireOnLoginCancelEvent() {
        synchronized (mListeners) {
            for (LoginEngineListener listener : mListeners) {
                listener.onLoginCancel();
            }
        }
    }

    protected void fireOnSignOutEvent() {
        synchronized (mListeners) {
            for (LoginEngineListener listener : mListeners) {
                listener.onSignOut();
            }
        }
    }

    protected void fireOnTokenReadyEvent(String token) {
        synchronized (mListeners) {
            for (LoginEngineListener listener : mListeners) {
                listener.onTokenReady(token);
            }
        }
    }

    protected void fireOnError(String errMsg) {
        synchronized (mListeners) {
            for (LoginEngineListener listener : mListeners) {
                listener.onError(errMsg);
            }
        }
    }

    public abstract boolean initialize(FragmentActivity fragmentActivity);

    public abstract void release();

    public abstract boolean login(String serialNumber, String productId);

    public abstract void signOut();

    public abstract boolean getAccessToken();

    public abstract void onResume();
}
