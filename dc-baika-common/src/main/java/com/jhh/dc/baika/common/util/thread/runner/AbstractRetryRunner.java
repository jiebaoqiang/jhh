package com.jhh.dc.baika.common.util.thread.runner;

import org.apache.log4j.Logger;

import com.jhh.dc.baika.common.util.thread.AsyncExecutor;

/**
 * 继承自抽象基类的带有重试机制的抽象执行类
 * <p>需要自行重写该类中的doExecute，实现业务逻辑</p>
 * @author xingmin
 */
public abstract class AbstractRetryRunner extends AbstractRunner{
    private static final Logger LOG = Logger.getLogger(AbstractRetryRunner.class);

    private int retry = 0;

    private int maxRetryTime = 3;

    @Override
    public void run() {
        boolean succeed = doExecute();
        if (!succeed) {
            if (retry ++ < maxRetryTime) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    LOG.error("Exception happens when retry thread ", e);
                }
                AsyncExecutor.execute(this);
            } else {
                LOG.error("Execute RetryRunner failed [" + toString() + "]");
            }
        }
    }

    /**
     * 执行方法
     * @return
     */
    @Override
    public abstract boolean doExecute();
}
