package org.smart.framework.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class StreamUtil {


    private static final Logger LOGGER = LoggerFactory.getLogger(StreamUtil.class);
    public static String getString(InputStream is) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line = null;
            while((line = reader.readLine()) != null){
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            LOGGER.error("get String failure",e);
            throw new RuntimeException(e);
        }
        return stringBuilder.toString();
    }

    public static void copyStream(InputStream inputStream, OutputStream outputStream) {
        try{
            int length;
            byte [] bytes = new byte[4*1024];
            while ((length=inputStream.read(bytes)) != -1){
                outputStream.write(bytes,0,length);
            }
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
