package com.wzw.tomcat.container;

public interface Service extends Container {
    void setServer(Server server);

    Engine[] getEngines();

    void setEngine(Engine engine);
}
