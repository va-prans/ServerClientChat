import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by CIA on 03/10/2017.
 */
public class ChatServer {
    public static final int PORT = 4444;
    public static Set<ServerThread> users = Collections.synchronizedSet(new HashSet<ServerThread>());
    public static void main(String[] args) throws IOException {
        ChatServer chatServer = new ChatServer();
        chatServer.runServer();


    }

    public void runServer() throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Server ready...");
        while (true) {
            Socket socket = serverSocket.accept();
            ServerThread serverThread = new ServerThread(socket, message -> {

                System.out.println("User input to server: " + message);
                for (ServerThread user : ChatServer.users) {

                    user.sendToUser(message);

                }

            });
            serverThread.start();
            ChatServer.users.add(serverThread);
        }
    }
}
