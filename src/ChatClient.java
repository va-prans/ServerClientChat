import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by CIA on 03/10/2017.
 */
public class ChatClient {
    static boolean running = true;
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 4444);
        BufferedReader bufferedReaderFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader bufferedReaderFromCmd = new BufferedReader(new InputStreamReader(System.in));

        Thread thread = new Thread(() -> {
            try {
                while (true){

                    String line = bufferedReaderFromClient.readLine();
                    if (line == null){
                        running = false;
                        break;
                    }
                    System.out.println(line);
                }
                System.out.println("Listen thread closed. Ending program");
                System.exit(0);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        thread.start();

        while (running) {
            String readerInput = bufferedReaderFromCmd.readLine();
            printWriter.println(readerInput);
        }
    }
}



