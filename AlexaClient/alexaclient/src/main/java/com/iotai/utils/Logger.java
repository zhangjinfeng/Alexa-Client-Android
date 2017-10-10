package com.iotai.utils;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


/**
 * @date 21.06.2014
 * @author css
 * 
 * Create a simple and more understandable Android logs. 
 * */

/**
 * @date 25.3.2015
 * @author Zhang Jinfeng
 *
 * Modify some parameters to support Octopus Communication Module.
 * */

 public class Logger {
    
    private static final String TAG = "AlexaClient";
	private static final String LOG_FILE_PATH = Environment.getExternalStorageDirectory() + "/.AlexaClient";

	static String className;
	static String methodName;
	static int lineNumber;
	
	public static boolean isDebug = true;
	
    private Logger(){
        /* Protect from instantiations */
    }

	public static boolean isDebuggable() {
		//return BuildConfig.DEBUG;
		return isDebug;
	}

	private static String createLog( String log ) {

		StringBuffer buffer = new StringBuffer();
		buffer.append("[");
		buffer.append(methodName);
		buffer.append(":");
		buffer.append(lineNumber);
		buffer.append("]");
		buffer.append(log);

		return buffer.toString();
	}
	
	private static void getMethodNames(StackTraceElement[] sElements){
		className = sElements[1].getFileName();
		methodName = sElements[1].getMethodName();
		lineNumber = sElements[1].getLineNumber();
	}

	public static void e(String message){
		if (!isDebuggable())
			return;

		// Throwable instance must be created before any methods  
		getMethodNames(new Throwable().getStackTrace());
		Log.e(TAG,className+"--"+createLog(message));
	}

	public static void i(String message){
		if (!isDebuggable())
			return;

		getMethodNames(new Throwable().getStackTrace());
		Log.i(TAG,className+"--"+createLog(message));
	}
	
	public static void d(String message){
		if (!isDebuggable())
			return;

		getMethodNames(new Throwable().getStackTrace());
		Log.d(TAG,className+"--"+createLog(message));
	}
	
	public static void v(String message){
		if (!isDebuggable())
			return;

		getMethodNames(new Throwable().getStackTrace());
		Log.v(TAG,className+"--"+createLog(message));
	}
	
	public static void w(String message){
		if (!isDebuggable())
			return;

		getMethodNames(new Throwable().getStackTrace());
		Log.w(TAG,className+"--"+createLog(message));
	}
	
	public static void wtf(String message){
		if (!isDebuggable())
			return;

		getMethodNames(new Throwable().getStackTrace());
		Log.wtf(TAG,className+"--"+createLog(message));
	}
	
    public static void i2file(String message) {
        if (!isDebuggable())
            return;

        getMethodNames(new Throwable().getStackTrace());
        Log.i(TAG,className+"--"+createLog(message));
        SimpleDateFormat    sDateFormat    =   new    SimpleDateFormat("yyyy-MM-dd  hh:mm:ss",Locale.CHINA);     
        String    date    =    sDateFormat.format(new    Date());
        wLog2File(date+"--"+className+"--"+createLog(message));
    }
	
	private static void wLog2File(String text)
	{       
	   
	   File logFileDir = new File(LOG_FILE_PATH);
	   if(!logFileDir.exists()){
	       logFileDir.mkdir();
	   }
	   SimpleDateFormat formatter = new SimpleDateFormat("yyyMMdd");
       Date curDate = new Date(System.currentTimeMillis());
	   File logFile = new File(LOG_FILE_PATH+"/cookie_comm_"+formatter.format(curDate)+"_.log");
	   if (!logFile.exists())
	   {
	      try
	      {
	         logFile.createNewFile();
	      } 
	      catch (IOException e)
	      {
	         // TODO Auto-generated catch block
	         e.printStackTrace();
	      }
	   }
	   try
	   {
	      //BufferedWriter for performance, true to set append to file flag
	      BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true)); 
	      buf.append(text);
	      buf.newLine();
	      buf.close();
	   }
	   catch (IOException e)
	   {
	      // TODO Auto-generated catch block
	      e.printStackTrace();
	   }
	}

}
