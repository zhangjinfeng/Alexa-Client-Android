package com.iotai.utils;

import java.util.UUID;

/**
 * Created by zhangjf9 on 2017/10/16.
 */

public class IDGenerator {
    public static String getUuid(String prefix) {
        if (prefix == null)
            prefix = "uuid";
        return prefix + UUID.randomUUID().toString();
    }

    public static String getMessageId() {
        return getUuid("MessageId");
    }

    public static String getDialogId() {
        return getUuid("DialogId");
    }
}
