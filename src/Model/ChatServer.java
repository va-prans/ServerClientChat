package Model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.*;

/**
 * Created by CIA on 03/10/2017.
 */
public class ChatServer {
    public static final int PORT = 4444;
    public static Set<ServerThread> users = Collections.synchronizedSet(new HashSet<ServerThread>());
    public static List<String> usernameList = Collections.synchronizedList(new ArrayList<>());

    volatile boolean running;
    ServerSocket serverSocket;

    StringProperty inputString = new SimpleStringProperty("");


    public void runServer() {
        try {
            serverSocket = new ServerSocket(PORT);
            running = true;
            System.out.println("Server ready...");
            while (running) {
                try {
                    Socket socket = serverSocket.accept();

                    ServerThread serverThread = new ServerThread(socket, message -> {

                        System.out.println("USER INPUT TO SERVER: " + message);
                        setInputString(message);

                        for (ServerThread user : ChatServer.users) {
                            if (user.isAuthorized()) {
                                user.sendToUser(message);
                            }
                        }
                    });
                    serverThread.start();
                    ChatServer.users.add(serverThread);
                } catch (SocketException se) {
                    System.out.println("Server socket closed.");
                }
            }
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopServer()
    {
        running = false;
        try
        {
            serverSocket.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public StringProperty getInputString()
    {
        return inputString;
    }

    public void setInputString(String inputString)
    {
        this.inputString.set(inputString);
    }
}
