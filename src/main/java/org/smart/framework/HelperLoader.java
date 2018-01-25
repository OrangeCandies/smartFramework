package org.smart.framework;

import org.smart.framework.helper.*;
import org.smart.framework.util.ClassUtil;

public final class HelperLoader {

    public static void init() {
        Class[] classes = new Class[]{
                ConfigHelper.class,
                ClassHelper.class,
                AopHelper.class,
                IocHelper.class,
                ControllerHelper.class

        };
        for(Class c:classes){
            ClassUtil.loadClass(c.getName(),true);
        }
    }
}
