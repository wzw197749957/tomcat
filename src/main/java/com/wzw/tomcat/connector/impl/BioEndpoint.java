package com.wzw.tomcat.connector.impl;


import com.wzw.tomcat.RequestProcessor;
import com.wzw.tomcat.connector.Endpoint;
import com.wzw.tomcat.connector.ProtocolHandler;
import com.wzw.tomcat.listener.ConnectorListener;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class BioEndpoint implements Endpoint {
    private ProtocolHandler protocolHandler;
    private ConnectorListener connectorListener;
    private ServerSocket serverSocket;

    public BioEndpoint(ConnectorListener connectorListener) {
        this.connectorListener = connectorListener;
    }

    @Override
    public void setProtocolHandler(ProtocolHandler protocolHandler) {
        this.protocolHandler = protocolHandler;
    }

    @Override
    public void init() throws IOException {
        bindPort();
    }

    @Override
    public void start() throws IOException {
        System.out.println(this.getClass().getName() + " start recv.");
        while (true) {
            // 开始接收请求
            Socket accept = this.serverSocket.accept();
            // 创建Processer处理请求。
            RequestProcessor processor = createProcessor(accept, connectorListener);
            protocolHandler.getExecutor().execute(processor);
        }
    }

    private RequestProcessor createProcessor(Socket accept, ConnectorListener connectorListener) {
        return new RequestProcessor(accept, connectorListener);
    }

    @Override
    public void bindPort() throws IOException {
        serverSocket = new ServerSocket(protocolHandler.getPort());
        System.out.println(this.getClass().getName() + " init port " + protocolHandler.getPort());
    }


}
