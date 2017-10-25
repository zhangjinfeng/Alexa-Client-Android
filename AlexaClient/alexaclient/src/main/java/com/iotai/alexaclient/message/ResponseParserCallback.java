package com.iotai.alexaclient.message;

/**
 * Created by zhangjf9 on 2017/10/25.
 */

public interface ResponseParserCallback {
    void onDirectiveReceived(Directive directive);
    void onAudoDataReceived(byte[] audioData);
    void onError(String errorMessage);
}
