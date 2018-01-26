package org.smart.framework.helper;

import org.smart.framework.bean.FormParam;
import org.smart.framework.bean.Param;
import org.smart.framework.util.ArrayUtil;
import org.smart.framework.util.CodeUtil;
import org.smart.framework.util.StreamUtil;
import org.smart.framework.util.StringUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public final class RequsetHelper {

    public static Param createParam(HttpServletRequest request) throws IOException {
        List<FormParam> formParams = new ArrayList<>();
        formParams.addAll(parseParameterNames(request));
        formParams.addAll(praseInputStream(request));
        return new Param(formParams);
    }

    public static List<FormParam> parseParameterNames(HttpServletRequest request) {
        List<FormParam> formParams = new ArrayList<>();
        Enumeration<String> parameterNames = request.getParameterNames();
        while(parameterNames.hasMoreElements()){
            String key = parameterNames.nextElement();
            String[] values = request.getParameterValues(key);
            if(ArrayUtil.isNotEmpty(values)){
                String value = null;
                if(values.length == 1){
                    value = values[0];
                }else{
                    StringBuilder sb = new StringBuilder("");
                    for(int i=0;i<values.length;i++){
                        sb.append(values[i]);
                        if(i != values.length-1){
                            sb.append(StringUtil.SEPARATOR);
                        }
                    }
                    value = sb.toString();
                }
                formParams.add(new FormParam(key,value));
            }
        }

        return formParams;
    }

    private static List<FormParam> praseInputStream(HttpServletRequest request) throws IOException {

        List<FormParam> paramLists = new ArrayList<>();
        String body = CodeUtil.decodeUrl(StreamUtil.getString(request.getInputStream()));
        if (StringUtil.isNotEmpty(body)) {
            String[] params = body.split("&");
            if (ArrayUtil.isNotEmpty(params)) {
                for (String param : params) {
                    String[] array = param.split("=");
                    if (ArrayUtil.isNotEmpty(array) && array.length == 2) {
                        String paramName = array[0];
                        String value = array[1];
                        paramLists.add(new FormParam(paramName, value));
                    }
                }
            }
        }
        return paramLists;
    }
}
