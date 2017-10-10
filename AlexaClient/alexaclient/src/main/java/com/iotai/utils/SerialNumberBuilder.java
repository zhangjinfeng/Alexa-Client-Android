package com.iotai.utils;

import android.content.Context;
import android.provider.Settings;
import android.telephony.TelephonyManager;

/**
 * Created by zhangjf9 on 2014/8/14.
 */
public class SerialNumberBuilder {
    public static String build(Context context)
    {
        String mac = "";
        String androidId = "";
        String imei = "";

        try
        {
            mac = NetworkUtil.getMacAddress("");
        }
        catch (Exception e)
        {
        }

        try
        {
            androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        }
        catch (Exception e)
        {
        }

        try
        {
            imei = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
        }
        catch (Exception e)
        {
        }

        String serial = android.os.Build.SERIAL;

        String originString = mac + ":" + androidId + ":" + imei + ":" + serial;
        String md5String = StringUtil.MD5(originString);

        Logger.i("originString:" + originString);
        Logger.i("md5String:" + md5String);

        return md5String;
    }
}