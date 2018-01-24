package framework.helper;

import framework.bean.Handler;
import framework.bean.Requset;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class ControllerHelper {

    /**
     *  大致是每一个请求路径和请求方法对于一个处理类和处理方法
     */
    private static final Map<Requset,Handler> ACTION_MAP = new HashMap<>();

    static{
        Set<Class<?>> controllerClasses = ClassHelper.getControlClass();
        for(Class<?> controllerClass:controllerClasses){
            
        }
    }

}
