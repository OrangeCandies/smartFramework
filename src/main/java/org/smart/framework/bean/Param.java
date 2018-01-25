package org.smart.framework.bean;

import org.smart.framework.util.CaseUtil;

import java.util.Map;

public class Param {

    private Map<String,Object> paramMap;

    public Param(Map<String,Object> paramMap){
        this.paramMap = paramMap;
    }

    public long getLong(String name){
        return CaseUtil.caseLong(paramMap.get(name));
    }

    public Map<String, Object> getMap() {
        return paramMap;
    }
}
