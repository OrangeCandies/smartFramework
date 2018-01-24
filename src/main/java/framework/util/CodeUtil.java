package framework.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public final class CodeUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(CodeUtil.class);

    public static String encodeUrl(String source){
        String dist = null;
        try {
            dist = URLEncoder.encode(source,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("encode url failure",e);
            throw new RuntimeException(e);
        }
        return dist;
    }

    public static String decodeUrl(String souce){
        String dist = null;
        try{
            dist = URLDecoder.decode(souce,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("url decode failure",e);
            throw new RuntimeException(e);
        }
        return dist;
    }
}
