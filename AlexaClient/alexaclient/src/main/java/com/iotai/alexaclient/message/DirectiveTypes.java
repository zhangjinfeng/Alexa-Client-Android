package com.iotai.alexaclient.message;

/**
 * Created by xu on 17/10/14.
 */

public interface DirectiveTypes {
    public static final String SPEAK = "Speak";
    public static final String PLAY = "Play";
    public static final String STOP = "Stop";
    public static final String STOP_CAPTURE = "StopCapture";
    public static final String SET_ALERT = "SetAlert";
    public static final String DELETE_ALERT = "DeleteAlert";
    public static final String SET_VOLUME = "SetVolume";
    public static final String ADJUST_VOLUME = "AdjustVolume";
    public static final String SET_MUTE = "SetMute";
    public static final String EXPECT_SPEECH = "ExpectSpeech";
    public static final String MEDIA_PLAY = "PlayCommandIssued";
    public static final String MEDIA_PAUSE = "PauseCommandIssued";
    public static final String MEDIA_NEXT = "NextCommandIssued";
    public static final String MEDIA_PREVIOUS = "PreviousCommandIssue";
    public static final String EXCEPTION = "Exception";
    public static final String SET_ENDPOINT = "SetEndpoint";
}
