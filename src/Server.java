import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Server {
    public static void main(String[] args) throws IOException {
        HttpResponse httpResponse = new HttpResponse();
        Charset utf8 = StandardCharsets.UTF_8;
        ResponseParameters responseParameters = null;
        String response = "";
        SocketChannel socketChannel = null;
        try {
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

            serverSocketChannel.socket().bind(new InetSocketAddress(9998));

            while (true) {
                System.out.println("Listening on Port 9999");
                socketChannel = serverSocketChannel.accept();

                //Perform the operations on received...
                System.out.println("Incoming Connection:  " + socketChannel.getRemoteAddress());
                ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                while (socketChannel.read(byteBuffer) > 0) {
                    byteBuffer.flip();
                    String lines = String.valueOf(utf8.decode(byteBuffer));
                    System.out.println(lines);
                    byteBuffer.clear();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            socketChannel.close();
        }
    }
}
