package framework.helper;

import framework.annocation.Action;
import framework.bean.Handler;
import framework.bean.Requset;
import framework.util.ArrayUtil;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class ControllerHelper {

    /**
     * 大致是每一个请求路径和请求方法对于一个处理类和处理方法
     */
    private static final Map<Requset, Handler> ACTION_MAP = new HashMap<>();

    static {
        Set<Class<?>> controllerClasses = ClassHelper.getControlClass();
        for (Class<?> controllerClass : controllerClasses) {
            Method[] methods = controllerClass.getMethods();
            for (Method m : methods) {
                if (m.isAnnotationPresent(Action.class)) {
                    Action action = m.getAnnotation(Action.class);
                    String mapping = action.value();

                    if (mapping.matches("\\w:/\\w*")) {
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
