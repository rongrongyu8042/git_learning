package com.mars.client;

import com.mars.foo.Foo;
import com.mars.hope.core.IoContext;

import java.io.*;

public class Client {
    public static void main(String[] args) {
        File cachePath = Foo.getCacheDir("client");
        IoContext.setup()
                .ioProvider(new IoSelectorProvider())
                .scheduler(new SchedulerImpl(1))
                .start();
        ServerInfo info = UDPSearcher.searchServer(10000);
        System.out.println("Server:" + info);

        if(info != null){
            TCPClient tcpClient = null;
            try{
                tcpClient = TCPClient.startWith(info , cachePath);
                if(tcpClient == null){
                    return;
                }
                //添加关闭操作

                //开启心跳调度

                write(tcpClient);
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    private static void write(TCPClient tcpClient) throws IOException {
        // 构建键盘输入流
        InputStream in = System.in;
        BufferedReader input = new BufferedReader(new InputStreamReader(in));

        do {
            // 键盘读取一行
            String str = input.readLine();
            if (str == null || Foo.COMMAND_EXIT.equalsIgnoreCase(str)) {
                break;
            }

            if (str.length() == 0) {
                continue;
            }

            // --f url
            if (str.startsWith("--f")) {
                String[] array = str.split(" ");
                if (array.length >= 2) {
                    String filePath = array[1];
                    File file = new File(filePath);
                    if (file.exists() && file.isFile()) {
                        FileSendPacket packet = new FileSendPacket(file);
                        tcpClient.send(packet);
                        continue;
                    }
                }
            }

            // 发送字符串
            tcpClient.send(str);
        } while (true);
    }
}
