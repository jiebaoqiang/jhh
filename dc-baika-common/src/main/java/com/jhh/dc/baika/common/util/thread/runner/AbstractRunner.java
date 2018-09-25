package com.jhh.dc.baika.common.util.thread.runner;

/**
 * 实现了runnable的 抽象执行基类
 * <p>可自行继承该类后，实现具体逻辑</p>
 * @author xingmin
 */
public abstract class AbstractRunner implements Runnable{

    /**
     * 线程执行主方法
     */
    @Override
    public abstract void run();

    /**
     * 执行方法
     * @return boolean
     */
    public abstract boolean doExecute();

}
