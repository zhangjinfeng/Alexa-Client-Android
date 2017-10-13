package com.iotai.utils;

import android.content.Context;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Properties;

/**
 * Created by zhangjf9 on 2017/8/20.
 */
public class Configuration implements Serializable {

    private Context mContext = null;
    private Properties mProperties = null;

    /*********************************** Key Definitions *************************************************/
    // APP-related information
    public final static String KEY_APP_VERSION = "Version";
    public final static String KEY_PRODUCT_ID = "ProductID";

    // Device-related information
    public final static String KEY_MAC_ADDRESS = "MacAddress";
    public final static String KEY_DEVICE_SERIAL_NUMBER = "DeviceSerialNumber";

    // Amazon-related information
    public final static String KEY_AMAZON_ACCESS_TOKEN = "AmazonAccessToken";
    public final static String  KEY_AVS_BASE_URL = "AVSBaseUrl";

    /*********************************** Default values *************************************************/
    // APP-related information
    public final static String DEFAULT_VALUE_APP_VERSION = "1.0";
    public static final String DEFAULT_VALUE_PRODUCT_ID = "AlexaClient";

    // Amazon-related informaiton
    public final static String  DEFAULT_VALUE_AVS_BASE_URL = "https://avs-alexa-na.amazon.com";

    public Configuration(Context context) {
        mProperties = new Properties();
    }

    public boolean initialize()
    {
        populateConfiguration();
        return true;
    }

    public boolean release()
    {
        return true;
    }

    public Properties getDefaultProperties()
    {
        Properties props = new Properties();

        // Server-related information
        props.put(KEY_APP_VERSION, DEFAULT_VALUE_APP_VERSION);
        props.put(KEY_PRODUCT_ID, DEFAULT_VALUE_PRODUCT_ID);

        String mac = "";
        try
        {
            mac = NetworkUtil.getMacAddress("");
        }
        catch (Exception e)
        {
        }
        props.put(KEY_MAC_ADDRESS, mac);

        String deviceSerialNumber = SerialNumberBuilder.build(mContext);
        props.put(KEY_DEVICE_SERIAL_NUMBER, deviceSerialNumber);

        // Amazon-related information
        props.put(KEY_AVS_BASE_URL, DEFAULT_VALUE_AVS_BASE_URL);

        return props;
    }

    public void reset()
    {
        mProperties = getDefaultProperties();
    }

    private void populateConfiguration()
    {
        Properties defaultProperties = getDefaultProperties();

        Enumeration defaultPropertyNames = defaultProperties.propertyNames();
        while (defaultPropertyNames.hasMoreElements())
        {
            String key = (String)defaultPropertyNames.nextElement();
            if (!mProperties.containsKey(key))
            {
                Object value = defaultProperties.getProperty(key);
                mProperties.put(key, value);
            }
        }
    }

    public String getValue(String key)
    {
        return mProperties.getProperty(key, "");
    }

    public Object setValue(String key, String value)
    {
        Object result;
        result = mProperties.setProperty(key, value);
        return result;
    }
}
