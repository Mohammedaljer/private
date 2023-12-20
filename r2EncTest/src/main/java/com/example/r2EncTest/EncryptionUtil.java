package com.example.r2EncTest;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Component;

import java.security.spec.KeySpec;
import java.util.Base64;
@Component
public class EncryptionUtil {

    private static final String ALGORITHM = "AES";
    private static final String SECRET_KEY_FACTORY_ALGORITHM = "PBKDF2WithHmacSHA256"; //This constant defines the algorithm used for generating the encryption key from a password. PBKDF2 with HMAC SHA-256 is a secure way to turn a password into a key.
    private static final int ITERATION_COUNT = 65536; //The number of iterations used in the key derivation process. A higher number makes the key derivation process more secure (but also slower).
    private static final int KEY_LENGTH = 256; // for AES-256
    private static final byte[] SALT = "salt235".getBytes(); // The primary purpose of using a salt is to prevent dictionary attacks and rainbow table attacks. 
    //In key derivation functions like PBKDF2, a salt is used alongside the user's password to derive a cryptographic key.
    //The salt ensures that the output (derived key) is unique even if the same password is input multiple times. It also protects against the use of pre-computed tables for reversing the key derivation.
    private static SecretKey getSecretKey(char[] password) throws Exception {
        SecretKeyFactory factory = SecretKeyFactory.getInstance(SECRET_KEY_FACTORY_ALGORITHM);
        KeySpec spec = new PBEKeySpec(password, SALT, ITERATION_COUNT, KEY_LENGTH);
        return new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
    }

    public static String encrypt(String data, String password) throws Exception {
        SecretKey secretKey = getSecretKey(password.toCharArray());
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedBytes = cipher.doFinal(data.getBytes());
        //byte[] is used to hold the raw encrypted data, which is a binary format.
        return Base64.getEncoder().encodeToString(encryptedBytes);
        //Base64 is a way of encoding binary data as text. It's used here because encrypted data is binary and may contain bytes that cannot be directly represented as text.
    }

    public static String decrypt(String encryptedData, String password) throws Exception {
        SecretKey secretKey = getSecretKey(password.toCharArray());
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
        return new String(decryptedBytes);
    }
}

