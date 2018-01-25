package org.smart.framework.util;

public final class CaseUtil {
    public static String caseString(Object object){
        return caseString(object,"");
    }

    public static String caseString(Object object,String defalultValue){
        return object != null ? String.valueOf(object):defalultValue;
    }

    public static double caseDouble(Object object){
        return caseDouble(object,0.0);
    }

    public static double caseDouble(Object object,double defalultValue){
        double value = defalultValue;
        if(object != null){
            String strValue = caseString(object);
            if(StringUtil.isNotEmpty(strValue)){
                try{
                    value = Double.parseDouble(strValue);
                }catch (NumberFormatException e){
                    value = defalultValue;
                }
            }
        }
        return value;
    }

    public static int caseInt(Object o,int defaultValue){
        int value = defaultValue;
        if(o != null){
            String vString = caseString(o);
            if(StringUtil.isNotEmpty(vString)){
                try{
                    value = Integer.parseInt(vString);
                }catch (NumberFormatException e){
                    e.printStackTrace();
                    value = defaultValue;
                }
            }
        }
        return value;
    }

    public static int caseInt(Object o){
       return caseInt(o,0);
    }
    public static long caseLong(Object o,long defaultValue){
        long value = defaultValue;
        if(o != null){
            String vString = caseString(o);
            if(StringUtil.isNotEmpty(vString)){
                try{
                    value = Long.parseLong(vString);
                }catch (NumberFormatException e){
                    e.printStackTrace();
                    value = defaultValue;
                }
            }
        }
        return value;
    }

    public static long caseLong(Object o){
        return caseLong(o,1L);
    }

    public static boolean caseBoolean(Object o,boolean defaultValue){
        boolean value = defaultValue;
        if(o != null){
            String vString = caseString(o);
            if(StringUtil.isNotEmpty(vString)){
                try{
                    value = Boolean.parseBoolean(vString);
                }catch (NumberFormatException e){
                    e.printStackTrace();
                    value = defaultValue;
                }
            }
        }
        return value;
    }

    public static boolean caseBoolean(Object o){
        return caseBoolean(o,false);
    }
}
