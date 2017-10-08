import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerThread extends Thread {
    Socket socket;
    private String username = "";
    private boolean authorized = false;
    private final OnUserMessage onUserMessage;
    private ChatProtocol chatProtocol = new ChatProtocol();
    public ServerThread(Socket socket, OnUserMessage onUserMessage) {

        this.socket = socket;
        this.onUserMessage = onUserMessage;
    }


    public void run() {
        try {
            String message = null;
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while ((message = bufferedReader.readLine()) != null) {

                if (message.length() < 4) {
                    continue;
                }

                String pMessage = chatProtocol.handleUserMessage(message, socket.getInetAddress().toString());

                String authenticationMsg = pMessage.substring(0,4);

                switch (authenticationMsg) {
                    case "J_OK":
                        authorized = true;
                        username = pMessage.substring(4, pMessage.length());
                        sendToUser(authenticationMsg);
                        break;
                    case "J_ER":
                        sendToUser(authenticationMsg);
                        break;
                    case "DATA":
                        if (authorized) {
                            String messageToSend = pMessage.substring(4, pMessage.length());
                            onUserMessage.messageUpdate(messageToSend);
                        }
                        break;
                    default:
                        break;
                }
                
            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendToUser(String message){
        try {
            if (authorized){
                PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
                printWriter.println("INCOMING MESSAGE FROM USERS: " + message);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isAuthorized() {
        return authorized;
    }

    public void setAuthorized(boolean authorized) {
        this.authorized = authorized;
    }

    public OnUserMessage getOnUserMessage() {
        return onUserMessage;
    }

    public ChatProtocol getChatProtocol() {
        return chatProtocol;
    }

    public void setChatProtocol(ChatProtocol chatProtocol) {
        this.chatProtocol = chatProtocol;
    }
}
