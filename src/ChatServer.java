import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by CIA on 03/10/2017.
 */
public class ChatServer {
    public static final int PORT = 4444;

    public static void main(String[] args) throws IOException {
        new ChatServer().runServer();
    }

    public void runServer() throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Server ready...");
        while (true) {
            Socket socket = serverSocket.accept();
            new ServerThread(socket).start();
        }
    }
}
