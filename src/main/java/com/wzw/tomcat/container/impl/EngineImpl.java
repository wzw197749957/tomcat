package com.wzw.tomcat.container.impl;

import com.wzw.tomcat.container.Engine;
import com.wzw.tomcat.container.Host;
import com.wzw.tomcat.container.Service;

public class EngineImpl implements Engine {
    private Service service;
    private Host[] hosts = new Host[0];

    @Override
    public Service getService() {
        return this.service;
    }

    @Override
    public void setService(Service service) {
        this.service = service;
    }

    @Override
    public void setHosts(Host[] hosts) {
        this.hosts = hosts;
    }

    @Override
    public Host[] getHosts() {
        return this.hosts;
    }

    @Override
    public void init() {
        for (int i = 0; i < hosts.length; i++) {
            hosts[i].init();
        }
    }

    @Override
    public void start() {
        for (int i = 0; i < hosts.length; i++) {
            hosts[i].start();
        }
    }

    @Override
    public void stop() {

    }

    @Override
    public void destory() {

    }
}
