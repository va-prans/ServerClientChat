import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by CIA on 03/10/2017.
 */
public class ChatServer {
    public static final int PORT = 4444;
    public static Set<ServerThread> users = Collections.synchronizedSet(new HashSet<ServerThread>());
    public static ArrayList<String> usernameList = new ArrayList<>();

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

                System.out.println("USER INPUT TO SERVER: " + message);

                for (ServerThread user : ChatServer.users) {
                    if (user.isAuthorized()) {
                        user.sendToUser(message);
                    }
                }
            });
            serverThread.start();
            ChatServer.users.add(serverThread);
        }
    }

    public static String getListOfUsers() {
        String listOfUsers = "";

        for (ServerThread user : ChatServer.users) {
            listOfUsers += user.getUsername() + ", ";
        }

        return listOfUsers;
    }
}
