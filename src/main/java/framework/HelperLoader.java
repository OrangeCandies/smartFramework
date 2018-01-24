package framework;

import framework.helper.ClassHelper;
import framework.helper.ConfigHelper;
import framework.helper.ControllerHelper;
import framework.helper.IocHelper;
import framework.util.ClassUtil;

public final class HelperLoader {

    public static void init() {
        Class[] classes = new Class[]{
                ConfigHelper.class,
                IocHelper.class,
                ClassHelper.class,
                ControllerHelper.class
        };
        for(Class c:classes){
            ClassUtil.loadClass(c.getName(),true);
        }
    }
}
