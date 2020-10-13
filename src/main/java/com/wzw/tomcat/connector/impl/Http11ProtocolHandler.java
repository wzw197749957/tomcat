package com.wzw.tomcat.connector.impl;


import com.wzw.tomcat.connector.Connector;
import com.wzw.tomcat.connector.Endpoint;
import com.wzw.tomcat.connector.ProtocolHandler;

import java.io.IOException;
import java.util.concurrent.Executor;

/**
 * http1.1
 */
public class Http11ProtocolHandler implements ProtocolHandler {
    private Executor executor;
    private Connector connector;
    private Endpoint endpoint;

    public Http11ProtocolHandler(Endpoint endpoint) {
        this.endpoint = endpoint;
    }

    @Override
    public void setExecutor(Executor executor) {
        this.executor = executor;
    }

    @Override
    public Connector getConnector() {
        return this.connector;
    }

    @Override
    public void setConnector(Connector connector) {
        this.connector = connector;
    }

    @Override
    public Integer getPort() {
        return this.connector.getPort();
    }

    @Override
    public void init() throws IOException {
        endpoint.init();
    }

    @Override
    public void start() throws IOException {
        endpoint.start();
    }

    @Override
    public Executor getExecutor() {
        return this.executor;
    }
}
