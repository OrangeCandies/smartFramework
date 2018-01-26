package org.smart.framework.helper;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class ServletHelper {

    private static final ThreadLocal<ServletHelper> SERVLET_HELPER_THREAD_LOCAL = new ThreadLocal<>();

    private HttpServletRequest request;
    private HttpServletResponse response;


    public ServletHelper(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }

    public static void init(HttpServletRequest request, HttpServletResponse response){
        SERVLET_HELPER_THREAD_LOCAL.set(new ServletHelper(request,response));
    }

    public static void destory(){
        SERVLET_HELPER_THREAD_LOCAL.remove();
    }

    private static HttpServletRequest getRequest() {
        return SERVLET_HELPER_THREAD_LOCAL.get().request;
    }

    private static HttpServletResponse getRespone(){
        return SERVLET_HELPER_THREAD_LOCAL.get().response;
    }

    private static HttpSession getSession(){
        return getRequest().getSession();
    }

    private static ServletContext getServletContext(){
        return getRequest().getServletContext();
    }

    public static void setRequestAttribute(String key,Object value){
        getRequest().setAttribute(key,value);
    }

    public static <T> T getRequsetAttribute(String key){
        return (T)getRequest().getAttribute(key);
    }

    public static void removeRequsetAttribute(String key){
        getRequest().removeAttribute(key);
    }

    public static void sendRedirect(String location){
        try {
            getRespone().sendRedirect(getRequest().getContextPath()+location);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setSessionAttribute(String key,Object value){
        getSession().setAttribute(key, value);
    }

    public static<T> T getSessionAttribute(String key){
        return (T)getSession().getAttribute(key);
    }

    public static void removeSessionAttribute(String key){
        getSession().removeAttribute(key);
    }

    //使会话失效
    public static void invalidateSession(){
        getSession().invalidate();
    }

}
