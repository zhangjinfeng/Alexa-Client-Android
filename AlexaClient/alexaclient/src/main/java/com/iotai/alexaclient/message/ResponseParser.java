package com.iotai.alexaclient.message;

import com.google.gson.Gson;
import com.iotai.utils.Logger;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.mail.BodyPart;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

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
        if ((inputStream == null)||(boundary == null))
            return;

        try {
            ByteArrayDataSource ds = new ByteArrayDataSource(inputStream, "multipart/form-data");
            MimeMultipart multipart = new MimeMultipart(ds);
            for (int i = 0; i < multipart.getCount(); i++) {
                BodyPart bodypart = multipart.getBodyPart(i);
                String contentType = bodypart.getContentType();
                Logger.d(bodypart.getContentType());


                if (contentType.equals("application/json; charset=UTF-8")) {
                    InputStream inputStreamBodyPart = bodypart.getInputStream();
                    BufferedReader streamReaderBodyPart = new BufferedReader(new InputStreamReader(inputStreamBodyPart, "UTF-8"));
                    StringBuilder stringBuilderBodyPart = new StringBuilder();

                    String line;
                    while ((line = streamReaderBodyPart.readLine()) != null)
                        stringBuilderBodyPart.append(line);

                    Directive directive = parseDirective(line);
                    if (directive != null)
                        responseParserCallback.onDirectiveReceived(directive);
                } else if (contentType.equals("application/octet-stream") ){
                    int audioDataLength = bodypart.getInputStream().available();
                    byte[] audioData = new byte[audioDataLength];
                    bodypart.getInputStream().read(audioData, 0, audioDataLength);
                    responseParserCallback.onAudoDataReceived(audioData);
                }
            }
        } catch (Exception e)
        {

        }


    }
}
