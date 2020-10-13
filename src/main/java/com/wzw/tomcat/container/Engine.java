package com.wzw.tomcat.container;


import com.wzw.tomcat.lifecycle.Lifecycle;

/**
 * 引擎容器
 */
public interface Engine extends Lifecycle {
    Service getService();

    void setService(Service service);

    void setHosts(Host[] hosts);

    Host[] getHosts();
}
