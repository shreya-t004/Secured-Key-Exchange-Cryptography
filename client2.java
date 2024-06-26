import java.io.*;
import java.net.*;

public class client2 {
    public static void main(String[] args) {
        try {
            // Connect to the server
            Socket socket = new Socket("localhost", 12345);
            PrintWriter toServer = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Receive the secret key data from Client 1
            String secretKeyData = fromServer.readLine();

            // Process the received secret key data
            int[] otherSecretKey = processSecretKeyData(secretKeyData);

            // Print the received secret key
            System.out.println("Secret key received from Client 1: ");
            printPolynomial(otherSecretKey);

            // Generate my secret key
            int[] mySecretKey = NewHopeKeyExchange.generateRandomPoly();

            // Print the generated polynomial
            System.out.println("My secret key (Client 2): ");
            printPolynomial(mySecretKey);

            // Perform key exchange with the other party's secret key
            int[] sharedKey = NewHopeKeyExchange.keyExchange(otherSecretKey);

            // Convert shared key to string for sending
            StringBuilder sb = new StringBuilder();
            for (int coeff : sharedKey) {
                sb.append(coeff).append(" ");
            }
            String sharedKeyData = sb.toString().trim();

            // Send shared key data to Client 1
            toServer.println(sharedKeyData);

            // Print the shared key
            System.out.println("Shared key sent to Client 1: ");
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

    // Process the received secret key data
    private static int[] processSecretKeyData(String secretKeyData) {
        String[] coeffsStr = secretKeyData.split(" ");
        int[] secretKey = new int[coeffsStr.length];
        for (int i = 0; i < coeffsStr.length; i++) {
            secretKey[i] = Integer.parseInt(coeffsStr[i]);
        }
        return secretKey;
    }
}
