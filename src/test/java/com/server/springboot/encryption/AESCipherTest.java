package com.server.springboot.encryption;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class AESCipherTest {

    private static final String KEYSTORE = "keystore";
    private static final String STOREPASS = "storepass";
    private static final String ALIAS = "alias";
    private static final String KEYPASS = "keypass";

    private static final Logger log = LoggerFactory.getLogger(AESCipherTest.class);

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setup() {
        createSystemProperties();
    }

    private void createSystemProperties() {
        System.setProperty("keystore","src/test/resources/client-aes-keystore.jck");
        System.setProperty("storepass","mystorepass");
        System.setProperty("alias","jceksaes");
        System.setProperty("keypass", "mykeypass");
    }





    @Test
    public void RestTemplate() {
        RestTemplate restTemplate = new RestTemplate();

        Quote quote = restTemplate.getForObject(
                "http://gturnquist-quoters.cfapps.io/api/random", Quote.class);

        System.out.println(quote.toString());

        log.info(quote.toString());
    }


    @Test
    public void DHTest() {

        try {
            String mode = "USE_SKIP_DH_PARAMS";

            DHKeyAgreement2 keyAgree = new DHKeyAgreement2();

            mode = "GENERATE_DH_PARAMS";

            keyAgree.setup(mode);

            //keyAgree.run();

            byte[] alicepublickeyEnc = keyAgree.AliceKeyGenerate();

            byte[] bobpublickeyEnc = keyAgree.BobKeyGenerate(alicepublickeyEnc);

            byte[] alicesecretkey = keyAgree.generateAliceSecretKey(bobpublickeyEnc);

            byte[] bobsecretkey = keyAgree.generateBobSecretKey(alicepublickeyEnc);

            keyAgree.compareSecrets(alicesecretkey, bobsecretkey);

            keyAgree.run();

            System.out.println("alicekey length: " + alicesecretkey.length);
            System.out.println("bobkey length: " + bobsecretkey.length);

            CryptoHelper c = new CryptoHelper();

            //
            // Creating a random UUID (Universally unique identifier).
            //
            UUID uuid = UUID.randomUUID();
            String randomUUIDString = uuid.toString();

            // Encryption Cipher Algorithm
            String message = randomUUIDString;// "this is the secret message to hash and then encrypt";

            System.out.println("randomUUID: " + message.substring(0,32));

       //      message = PBKDF2WithHmacSHA1(message);

            String encryptedMessage = "";

            for(int i=0; i< 10; i++) {

                encryptedMessage = c.encryptMessage(toHex(alicesecretkey).substring(0, 32), message);

                message = encryptedMessage;
            }

            // Decryption Cipher Algorithm
            String decryptedMessage = "";
            message = encryptedMessage;

            for(int i=0; i < 10; i++) {

                decryptedMessage = c.decryptMessage(toHex(bobsecretkey).substring(0, 32), message);

                message = decryptedMessage;

            }

            System.out.println("decrypted message 1st round: " + message.substring(0,32));
            String secretkey = message.substring(0,32);

            message = "this is the secret message";
            encryptedMessage = "";

            for(int i=0; i< 10; i++) {

                encryptedMessage = c.encryptMessage(secretkey.substring(0, 32), message);

                message = encryptedMessage;
            }

            // Decryption Cipher Algorithm
            decryptedMessage = "";
            message = encryptedMessage;

            for(int i=0; i < 10; i++) {

                decryptedMessage = c.decryptMessage(secretkey.substring(0,32), message);

                message = decryptedMessage;

            }

            System.out.println("decrypted message 2nd round: " + message);

            CryptoHelper d = new CryptoHelper();
            String messageToEncryptJWT = d.CreateJWT(secretkey.substring(0, 32), "bob@gmail.com", "1", "facebook", "empty", "Android", "ABC123","Bob", "Smith", "false");

            String encryptedMessageJWT = c.encryptMessage(secretkey.substring(0, 32), messageToEncryptJWT);
            String decryptedMessageJWT = c.decryptMessage(secretkey.substring(0, 32), encryptedMessageJWT);
            log.debug("Original Message: {}, Encrypted Message: {}, Decrypted Message: {}", messageToEncryptJWT, encryptedMessageJWT, decryptedMessageJWT);
            assertThat(decryptedMessageJWT, is(messageToEncryptJWT));

            System.out.println("This is the JWT token verify step:");
            AuthHelper a = new AuthHelper(secretkey.substring(0,32));
            TokenInfo ti = a.verifyToken(decryptedMessageJWT);

            // aesTest(alicesecretkey, bobsecretkey);
            // keyAgree.desTest();

        } catch (Exception e) {
            System.err.println("Error: " + e);
            System.exit(1);
        }
    }

    @Test
    public void DH256keyTest() {

        try {
            String mode = "USE_SKIP_DH_PARAMS";

            DHKeyAgreement2 keyAgree = new DHKeyAgreement2();

            mode = "GENERATE_DH_PARAMS";

            keyAgree.setup(mode);

            //keyAgree.run();

            byte[] alicepublickeyEnc = keyAgree.AliceKeyGenerate();

            byte[] bobpublickeyEnc = keyAgree.BobKeyGenerate(alicepublickeyEnc);

            byte[] alicesecretkey = keyAgree.generateAliceSecretKey(bobpublickeyEnc);

            byte[] bobsecretkey = keyAgree.generateBobSecretKey(alicepublickeyEnc);



           //ToDo Try to use a 256 bit key with encryptor using Bob or alice secretkey
           // Key key = KeystoreUtil.getKeyFromKeyStore("src/test/resources/client-aes-keystore.jck", "mystorepass", "jceksaes", "mykeypass");
            //AESCipher cipher = new AESCipher(key);

            // Generate the secret key specs.
            //SecretKeySpec secretKeySpec = new SecretKeySpec(bobsecretkey, "AES");
            // Instantiate the cipher
            //Cipher cipher = Cipher.getInstance("AES");
            //cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);

         //   KeyGenerator k = new KeyGenerator();

            try {

                AESCipher cipher = new AESCipher(bobsecretkey);
                String abcencryptedMessage = cipher.getEncryptedMessage("this is a test");
                System.out.println("blahblah" + abcencryptedMessage);
            }
            catch (Exception e)
            {
              System.out.println("Exception" + e);
            }


            SecureRandom random = new SecureRandom(bobsecretkey);
            System.out.println(random);
            //random.nextBytes(bobsecretkey);
            //random.
            //End ToDo

            keyAgree.compareSecrets(alicesecretkey, bobsecretkey);

            System.out.println("alicekey length: " + alicesecretkey.length);
            System.out.println("bobkey length: " + bobsecretkey.length);

            CryptoHelper c = new CryptoHelper();

            //
            // Creating a random UUID (Universally unique identifier).
            //
            UUID uuid = UUID.randomUUID();
            String randomUUIDString = uuid.toString();

            // Encryption Cipher Algorithm
            String message = randomUUIDString;// "this is the secret message to hash and then encrypt";

            System.out.println("randomUUID: " + message.substring(0,32));

            //      message = PBKDF2WithHmacSHA1(message);

            String encryptedMessage = "";

            for(int i=0; i< 10; i++) {

                encryptedMessage = c.encryptMessage(toHex(alicesecretkey).substring(0, 32), message);

                message = encryptedMessage;
            }

            // Decryption Cipher Algorithm
            String decryptedMessage = "";
            message = encryptedMessage;

            for(int i=0; i < 10; i++) {

                decryptedMessage = c.decryptMessage(toHex(bobsecretkey).substring(0, 32), message);

                message = decryptedMessage;

            }

            System.out.println("decrypted message 1st round: " + message.substring(0,32));
            String secretkey = message.substring(0,32);

            message = "this is the secret message";
            encryptedMessage = "";

            for(int i=0; i< 10; i++) {

                encryptedMessage = c.encryptMessage(secretkey.substring(0, 32), message);

                message = encryptedMessage;
            }

            // Decryption Cipher Algorithm
            decryptedMessage = "";
            message = encryptedMessage;

            for(int i=0; i < 10; i++) {

                decryptedMessage = c.decryptMessage(secretkey.substring(0,32), message);

                message = decryptedMessage;

            }

            System.out.println("decrypted message 2nd round: " + message);



        } catch (Exception e) {
            System.err.println("Error: " + e);
            System.exit(1);
        }
    }


//    @Test public void AlicePublicKeyGenerateTest() {
//
//        try {
//            String mode = "USE_SKIP_DH_PARAMS";
//
//            DHKeyAgreement2 keyAgree = new DHKeyAgreement2();
//
//            mode = "GENERATE_DH_PARAMS";
//
//            keyAgree.setup(mode);
//
//            byte[] alicepublickey = keyAgree.AliceKeyGenerate();
//
//            System.out.println(toHex(alicepublickey));
//
//            byte[] alicesecretkey = keyAgree.generateAliceSecretKey(); //ToDo depends on the public key from Bob
//
//            System.out.println(alicesecretkey);
//
//       //     System.out.println("alicekey length: " + alicesecretkey.length);
//
//        } catch (Exception e) {
//            System.err.println("Error: " + e);
//            System.exit(1);
//        }
//    }


//    @Test public void BobPublicKeyGenerateTest() {
//
//        try {
//            String mode = "USE_SKIP_DH_PARAMS";
//
//            DHKeyAgreement2 keyAgree = new DHKeyAgreement2();
//
//            mode = "GENERATE_DH_PARAMS";
//
//            keyAgree.setup(mode);
//
//            keyAgree.BobKeyGenerate();
//
//            byte[] bobsecretkey = keyAgree.generateBobSecretKey(); //ToDo depends on the public key generated from Alice
//
//            System.out.println("bobkey length: " + bobsecretkey.length);
//
//        } catch (Exception e) {
//            System.err.println("Error: " + e);
//            System.exit(1);
//        }
//    }


    public void aesTest(byte[] alicesecretkey, byte[] bobsecretkey) throws Exception {
        System.out.println("alicesecretkey hex: " + toHex(alicesecretkey).substring(0,32));
        System.out.println("bobsecretkey hex: " + toHex(bobsecretkey).substring(0,32));

        String encryptedMessage = encryptMessage(toHex(alicesecretkey).substring(0,32));
        decryptMessageKey(toHex(bobsecretkey).substring(0,32), encryptedMessage);

    }



    public void sha256Test(byte[] alicesecretkey, byte[] bobsecretkey) throws Exception {
        String alicehashkey = PBKDF2WithHmacSHA1(toHex(alicesecretkey)); // this hash key is different
        String bobhashkey = PBKDF2WithHmacSHA1(toHex(bobsecretkey));     // than this hash key, they need to be same
        System.out.println("alicehashkey: " + alicehashkey);
        System.out.println("bobhashkey: " + bobhashkey);
    }

    @Test
    public void PBKDF2WithHmacSHA1Test() {

        // String  originalPassword = "password";
        String  originalPassword = "4D:CD:18:1D:8B:AC:01:74:81:6B:A2:5F:9A:34:77:65:1F:89:FE:2C:5C:4E:CA:B3:B1:74:63:B3:0B:48:68:C8:F3:78:04:E9:5D:0B:1C:53:C7:F6:60:A4:2B:80:65:C1:5F:2A:D1:27:93:D3:79:D9:70:7A:BC:4E:F1:EC:FB:11";

        try {
            String generatedSecuredPasswordHash = generateStorngPasswordHash(originalPassword);
            System.out.println(generatedSecuredPasswordHash);
        }
        catch (Exception e)
        {
           System.out.println(e);
        }
    }

    public String PBKDF2WithHmacSHA1(String key) {

        // String  originalPassword = "password";
        String  originalPassword = key;

        try {
            String generatedSecuredPasswordHash = generateStorngPasswordHash(originalPassword);
            System.out.println(generatedSecuredPasswordHash);
            return generatedSecuredPasswordHash;
        }
        catch (Exception e)
        {
            System.out.println(e);
            return "";
        }
    }

    @Test
    public void DateExpire() {

        String d1 = new String("2015-10-06T18:55:01.000Z");

        // DateTime date = DateTime.parse("2007-03-12T00:00:00.000+01:00");

        DateTime date = DateTime.parse(d1);

        System.out.println(date);

        //ToDo Need to Research Expire time method online, because this method is buggy.
        Period rentalPeriod = new Period().withDays(0).withHours(0);
        boolean l = date.toDateTime().plus(rentalPeriod).isBeforeNow();
        System.out.println("rentalPeriod: " + rentalPeriod);
        System.out.println("expired Y/N: " + l); // this is buggy, and does not quite give 24 hours precision when expire time is tested up to the hour precision a problem.

    }

    @Test
    public void KeyPairGenerator() throws InvalidKeySpecException, NoSuchProviderException, NoSuchAlgorithmException, InvalidAlgorithmParameterException {

        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2048);
        KeyPair kp = kpg.genKeyPair();
        Key publicKey = kp.getPublic();
        Key privateKey = kp.getPrivate();

        // extract the encoded private key, this is an unencrypted PKCS#8 private key
        byte[] encodedprivkey = kp.getPrivate().getEncoded();
        byte[] encodedpubkey = kp.getPublic().getEncoded();

        KeyFactory fact = KeyFactory.getInstance("RSA");
        RSAPublicKeySpec pub = fact.getKeySpec(kp.getPublic(),
                RSAPublicKeySpec.class);
        RSAPrivateKeySpec priv = fact.getKeySpec(kp.getPrivate(),
                RSAPrivateKeySpec.class);

        PublicKey pubkey = fact.generatePublic(pub);
        PrivateKey privkey = fact.generatePrivate(priv);

        System.out.println("Public Key : "+ toHexString(( pubkey.getEncoded())));
        System.out.println("Private Key : "+ toHexString( privkey.getEncoded() ));

        System.out.println("pub Modulus: " + pub.getModulus());
        System.out.println("pub PublicExponent: " + pub.getPublicExponent());

        System.out.println("private Modulus: " + priv.getModulus());
        System.out.println("private PrivateExponent: " + priv.getPrivateExponent());

        System.out.println("Public encoded Key: " + toHexString(encodedpubkey));
        System.out.println("Private encoded Key: " + toHexString(encodedprivkey));

        //ToDo How to encrypt and decrypt with RSA Keypair generated ? 

    }

    /*
     * Converts a byte array to hex string
     */
    private String toHexString(byte[] block) {
        StringBuffer buf = new StringBuffer();

        int len = block.length;

        for (int i = 0; i < len; i++) {
            byte2hex(block[i], buf);
            if (i < len-1) {
                buf.append(":");
            }
        }
        return buf.toString();
    }

    /*
     * Converts a byte to hex digit and writes to the supplied buffer
     */
    private void byte2hex(byte b, StringBuffer buf) {
        char[] hexChars = { '0', '1', '2', '3', '4', '5', '6', '7', '8',
                '9', 'A', 'B', 'C', 'D', 'E', 'F' };
        int high = ((b & 0xf0) >> 4);
        int low = (b & 0x0f);
        buf.append(hexChars[high]);
        buf.append(hexChars[low]);
    }


    @Test
    public void JWTCreateAndValidate() throws Exception {

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
        CryptoHelper c = new CryptoHelper();
        String JWE = c.createJWE("bob@gmail.com", "1", "facebook", "empty", "Android", "ABC123","Bob", "Smith", "false");
        JWTHelper JWT = c.decryptJWE(JWE);

        log.debug("Original JWT token: {}, JWE token: {}", JWT.getJWT(), JWE);

    }

    @Test
    public void lengthTest() {

        String fbToken1 = new String("CAAUV3RQqq1ABAKm6pQA1KC22fUrmeH5NS2pvbT2mdmwjkxp7gPVpCwFftT58szfQoZCWZCk7IezHquZAJARCIDXh1iVt0a3bYDOQKYBYaROz76cY6L6d074NvRb8SJNQsMDXogMgaNbU0VDIYNcl0jEsqOJaQNPzaZCA7ORdKSI5gBJ3m1qOzmFEXeJUF4gNN5mipHDBtovQSynyHJiVsrmX7ywQfSeb22YZBBRGbZBAZDZD");
        String fbToken2 = new String("CAAUV3RQqq1ABAFSrU7DENfS6BXti2hqvzhiHI2xSAZCCHmWMgemzvTJrpg47GZA6omZAOIYG1HKZAewCAoocA9HowPhmT1PSvP9OoherYq4uZC2dKzLNTkSWMye243QnZCo1TZC0NNfwHqnZAtQBLX0gA4LBI46CrW80fI9SZBTeVh9CNr7sgLCAyEc7oIZAtpRS8GAELEkaqhBNcZBfgYWVVBwuMbIAI1zjZBaU6hAZCRnTUOQZDZD");

        assertThat(fbToken1.length(), is(not(fbToken2.length())));
        log.debug("fbToken1.length: " + fbToken1.length() + " fbToken2.length: " + fbToken2.length()); // greater than 200, but less than 300

        String googleToken1 = new String("ya29.nAEox9z2iCuZHMxWccOLjEWD8rvTS0urlPzZmTMFZSb8P_eMx89FFuoFyXvVO6uaHSpBybxfZxuzIw");
        String googleToken2 = new String("ya29.mwH8z6DRzsc-figgFUXFIKzIlk3OOrsZ9UTRVd5-2pkow7f7Glui2am4sf4DH0zPhEtXGIDjuO5txQ");

        log.debug("googleToken1.length: " + googleToken2.length() + " googleToken2.length: " + googleToken2.length()); // greater than 70, but less than 100

        assertThat(googleToken1.length(), is(googleToken2.length()));

    }

    @Test
    public void encryptMessage() throws UnsupportedEncodingException {

        String key = "770A8A65DA156D24EE2A093277530142";
        AESCipher cipher = new AESCipher(key.getBytes("UTF-8"));

        String encryptedMessage = cipher.getEncryptedMessage("this is message");
        log.debug("Message is: {}", encryptedMessage);

        assertThat(encryptedMessage, is(notNullValue()));
        assertThat(encryptedMessage, is(not("this is message")));
    }

    @Test
    public void decryptMessage() throws UnsupportedEncodingException {

        String key = "770A8A65DA156D24EE2A093277530142";

        System.out.println("key length: " + key.length());
        AESCipher cipher = new AESCipher(key.getBytes("UTF-8"));

        String messageToEncrypt = "this is the secret message I want to encode";

        String encryptedMessage = cipher.getEncryptedMessage(messageToEncrypt);
        String decryptedMessage = cipher.getDecryptedMessage(encryptedMessage);

        log.debug("Original Message: {}, Encrypted Message: {}, Decrypted Message: {}", messageToEncrypt, encryptedMessage, decryptedMessage);
        assertThat(decryptedMessage, is(messageToEncrypt));
    }


    public String encryptMessage(String key) throws UnsupportedEncodingException {

        // String key = "770A8A65DA156D24EE2A093277530142";

        System.out.println("encrypt messagekey: " + key);

        AESCipher cipher = new AESCipher(key.getBytes("UTF-8"));

        String encryptedMessage = cipher.getEncryptedMessage("this is the secret message I want to encode");
        log.debug("Message is: {}", encryptedMessage);

        assertThat(encryptedMessage, is(notNullValue()));
        assertThat(encryptedMessage, is(not("this is the secret message I want to encode")));
        return encryptedMessage;
    }


    public void decryptMessage(String key) throws UnsupportedEncodingException {

        // String key = "770A8A65DA156D24EE2A093277530142";
        AESCipher cipher = new AESCipher(key.getBytes("UTF-8"));

        String messageToEncrypt = "this is the secret message I want to encode";

        String encryptedMessage = cipher.getEncryptedMessage(messageToEncrypt);
        String decryptedMessage = cipher.getDecryptedMessage(encryptedMessage);

        log.debug("Original Message: {}, Encrypted Message: {}, Decrypted Message: {}", messageToEncrypt, encryptedMessage, decryptedMessage);
        assertThat(decryptedMessage, is(messageToEncrypt));
    }

    public void decryptMessageKey(String key, String encryptedMessage) throws UnsupportedEncodingException {

        // String key = "770A8A65DA156D24EE2A093277530142";
        AESCipher cipher = new AESCipher(key.getBytes("UTF-8"));

        String messageToEncrypt = "this is the secret message I want to encode";

        //String encryptedMessage = cipher.getEncryptedMessage(messageToEncrypt);
        String decryptedMessage = cipher.getDecryptedMessage(encryptedMessage);

        log.debug("Original Message: {}, Encrypted Message: {}, Decrypted Message: {}", messageToEncrypt, encryptedMessage, decryptedMessage);
        assertThat(decryptedMessage, is(messageToEncrypt));
    }

    // http://howtodoinjava.com/2013/07/22/how-to-generate-secure-password-hash-md5-sha-pbkdf2-bcrypt-examples/
    private static String generateStorngPasswordHash(String password) throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        int iterations = 1000;
        char[] chars = password.toCharArray();
        byte[] salt = getSalt().getBytes();

        PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, 16 * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = skf.generateSecret(spec).getEncoded();
        System.out.println("hash length: " + hash.length);

        return toHex(hash);
        //return iterations + ":" + toHex(salt) + ":" + toHex(hash);
    }

    private static String getSalt() throws NoSuchAlgorithmException
    {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt.toString();
    }

    private static String toHex(byte[] array) throws NoSuchAlgorithmException
    {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        if(paddingLength > 0)
        {
            return String.format("%0"  +paddingLength + "d", 0) + hex;
        }else{
            return hex;
        }
    }


    @Test
    public void encryptMessageFromKeystore() {

        Key key = KeystoreUtil.getKeyFromKeyStore("src/test/resources/client-aes-keystore.jck", "mystorepass", "jceksaes", "mykeypass");

        AESCipher cipher = new AESCipher(key);

        String encryptedMessage = cipher.getEncryptedMessage("this is message");
        log.debug("Message is: {}", encryptedMessage);

        assertThat(encryptedMessage, is(notNullValue()));
        assertThat(encryptedMessage, is(not("this is message")));
    }

    @Test
    public void decryptMessageFromKeystore() {

        Key key = KeystoreUtil.getKeyFromKeyStore("src/test/resources/client-aes-keystore.jck", "mystorepass", "jceksaes", "mykeypass");
        AESCipher cipher = new AESCipher(key);

        try {
            byte[] hash = key.getEncoded();
            System.out.println("key: " + toHex(hash) + " length: " + toHex(hash).length());

        }
        catch (Exception e) {


        }

        String messageToEncrypt = "this is the secret message I want to encode";

        String encryptedMessage = cipher.getEncryptedMessage(messageToEncrypt);
        String decryptedMessage = cipher.getDecryptedMessage(encryptedMessage);

        log.debug("Original Message: {}, Encrypted Message: {}, Decrypted Message: {}", messageToEncrypt, encryptedMessage, decryptedMessage);
        assertThat(decryptedMessage, is(messageToEncrypt));
    }

    @Test
    public void usingStringBasedIV() {

        Key key = KeystoreUtil.getKeyFromKeyStore("src/test/resources/client-aes-keystore.jck", "mystorepass", "jceksaes", "mykeypass");
        String iv = "1111111111111111";

        AESCipher cipher = new AESCipher(key, iv.getBytes());

        String encryptedMessage = cipher.getEncryptedMessage("this is message");
        log.debug("Message is: {}", encryptedMessage);

        assertThat(encryptedMessage, is(notNullValue()));
        assertThat(encryptedMessage, is(not("this is message")));
    }

    @Test
    public void usingStringBasedIVWithUTF8ExtendedCharacter() throws UnsupportedEncodingException {

        thrown.expectCause(isA(InvalidAlgorithmParameterException.class));
        thrown.expectMessage("Wrong IV length: must be 16 bytes long");

        Key key = KeystoreUtil.getKeyFromKeyStore("src/test/resources/client-aes-keystore.jck", "mystorepass", "jceksaes", "mykeypass");
        String iv = "111111111111111\u4111";

        AESCipher cipher = new AESCipher(key, iv.getBytes("UTF-8"));

        String encryptedMessage = cipher.getEncryptedMessage("this is message");
        log.debug("Message is: {}", encryptedMessage);

        assertThat(encryptedMessage, is(notNullValue()));
        assertThat(encryptedMessage, is(not("this is message")));
    }

    @Test
    public void usingStringBasedIVWithIncorrectLength() throws UnsupportedEncodingException {

        thrown.expectCause(isA(InvalidAlgorithmParameterException.class));
        thrown.expectMessage("Wrong IV length: must be 16 bytes long");

        Key key = KeystoreUtil.getKeyFromKeyStore("src/test/resources/client-aes-keystore.jck", "mystorepass", "jceksaes", "mykeypass");
        String iv = "11111111";

        AESCipher cipher = new AESCipher(key, iv.getBytes("UTF-8"));

        cipher.getEncryptedMessage("this is message");
    }

    @Test
    public void encryptMessageFromKeystoreWithIv() {

        Key key = KeystoreUtil.getKeyFromKeyStore("src/test/resources/client-aes-keystore.jck", "mystorepass", "jceksaes", "mykeypass");
        byte[] iv = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        AESCipher cipher = new AESCipher(key, iv);

        String encryptedMessage = cipher.getEncryptedMessage("this is message");
        log.debug("Message is: {}", encryptedMessage);

        assertThat(encryptedMessage, is(notNullValue()));
        assertThat(encryptedMessage, is(not("this is message")));
    }

    @Test
    public void decryptMessageFromKeystoreWithIv() {

        Key key = KeystoreUtil.getKeyFromKeyStore("src/test/resources/client-aes-keystore.jck", "mystorepass", "jceksaes", "mykeypass");
        byte[] iv = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        AESCipher cipher = new AESCipher(key, iv);

        String messageToEncrypt = "this is the secret message I want to encode";

        String encryptedMessage = cipher.getEncryptedMessage(messageToEncrypt);
        String decryptedMessage = cipher.getDecryptedMessage(encryptedMessage);

        log.debug("Original Message: {}, Encrypted Message: {}, Decrypted Message: {}", messageToEncrypt, encryptedMessage, decryptedMessage);
        assertThat(decryptedMessage, is(messageToEncrypt));
    }

    @Test
    public void encryptDecryptUsingDifferentCiphersSameIV() {

        Key key = KeystoreUtil.getKeyFromKeyStore("src/test/resources/client-aes-keystore.jck", "mystorepass", "jceksaes", "mykeypass");
        byte[] iv = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        byte[] differentIV = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

        AESCipher cipher = new AESCipher(key, iv);
        AESCipher differentCipher = new AESCipher(key, differentIV);

        String messageToEncrypt = "this is the secret message I want to encode";

        String encryptedMessage = cipher.getEncryptedMessage(messageToEncrypt);
        String decryptedMessage = differentCipher.getDecryptedMessage(encryptedMessage);

        log.debug("Original Message: {}, Encrypted Message: {}, Decrypted Message: {}", messageToEncrypt, encryptedMessage, decryptedMessage);
        assertThat(decryptedMessage, is(messageToEncrypt));
    }

    @Test
    public void encryptDecryptUsingDifferentCiphersDifferentIV() {

        Key key = KeystoreUtil.getKeyFromKeyStore("src/test/resources/client-aes-keystore.jck", "mystorepass", "jceksaes", "mykeypass");
        byte[] iv = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        byte[] differentIV = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};

        AESCipher cipher = new AESCipher(key, iv);
        AESCipher differentCipher = new AESCipher(key, differentIV);

        String messageToEncrypt = "this is the secret message I want to encode";

        String encryptedMessage = cipher.getEncryptedMessage(messageToEncrypt);
        String decryptedMessage = differentCipher.getDecryptedMessage(encryptedMessage);

        log.debug("Encrypted: [{}], Decrypted[{}]", encryptedMessage, decryptedMessage);

        assertThat("Original message should have not been the same after decoding with a different IV", decryptedMessage, is(not(messageToEncrypt)));
    }

}
