package com.jw.chat;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池管理
 * Created by Administrator on 2017/5/22.
 */

public class ThreadManager {
    private static ThreadManager threadManager;
    ThreadPoolProxy threadPoolProxy;
    private ThreadManager(){

    }
    public synchronized static ThreadManager getInstance(){
        if(threadManager==null)
            threadManager=new ThreadManager();
        return threadManager;
    }
    public ThreadPoolProxy createLongPool(int corePoolSize,int maximumPoolSize,long keepAliveTime){
        if(threadPoolProxy==null)
            threadPoolProxy=new ThreadPoolProxy(corePoolSize, maximumPoolSize,keepAliveTime);
        return threadPoolProxy;
    }
    public void createShortPool(){

    }
    public class ThreadPoolProxy {
        private ThreadPoolExecutor executor;
        private int corePoolSize;
        private int maximumPoolSize;
        private long keepAliveTime;
        public ThreadPoolProxy(int corePoolSize,int maximumPoolSize,long keepAliveTime) {
            this.corePoolSize=corePoolSize;
            this.maximumPoolSize=maximumPoolSize;
            this.keepAliveTime=keepAliveTime;
        }

        public void execute(Runnable runnable) {
            if(executor==null)
                executor=new ThreadPoolExecutor(corePoolSize,maximumPoolSize,keepAliveTime, TimeUnit.MINUTES,
                        new LinkedBlockingQueue<Runnable>(10));
            executor.execute(runnable);
        }

    }
}
