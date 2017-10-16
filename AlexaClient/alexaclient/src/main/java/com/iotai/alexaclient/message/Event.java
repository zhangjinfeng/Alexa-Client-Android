package com.iotai.alexaclient.message;

import java.util.List;

/**
 * Created by xu on 17/10/14.
 */

public class Event {
    Header header;
    Object payload;
    List<Event> context;

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public Object getPayload() {
        return payload;
    }

    public void setPayload(Object payload) {
        this.payload = payload;
    }


    public static class Header{

        String namespace;
        String name;
        String messageId;
        String dialogRequestId;

        public String getNamespace() {
            return namespace;
        }


        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setNamespace(String namespace) {
            this.namespace = namespace;
        }

        public String getMessageId() {
            return messageId;
        }

        public String getDialogRequestId() {
            return dialogRequestId;
        }

        public void setDialogRequestId(String dialogRequestId) {
            this.dialogRequestId = dialogRequestId;
        }
    }

//    public static class Payload{
//        String token;
//        String profile;
//        String format;
//        Boolean muted;
//        Long volume;
//        Long offsetInMilliseconds;
//
//        public String getProfile() {
//            return profile;
//        }
//
//        public String getFormat() {
//            return format;
//        }
//    }
}
