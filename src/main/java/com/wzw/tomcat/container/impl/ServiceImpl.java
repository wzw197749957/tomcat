package com.wzw.tomcat.container.impl;

import com.wzw.tomcat.container.Engine;
import com.wzw.tomcat.container.Server;
import com.wzw.tomcat.container.Service;

public class ServiceImpl implements Service {
    private Server server;
    private Engine[] engines = new Engine[0];

    @Override
    public void setServer(Server server) {
        this.server = server;
    }

    @Override
    public Engine[] getEngines() {
        return engines;
    }

    @Override
    public void setEngine(Engine engine) {
        Engine[] result = new Engine[engines.length + 1];
        System.arraycopy(engines, 0, result, 0, engines.length);
        result[engines.length] = engine;
        engines = result;
    }


    @Override
    public void init() {
        for (Engine engine : engines) {
            engine.init();
        }
    }

    @Override
    public void start() {
        for (Engine engine : engines) {
            engine.start();
        }
    }

    @Override
    public void stop() {

    }

    @Override
    public void destory() {

    }
}
