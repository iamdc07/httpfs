import model.Config;
import model.ServerParameters;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class Serve extends Thread {
    SocketChannel socketChannel;

    public Serve(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }

    @Override
    public void run() {
        try {
            System.out.println("Thread forked!");
            HttpRequest httpRequest = new HttpRequest();
            Charset utf8 = StandardCharsets.UTF_8;
            StringBuilder sb = new StringBuilder();

            //Perform the operations on received request...
            if (Config.isVerbose)
                System.out.println("Incoming Connection:  " + socketChannel.getRemoteAddress());

            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            System.out.println("BEFORE:" + byteBuffer.hasRemaining());
            ServerParameters serverParameters = new ServerParameters();
            while (byteBuffer.hasRemaining()) {
//                            System.out.println("Hello");
                int length = socketChannel.read(byteBuffer);
//                            System.out.println(length);
//                            System.out.println(byteBuffer.hasRemaining());
                if (length == -1)
                    break;

                if (length > 0) {
                    byteBuffer.flip();

                    String lines = String.valueOf(utf8.decode(byteBuffer));
//                                System.out.println(lines);
//                                System.out.println(byteBuffer.hasRemaining());
                    ByteBuffer buf = utf8.encode(lines);
                    sb.append(lines);
//                                System.out.println(socketChannel.read(byteBuffer));
//                                response = response.concat(lines);
//                                System.out.println("END:" + byteBuffer.hasRemaining());
                }
            }
            byteBuffer.clear();

            httpRequest.processRequest(sb, serverParameters);
            // Send data back to the client
            ByteBuffer buf = utf8.encode(serverParameters.response);
            socketChannel.write(buf);
            socketChannel.finishConnect();
            socketChannel.close();
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
        }


    }
}
