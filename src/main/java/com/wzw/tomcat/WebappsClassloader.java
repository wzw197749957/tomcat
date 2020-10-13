package com.wzw.tomcat;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 加载Web的/WEB-INF/classes/下的class
 */
public class WebappsClassloader extends URLClassLoader {

    private static final String CLASS_FILE_SUFFIX  = ".class";

    private Map<String,Class<?>> classMap = new ConcurrentHashMap<>();

    private ClassLoader javaseClassLoader;

    private ClassLoader parent;

    public WebappsClassloader(URL[] urls) {
        super(urls);
        ClassLoader p = getParent();
        if (p == null) {
            p = getSystemClassLoader();
        }
        this.parent = p;

        ClassLoader j = String.class.getClassLoader();
        if (j == null) {
            j = getSystemClassLoader();
            while (j.getParent() != null) {
                j = j.getParent();
            }
        }
        this.javaseClassLoader = j;


    }

    protected Class<?> findLoadedClass0(String name){
        String path = binaryNameToPath(name, true);
        return classMap.get(path);
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        synchronized (getClassLoadingLock(name)) {
            Class<?> clazz = null;
            // 查找定义缓存中是否已经加载了该类。
            clazz = findLoadedClass0(name);
            if (clazz != null){
                if (resolve)
                    resolveClass(clazz);
                return clazz;
            }

            // 调用父类查看是否已经加载该类
            clazz = findLoadedClass(name);
            if (clazz != null){
                if (resolve)
                    resolveClass(clazz);
                return clazz;
            }

            // 尝试从java
            String resourceName = binaryNameToPath(name, false);
            ClassLoader javaseLoader = getJavaseClassLoader();
            boolean tryLoadingFromJavaseLoader;
            try {
                URL url = javaseLoader.getResource(resourceName);
                tryLoadingFromJavaseLoader = (url != null);
            } catch (Throwable t) {
                tryLoadingFromJavaseLoader = true;
            }

            if (tryLoadingFromJavaseLoader) {
                try {
                    clazz = javaseLoader.loadClass(name);
                    if (clazz != null) {
                        if (resolve)
                            resolveClass(clazz);
                        return clazz;
                    }
                } catch (ClassNotFoundException e) {
                    // Ignore
                }
            }

            // 调用父类的加载
            try {
                clazz = Class.forName(name, false, parent);
                if (clazz != null){
                    return clazz;
                }
            }catch (Exception e){

            }


            clazz = findClass(name);
            if (clazz != null){
                String key = binaryNameToPath(name, true);
                classMap.put(key ,clazz);
                return clazz;
            }
        }
        throw new ClassNotFoundException(name);
    }

    private ClassLoader getJavaseClassLoader() {
        return this.javaseClassLoader;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        String path = binaryNameToPath(name , true);
        URL[] urLs = super.getURLs();
        File classPath = null;
        for (URL url : urLs){
            File base = null;
            try {
                base = new File(url.toURI());
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            classPath = new File(base,path);
            if(classPath.exists()){
                break;
            }
            classPath = null;
        }
        if (classPath == null){
            throw new ClassNotFoundException();
        }
        byte[] classBytes = loadClassBytes(classPath);
        if (classBytes == null){
            throw new ClassNotFoundException();
        }
        return this.defineClass(name,classBytes,0,classBytes.length);
    }

    private byte[] loadClassBytes(File classFile){
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            FileInputStream fis = new FileInputStream(classFile);
            byte []buffer = new byte[1024];
            int len = 0;
            while ((len = fis.read(buffer))!=-1){
                bos.write(buffer,0,len);
            }
            bos.flush();
            return bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String binaryNameToPath(String binaryName, boolean withLeadingSlash) {
        StringBuilder path = new StringBuilder(7 + binaryName.length());
        if (withLeadingSlash) {
            path.append('/');
        }
        path.append(binaryName.replace('.', '/'));
        path.append(CLASS_FILE_SUFFIX);
        return path.toString();
    }


    private String nameToPath(String name) {
        if (name.startsWith("/")) {
            return name;
        }
        StringBuilder path = new StringBuilder(
                1 + name.length());
        path.append('/');
        path.append(name);
        return path.toString();
    }
}
