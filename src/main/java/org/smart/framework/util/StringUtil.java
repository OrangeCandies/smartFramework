package org.smart.framework.util;

import org.apache.commons.lang3.StringUtils;

public final class StringUtil {

    public static boolean isEmpty(String str){
        if(str != null){
            str = StringUtils.trim(str);
        }
        return StringUtils.isEmpty(str);
    }

    public static boolean isNotEmpty(String str){
        return !isEmpty(str);
    }
}
