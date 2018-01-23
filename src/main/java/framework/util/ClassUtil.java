package framework.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ClassUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClassUtil.class);

    /**
     * 得到该类的类加载器用于加载指定包下的类
     * @return
     */
    public static ClassLoader getClassLoager(){
        return Thread.currentThread().getContextClassLoader();
    }

    /**
     *         借助获取的此类的类加载器进行指定包下的类加载工作
     * @param className
     * @param isInitialized
     * @return
     */
    public static Class<?> loadClass(String className,boolean isInitialized){
        Class<?> t = null;
        try {
            t = Class.forName(className,isInitialized,getClassLoager());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            LOGGER.error("load class failed",e);
            throw new RuntimeException(e);
        }

        return t;

    }

    /**
     *        拿到指定包下的所有的类对象的集合
     *        在拿到的过程中会去主动加载
     *        packageName 下有jar或者其他文件类型
     *        对于jar文件进行加载 其他文件扔给addClass进行加载
     * @param packageName
     * @return
     */
    public static Set<Class<?>> getClassSet(String packageName){
        Set<Class<?>> classes = new HashSet<Class<?>>();
        try{
            Enumeration<URL> urls = getClassLoager().getResources(packageName.replace('.','/'));
            while(urls.hasMoreElements()){
                URL url = urls.nextElement();
                if(url != null){
                    String protocol = url.getProtocol();
                    if(protocol.equals("file")){
                        //class文件走此加载方式
                        String packagePath = url.getPath().replaceAll("%20"," ");
                        addClass(classes,packagePath,packageName);
                    }else if(protocol.equals("jar")){
                        // jar 文件 拿到所有的classw文件加载
                        JarURLConnection connection = (JarURLConnection) url.openConnection();
                        if(connection != null){
                            JarFile jarFile = connection.getJarFile();
                            if(jarFile != null){
                                Enumeration<JarEntry> jarEntities = jarFile.entries();
                                while(jarEntities.hasMoreElements()){
                                    JarEntry jarEntry = jarEntities.nextElement();
                                    String name = jarEntry.getName();
                                    if(name.endsWith(".class")){
                                        String className = name.substring(0,name.lastIndexOf('.')).replaceAll(".","/");
                                        doAddClass(classes,className);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return classes;
    }

    private static void addClass(Set<Class<?>> classSet, String packagePath, final String packageName){
        File[] files = new File(packagePath).listFiles(new FileFilter() {
            public boolean accept(File pathname) {
                return (pathname.isFile()&&pathname.getName().endsWith(".class") || pathname.isDirectory());
            }
        });
        for(File file:files){
            String fileName = file.getName();
            if(file.isFile()){
                String className = fileName.substring(0,fileName.lastIndexOf('.'));
                if(StringUtil.isNotEmpty(packageName)){
                    className = packageName+"."+className;
                }
                doAddClass(classSet,className);
            }else{
                String subPackagePath = fileName;
                if(StringUtil.isNotEmpty(subPackagePath)){
                    subPackagePath = packagePath+"/"+subPackagePath;
                }
                String subPackageName = fileName;
                if(StringUtil.isNotEmpty(subPackageName)){
                    subPackageName = packageName+"."+subPackageName;
                }
                addClass(classSet,packagePath,packageName);
            }
        }
    }

    private static void doAddClass(Set<Class<?>> classSet,String className){
        Class<?> cls = loadClass(className,false);
        classSet.add(cls);
    }
}
