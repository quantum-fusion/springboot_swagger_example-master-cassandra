package com.server.springboot.encryption;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.security.Key;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Strings.isNullOrEmpty;

/**
 * Created by quantum-fusion on 10/8/15.
 */
public class CryptoHelper {

    private static final String KEYSTORE = "keystore";
    private static final String STOREPASS = "storepass";
    private static final String ALIAS = "alias";
    private static final String KEYPASS = "keypass";

    private static final Logger LOG = LoggerFactory.getLogger(CryptoHelper.class);


    public CryptoHelper() {
        System.out.println("Constructor called");

    }

    public String encryptMessage(String key, String message) throws UnsupportedEncodingException {

        AESCipher cipher = new AESCipher(key.getBytes("UTF-8"));
        String encryptedMessage = cipher.getEncryptedMessage(message);

        LOG.debug("encrypt messagekey: " + key);
        LOG.debug("Message is: {}", encryptedMessage);

        return encryptedMessage;
    }

    public String decryptMessage(String key, String encryptedMessage) throws UnsupportedEncodingException {

        AESCipher cipher = new AESCipher(key.getBytes("UTF-8"));
        String decryptedMessage = cipher.getDecryptedMessage(encryptedMessage);

        LOG.debug("decrypt messagekey: " + key);
        LOG.debug("Encrypted Message: {}, Decrypted Message: {}", encryptedMessage, decryptedMessage);

        return decryptedMessage;
    }

    public void createSystemProperties() {

        String root = System.getProperty("user.dir");
        String filepath = "/src/test/resources/client-aes-keystore.jck"; // in case of Windows: "\\path \\to\\yourfile.txt
        String keystoreFileLocation = root+filepath;
        System.setProperty("keystore",keystoreFileLocation);
        System.setProperty("storepass","mystorepass");
        System.setProperty("alias","jceksaes");
        System.setProperty("keypass", "mykeypass");
    }

    public String createJWE(String userId, String duration, String iss, String sub, String aud, String jti, String firstName, String lastName, String admin) throws Exception {

        try {

        createSystemProperties();

        String keystoreFileLocation = System.getProperty(KEYSTORE);
        String storePass = System.getProperty(STOREPASS);
        String alias = System.getProperty(ALIAS);
        String keyPass = System.getProperty(KEYPASS);

        LOG.debug("createJWT::keystoreFileLocation: " + keystoreFileLocation);

        checkArgument(!isNullOrEmpty(keystoreFileLocation), "Please provide a 'keystore' file location.");
        checkArgument(!isNullOrEmpty(storePass), "Please provide a 'storepass' password for keystore");
        checkArgument(!isNullOrEmpty(alias), "Please provide an 'alias' for the specific key");
        checkArgument(!isNullOrEmpty(keyPass), "Please provide a 'keypass' for the specific key");
        LOG.debug("Keystore: {}\nStorePass: {}\nAlias: {}\nKeyPass: {}\n", keystoreFileLocation, storePass, alias, keyPass);

        Key keyFromKeyStore = KeystoreUtil.getKeyFromKeyStore(keystoreFileLocation, storePass, alias, keyPass);
        AESCipher cipher;

        cipher = new AESCipher(keyFromKeyStore);
        String key = MainApp.showKey(cipher);

        LOG.debug("This is the cipher key: " + cipher.getKey());
        LOG.debug("This is the Base64 Encoded key: " + key);

        String passdecryptedMessage = new String();

            // https://self-issued.info/docs/draft-ietf-oauth-json-web-token.html
            // http://jwt.io
            //
            // 4.1.1. "iss" (Issuer) Claim       ; Map to social login type (facebook, google, twitter, linkedin) @runtime
            //4.1.2. "sub" (Subject) Claim       ; Map to social token issued @ runtime
            //4.1.3. "aud" (Audience) Claim      ; Maps to Http User-Agent @ run time
            //4.1.4. "exp" (Expiration Time) Claim ; Expire time of JWT token generated @ runtime
            //4.1.5. "nbf" (Not Before) Claim      ; (Optional) The nbf (not before) claim identifies the time before which the JWT MUST NOT be accepted for processing.
            //4.1.6. "iat" (Issued At) Claim       ; Issued time of JWT token generated @ runtime
            //4.1.7. "jti" (JWT ID) Claim          ; (Optional) The jti (JWT ID) claim provides a unique identifier for the JWT. The jti value is a case-sensitive string.
            // "name": "John Doe"                ; Maps to Name from social login token extracted from social server @ runtime
            // "admin": true / false             ; False for all regular login customers @ runtime
            String messageToEncrypt = CreateJWT(key, userId, duration, iss, sub, aud, jti, firstName, lastName, admin);

            LOG.debug("JWT:" + messageToEncrypt);

            if (!messageToEncrypt.equals("Expired")) {
                String encryptedMessage = cipher.getEncryptedMessage(messageToEncrypt);
                return encryptedMessage;
            }

            return messageToEncrypt;

        } catch (Exception e) {
            LOG.debug("token expired" + e);
            return "400/Error" + e;
        }
    }

