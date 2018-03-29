package com.server.springboot.encryption;

import com.google.common.base.Throwables;
import com.google.common.io.BaseEncoding;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

public class AESCipher {

    // http://docs.oracle.com/javase/7/docs/api/javax/crypto/Cipher.html
    private static final String ALGORITHM_AES128 = "AES/CBC/PKCS5Padding";
    // ECP, default
//    private static final String ALGORITHM_AES256 = "AES";
    private static final byte[] INITIAL_IV = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    // private static final byte[] INITIAL_IV = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    private final SecretKeySpec secretKeySpec;
    private final Cipher cipher;
    private IvParameterSpec iv;

    /**
     * Create AESCipher based on existing {@link Key}
     *
     * @param key Key
     */
    public AESCipher(Key key) {
        this(key.getEncoded());
    }

    /**
     * Create AESCipher based on existing {@link Key} and Initial Vector (iv) in bytes
     *
     * @param key Key
     */
    public AESCipher(Key key, byte[] iv) {
        this(key.getEncoded(), iv);
    }

    /**
     * <p>Create AESCipher using a byte[] array as a key</p>
     * <p/>
     * <p><strong>NOTE:</strong> Uses an Initial Vector of 16 0x0 bytes. This should not be used to create strong security.</p>
     *
     * @param key Key
     */
    public AESCipher(byte[] key) {
        this(key, INITIAL_IV);
    }

    private AESCipher(byte[] key, byte[] iv) {
        try {
            this.secretKeySpec = new SecretKeySpec(key, "AES");
            this.iv = new IvParameterSpec(iv);
            this.cipher = Cipher.getInstance(ALGORITHM_AES128);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw Throwables.propagate(e);
        }
    }

    /**
     * Takes message and encrypts with Key
     *
     * @param message String
     * @return String Base64 encoded
     */
    public String getEncryptedMessage(String message) {
        try {
            Cipher cipher = getCipher(Cipher.ENCRYPT_MODE);

            byte[] encryptedTextBytes = cipher.doFinal(message.getBytes("UTF-8"));

            return BaseEncoding.base64().encode(encryptedTextBytes);
        } catch (IllegalBlockSizeException | BadPaddingException | UnsupportedEncodingException | InvalidKeyException | InvalidAlgorithmParameterException e) {
            throw Throwables.propagate(e);
        }
    }

    /**
     * Takes Base64 encoded String and decodes with provided key
     *
     * @param message String encoded with Base64
     * @return String
     */
    public String getDecryptedMessage(String message) {
        try {
            Cipher cipher = getCipher(Cipher.DECRYPT_MODE);

            byte[] encryptedTextBytes = BaseEncoding.base64().decode(message);
            byte[] decryptedTextBytes = cipher.doFinal(encryptedTextBytes);

            return new String(decryptedTextBytes);
        } catch (IllegalBlockSizeException | BadPaddingException | InvalidKeyException | InvalidAlgorithmParameterException e) {
            throw Throwables.propagate(e);
        }
    }

    public byte[] decrypt(byte[] cipherText, byte[] key, byte [] iv)
    {
        try{
            Cipher c = Cipher.getInstance("AES/CBC/NoPadding");
            //SecretKeySpec sks = new SecretKeySpec(key, "AES");
            //IvParameterSpec ips = new IvParameterSpec(iv);
          //  c.init(Cipher.DECRYPT_MODE, this.secretKeySpec, iv);
            //Re-use of "cipherText"
            cipherText = c.doFinal(cipherText);
        }
        catch(Exception e){ System.out.println("Exception caught:"+e.getMessage());}
        return cipherText;
    }

    public static byte[] encrypt(byte[] plainText, byte[] key, byte [] iv)
    {
        try{
            Cipher c = Cipher.getInstance("AES/CBC/NoPadding");
           // SecretKeySpec sks = new SecretKeySpec(key, "AES");
            //IvParameterSpec ips = new IvParameterSpec(iv);
         //   c.init(Cipher.ENCRYPT_MODE, sks, ips);
            //Re-use of "plainText"
            plainText = c.doFinal(plainText);
        }
        catch(Exception e){System.out.println("Exception caught:"+e.getMessage());}
        return plainText;
    }



    /**
     * Get IV in Base64 Encoded String
     *
     * @return String Base64 Encoded
     */
    public String getIV() {
        return BaseEncoding.base64().encode(iv.getIV());
    }

    /**
     * Base64 encoded version of key
     *
     * @return String
     */
    public String getKey() {
        return getKey(KeyEncoding.BASE64);
    }

    public String getKey(KeyEncoding encoding) {
        String result = null;
        switch (encoding) {
            case BASE64:
                result = BaseEncoding.base64().encode(secretKeySpec.getEncoded());
                break;
            case HEX:
                result = BaseEncoding.base16().encode(secretKeySpec.getEncoded());
                break;
            case BASE32:
                result = BaseEncoding.base32().encode(secretKeySpec.getEncoded());
                break;
        }

        return result;
    }

    private Cipher getCipher(int encryptMode) throws InvalidKeyException, InvalidAlgorithmParameterException {
        cipher.init(encryptMode, getSecretKeySpec(), iv);
        return cipher;
    }

    private SecretKeySpec getSecretKeySpec() {
        return secretKeySpec;
    }


}
