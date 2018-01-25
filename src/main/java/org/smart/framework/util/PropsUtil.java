package org.smart.framework.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropsUtil {
    public static final Logger LOGGER = LoggerFactory.getLogger(PropsUtil.class);

    public static Properties loadProps(String filename) {
        Properties properties = null;
        InputStream is = null;
        try {
            is = Thread.currentThread().getContextClassLoader().getResourceAsStream(filename);

            if(is == null){
                throw new FileNotFoundException(filename+" file is not found ");
            }

            properties = new Properties();
            properties.load(is);

        }catch (IOException e){
            LOGGER.error("load properties file faild",e);
        }finally {
            if(is != null){
                try {
                    is.close();
                } catch (IOException e) {
                    LOGGER.error("close inputstream failed",e);
                }
            }
        }
        return properties;
    }

    public static String getString(Properties properties, String key){
        return getString(properties,key,"");
    }

    public static String getString(Properties properties, String key, String s) {
        String value = s;
        if(properties.containsKey(key)){
            value = properties.getProperty(key);
        }
        return value;
    }

    public static int getInt(Properties properties,String key){
        return getInt(properties,key,0);
    }

    public static int getInt(Properties properties, String key, int defaultValue) {
        int value = defaultValue;
        if(properties.containsKey(key)){
            value = CaseUtil.caseInt(properties.getProperty(key));
        }
        return value;
    }

    public static boolean getBoolean(Properties properties,String key){
        return getBoolean(properties,key,false);
    }

    public static boolean getBoolean(Properties properties, String key, boolean defaultValue) {
        boolean value = defaultValue;
        if(properties.containsKey(key)){
            value = CaseUtil.caseBoolean(properties.getProperty(key));
        }
        return value;
    }
}
