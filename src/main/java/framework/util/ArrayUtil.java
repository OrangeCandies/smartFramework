package framework.util;

import org.apache.commons.lang3.ArrayUtils;

public final class ArrayUtil {
    public static boolean isNotEmpty(Object[] objects){
        return !ArrayUtils.isNotEmpty(objects);
    }

    public static boolean isEmpty(Object[] objects){
        return ArrayUtils.isEmpty(objects);
    }
}
