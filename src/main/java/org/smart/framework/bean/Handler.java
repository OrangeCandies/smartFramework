package org.smart.framework.bean;

import java.lang.reflect.Method;

public class Handler {
    private Class<?> controlClass;
    private Method actionMethod;

    public Handler(Class<?> controlClass, Method actionMethod) {
        this.controlClass = controlClass;
        this.actionMethod = actionMethod;
    }

    public Class<?> getControlClass() {
        return controlClass;
    }

    public Method getActionMethod() {
        return actionMethod;
    }
}
