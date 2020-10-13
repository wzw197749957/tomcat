package com.wzw.tomcat.connector;

import java.io.IOException;
import java.util.concurrent.Executor;

public interface ProtocolHandler {

    void setExecutor(Executor executor);

    Connector getConnector();

    void setConnector(Connector connector);

    Integer getPort();

    void init() throws IOException;

    void start() throws IOException;

    Executor getExecutor();
}
