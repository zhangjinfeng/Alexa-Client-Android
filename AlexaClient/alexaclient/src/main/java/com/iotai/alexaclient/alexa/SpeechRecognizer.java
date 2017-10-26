package com.iotai.alexaclient.alexa;

import android.support.annotation.Nullable;

import com.iotai.alexaclient.http.GenericSender;
import com.iotai.alexaclient.message.Directive;
import com.iotai.alexaclient.message.Event;
import com.iotai.alexaclient.message.EventBuilder;
import com.iotai.alexaclient.message.ResponseParser;
import com.iotai.alexaclient.message.ResponseParserCallback;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;

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
    BlockingQueue<AudioElement> mAudioBlockingQueue = new LinkedBlockingQueue<>();

    @Override
    public String getNameSpace() {
        return AlexaInterfaceNames.SPEECH_RECOGNIZER;
    }

    @Override
    public boolean initialize() {
        return true;
    }

    @Override
    public boolean release() {
        return true;
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
                stop();
                break;
            case "ExpectSpeech":
                start();
                break;
            default:
                break;
        }
    }


    public void start() {
        if (mState != State.IDLE)
            return;

        mState = State.RECOGNIZING;

        Event recognizeEvent = EventBuilder.buildRecognizeEvent(null, null, null);

        GenericSender.getInstance().sendAudioEvent(recognizeEvent, mAudioRequestBody, new RecognizeRequestCallback());
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

        // Add audio data into the queue
        AudioElement audioElement = new AudioElement(audioData, length);
        mAudioBlockingQueue.add(audioElement);
    }

    private class RecognizeRequestCallback implements Callback {

        @Override
        public void onFailure(Call call, IOException e) {

        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            if (response == null)
                return;

            String boundary = response.header("boundary");
            InputStream inputStream = response.body().byteStream();
            ResponseParser.parseResponse(inputStream, boundary, new RecognizeResponseParserCallback());
        }
    }

    private class RecognizeResponseParserCallback implements ResponseParserCallback {

        @Override
        public void onDirectiveReceived(Directive directive) {

        }

        @Override
        public void onAudoDataReceived(byte[] audioData) {

        }

        @Override
        public void onError(String errorMessage) {

        }
    }

    private AudioRequestBody mAudioRequestBody = new AudioRequestBody();

    private class AudioRequestBody extends RequestBody {
        @Nullable
        @Override
        public MediaType contentType() {
            return MediaType.parse("application/octet-stream");
        }

        @Override
        public void writeTo(BufferedSink bufferedSink) throws IOException {
            while (mState == State.RECOGNIZING)
            {
                try {
                    AudioElement audioElement = mAudioBlockingQueue.take();
                    if (bufferedSink != null)
                        bufferedSink.write(audioElement.getAudioData());

                    Thread.sleep(10);
                } catch (Exception e)
                {

                }
            }
        }
    }

    private class AudioElement {
        private byte[] mAudioData;
        private int mLength;

        public AudioElement(byte[] audioData, int length)
        {
            mAudioData = Arrays.copyOf(audioData, length);
            mLength = length;
        }

        byte[] getAudioData()
        {
            return mAudioData;
        }

        int getLength()
        {
            return mLength;
        }
    }

}