    public JWTHelper decryptJWE(String token) throws Exception
    {
        JWTHelper jh = new JWTHelper();
     try {
         createSystemProperties();

         String keystoreFileLocation = System.getProperty(KEYSTORE);
         String storePass = System.getProperty(STOREPASS);
         String alias = System.getProperty(ALIAS);
         String keyPass = System.getProperty(KEYPASS);

         LOG.debug("keystoreFileLocation: " + keystoreFileLocation);

         checkArgument(!isNullOrEmpty(keystoreFileLocation), "Please provide a 'keystore' file location.");
         checkArgument(!isNullOrEmpty(storePass), "Please provide a 'storepass' password for keystore");
         checkArgument(!isNullOrEmpty(alias), "Please provide an 'alias' for the specific key");
         checkArgument(!isNullOrEmpty(keyPass), "Please provide a 'keypass' for the specific key");
         LOG.debug("Keystore: {}\nStorePass: {}\nAlias: {}\nKeyPass: {}\n", keystoreFileLocation, storePass, alias, keyPass);

         Key keyFromKeyStore = KeystoreUtil.getKeyFromKeyStore(keystoreFileLocation, storePass, alias, keyPass);
         AESCipher cipher;

         cipher = new AESCipher(keyFromKeyStore);
         String key = MainApp.showKey(cipher);

         LOG.debug("This is the cipher key: " + cipher.getKey());
         LOG.debug("This is the Base64 Encoded key: " + key);
         LOG.debug("JWE token: " + token);

         String dMessage = "";

         String decryptedMessage = cipher.getDecryptedMessage(token);
         dMessage = decryptedMessage;
         LOG.debug("Original JWT token: {}, JWE token: {}", decryptedMessage, token);

         LOG.debug("This is the JWT token verify step:");
         AuthHelper a = new AuthHelper(key);

         TokenInfo ti = a.verifyToken(dMessage);

         jh.setJWT(dMessage);
         jh.setT(ti);

         return jh;
    }
    catch (Exception e)
    {
        LOG.debug("token has expired: ");
        jh.setJWT("Expired");
        return jh;
    }
   }

    // https://self-issued.info/docs/draft-ietf-oauth-json-web-token.html
    // http://jwt.io
    //
    // 4.1.1. "iss" (Issuer) Claim       ; Map to social login type (facebook, google, twitter, linkedin) @runtime
    //4.1.2. "sub" (Subject) Claim       ; Unused field for the moment
    //4.1.3. "aud" (Audience) Claim      ; Maps to Http User-Agent @ run time
    //4.1.4. "exp" (Expiration Time) Claim ; Expire time of JWT token generated @ runtime
    //4.1.5. "nbf" (Not Before) Claim      ; (Optional) The nbf (not before) claim identifies the time before which the JWT MUST NOT be accepted for processing.
    //4.1.6. "iat" (Issued At) Claim       ; Issued time of JWT token generated @ runtime
    //4.1.7. "jti" (JWT ID) Claim          ; The jti (JWT ID) claim provides a unique identifier for the JWT. The jti value is a case-sensitive string. Maps to token issued @ runtime.
    // "firstName": "John"                ; Maps to firstName from social login token extracted from social server @ runtime
    // "lastName" : "Doe"                 ; Maps to firstName from social login token extracted from social server @ runtime
    // "admin": true / false             ; False for all regular login customers @ runtime
    public String CreateJWT(String key, String userId, String duration, String iss, String sub, String aud, String jti, String firstName, String lastName, String admin) throws Exception {
       try {

            AuthHelper a = new AuthHelper(key);
            long l = Long.parseLong(duration);
            String u = new String(a.createJsonWebToken(userId, l, iss, sub, jti, aud, firstName, lastName, admin));

            try {

                TokenInfo ti = a.verifyToken(u);
            }
            catch (Exception e)
            {
                System.out.println("token expired");
                return "Expired";
            }

            return u;

        } catch (NumberFormatException nfe) {
            System.out.println("NumberFormatException: " + nfe.getMessage());

           return "Expired";
        }
    }

}
