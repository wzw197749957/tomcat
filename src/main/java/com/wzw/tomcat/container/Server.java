package com.wzw.tomcat.container;


import com.wzw.tomcat.connector.Connector;
import com.wzw.tomcat.lifecycle.Lifecycle;

import java.util.concurrent.Executor;

public interface Server extends Lifecycle {

    void setServices(Service []services);

    Service[] getServices();

    void setConnector(Connector connector);

    Executor getExecutor();

}
