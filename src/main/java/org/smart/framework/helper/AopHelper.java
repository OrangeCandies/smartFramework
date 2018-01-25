package org.smart.framework.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smart.framework.annocation.Aspect;
import org.smart.framework.proxy.AspectProxy;
import org.smart.framework.proxy.Proxy;
import org.smart.framework.proxy.ProxyManager;

import java.lang.annotation.Annotation;
import java.util.*;

public final class AopHelper {


    private static final Logger LOGGER = LoggerFactory.getLogger(AopHelper.class);

    /**
     * 将增强类注入到IOC容器中
     */
    static {
        try {
            Map<Class<?>, Set<Class<?>>> proxyMap = createProxyMap();
            Map<Class<?>, List<Proxy>> targetMap = createTargetMap(proxyMap);
            for (Map.Entry<Class<?>, List<Proxy>> entry : targetMap.entrySet()) {
                Class<?> c = entry.getKey();
                List<Proxy> proxies = entry.getValue();
                Object proxy = ProxyManager.createProxy(c, proxies);
                BeanHelper.setBean(c, proxy);
            }
        } catch (Exception e) {
            LOGGER.error("注入增强类失败", e);
        }
    }

    /**
     * 取出所有的标注有Aspect注解并且继承自AspectProxy代理模板类的类 产生一对多的映射关系 即一个代理类和所有被类的关系
     *
     * @return
     * @throws Exception
     */
    private static Map<Class<?>, Set<Class<?>>> createProxyMap() throws Exception {
        Map<Class<?>, Set<Class<?>>> proxyMap = new HashMap<>();
        Set<Class<?>> proxyClasses = ClassHelper.getClassBySuper(AspectProxy.class);
        for (Class<?> t : proxyClasses) {
            if (t.isAnnotationPresent(Aspect.class)) {
                Aspect aspect = t.getAnnotation(Aspect.class);
                Set<Class<?>> targetClass = createTargetClass(aspect);
                proxyMap.put(t, targetClass);
            }
        }
        return proxyMap;
    }

    /**
     * 取出所有标注有Aspect的类并包装成集合  proxyClass -> targetClass 一对多关系
     *
     * @param aspect
     * @return
     * @throws Exception
     */
    private static Set<Class<?>> createTargetClass(Aspect aspect) throws Exception {
        Set<Class<?>> classes = new HashSet<>();
        Class<? extends Annotation> annotation = aspect.value();
        if (annotation != null && !annotation.equals(Aspect.class)) {
            classes.addAll(ClassHelper.getClassSetByAnnotation(annotation));
        }
        return classes;
    }

    /**
     * 根据代理类和被代理类的一对多关系
     * 因为每个被代理类可能不止有一个增强 因此产生一个代理链 并注入IOC管理模块中代替原来的本类
     * ProoxyChain.doProxyChain()
     *
     * @param proxyClasses
     * @return
     * @throws Exception
     */

    private static Map<Class<?>, List<Proxy>> createTargetMap(Map<Class<?>, Set<Class<?>>> proxyClasses) throws Exception {
        Map<Class<?>, List<Proxy>> targetMap = new HashMap<>();
        for (Map.Entry<Class<?>, Set<Class<?>>> proxyMap : proxyClasses.entrySet()) {

            Class<?> proxy = proxyMap.getKey();
            for (Class<?> c : proxyMap.getValue()) {
                Proxy proxyClass = (Proxy) proxy.newInstance();
                if (targetMap.containsKey(c)) {
                    List<Proxy> proxies = targetMap.get(c);
                    proxies.add(proxyClass);
                } else {
                    List<Proxy> proxies = new ArrayList<>();
                    proxies.add(proxyClass);
                    targetMap.put(c, proxies);
                }
            }
        }
        return targetMap;
    }

}
