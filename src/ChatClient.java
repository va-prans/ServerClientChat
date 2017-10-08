import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by CIA on 03/10/2017.
 */
public class ChatClient {
    public static void main(String[] args) throws IOException {
        String name = "x";
        Socket socket = new Socket("localhost", 4444);
        BufferedReader bufferedReaderFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
        printWriter.println(name);
        BufferedReader bufferedReaderFromCmd = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            String readerInput = bufferedReaderFromCmd.readLine();
            printWriter.println(name + ": " + readerInput);
            System.out.println(bufferedReaderFromClient.readLine());
        }
    }


}
