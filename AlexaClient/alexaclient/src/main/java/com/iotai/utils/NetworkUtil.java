package com.iotai.utils;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by zhangjf9 on 2014/8/14.
 */
public class NetworkUtil {
    public static String getMacAddress(String splitter) throws Exception
    {
        Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
        System.out.println("networkInterfaces: " + networkInterfaces);
        
        while (networkInterfaces.hasMoreElements())
        {
            NetworkInterface networkInterface = networkInterfaces.nextElement();
            byte[] mac = networkInterface.getHardwareAddress();
            if(mac != null)
            {
	            System.out.println("macs: " + mac);
	            if ((mac != null)&&(mac.length > 0))
	            {
	                StringBuilder sb = new StringBuilder();
	                for (int i = 0; i < mac.length; i++)
	                {
	                    sb.append(String.format("%02X%s", mac[i],(i < mac.length - 1) ? splitter : ""));
	                }
	
	                return sb.toString().toLowerCase();
	            }
            }
        }
        
        System.out.println("mac address is null");
        return null;
    }

    /*
     * Get the IP from locals
     */
    public static List<String> getIpAddresses() {
        ArrayList<String> ipAddressList = new ArrayList<>();
        try {
            Enumeration<?> e1 = NetworkInterface.getNetworkInterfaces();
            while (e1.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface) e1.nextElement();
                Enumeration<?> e2 = ni.getInetAddresses();
                while (e2.hasMoreElements()) {
                    InetAddress ia = (InetAddress) e2.nextElement();
                    if(ia instanceof Inet4Address){
                        String[] ipArray = ia.getHostAddress().split("\\.");
                        if (ipArray.length == 4)
                        {
                            if(ipArray[0].equals("127"))
                                continue;
                            ipAddressList.add(ia.getHostAddress());
                        }
                    }
                }
            }
        } catch (SocketException e) {
//            logger.error(e.getMessage());
            //e.printStackTrace();
            System.exit(-1);
        }
        return ipAddressList;
    }
    
    /*
	 * ��ȡ����IP
	 */
	public static String[] GetInetIpToArray() {
		List<String> list = new ArrayList<String>(); 
		try {
			Enumeration<?> e1 = NetworkInterface.getNetworkInterfaces();
			while (e1.hasMoreElements()) {
				NetworkInterface ni = (NetworkInterface) e1.nextElement();
				Enumeration<?> e2 = ni.getInetAddresses();
				while (e2.hasMoreElements()) {
					InetAddress ia = (InetAddress) e2.nextElement();
					if(ia instanceof Inet4Address){
						if(ia.getHostAddress().split("\\.").length>0)
						{
							String[] ipArray = ia.getHostAddress().split("\\.");
							if(!ipArray[0].equals("127"))
							{
								list.add(ia.getHostAddress());
							}
						}
					}
				}
			}
		} catch (SocketException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		String[] ipAddress = new String[list.size()];
		for(int i=0;i<list.size();i++){ 
			ipAddress[i]=list.get(i); 
		} 
		return ipAddress;
	}

    public static String getLocalAddress(String remoteAddress)
    {
    	System.out.println("inside getLocalAddress");
        String address = "";

        if ((remoteAddress == null)||(remoteAddress.length() == 0))
        {
            String[] localAddressPrefixArray = new String[3];
            localAddressPrefixArray[0] = "192.168.";
            localAddressPrefixArray[1] = "10.";
            localAddressPrefixArray[2] = "172.";

            for (int i = 0; i < localAddressPrefixArray.length; i++)
            {
                address = getLocalAddressByPrefix(localAddressPrefixArray[i]);
                if ((address != null)&&(address.length() > 0))
                {
                    return address;
                }
                else if(address == null)
                {
                	System.out.println("return empty ip address");
                	return "";
                }
            }
        }
        else {
            String[] segments = remoteAddress.split("\\.");
            if (segments.length != 4)
                return "";
            String prefix = segments[0] + "." + segments[1];
            address = getLocalAddressByPrefix(prefix);
        }

        return address;
    }

    public static String getLocalAddressByPrefix(String prefix) {
        if ((prefix == null)||(prefix.length() == 0))
            return "";

        String address = "";
        System.out.println("getting a list of local addresses");
        List<String> localAddressList = NetworkUtil.getIpAddresses();
        System.out.println("locaAddressList = " + localAddressList);
        
        if(localAddressList.size() == 0)	//network cable is not plugged in
    	{
        	System.out.println("ip list is empty");
    		return null;
    	}
        
        for (String localAddress : localAddressList)
        {
            if (localAddress.substring(0,prefix.length()).equals(prefix))
            {
                address = localAddress;
                break;
            }
        }

        return address;
    }
    
}