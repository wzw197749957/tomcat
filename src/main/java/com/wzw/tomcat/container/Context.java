package com.wzw.tomcat.container;


import com.wzw.tomcat.lifecycle.Lifecycle;

/**
 * 应用上下文
 */
public interface Context extends Lifecycle {

    void setContextName(String contextName);

    String getContextName();

    void setWrapper(Wrapper wrapper);

    Wrapper getWrapper(String urlPattern);

    void setClassloader(ClassLoader classloader);

}
