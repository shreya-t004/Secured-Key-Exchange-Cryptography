import java.io.*;
import java.net.*;

public class client1 {
    public static void main(String[] args) {
        try {
            // Connect to the server
            Socket socket = new Socket("localhost", 12345);
            PrintWriter toServer = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Generate my secret key
            int[] mySecretKey = NewHopeKeyExchange.generateRandomPoly();

            // Print the generated polynomial
            System.out.println("My secret key (Client 1): ");
            printPolynomial(mySecretKey);

            // Convert my secret key to string for sending
            StringBuilder sb = new StringBuilder();
            for (int coeff : mySecretKey) {
                sb.append(coeff).append(" ");
            }
            String secretKeyData = sb.toString().trim();

            // Send my secret key data to the server
            toServer.println(secretKeyData);

            // Receive shared key data from the server
            String sharedKeyData = fromServer.readLine();

            // Process the received shared key data
            int[] sharedKey = processSharedKeyData(sharedKeyData);

            // Print the shared key
            System.out.println("Shared key received from Client 2: ");
            printPolynomial(sharedKey);

            // Close connections
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Print the polynomial
    private static void printPolynomial(int[] polynomial) {
        for (int i = 0; i < polynomial.length; i++) {
            System.out.print(polynomial[i] + " ");
        }
        System.out.println();
    }

    // Process the received shared key data
    private static int[] processSharedKeyData(String sharedKeyData) {
        String[] coeffsStr = sharedKeyData.split(" ");
        int[] sharedKey = new int[coeffsStr.length];
        for (int i = 0; i < coeffsStr.length; i++) {
            sharedKey[i] = Integer.parseInt(coeffsStr[i]);
        }
        return sharedKey;
    }
}
