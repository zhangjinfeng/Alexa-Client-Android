package com.iotai.alexaclient.http;

/**
 * Created by zhangjf9 on 2017/10/13.
 */

public interface DownChannelListener {
    void onDownChannelConnected();
    void onDownChannelDisconnected();
    void onDownChannelReadLine(String line);
}
