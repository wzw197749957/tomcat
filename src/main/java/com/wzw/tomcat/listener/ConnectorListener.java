package com.wzw.tomcat.listener;


import com.wzw.tomcat.Request;
import com.wzw.tomcat.Response;
import com.wzw.tomcat.connector.Connector;

public interface ConnectorListener {
    Connector getConnector();

    void setConnector(Connector connector);

    void service(Request request, Response response) throws Exception;
}
