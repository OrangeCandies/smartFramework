package org.smart.framework.proxy;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.List;

public class ProxyManager {

    public static <T> T createProxy(final Class<?> targetClass, final List<Proxy> proxies) {
        return (T) Enhancer.create(targetClass, new MethodInterceptor() {

            /**
             *        实际包装类应该修改的地方，此处调用一个代理链
             *        在代理链的末端会主动调用代理方法，再返回回来
             * @param o
             * @param method
             * @param objects
             * @param methodProxy
             * @return
             * @throws Throwable
             */
            @Override
            public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
               return new ProxyChain(targetClass, o, method, methodProxy, objects, proxies).doProxyChain();
            }
        });
    }
}
