package com.iotai.alexaclient.message;

/**
 * Created by xu on 17/10/14.
 */

public class Directive {
    private Header header;
    private Object payload;

    public class Header{
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

        public String getMessageId() {
            return messageId;
        }

        public String getDialogRequestId() {
            return dialogRequestId;
        }
    }

}
