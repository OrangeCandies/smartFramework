# smartFramework
自己动手实现的一个类似于SpringMvc的MVC框架优点有
1. 轻量的MVC框架，基于注解的配置
2. 自己完成了类似于Spring Core和Context模块动态类加载和扫描注册,管理
3. 当前端控制器接受到请求后会根据请求方法和请求路径生成Request,对应着一个确定的Controller的方法完成数据的绑定以及后面jsp的调用
4. 实现了基于注解AOP 通过继承AOP的模板类并重写需要的方法替换Ioc容器的被代理类实现代理
5. 对于某个类有多个代理注解 会生成一条代理链 依次递归调用代理链直到到达代理链的末端再调用方法
6. 实现了基于注解的事务提交和回滚
7. 通过Apache的工具提供了文件上传和安全控制


对于整个框架继续做一个总结
0. 启动初始化阶段
   整个项目有一个显示的Servlet即DispacherServlet,这个Servlet会接受所有的请求并进行处理
   在DispacherServlet的init()方法中会初始化整个框架，首先会调用HelperLoader.init()方法
   此方法是显式加载几个Helper类完成框架基本功能的初始化，对应的功能如下
    ConfigHelper.class --- 加载和读取配置信息 例如资源目录，包名和数据库连接信息
    ClassHelper.class  --- 扫描整个包并将所有的类加载起来并统一管理
    BeanHelper.class   ---- 为每个Class创建实例并管理起来
    AopHelper.class   --- 为被代理的类重新注入实例对象
    IocHelper.class  ---- 扫描带有@injue注解的域并根据类型注入实例对象
    Controller.class ---  根据Controller类的方法注解 生成了Requst -- Handler 对象关系
    
    DispacherServleth还会注册2个动态的Servlet为静态资源和JSP资源提供逃逸。
