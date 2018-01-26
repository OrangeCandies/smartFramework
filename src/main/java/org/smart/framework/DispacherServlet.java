package org.smart.framework;

import org.smart.framework.bean.Data;
import org.smart.framework.bean.Handler;
import org.smart.framework.bean.Param;
import org.smart.framework.bean.View;
import org.smart.framework.helper.*;
import org.smart.framework.util.JsonUtil;
import org.smart.framework.util.ReflectionUtil;

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


        UploadHelper.init(servletContext);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        //松耦合 使Controller和Service在全局都能拿到Requset和Response
        // 使用ThreadLocal来解决这个问题
        ServletHelper.init(req,resp);

        String requestMethod = req.getMethod().toLowerCase();
        String requestPath = req.getPathInfo();

        //过滤掉favicon.ico 据说会挂掉
        try{
            if(requestPath.equals("/favicon.ico")){
                return;
            }
            // 获取处理类
            Handler handler = ControllerHelper.getHandler(requestMethod, requestPath);
            if (handler != null) {
                Class<?> controllerClass = handler.getControlClass();
                Object o = BeanHelper.getBean(controllerClass);
                // 创建参数请求对象
                Param param = null;
                if(UploadHelper.isMultipart(req)){
                    param = UploadHelper.createParam(req);
                }else{
                    param = RequsetHelper.createParam(req);
                }


                Method action = handler.getActionMethod();
                Object result = null;
                if (param.isEmpty()) {
                    result = ReflectionUtil.invokeMethod(o, action);
                } else {
                    result = ReflectionUtil.invokeMethod(o, action, param);
                }

                if (result instanceof View) {
                    handViewResult((View)result,req,resp);
                } else if (result instanceof Data) {
                    handDataResult((Data)result,resp);
                }
            }
        }finally {
            ServletHelper.destory();
        }
    }

    private void handViewResult(View view,HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
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
    }

    private void handDataResult(Data data,HttpServletResponse resp) throws IOException {
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
