package Model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.time.LocalTime;

public class ServerThread extends Thread {
    Socket socket;
    private String username = "";
    private boolean authorized = false;
    private final OnUserMessage onUserMessage;
    private ChatProtocol chatProtocol = new ChatProtocol();
    private int timeout = 60;

    public ServerThread(Socket socket, OnUserMessage onUserMessage) {

        this.socket = socket;
        this.onUserMessage = onUserMessage;
    }

    public void run() {
        try {
            String message = null;
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                while ((message = bufferedReader.readLine()) != null) {

                    if (message.length() < 4) {
                        continue;
                    }

                    timeout = 60;

                    String pMessage = chatProtocol.handleUserMessage(message, socket.getInetAddress().toString());

                    String authenticationMsg = pMessage.substring(0,4);

                    switch (authenticationMsg) {
                        case "J_OK":
                            username = pMessage.substring(4, pMessage.length());
                            onUserMessage.messageUpdate(username + " has joined the channel.");
                            authorized = true;
                            sendToUser(authenticationMsg);
                            ChatServer.usernameList.add(username);
                            onUserMessage.messageUpdate("LIST OF USERS: " + ChatServer.usernameList.toString());
                            break;
                        case "J_ER":
                            sendToUser(pMessage);
                            break;
                        case "DATA":
                            if (authorized) {
                                int length = 5 + username.length();
                                String messageToSend = pMessage.substring(length, pMessage.length());
                                onUserMessage.messageUpdate(username + ":" + messageToSend);
                            }
                            break;
                        case "QUIT":
                            if (authorized) {
                                quit();
                            }
                            break;
                        default:
                            break;
                    }
                }
                socket.close();
                ChatServer.users.remove(this);
            } catch (SocketException se) {
                socket.close();
                ChatServer.users.remove(this);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void quit()
    {
        sendToUser("SESSION ENDED");
        authorized = false;
        ChatServer.usernameList.remove(username);
        onUserMessage.messageUpdate(username + " has left the channel.");
        onUserMessage.messageUpdate("LIST OF USERS: " + ChatServer.usernameList.toString());
        ChatServer.users.remove(this);

        try {
            socket.shutdownOutput();
            socket.shutdownInput();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendToUser(String message){
        try {
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
            printWriter.println(message);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public boolean decrementTimeout()
    {
        timeout--;
        if (timeout < 1)
        {
            return true;
        }
        return false;
    }

    public Socket getSocket() {
        return socket;
    }

    public String getUsername() {
        return username;
    }

    public boolean isAuthorized() {
        return authorized;
    }
}
