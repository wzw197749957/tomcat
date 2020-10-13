package com.wzw.tomcat.container.impl;

import com.google.common.collect.Maps;
import com.wzw.tomcat.Servlet;
import com.wzw.tomcat.container.Context;
import com.wzw.tomcat.container.Wrapper;

import java.util.Map;
import java.util.Set;

public class ContextImpl implements Context {
    private Map<String, Wrapper> wrapperMap = Maps.newConcurrentMap();
    private String contextName;
    private ClassLoader classLoader;

    @Override
    public void setContextName(String contextName) {
        this.contextName = contextName;
    }

    @Override
    public String getContextName() {
        return this.contextName;
    }

    @Override
    public void setWrapper(Wrapper wrapper) {
        wrapperMap.put(wrapper.getUrlPattern(), wrapper);
    }

    @Override
    public Wrapper getWrapper(String urlPattern) {
        return wrapperMap.get(urlPattern);
    }

    @Override
    public void setClassloader(ClassLoader classloader) {
        this.classLoader = classloader;
    }

    @Override
    public void init() {
        Set<Map.Entry<String, Wrapper>> entries = wrapperMap.entrySet();
        for (Map.Entry<String, Wrapper> entry : entries) {
            Wrapper value = entry.getValue();
            try {
                Class<?> aClass = this.classLoader.loadClass(value.getClassName());
                Servlet servlet = (Servlet) aClass.newInstance();
                value.setServlet(servlet);
                servlet.init();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void destory() {

    }
}
