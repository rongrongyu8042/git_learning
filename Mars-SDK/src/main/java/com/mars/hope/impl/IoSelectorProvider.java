package com.mars.hope.impl;

import com.mars.hope.core.IoProvider;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.HashMap;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class IoSelectorProvider implements IoProvider {



    private final Selector readSelector;
    private final Selector writeSelector;

    private final HashMap<SelectionKey, Runnable> inputCallbackMap = new HashMap();
    private final HashMap<SelectionKey, Runnable> outputCallbackMap = new HashMap();

    private final ExecutorService inputHandlePool;
    private final ExecutorService outputHandlePool;

    public IoSelectorProvider() throws IOException {
        readSelector = Selector.open();
        writeSelector = Selector.open();

        //为什么这里可以用Executors？
        /**
         * 这里的keepAliveTime设置成多大合适？
         */
        inputHandlePool = new ThreadPoolExecutor(4,5,60,
                TimeUnit.SECONDS,new ArrayBlockingQueue<Runnable>(100),
                new NameableThreadFactory("IoProvider-Input-Thread-"));

        outputHandlePool  = new ThreadPoolExecutor(4,5,60,
                TimeUnit.SECONDS,new ArrayBlockingQueue<Runnable>(100),
                new NameableThreadFactory("IoProvider-Input-Thread-"));

        startRead();

    }

    private void startRead(){

    }




    static class SelectThread extends Thread{
        private final AtomicBoolean isClosed;
        private final AtomicBoolean locker;
        private final Selector selector;
        private final HashMap<SelectionKey,Runnable> callMap;
        private final ExecutorService pool;
        private final int keyOps;

        SelectThread(String name,AtomicBoolean isClosed){
            
        }

    }


}
