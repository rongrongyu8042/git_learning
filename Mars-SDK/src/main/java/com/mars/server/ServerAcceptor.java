package com.mars.server;

import java.io.IOException;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.concurrent.CountDownLatch;

public class ServerAcceptor extends Thread {
    private final CountDownLatch latch = new CountDownLatch(1);
    private final Selector selector;
    private final AcceptListener listener;
    private boolean done = false;

    ServerAcceptor(AcceptListener listener) throws IOException {
        super("Server-Accept-Thread");
        this.listener = listener;
        this.selector = Selector.open();
    }

    @Override
    public void run() {
        super.run();
        /**
         * RE：没整明白为什么一上来要countDown()
         */
        latch.countDown();
        Selector selector = this.selector;
        do{
            try{
                if(selector.select() == 0){
                    if(done) break;
                    continue;
                }
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while(iterator.hasNext()){
                    if(done) break;
                    SelectionKey key = iterator.next();
                    iterator.remove();

                    if(key.isAcceptable()){
                        ServerSocketChannel serverSocketChannel =(ServerSocketChannel) key.channel();
                        SocketChannel socketChannel = serverSocketChannel.accept();
                        listener.onNewSocketArrived(socketChannel);
                    }
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }while(!done);
        System.out.println("ServerAcceptor Finished!");
    }

    void exit(){
        done = true;
        CloseUtils.close(selector);
    }

    Selector getSelector(){
        return selector;
    }

    public boolean awaitRunning() {
        try {
            latch.await();
            return true;
        }catch (InterruptedException e){
            return false;
        }
    }

    interface AcceptListener{
        void onNewSocketArrived(SocketChannel channel);
    }
}
