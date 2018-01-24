package framework.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectionUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReflectionUtil.class);


    public static Object newInstance(Class<?> c) {
        Object object = null;
        try {
            object = c.newInstance();
        } catch (Exception e) {
            LOGGER.error("instance failed");
            throw new RuntimeException(e);
        }
        return object;
    }

    public static Object invokeMethod(Object o, Method method, Object... params) {
        Object o1 = null;
        try {
            method.setAccessible(true);
            o1 = method.invoke(o, params);
        } catch (Exception e) {
            LOGGER.error(" invoke method failed", e);
            throw new RuntimeException(e);
        }
        return o1;
    }

    public static void setFiled(Object o, Field f, Object val) {
        f.setAccessible(true);
        try {
            f.set(o,val);
        } catch (IllegalAccessException e) {
            LOGGER.error("set field failure",e);
            throw new RuntimeException(e);
        }
    }

}
