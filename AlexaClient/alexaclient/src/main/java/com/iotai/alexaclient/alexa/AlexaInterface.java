package com.iotai.alexaclient.alexa;

import com.iotai.alexaclient.message.Directive;
import com.iotai.alexaclient.message.Event;

/**
 * Created by xu on 17/10/14.
 */

public interface AlexaInterface {
    String getName();
    boolean initialize();
    boolean release();
    Event getCurrentState();

    void onDirectiveReceived(Directive directive);
}
