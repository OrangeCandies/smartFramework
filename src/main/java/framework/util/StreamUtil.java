package framework.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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
}
