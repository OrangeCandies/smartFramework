package framework.helper;

import framework.annocation.Controller;
import framework.annocation.Service;
import framework.util.ClassUtil;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class ClassHelper {
    private static final Set<Class<?>> CLASS_SET;

    static {
        String basePackage = ConfigHelper.getAppBasePackage();
        CLASS_SET = ClassUtil.getClassSet(basePackage);
    }

    public static Set<Class<?>> getClassSet() {
        return CLASS_SET;
    }

    public static Set<Class<?>> getServiceClass() {
        Set<Class<?>> set = CLASS_SET.stream().filter(cls -> cls.isAnnotationPresent(Service.class)).collect(Collectors.toSet());
        return set;
    }

    public static Set<Class<?>> getControlClass() {
        Set<Class<?>> set = CLASS_SET.stream().filter(cls -> cls.isAnnotationPresent(Controller.class)).collect(Collectors.toSet());
        return set;
    }

    public static Set<Class<?>> getClassBySuper(Class<?> superClass) {
        Set<Class<?>> set = CLASS_SET.stream().filter(cls -> cls.isAssignableFrom(superClass) && !superClass.equals(cls)).collect(Collectors.toSet());
        return set;
    }

    public static Set<Class<?>> getClassSetByAnnotation(Class<? extends Annotation> annotation) {
        Set<Class<?>> set = CLASS_SET.stream().filter(cls -> cls.isAnnotationPresent(annotation)).collect(Collectors.toSet());
        return set;
    }

    public static Set<Class<?>> getBeanClass() {
        Set<Class<?>> set = new HashSet<>();
        set.addAll(getServiceClass());
        set.addAll(getControlClass());
        return set;
    }

}
