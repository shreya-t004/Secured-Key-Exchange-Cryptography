import java.security.SecureRandom;

public class NewHopeKeyExchange {
    
    private static final int N = 256;
    private static final int Q = 12289;
    private static final int POLY_SIZE = 1024;
    private static final int NTT_SIZE = 2048;
    private static final int LOG_Q = 12; // Logarithm base 2 of Q
    
    private static final SecureRandom random = new SecureRandom();
    
    // Generate a random polynomial
    public static int[] generateRandomPoly() {
        int[] poly = new int[POLY_SIZE];
        for (int i = 0; i < POLY_SIZE; i++) {
            poly[i] = random.nextInt(Q);
        }
        return poly;
    }
    
    // Perform polynomial addition
    private static int[] addPolynomials(int[] poly1, int[] poly2) {
        int[] result = new int[POLY_SIZE];
        for (int i = 0; i < POLY_SIZE; i++) {
            result[i] = (poly1[i] + poly2[i]) % Q;
        }
        return result;
    }
    
    // Perform polynomial subtraction
    private static int[] subtractPolynomials(int[] poly1, int[] poly2) {
        int[] result = new int[POLY_SIZE];
        for (int i = 0; i < POLY_SIZE; i++) {
            result[i] = (poly1[i] - poly2[i] + Q) % Q;
        }
        return result;
    }
    
    // Key exchange protocol
    public static int[] keyExchange(int[] mySecretKey) {
        // Step 1: Generate my polynomial
        int[] myPoly = generateRandomPoly();
        
        // Step 2: Send myPoly to the other party
        
        // Step 3: Receive the other party's polynomial
        int[] otherPoly = generateRandomPoly(); // In a real scenario, this would be received from the other party
        
        // Step 4: Generate a shared key
        int[] sharedPoly = subtractPolynomials(multiplyPolynomials(myPoly, otherPoly), mySecretKey);
        
        // Step 5: Hash the shared key to obtain a shorter key
        //int[] hashedKey = hash(sharedPoly);
        
        return sharedPoly;
    }
    
    // Multiply two polynomials using NTT (Number Theoretic Transform)
    private static int[] multiplyPolynomials(int[] poly1, int[] poly2) {
        int[] result = new int[NTT_SIZE];
        int[] ntt1 = ntt(poly1);
        int[] ntt2 = ntt(poly2);
        for (int i = 0; i < NTT_SIZE; i++) {
            result[i] = (int) ((long) ntt1[i] * ntt2[i] % Q);
        }
        return inverseNtt(result);
    }
    
    // Number Theoretic Transform (NTT)
    private static int[] ntt(int[] poly) {
        int[] result = new int[NTT_SIZE];
        for (int i = 0; i < NTT_SIZE; i++) {
            for (int j = 0; j < POLY_SIZE; j++) {
                result[i] = (result[i] + poly[j] * omega(i * j)) % Q;
            }
        }
        return result;
    }
    
    // Inverse Number Theoretic Transform (inverse NTT)
    private static int[] inverseNtt(int[] poly) {
        int[] result = new int[NTT_SIZE];
        int invN = modInverse(NTT_SIZE, Q);
        for (int i = 0; i < NTT_SIZE; i++) {
            for (int j = 0; j < POLY_SIZE; j++) {
                result[i] = (result[i] + poly[j] * omega(-i * j) % Q) * invN % Q;
            }
        }
        return result;
    }
    
    // Compute omega, the NTT primitive root
    private static int omega(int x) {
        return expMod(3, (Q - 1) / NTT_SIZE * x, Q);
    }
    
    // Compute modular exponentiation
    private static int expMod(int base, int exponent, int modulus) {
        int result = 1;
        while (exponent > 0) {
            if (exponent % 2 == 1) {
                result = (result * base) % modulus;
            }
            base = (base * base) % modulus;
            exponent /= 2;
        }
        return result;
    }
    
    // Compute modular inverse
    private static int modInverse(int a, int m) {
        a = a % m;
        for (int x = 1; x < m; x++) {
            if ((a * x) % m == 1) {
                return x;
            }
        }
        return 1;
    }
    
    // Simple hash function 
    private static int[] hash(int[] input) {
        int[] result = new int[32];
        for (int i = 0; i < 32; i++) {
            result[i] = input[i % input.length] ^ input[(i + 13) % input.length]; // Simple XOR hash
        }
        return result;
    }
}
