package org.smart.framework.helper;

import org.smart.framework.annocation.Inject;
import org.smart.framework.util.CollectionUtil;
import org.smart.framework.util.ReflectionUtil;
import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.Field;
import java.util.Map;

public final class IocHelper {

    static{
        Map<Class<?>,Object> beanMap = BeanHelper.getBeanMap();
        System.out.println(beanMap.size());
        if(CollectionUtil.isNotEmpty(beanMap)){
            System.out.println("in it");
            for(Map.Entry<Class<?>,Object>entry: beanMap.entrySet()){
                Class<?> t = entry.getKey();
                Object value = entry.getValue();

                Field [] fields = t.getFields();
                if(ArrayUtils.isNotEmpty(fields)){
                    for(Field f:fields){
                        if(f.isAnnotationPresent(Inject.class)){
                            System.out.println(f);
                            Class<?> beanType = f.getType();
                            Object instance = beanMap.get(beanType);
                            if(instance != null){
                                ReflectionUtil.setFiled(value,f,instance);
                            }
                        }
                    }
                }
            }
        }
    }
}
