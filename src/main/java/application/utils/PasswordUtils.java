package application.utils;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.interfaces.PBEKey;
import javax.crypto.spec.PBEKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Random;

public class PasswordUtils {
    private static final Random random = new SecureRandom();
    private static final String alphabet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final int iterations = 10000;
    private static final int keylength = 256;

    public static String getSaltvalue(int length) {
        StringBuilder finalval = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            finalval.append(alphabet.charAt(random.nextInt(alphabet.length())));
        }
        return new String(finalval);
    }

    public static byte[] hash(char[] pass, byte[] salt) {
        PBEKeySpec spec = new PBEKeySpec(pass, salt, iterations, keylength);
        Arrays.fill(pass, Character.MIN_VALUE);
        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            return skf.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new AssertionError("Error while hashing a password: " + e.getMessage(), e);
        } finally {
            spec.clearPassword();
        }
    }

    public static String generateSecuredPass(String pass, String salt) {
        String finalval = null;
        byte[] securedPass = hash(pass.toCharArray(), salt.getBytes());
        finalval = Base64.getEncoder().encodeToString(securedPass);
        return finalval;
    }

    public static boolean verifyPass(String pass, String securedPass, String salt) {
        return securedPass.equalsIgnoreCase(generateSecuredPass(pass, salt));
    }
}
