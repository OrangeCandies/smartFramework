package org.smart.framework.proxy;

import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ProxyChain {

    private final Class<?> targetClass;
    private final Object targetObject;
    private final Method targetMethod;
    private final MethodProxy methodProxy;
    private final Object[] param;

    private List<Proxy> proxies = new ArrayList<>();
    private int proxyIndex = 0;

    public ProxyChain(Class<?> targetClass, Object targetObject, Method targetMethod, MethodProxy methodProxy,
                      Object[] param, List<Proxy> proxies) {
        this.targetClass = targetClass;
        this.targetObject = targetObject;
        this.targetMethod = targetMethod;
        this.methodProxy = methodProxy;
        this.param = param;
        this.proxies = proxies;
    }

    public Object getTargetObject() {
        return targetObject;
    }

    public Method getTargetMethod() {
        return targetMethod;
    }

    public Object[] getParam() {
        return param;
    }

    public Object doProxyChain() throws Throwable{
        Object result = null;
        if(proxyIndex < proxies.size()){
            result = proxies.get(proxyIndex++).doProxy(this);
        }else{
            result = methodProxy.invokeSuper(targetObject,param);
        }
        return result;
    }

}
