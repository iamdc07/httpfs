import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Server {
    public static void main(String[] args) throws IOException {
        HttpRequest httpRequest = new HttpRequest();
        Scanner scanner = new Scanner(System.in);
        Charset utf8 = StandardCharsets.UTF_8;

        String response = "";

        SocketChannel socketChannel = null;
        try {
            while (!Config.isValid) {
                String command = scanner.nextLine();
//            System.out.println(serverParameters.isValid);
                validate(command);

                if (Config.isValid) {
                    ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

                    serverSocketChannel.socket().bind(new InetSocketAddress(Config.port));

                    while (true) {
                        StringBuilder sb = new StringBuilder();
                        System.out.println("Listening on Port " + Config.port);
                        socketChannel = serverSocketChannel.accept();

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
                } else {
                    System.out.println("Please enter a valid command.\n");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (socketChannel != null)
                socketChannel.close();
        }
    }

    private static void validate(String input) {
        String[] words = input.split(" ");

        if (words[0].equalsIgnoreCase("httpfs")) {

            for (int i = 1; i < words.length; i++) {
                switch (words[i]) {
                    case "-v":
                        Config.isVerbose = true;
                        break;
                    case "-p":
                        Config.hasPort = true;
                        Config.port = Integer.parseInt(words[i + 1]);
                        i++;
                        break;
                    case "-d":
                        Config.hasPath = true;
                        Config.path = words[i + 1];
                        i++;
                        break;
                    default:
                        return;
                }
            }

            if (!Config.hasPort) {
                Config.port = 8080;
                Config.hasPort = true;
            }

            if (Config.hasPath)
                Config.isValid = true;
        }
    }
}

// httpfs -v -d ./
// httpfs -v -d ../../../files