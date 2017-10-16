package com.iotai.alexaclient.alexa;

/**
 * Created by xu on 17/10/14.
 */

public interface AlexaInterface {
    String getName();
    boolean initialize();
    boolean release();
    String getCurrentState();
}
