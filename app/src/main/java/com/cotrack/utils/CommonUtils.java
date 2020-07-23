package com.cotrack.utils;

import java.util.Base64;
import java.util.Random;

public class CommonUtils {

    // Generates a random int with n digits
    public static String generateRandomDigits(int n) {
        int m = (int) Math.pow(10, n - 1);
        int random = m + new Random().nextInt(9 * m);
        return String.format("%0" + n + "d", random);
    }

    public static String decode(String encodedString){
        return new String(Base64.getDecoder().decode(encodedString));
    }

    public static String encode(String text){
        return new String(Base64.getEncoder().encode(text.getBytes()));
    }

}
