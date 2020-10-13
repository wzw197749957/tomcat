package com.wzw.tomcat.lifecycle;

public interface Lifecycle {

    void init();

    void start();

    void stop();

    void destory();
}
