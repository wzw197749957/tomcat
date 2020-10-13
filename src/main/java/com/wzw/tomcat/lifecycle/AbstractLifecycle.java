package com.wzw.tomcat.lifecycle;

public abstract class AbstractLifecycle implements Lifecycle {

    protected abstract void initImpl();

    protected abstract void startImpl();

    protected abstract void stopImpl();

    protected abstract void destoryImpl();

    @Override
    public void init() {
        System.out.println(this.getClass().getName() + "  init begin");
        initImpl();
        System.out.println(this.getClass().getName() + "  init finished");
    }

    @Override
    public void start() {
        System.out.println(this.getClass().getName() + "  start begin");
        startImpl();
        System.out.println(this.getClass().getName() + "  start finished");
    }

    @Override
    public void stop() {
        System.out.println(this.getClass().getName() + "  stop begin");
        stopImpl();
        System.out.println(this.getClass().getName() + "  stop finished");
    }

    @Override
    public void destory() {
        System.out.println(this.getClass().getName() + "  destory begin");
        destoryImpl();
        System.out.println(this.getClass().getName() + "  destory finished");
    }


}
