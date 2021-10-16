package com.mars.server;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * 实现AcceptListener接口，省一个实现类
 */
public class TCPServer implements ServerAcceptor.AcceptListener {

    private final int port;
    private final File cachePath;


    //在start()方法中提前记录
    /*private ServerAcceptor acceptor;
    private ServerSocketChannel server;*/

    public TCPServer(int port, File cachePath) {
        this.port = port;
        this.cachePath = cachePath;
    }

    public boolean start() {
        try{
            //启动Acceptor线程
            ServerAcceptor acceptor = new ServerAcceptor(this);

            ServerSocketChannel server = ServerSocketChannel.open();
            server.configureBlocking(false);
            server.socket().bind(new InetSocketAddress(port));
            server.register(acceptor.getSelector() , SelectionKey.OP_ACCEPT);//注册“连接事件”感兴趣


            acceptor.start();
            if(acceptor.awaitRunning()){
                System.out.println("服务器准备就绪～");
                System.out.println("服务器信息：" + server.getLocalAddress().toString());
                return true;
            } else {
                System.out.println("启动异常！");
                return false;
            }
        }catch (IOException e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 建立新连接后触发回调
     * @param channel
     */
    @Override
    public void onNewSocketArrived(SocketChannel channel) {

    }
}
