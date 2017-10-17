package com.iotai.alexaclient.alexa;

import com.iotai.alexaclient.http.DownChannel;
import com.iotai.alexaclient.http.DownChannelListener;
import com.iotai.alexaclient.http.GenericSender;
import com.iotai.alexaclient.message.Directive;
import com.iotai.alexaclient.message.Event;
import com.iotai.alexaclient.message.EventBuilder;
import com.iotai.alexaclient.message.EventTypes;
import com.iotai.alexaclient.message.ResponseParser;
import com.iotai.utils.Logger;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xu on 17/10/14.
 */

public class AlexaInterfaceManager implements DownChannelListener {
    private static volatile AlexaInterfaceManager _instance;

    LinkedHashMap<String, AlexaInterface> mAlexaInterfaceHashMap;

    public static AlexaInterfaceManager getInstance()
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

    @Override
    public void onDownChannelReadLine(String line) {
        Logger.i("onDownChannelReadLine: " + line);
        try {
            Directive directive = ResponseParser.parseDirective(line);
            if (directive != null)
            {
                dispatchDirective(directive);
            }
        } catch (Exception e) {
            Logger.e("Bad line");
        }
    }

    private void dispatchDirective(Directive directive) {
        if (directive == null)
            return;

        Directive.Header header = directive.getHeader();
        if (header == null)
            return;

        AlexaInterface alexaInterface = mAlexaInterfaceHashMap.get(header.getNamespace());
        if (alexaInterface != null)
            alexaInterface.onDirectiveReceived(directive);
    }

    private void sendSynchronizeStates() {
        List<Event> context = new ArrayList<Event>();
        Iterator<Map.Entry<String, AlexaInterface>> iterator = mAlexaInterfaceHashMap.entrySet().iterator();
        while (iterator.hasNext())
        {
            Map.Entry<String, AlexaInterface> entry = iterator.next();
            AlexaInterface alexaInterface = entry.getValue();
            if (alexaInterface != null)
            {
                context.add(alexaInterface.getCurrentState());
            }
        }

        Event event = EventBuilder.buildEvent(EventTypes.EVENT_TYPE_SYNCHRONIZE_STATE, context);

        GenericSender.getInstance().sendEvent(event);
    }
}
