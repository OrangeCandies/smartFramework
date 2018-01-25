package org.smart.framework.helper;

import org.smart.framework.annocation.Action;
import org.smart.framework.bean.Handler;
import org.smart.framework.bean.Requset;
import org.smart.framework.util.ArrayUtil;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class ControllerHelper {

    /**
     * 大致是每一个请求路径和请求方法对于一个处理类和处理方法
     */
    public static final Map<Requset, Handler> ACTION_MAP = new HashMap<>();

    static {
        Set<Class<?>> controllerClasses = ClassHelper.getControlClass();
        for (Class<?> controllerClass : controllerClasses) {
            Method[] methods = controllerClass.getMethods();
            for (Method m : methods) {
                if (m.isAnnotationPresent(Action.class)) {
                    Action action = m.getAnnotation(Action.class);
                    String mapping = action.value();
                    if (!mapping.matches("\\w:/\\w*")) {
                        String[] array = mapping.split(":");
                        if (ArrayUtil.isNotEmpty(array) && array.length == 2) {
                            String requestMethod = array[0];
                            String requsetPath = array[1];
                            Requset requset = new Requset(requestMethod, requsetPath);
                            Handler handler = new Handler(controllerClass, m);
                            ACTION_MAP.put(requset, handler);
                        }
                    }
                }
            }

        }
    }

    public static Handler getHandler(String reqMethod, String reqPath) {
        Requset requset = new Requset(reqMethod, reqPath);
        return ACTION_MAP.get(requset);
    }

}
