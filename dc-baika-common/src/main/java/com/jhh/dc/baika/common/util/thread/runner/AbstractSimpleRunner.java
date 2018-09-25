package com.jhh.dc.baika.common.util.thread.runner;

import org.apache.log4j.Logger;

/**
 * 继承自抽象基类的简单的抽象执行类
 * <p>需要自行重写该类中的doExecute，实现业务逻辑</p>
 * @author xingmin
 */
public abstract class AbstractSimpleRunner extends AbstractRunner{
    private static final Logger LOG = Logger.getLogger(AbstractSimpleRunner.class);

    @Override
    public void run() {
        boolean succeed = doExecute();
        if (!succeed) {
            LOG.error("Execute SimpleRunner failed [" + toString() + "]");
        }
    }

    /**
     * 执行方法入口
     * @return
     */
    @Override
    public abstract boolean doExecute();
}
