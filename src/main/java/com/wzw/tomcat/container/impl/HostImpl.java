package com.wzw.tomcat.container.impl;

import com.wzw.tomcat.container.Context;
import com.wzw.tomcat.container.Engine;
import com.wzw.tomcat.container.Host;

public class HostImpl implements Host {
    private Engine engine;
    private String name;
    private String appBase;
    private Context[] contexts = new Context[0];

    @Override
    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    @Override
    public Engine getEngine() {
        return this.engine;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setAppBase(String appbase) {
        this.appBase = appbase;
    }

    @Override
    public String getAppBase() {
        return this.appBase;
    }

    @Override
    public void setContext(Context context) {
        Context result[] = new Context[contexts.length + 1];
        System.arraycopy(this.contexts, 0, result, 0, this.contexts.length);
        result[contexts.length] = context;
        this.contexts = result;
    }

    @Override
    public Context[] getContextes() {
        return this.contexts;
    }

    @Override
    public void init() {
        for (int i = 0; i < contexts.length; i++) {
            contexts[i].init();
        }
    }

    @Override
    public void start() {
        for (int i = 0; i < contexts.length; i++) {
            contexts[i].start();
        }
    }

    @Override
    public void stop() {

    }

    @Override
    public void destory() {

    }
}
