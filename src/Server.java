import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server
{
    static ServerSocket server = null;
    static Socket connection = null;

    static ObjectOutputStream output;
    static ObjectInputStream input;



    public static void main(String[] args)
    {
        try
        {
            server = new ServerSocket(6789, 100);
            while (true)
            {
                try
                {
                    waitForConnection();
                    setupStreams();
                    whileOpen();
                }
                catch (EOFException eofException)
                {
                    System.out.println("Connection terminated.");
                }
                finally
                {
                    close();
                }
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    static void waitForConnection() throws IOException
    {
        System.out.println("Connecting...");
        connection = server.accept();
        System.out.println("Connected to: " + connection.getInetAddress().getHostName());
    }

    static void setupStreams() throws IOException
    {
        output = new ObjectOutputStream(connection.getOutputStream());
        output.flush();
        input = new ObjectInputStream(connection.getInputStream());
        System.out.println("Stream set up.");
    }

    static void whileOpen() throws IOException
    {
        while (true)
        {

        }
    }

    static void close()
    {
        try
        {
            output.close();
            input.close();
            connection.close();
        }
        catch (IOException ioException)
        {
            ioException.printStackTrace();
        }
    }
}
