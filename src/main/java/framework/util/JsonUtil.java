package framework.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class JsonUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonUtil.class);

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static <T> String toJson(T o){
        String json = null;
        try{
            json = OBJECT_MAPPER.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            LOGGER.error(" convert json failure ",e);
            throw new RuntimeException(e);
        }
        return json;
    }

    public static <T> T fromJson(String json,Class<?>c){
        T o = null;
        try {
            o = (T) OBJECT_MAPPER.readValue(json,c);
        } catch (Exception e) {
            LOGGER.error(" convert pojo failure",e);
            throw new RuntimeException(e);
        }
        return o;
    }
}
