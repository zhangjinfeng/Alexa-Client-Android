package com.iotai.alexaclient.alexa;

import com.iotai.alexaclient.http.GenericSender;
import com.iotai.alexaclient.message.Directive;
import com.iotai.alexaclient.message.Event;
import com.iotai.alexaclient.message.EventBuilder;

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
    private boolean mIsProcessing = false;

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
        mIsProcessing = true;
        return true;
    }

    private void stopCapture() {
        mIsProcessing = false;
    }

    public void start() {
        if (mState != State.IDLE)
            return;

        Event recognizeEvent = EventBuilder.buildRecognizeEvent(null, null, null);

        GenericSender.getInstance().sendEvent(recognizeEvent);

        mState = State.RECOGNIZING;
    }

    public void stop() {
        if (mState == State.IDLE)
            return;

        mState = State.IDLE;
    }

    public void feedAudio(byte[] audioData, int length)
    {
        if (mState != State.RECOGNIZING)
            return;


    }
}
