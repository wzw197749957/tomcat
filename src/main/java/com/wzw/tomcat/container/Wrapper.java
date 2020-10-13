package com.wzw.tomcat.container;


import com.wzw.tomcat.Servlet;

public interface Wrapper {
    Servlet getServlet();

    void setServlet(Servlet servlet);

    void setUrlPattern(String urlPattern);

    String getUrlPattern();

    void setClassName(String className);

    String getClassName();

    void setServletName(String servletName);

    String getServletName();

    void setContext(Context context);
}
