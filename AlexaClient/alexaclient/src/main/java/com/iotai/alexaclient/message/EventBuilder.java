package com.iotai.alexaclient.message;

import com.iotai.utils.IDGenerator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by xu on 17/10/14.
 */

public class EventBuilder {
    public static Event buildEvent(final String eventType, List<Event> context) {
        Event event = null;
        switch (eventType)
        {
            case EventTypes.EVENT_TYPE_SYNCHRONIZE_STATE:
                event = buildSynchronizeStateEvent(context);
                break;
            default:
                break;
        }

        return event;
    }

    private static Event buildSynchronizeStateEvent(List<Event> context) {
        Event event = new Event();

        if (context != null)
            event.setContext(context);

        Event.Header header = new Event.Header();
        header.setNamespace("System");
        header.setName("SynchronizeState");
        header.setMessageId(IDGenerator.getMessageId());
        header.setDialogRequestId(IDGenerator.getDialogId());
        event.setHeader(header);

        return event;
    }
    public static Event buildRecognizeEvent(List<Event> context, String profile, String initiatorType) {
        Event event = new Event();

        if (context != null)
            event.setContext(context);

        Event.Header header = new Event.Header();
        header.setNamespace("SpeechRecognizer");
        header.setName("Recognize");
        header.setMessageId(IDGenerator.getMessageId());
        header.setDialogRequestId(IDGenerator.getDialogId());
        event.setHeader(header);

        try {
            JSONObject payload = new JSONObject();
            payload.put("format", "AUDIO_L16_RATE_16000_CHANNELS_1");

            if ((profile == null)||(profile.length() == 0))
                profile = "NEAR_FIELD";
            payload.put("profile", profile);

            if ((initiatorType == null)||(initiatorType.length() == 0))
                initiatorType = "WAKEWORD";
            JSONObject initiator = new JSONObject();
            initiator.put("type", initiatorType);
            payload.put("initiator", initiator);

            event.setPayload(payload);
        } catch (JSONException e)
        {

        }

        return event;

    }
}
