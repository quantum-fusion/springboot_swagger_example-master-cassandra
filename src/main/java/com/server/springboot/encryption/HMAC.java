package com.server.springboot.encryption;

/**
 * Created by quantum-fusion on 3/30/18.
 */

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.sun.org.apache.xml.internal.security.utils.Base64;

/**
 * Encryption class to show how to generate encoded(HMAC-x) signatures.
 *
 */

public class HMAC {

    private static String toHexString(byte[] bytes) {
        Formatter formatter = new Formatter();
        for (byte b : bytes) {
            formatter.format("%02x", b);
        }
        return formatter.toString();
    }

     public static String calculateHMAC (String message, String key, String algorithm) {

        try {

            // 1. Get an algorithm instance.
            Mac sha256_hmac = Mac.getInstance(algorithm);

            // 2. Create secret key.
            SecretKeySpec secret_key = new SecretKeySpec(key.getBytes("UTF-8"), algorithm);

            // 3. Assign secret key algorithm.
            sha256_hmac.init(secret_key);

            // 4. Generate Hex cipher string.
            return toHexString(sha256_hmac.doFinal(message.getBytes("UTF-8")));


            /**
             * Here are the Based64 outputs for given algorithms:-
             *
             * HmacMD5 = hpytHW6XebJ/hNyJeX/A2w==
             * HmacSHA1 = CZbtauhnzKs+UkBmdC1ssoEqdOw=
             * HmacSHA256 =gCZJBUrp45o+Z5REzMwyJrdbRj8Rvfoy33ULZ1bySXM=
             * HmacSHA512 = OAqi5yEbt2lkwDuFlO6/4UU6XmU2JEDuZn6+1pY4xLAq/JJGSNfSy1if499coG1K2Nqz/yyAMKPIx9C91uLj+w==
             */

        } catch (NoSuchAlgorithmException e) {

            e.printStackTrace();

        } catch (UnsupportedEncodingException e) {

            e.printStackTrace();

        } catch (InvalidKeyException e) {

            e.printStackTrace();

        }

         return "";

    }


    public static void main(String args[]) {

        String message = "This is my message.";
        String key = "your_key";
        String algorithm = "HmacSHA256";  // OPTIONS= HmacSHA512, HmacSHA256, HmacSHA1, HmacMD5

        System.out.println(calculateHMAC(message, key, algorithm));

    }

}
