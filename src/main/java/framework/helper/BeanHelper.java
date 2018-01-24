package framework.helper;

import framework.util.ReflectionUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class BeanHelper {
    private static final Map<Class<?>, Object>BEAN_MAP = new HashMap<>();
    static {
        Set<Class<?>> beanClass = ClassHelper.getBeanClass();
        for(Class c:beanClass){
            Object o = ReflectionUtil.newInstance(c);
            BEAN_MAP.put(c,o);
        }
    }

    public static Map<Class<?>, Object> getBeanMap(){
        return BEAN_MAP;
    }

    public static <T> T getBean(Class<T> tClass){
        if(!BEAN_MAP.containsKey(tClass)){
            throw new RuntimeException("Can't get bean by class"+tClass);
        }
        return (T)BEAN_MAP.get(tClass);
    }
}
