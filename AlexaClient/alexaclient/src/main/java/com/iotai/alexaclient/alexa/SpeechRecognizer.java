package com.iotai.alexaclient.alexa;

import com.iotai.alexaclient.message.Directive;
import com.iotai.alexaclient.message.Event;

/**
 * Created by zhangjf9 on 2017/10/17.
 */

public class SpeechRecognizer implements AlexaInterface {
    enum State {
        IDLE,
        RECOGNIZING,
        BUSY,
        EXPECTING_SPEECH
    }

    private State mState = State.IDLE;

    @Override
    public String getName() {
        return AlexaInterfaceNames.SPEECH_RECOGNIZER;
    }

    @Override
    public boolean initialize() {
        return false;
    }

    @Override
    public boolean release() {
        return false;
    }

    @Override
    public Event getCurrentState() {
        return null;
    }

    @Override
    public void onDirectiveReceived(Directive directive) {
        if ((directive == null)||(directive.getHeader() == null))
            return;

        switch (directive.getHeader().getName())
        {
            case "StopCapture":
                stopCapture();
                mState = State.IDLE;
                break;
            case "ExpectSpeech":
                if (startCapture())
                    mState = State.RECOGNIZING;
                else
                    mState = State.IDLE;
                break;
            default:
                break;
        }
    }

    private boolean startCapture() {
        return true;
    }

    private void stopCapture() {

    }
}
