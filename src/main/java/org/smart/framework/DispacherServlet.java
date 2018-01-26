package org.smart.framework;

import org.smart.framework.bean.Data;
import org.smart.framework.bean.Handler;
import org.smart.framework.bean.Param;
import org.smart.framework.bean.View;
import org.smart.framework.helper.BeanHelper;
import org.smart.framework.helper.ConfigHelper;
import org.smart.framework.helper.ControllerHelper;
import org.smart.framework.util.*;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@WebServlet(urlPatterns = {"/*"}, loadOnStartup = 0)
public class DispacherServlet extends HttpServlet {


    @Override
    public void init(ServletConfig config) throws ServletException {
        HelperLoader.init();
        // 得到容器
        ServletContext servletContext = config.getServletContext();

        // 注册Servlet 处理JSP请求
        ServletRegistration jspServlet = servletContext.getServletRegistration("jsp");
        jspServlet.addMapping(ConfigHelper.getAppJspPath() + "*");
        //注册Servlet处理静态资源
        ServletRegistration defaultServlet = servletContext.getServletRegistration("default");
        defaultServlet.addMapping(ConfigHelper.getAppAssetPath() + "*");

    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestMethod = req.getMethod().toLowerCase();
        String requestPath = req.getPathInfo();
        // 获取处理类
        System.out.println(requestMethod + " " + requestPath);
        Handler handler = ControllerHelper.getHandler(requestMethod, requestPath);
        if (handler != null) {
            Class<?> controllerClass = handler.getControlClass();
            Object o = BeanHelper.getBean(controllerClass);
            // 创建参数请求对象
            Map<String, Object> map = new HashMap<>();
            Enumeration<String> parameterNames = req.getParameterNames();
            while (parameterNames.hasMoreElements()) {
                String paramName = parameterNames.nextElement();
                String value = req.getParameter(paramName);
                map.put(paramName, value);
            }
            String body = CodeUtil.decodeUrl(StreamUtil.getString(req.getInputStream()));
            if (StringUtil.isNotEmpty(body)) {
                String[] params = body.split("&");
                if (ArrayUtil.isNotEmpty(params)) {
                    for (String param : params) {
                        String[] array = param.split("=");
                        if (ArrayUtil.isNotEmpty(array) && array.length == 2) {
                            String paramName = array[0];
                            String value = array[1];
                            map.put(paramName, value);
                        }
                    }
                }
            }
            Param param = new Param(map);
            Method action = handler.getActionMethod();
            Object result = null;
            if (param.isEmpty()) {
                result = ReflectionUtil.invokeMethod(o, action);
            } else {
                result = ReflectionUtil.invokeMethod(o, action, param);
            }

            if (result instanceof View) {

                View view = (View) result;
                String path = view.getPath();
                if (path.startsWith("/")) {
                    resp.sendRedirect(req.getContextPath() + path);
                } else {
                    Map<String, Object> model = view.getModel();
                    for (Map.Entry<String, Object> entry : model.entrySet()) {
                        req.setAttribute(entry.getKey(), entry.getValue());
                    }
                    req.getRequestDispatcher(ConfigHelper.getAppJspPath() + path).forward(req, resp);
                }
            } else if (result instanceof Data) {
                Data data = (Data) result;
                Object o1 = data.getModel();
                if (o1 != null) {
                    resp.setContentType("application/json");
                    resp.setCharacterEncoding("UTF-8");
                    PrintWriter writer = resp.getWriter();
                    String json = JsonUtil.toJson(o1);
                    writer.write(json);
                    writer.flush();
                    writer.close();
                }
            }
        }
    }
}
