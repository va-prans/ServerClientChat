package Model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableObjectValue;
import javafx.beans.value.ObservableStringValue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

/**
 * Created by CIA on 03/10/2017.
 */
public class ChatClient {
    Socket socket;
    StringProperty inputString = new SimpleStringProperty("");

    public boolean run(String ip, int port) {
        try {
            socket = new Socket(ip, port);
            BufferedReader bufferedReaderFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            Thread thread = new Thread(() -> {
                try {
                    while (true){
                        String line;
                        try {
                            line = bufferedReaderFromClient.readLine();
                        } catch (SocketException e) {
                            break;
                        }
                        if (line == null) {
                            break;
                        }
                        setInputString(line);
                    }
                    System.out.println("Client connection terminated.");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            thread.start();

        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public Socket getSocket()
    {
        return socket;
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



