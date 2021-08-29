package com.xsh.threadpool;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池工厂
 */
public class ThreadPoolFactoty {

    // 定义一个线程池名称，不同的服务定义不同的线程池名称
    private static final String COMMON_THREAD_POOL = "COMMON_POOL";

    private static final String ADMIN_THREAD_POOL = "ADMIN_THREAD_POOL";


    private static final String SYSTEM_THREAD_POOL = "SYSTEM_THREAD_POOL";

    // 定义一个线程池map存储线程
    private static ConcurrentHashMap<String, ThreadPoolExecutor> threadPoolMap = new ConcurrentHashMap<>();

    /**
     * 获取对应线程池
     */
    public static ThreadPoolExecutor getThreadPool(String poolName) {
        ThreadPoolExecutor executor = threadPoolMap.get(poolName);
        if (executor != null) {
            return executor;
        }
        ThreadPoolExecutor newPoolExecutor = new ThreadPoolExecutor(4,
                8,
                5000,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(5728),
                new SimpleThreadFactory(poolName));

        executor = threadPoolMap.putIfAbsent(poolName, newPoolExecutor);
        return executor == null ? newPoolExecutor : executor;
    }
}
