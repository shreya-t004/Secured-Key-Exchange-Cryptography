import java.io.*;
import java.net.*;

public class server {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(12345);
            System.out.println("Server started. Waiting for clients...");

            // Accept the first client
            Socket client1Socket = serverSocket.accept();
            System.out.println("Client 1 connected.");

            // Set up streams for communication with Client 1
            BufferedReader fromClient1 = new BufferedReader(new InputStreamReader(client1Socket.getInputStream()));
            PrintWriter toClient1 = new PrintWriter(client1Socket.getOutputStream(), true);

            // Wait for Client 1 to send its secret key data
            String secretKeyData1 = fromClient1.readLine();
            System.out.println("Secret key data received from Client 1.");

            // Accept the second client
            Socket client2Socket = serverSocket.accept();
            System.out.println("Client 2 connected.");

            // Set up streams for communication with Client 2
            BufferedReader fromClient2 = new BufferedReader(new InputStreamReader(client2Socket.getInputStream()));
            PrintWriter toClient2 = new PrintWriter(client2Socket.getOutputStream(), true);

            // Send Client 1's secret key data to Client 2
            toClient2.println(secretKeyData1);
            System.out.println("Secret key data sent from Client 1 to Client 2.");

            // Wait for Client 2 to send its secret key data
            String secretKeyData2 = fromClient2.readLine();
            System.out.println("Secret key data received from Client 2.");

            // Send Client 2's secret key data to Client 1
            toClient1.println(secretKeyData2);
            System.out.println("Secret key data sent from Client 2 to Client 1.");

            // Close connections
            client1Socket.close();
            client2Socket.close();
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
