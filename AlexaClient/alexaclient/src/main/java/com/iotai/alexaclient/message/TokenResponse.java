package com.iotai.alexaclient.message;

/**
 * Created by zhangjf9 on 2017/10/23.
 */

public class TokenResponse {
    public String access_token;
    public String refresh_token;
    public String token_type;
    public long expires_in;

    public String getAccessToken() {
        return access_token;
    }

    public void setAccessToken(String access_token) {
        this.access_token = access_token;
    }

    public String getRefreshToken() {
        return refresh_token;
    }

    public void setRefreshToken(String refresh_token) {
        this.refresh_token = refresh_token;
    }


    public String getTokenType() {
        return token_type;
    }

    public void setTokenType(String token_type) {
        this.token_type = token_type;
    }

    public long getExpiresIn() {
        return expires_in;
    }

    public void setExpiresIn(long expires_in) {
        this.expires_in = expires_in;
    }
}
