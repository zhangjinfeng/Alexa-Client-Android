package com.iotai.alexaclient.http;

/**
 * Created by zhangjf9 on 2017/10/13.
 */

public interface URLConstants {
    final static String ALEXA_API_VERSION = "v20160207";
    final static String ALEXA_NORTH_AMERICA_BASE_URL = "https://avs-alexa-na.amazon.com";
    final static String ALEXA_EUROPE_BASE_URL = "https://avs-alexa-eu.amazon.com";
    final static String ALEXA_BASE_URL = ALEXA_NORTH_AMERICA_BASE_URL;

    final static String URL_END_POINT_DIRECTIVES = "/" + ALEXA_API_VERSION + "/directives";
    final static String URL_END_POINT_EVENTS = "/" + ALEXA_API_VERSION + "/events";
    final static String URL_END_POINT_PING = "/ping";

    final static String ALEXA_DIRECTIVES_URL = ALEXA_BASE_URL + URL_END_POINT_DIRECTIVES;
    final static String ALEXA_EVENTS_URL = ALEXA_BASE_URL + URL_END_POINT_EVENTS;
    final static String ALEXA_PING_URL = ALEXA_BASE_URL + URL_END_POINT_PING;

}
