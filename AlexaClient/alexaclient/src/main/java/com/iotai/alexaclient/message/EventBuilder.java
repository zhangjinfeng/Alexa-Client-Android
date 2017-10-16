package com.iotai.alexaclient.message;

import com.iotai.utils.IDGenerator;

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

        return null;
    }
//    public static build
}
