package com.wzw.tomcat.connector;


import com.wzw.tomcat.container.Server;
import com.wzw.tomcat.container.Service;
import com.wzw.tomcat.lifecycle.AbstractLifecycle;

import java.io.IOException;

/**
 * 连接器
 */
public class Connector extends AbstractLifecycle {
    //端口
    private Integer port;
    private Service service;
    private Server server;
    private ProtocolHandler protocolHandler;

    public Connector(ProtocolHandler protocolHandler) {
        this.protocolHandler = protocolHandler;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public Server getServer() {
        return this.server;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    @Override
    protected void initImpl() {
        try {
            protocolHandler.init();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void startImpl() {
        try {
            protocolHandler.setExecutor(server.getExecutor());
            protocolHandler.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void stopImpl() {

    }

    @Override
    protected void destoryImpl() {

    }
}
