package com.mars.server;

import com.mars.foo.Foo;
import com.mars.foo.FooGui;
import com.mars.hope.core.IoContext;
import com.mars.hope.impl.IoSelectorProvider;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class Server {
    //服务器固化UDP端口
    public static int PORT_SERVER = 30401;

    public static void main(String[] args) throws IOException {
        File cachePath = Foo.getCacheDir("server");
        //这里还是用的普通Provider，在ClientTest中用了Stealing。
        IoContext.setup()
                .ioProvider(new IoSelectorProvider())
                .scheduler(new SchedulerImpl(1))
                .start();
        TCPServer tcpServer = new TCPServer(PORT_SERVER, cachePath);
        boolean isSucceed = tcpServer.start();
        if(!isSucceed){
            System.out.println("Start TCP server failed!");
            return;
        }

        UDPProvider.start(PORT_SERVER);
        // 启动Gui界面
        FooGui gui = new FooGui("Clink-Server", tcpServer::getStatusString);
        gui.doShow();

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String str;
        do{
            str = bufferedReader.readLine();
            if (str == null || Foo.COMMAND_EXIT.equalsIgnoreCase(str)) {
                break;
            }
            if (str.length() == 0) {
                continue;
            }
            // 发送字符串
            tcpServer.broadcast(str);
        }while(true);
    }
}
