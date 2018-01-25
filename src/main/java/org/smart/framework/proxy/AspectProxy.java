package org.smart.framework.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

public abstract class AspectProxy implements Proxy {


    private static final Logger LOGGER = LoggerFactory.getLogger(AspectProxy.class);
    @Override
    public Object doProxy(ProxyChain proxyChain) {
        Object result = null;
        Object target = proxyChain.getTargetObject();
        Method method = proxyChain.getTargetMethod();
        Object[] params = proxyChain.getParam();

        begin();
        try {
            if(intercept(target,method,params)){
                before(target,method,params);
                result = method.invoke(target,params);
                after(target,method,params);
            }else{
                result = proxyChain.doProxyChain();
            }

        } catch (Exception e) {
            LOGGER.error("proxy failure",e);
            error(target,method,params,e);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            end();
        }
        return result;
    }

    public void before(Object targetClass, Method method, Object[] params) {
    }

    public void after(Object targetClass, Method method, Object[] params) {
    }

    public  void error(Object targetClass, Method method, Object[] params, Exception e) {

    }

    public boolean intercept(Object targetClass, Method method, Object[] params) {
        return true;
    }

    public void begin(){

    }

    public void end(){

    }
}
