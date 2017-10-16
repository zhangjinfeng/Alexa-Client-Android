package com.iotai.alexaclient.alexa;

import com.iotai.alexaclient.http.DownChannel;
import com.iotai.alexaclient.http.DownChannelListener;
import com.iotai.utils.Logger;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by xu on 17/10/14.
 */

public class AlexaInterfaceManager implements DownChannelListener {
    private static volatile AlexaInterfaceManager _instance;

    LinkedHashMap<String, AlexaInterface> mAlexaInterfaceHashMap;

    public static AlexaInterfaceManager get_instance()
    {
        if (_instance == null)
        {
            synchronized (AlexaInterfaceManager.class)
            {
                if (_instance == null)
                {
                    _instance = new AlexaInterfaceManager();
                }
            }
        }

        return _instance;
    }

    private AlexaInterfaceManager()
    {
        mAlexaInterfaceHashMap = new LinkedHashMap<String, AlexaInterface>();
        addAlexaInterfaces();
    }

    protected void addAlexaInterfaces() {

    }

    protected void removeAlexaInterfaces() {

    }

    public boolean initialize()
    {
        Iterator<Map.Entry<String, AlexaInterface>> iterator = mAlexaInterfaceHashMap.entrySet().iterator();
        while (iterator.hasNext())
        {
            try {
                Map.Entry<String, AlexaInterface> entry = iterator.next();
                AlexaInterface alexaInterface = entry.getValue();
                if (alexaInterface != null)
                {
                    alexaInterface.initialize();
                    Logger.i("Initialize alexa interface : " + alexaInterface.getName());
                }

            } catch (Exception e)
            {
                Logger.e(e.getStackTrace().toString());
            }
        }

        DownChannel.getInstance().addListener(this);

        return true;
    }

    public void release()
    {
        DownChannel.getInstance().removeListener(this);

        Iterator<Map.Entry<String, AlexaInterface>> iterator = mAlexaInterfaceHashMap.entrySet().iterator();
        while (iterator.hasNext())
        {
            try {
                Map.Entry<String, AlexaInterface> entry = iterator.next();
                AlexaInterface alexaInterface = entry.getValue();
                if (alexaInterface != null)
                {
                    alexaInterface.release();
                    Logger.i("Release alexa interface : " + alexaInterface.getName());
                }

            } catch (Exception e)
            {
                Logger.e(e.getStackTrace().toString());
            }
        }

        removeAlexaInterfaces();
    }




    @Override
    public void onDownChannelConnected() {
        sendSynchronizeStates();
    }

    @Override
    public void onDownChannelDisconnected() {

    }


    private void sendSynchronizeStates() {

    }
}
