package org.smart.framework.bean;

import org.smart.framework.util.CaseUtil;
import org.smart.framework.util.CollectionUtil;
import org.smart.framework.util.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Param {

   private List<FormParam> formParams;
   private List<FileParam> fileParams;

    public Param(List<FormParam> formParams, List<FileParam> fileParams) {
        this.formParams = formParams;
        this.fileParams = fileParams;
    }

    public Map<String,Object> getFiledMap(){
        Map<String,Object> map = new HashMap<>();
        if(CollectionUtil.isNotEmpty(formParams)){
            for(FormParam f:formParams){
                String fieldName = f.getFieldName();
                Object fieldValue = f.getFieldValue();
                if(map.containsKey(fieldName)){
                  fieldValue = map.get(fieldName)+ StringUtil.SEPARATOR+fieldValue;
                }
                map.put(fieldName,fieldValue);
            }
        }
        return map;
    }

    public Map<String,List<FileParam>> getFileMap(){
        Map<String,List<FileParam>> fileMap = new HashMap<>();
        if(CollectionUtil.isNotEmpty(fileParams)){
           for(FileParam fileParam : fileParams){
               String fieldName = fileParam.getFieldName();
               List<FileParam> fileParamList;
               if(fileMap.containsKey(fileParam)){
                   fileParamList = fileMap.get(fileParam);
               }else{
                   fileParamList = new ArrayList<>();
               }
               fileParamList.add(fileParam);
               fileMap.put(fieldName,fileParamList);
           }
        }
        return fileMap;
    }

    public List<FileParam> getFileList(String fieldName){
        return getFileMap().get(fieldName);
    }

    public FileParam getFile(String fieldName){
        List<FileParam>  params = getFileMap().get(fieldName);
        if(CollectionUtil.isNotEmpty(params) && params.size() == 1){
            return params.get(0);
        }
        return null;
    }

    public boolean isEmpty(){
        return CollectionUtil.isEmpty(fileParams)&&CollectionUtil.isEmpty(formParams);
    }

    public String getString(String name){
        return CaseUtil.caseString(getFiledMap().get(name));
    }
    public double getDouble(String name){
        return CaseUtil.caseDouble(getFiledMap().get(name));
    }
    public long getLong(String name){
        return CaseUtil.caseLong(getFiledMap().get(name));
    }
    public int getInt(String name){
        return CaseUtil.caseInt(getFiledMap().get(name));
    }
    public boolean getBoolean(String name){
        return CaseUtil.caseBoolean(getFiledMap().get(name));
    }



}
