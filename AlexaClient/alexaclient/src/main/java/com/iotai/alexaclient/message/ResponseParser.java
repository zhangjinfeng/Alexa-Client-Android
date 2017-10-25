package com.iotai.alexaclient.message;

import com.google.gson.Gson;

import java.io.InputStream;

/**
 * Created by xu on 17/10/16.
 */

public class ResponseParser {
    public static Directive parseDirective(String line) {
        Directive directive = null;
        Gson gson = new Gson();
        try {
            directive = gson.fromJson(line, Directive.class);
        } catch (Exception e)
        {

        }

        return directive;
    }

    public static void parseResponse(InputStream inputStream, String boundary, ResponseParserCallback responseParserCallback) {

    }
}
