package org.smart.aspect;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smart.framework.annocation.Aspect;
import org.smart.framework.annocation.Controller;
import org.smart.framework.proxy.AspectProxy;

import java.lang.reflect.Method;


@Aspect(Controller.class)
public class ControllerAspect extends AspectProxy {

    private static final Logger LOGGER = LoggerFactory.getLogger(ControllerAspect.class);
    private long beginTime = 0;
    @Override
    public void before(Object targetClass, Method method, Object[] params) {

        LOGGER.warn("Time begin");

        beginTime = System.currentTimeMillis();
    }

    @Override
    public void after(Object targetClass, Method method, Object[] params) {
        LOGGER.warn("This service cost "+(System.currentTimeMillis()-beginTime)+" ms");
    }

}
