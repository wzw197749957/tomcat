package com.wzw.tomcat.connector;

import java.io.IOException;

public interface Endpoint {

    void setProtocolHandler(ProtocolHandler protocolHandler);

    void init() throws IOException;

    void start() throws IOException;

    void bindPort() throws IOException;
}
