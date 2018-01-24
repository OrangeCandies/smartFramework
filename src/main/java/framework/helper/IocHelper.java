package framework.helper;

import framework.annocation.Inject;
import framework.util.CollectionUtil;
import framework.util.ReflectionUtil;
import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.Field;
import java.util.Map;

public final class IocHelper {

    static{
        Map<Class<?>,Object> beanMap = BeanHelper.getBeanMap();
        if(CollectionUtil.isNotEmpty(beanMap)){

            for(Map.Entry<Class<?>,Object>entry: beanMap.entrySet()){
                Class<?> t = entry.getKey();
                Object value = entry.getValue();

                Field [] fields = t.getFields();
                if(ArrayUtils.isNotEmpty(fields)){
                    for(Field f:fields){
                        if(f.isAnnotationPresent(Inject.class)){
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
