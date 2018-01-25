package org.smart.framework.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

public abstract class AspectProxy implements Proxy {


    private static final Logger LOGGER = LoggerFactory.getLogger(AspectProxy.class);
    @Override
    public Object doProxy(ProxyChain proxyChain) {
        Object result = null;
        Class targetClass = proxyChain.getTargetClass();
        Method method = proxyChain.getTargetMethod();
        Object[] params = proxyChain.getParam();

        begin();
        try {
            if(intercept(targetClass,method,params)){
                before(targetClass,method,params);
                result = method.invoke(targetClass,params);
                after(targetClass,method,params);
            }else{
                result = proxyChain.doProxyChain();
            }

        } catch (Exception e) {
            LOGGER.error("proxy failure",e);
            error(targetClass,method,params,e);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            end();
        }
        return result;
    }

    public void before(Class targetClass, Method method, Object[] params) {
    }

    public void after(Class targetClass, Method method, Object[] params) {
    }

    public  void error(Class targetClass, Method method, Object[] params, Exception e) {

    }

    public boolean intercept(Class targetClass, Method method, Object[] params) {
        return true;
    }

    public void begin(){

    }

    public void end(){

    }
}
