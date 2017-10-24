package com.iotai.alexaclient.login;

/**
 * Created by zhangjf9 on 2017/10/23.
 */

public class LoginEngineFactory {
    public static LoginEngine getAmazonLoginEngine(AuthorizationType authorizationType) {
        LoginEngine loginEngine = null;
        switch (authorizationType) {
            case LOCAL_AUTHORIZATION:
                loginEngine = new LocalAuthorizationLoginEngine();
                break;
            case REMOTE_AUTHORIZATION:
                loginEngine = new RemoteAuthorizationLoginEngine();
                break;
            default:
                break;
        }

        return loginEngine;
    }
}
