package com.example.pandia.luxury.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Util {
    public static boolean isValidString(String string) {
        return string != null && !string.isEmpty();
    }

    public static String hashCredential(String plainText, String salt) {
        byte [] secretbytes = {0x23, 0x40, 0x6c, 0x75, 0x78, 0x75, 0x72, 0x79,
                               0x61, 0x69, 0x64, 0x6e, 0x61, 0x70, 0x33, 0x31};

        return getSHA512SecurePassword(plainText, salt + new String(secretbytes));
    }

    private static String getSHA512SecurePassword(String passwordToHash, String salt){
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(salt.getBytes(StandardCharsets.UTF_8));
            byte[] bytes = md.digest(passwordToHash.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++){
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        }
        catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        return generatedPassword;
    }
}
