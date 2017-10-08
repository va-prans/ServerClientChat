import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerThread extends Thread {
    Socket socket;
    private String username;
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

                String pMessage = chatProtocol.handleUserMessage(message);

//                if (pMessag)
                //returns JOIN SUCCESS
                //then authorize and add username

                //onUserMessage.messageUpdate(message);




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
