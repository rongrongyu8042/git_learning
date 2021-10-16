package com.mars.hope.core;

public class IoContext {
    private static IoContext INSTANCE;

    private final IoProvider ioProvider;
    private final Scheduler scheduler;

    public IoContext(IoProvider ioProvider, Scheduler scheduler) {
        this.ioProvider = ioProvider;
        this.scheduler = scheduler;
    }
    /*public IoProvider getIoProvider() {
        return ioProvider;
    }

    public Scheduler getScheduler() {
        return scheduler;
    }

    public static IoContext get(){
        return INSTANCE;
    }*/

    public static StartedBoot setup(){
        return new StartedBoot();
    }

    /**
     * 这里用内部类，通过setup()透出，把我们创建的IoProvider子类和Scheduler子类传入INSTANCE，后续使用
     * 那么为什么不直接在IoContext中使用set方法呢？
     * 因为解耦吧：
     */
    public static class StartedBoot{
        private IoProvider ioProvider;
        private Scheduler scheduler;
        private StartedBoot(){

        }
        public StartedBoot ioProvider(IoProvider ioProvider) {
            this.ioProvider = ioProvider;
            return this;
        }

        public StartedBoot scheduler(Scheduler scheduler) {
            this.scheduler = scheduler;
            return this;
        }
        public void start(){
            INSTANCE = new IoContext(ioProvider , scheduler);
            return;
        }
    }
}
