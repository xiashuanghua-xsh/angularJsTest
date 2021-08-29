package com.xsh.threadpool;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 线程池创建
 */
public class SimpleThreadFactory implements ThreadFactory {
    private ThreadGroup group;

    private AtomicInteger threadNumber = new AtomicInteger(1);

    private String namePrefix;

    /**
     * 构造函数
     *
     * @param namePrefix
     */
    public SimpleThreadFactory(String namePrefix) {
        SecurityManager sm = System.getSecurityManager();
        group = (sm != null) ? sm.getThreadGroup() : Thread.currentThread().getThreadGroup();
        this.namePrefix = namePrefix;
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(group, r, namePrefix + "_" + threadNumber.getAndIncrement(), 0);
        if (t.isDaemon()) {
            t.setDaemon(false);
        }
        if (t.getPriority() != Thread.NORM_PRIORITY) {
            t.setPriority(Thread.NORM_PRIORITY);
        }
        return t;
    }
}
