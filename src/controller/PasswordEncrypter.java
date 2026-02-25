package controller;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.MessageDigest;
import java.util.Base64;

public class PasswordEncrypter {

    // Constant-time compare
    private  boolean constantTimeEquals(byte[] a, byte[] b) {
        return MessageDigest.isEqual(a, b);
    }

    public boolean verifyPBKDF2SHA256(String djangoPasswordEntry, String candidatePassword) throws Exception {
        // Example djangoPasswordEntry:
        // "pbkdf2_sha256$260000$randomsalt$Base64Hash"
        String[] parts = djangoPasswordEntry.split("\\$");
        if (parts.length != 4) {
            throw new IllegalArgumentException("Unexpected Django password format");
        }
        String algorithm = parts[0];               // "pbkdf2_sha256"
        int iterations = Integer.parseInt(parts[1]);
        String salt = parts[2];
        String storedBase64Hash = parts[3];

        if (!"pbkdf2_sha256".equals(algorithm)) {
            throw new IllegalArgumentException("This method only supports pbkdf2_sha256");
        }

        // Django uses 256-bit derived key for pbkdf2_sha256
        int dkLenBits = 256;
        PBEKeySpec spec = new PBEKeySpec(candidatePassword.toCharArray(), salt.getBytes("UTF-8"), iterations, dkLenBits);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        byte[] derived = skf.generateSecret(spec).getEncoded();

        // Django base64-encodes the derived bytes (standard Base64, no newline)
        String derivedBase64 = Base64.getEncoder().encodeToString(derived);

        // Constant-time comparison (comparing bytes)
        return constantTimeEquals(derivedBase64.getBytes("UTF-8"), storedBase64Hash.getBytes("UTF-8"));
    }

//    public static void main(String[] args) throws Exception {
//        String stored = "pbkdf2_sha256$260000$mysalt$..."; // replace with real
//        String attempt = "guessPassword";
//        boolean ok = verifyPBKDF2SHA256(stored, attempt);
//        System.out.println("Password ok? " + ok);
//    }
}

