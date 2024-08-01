package com.iage.easyAPI.utility;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5HashingImpl {

    public static String getMD5(String password) {

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");

            md.digest(password.getBytes());

            byte[] bytes = md.digest();

            // converting byte to hexadecimal format
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }

            return sb.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
