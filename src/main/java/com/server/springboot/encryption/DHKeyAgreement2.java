package com.server.springboot.encryption;

/*
 * Copyright (c) 1997, 2001, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle nor the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * http://docs.oracle.com/javase/7/docs/technotes/guides/security/crypto/CryptoSpec.html#DH2Ex
 */

import java.math.BigInteger;
import java.security.*;
import java.security.spec.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import javax.crypto.interfaces.*;

/**
 * This program executes the Diffie-Hellman key agreement protocol
 * between 2 parties: Alice and Bob.
 *
 * By default, preconfigured parameters (1024-bit prime modulus and base
 * generator used by SKIP) are used.
 * If this program is called with the "-gen" option, a new set of
 * parameters is created.
 */


/**
 * Created by quantum-fusion on 10/2/15.
 */
public class DHKeyAgreement2 {

    private DHParameterSpec dhSkipParamSpec;

    private KeyAgreement aliceKeyAgree;
    private KeyAgreement bobKeyAgree;

  //  private PublicKey bobPubKey;
  //  private PublicKey alicePubKey;

    public DHKeyAgreement2() {

        System.out.println("constructor was executed");

    }

    public static void commandLine(String argv[]) {
        try {
            String mode = "USE_SKIP_DH_PARAMS";

            DHKeyAgreement2 keyAgree = new DHKeyAgreement2();

            if (argv.length > 1) {
                keyAgree.usage();
                throw new Exception("Wrong number of command options");
            } else if (argv.length == 1) {
                if (!(argv[0].equals("-gen"))) {
                    keyAgree.usage();
                    throw new Exception("Unrecognized flag: " + argv[0]);
                }
                mode = "GENERATE_DH_PARAMS";
            }

            keyAgree.setup(mode);
            keyAgree.run();

        } catch (Exception e) {
            System.err.println("Error: " + e);
            System.exit(1);
        }
    }

    public void setup(String mode) throws Exception {


        if (mode.equals("GENERATE_DH_PARAMS")) {
            // Some central authority creates new DH parameters
            System.out.println
                    ("Creating Diffie-Hellman parameters (takes VERY long) ...");
            AlgorithmParameterGenerator paramGen
                    = AlgorithmParameterGenerator.getInstance("DH");
            paramGen.init(512);
            AlgorithmParameters params = paramGen.generateParameters();
            dhSkipParamSpec = (DHParameterSpec) params.getParameterSpec
                    (DHParameterSpec.class);
        } else {
            // use some pre-generated, default DH parameters
            System.out.println("Using SKIP Diffie-Hellman parameters");
            dhSkipParamSpec = new DHParameterSpec(skip1024Modulus,
                    skip1024Base);
        }
    }

    public void run() throws Exception {

//        AliceKeyGenerate();
//
//        BobKeyGenerate();
//
//        byte[] alicesecretkey = generateAliceSecretKey();
//
//        byte[] bobsecretkey = generateBobSecretKey();
//
//        compareSecrets(alicesecretkey, bobsecretkey);
//
//        desTest();

    }

        public byte[] AliceKeyGenerate() throws Exception {


        /*
         * Alice creates her own DH key pair, using the DH parameters from
         * above
         */
        System.out.println("ALICE: Generate DH keypair ...");
        KeyPairGenerator aliceKpairGen = KeyPairGenerator.getInstance("DH");
        aliceKpairGen.initialize(dhSkipParamSpec);
        KeyPair aliceKpair = aliceKpairGen.generateKeyPair();

        // Alice creates and initializes her DH KeyAgreement object
        System.out.println("ALICE: Initialization ...");
        KeyAgreement aliceKeyAgree = KeyAgreement.getInstance("DH");
        aliceKeyAgree.init(aliceKpair.getPrivate());

        this.aliceKeyAgree = aliceKeyAgree;

        // Alice encodes her public key, and sends it over to Bob.
        byte[] alicePubKeyEnc = aliceKpair.getPublic().getEncoded();

        // this.alicePubKeyEnc = alicePubKeyEnc;

        System.out.println("alicePubKeyEnc: " + toHexString(alicePubKeyEnc));

        return alicePubKeyEnc;

    }


    //ToDo
    /*

    Generate a AES256 bit key with ECDSA
     */
    public void ECDSADH() {
        // https://stackoverflow.com/questions/21081713/diffie-hellman-key-exchange-in-java


    }

        public byte[] BobKeyGenerate(byte[] alicePubKeyEnc) throws Exception {


        /*
         * Let's turn over to Bob. Bob has received Alice's public key
         * in encoded format.
         * He instantiates a DH public key from the encoded key material.
         */
        KeyFactory bobKeyFac = KeyFactory.getInstance("DH");
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec
                (alicePubKeyEnc);
        PublicKey alicePubKey = bobKeyFac.generatePublic(x509KeySpec);

        /*
         * Bob gets the DH parameters associated with Alice's public key.
         * He must use the same parameters when he generates his own key
         * pair.
         */
        DHParameterSpec dhParamSpec = ((DHPublicKey) alicePubKey).getParams();

        // Bob creates his own DH key pair
        System.out.println("BOB: Generate DH keypair ...");
        KeyPairGenerator bobKpairGen = KeyPairGenerator.getInstance("DH");
        bobKpairGen.initialize(dhParamSpec);
        KeyPair bobKpair = bobKpairGen.generateKeyPair();

        // Bob creates and initializes his DH KeyAgreement object
        System.out.println("BOB: Initialization ...");
        KeyAgreement bobKeyAgree = KeyAgreement.getInstance("DH");
        bobKeyAgree.init(bobKpair.getPrivate());

        this.bobKeyAgree = bobKeyAgree;

        // Bob encodes his public key, and sends it over to Alice.
        byte[] bobPubKeyEnc = bobKpair.getPublic().getEncoded();

       // this.bobPubKeyEnc = bobPubKeyEnc;

        System.out.println("Bob PubKeyEnc:" + toHexString(bobPubKeyEnc));

            return bobPubKeyEnc;

    }

    public byte[] generateAliceSecretKey(byte[] bobPubKeyEnc) throws Exception {

        /*
         * Alice uses Bob's public key for the first (and only) phase
         * of her version of the DH
         * protocol.
         * Before she can do so, she has to instantiate a DH public key
         * from Bob's encoded key material.
         *
         */
        byte[] aliceSharedSecret = {0,0};

        try {

            KeyFactory aliceKeyFac = KeyFactory.getInstance("DH");
            X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(bobPubKeyEnc);
            PublicKey bobPubKey = aliceKeyFac.generatePublic(x509KeySpec);
            // this.bobPubKey = bobPubKey;

            System.out.println("bobPubKey: " + bobPubKey.toString());

            System.out.println("ALICE: Execute PHASE1 ...");
            aliceKeyAgree.doPhase(bobPubKey, true);


            aliceSharedSecret = aliceKeyAgree.generateSecret();
            int aliceLen = aliceSharedSecret.length;

            System.out.println("Alice secret: " +
                    toHex(aliceSharedSecret));

            return aliceSharedSecret;

        }
        catch (Exception e)
        {
            System.out.println(e);
        }

        return aliceSharedSecret;

    }

    public byte[] generateBobSecretKey(byte[] alicePubKeyEnc) throws Exception {

        KeyFactory bobKeyFac = KeyFactory.getInstance("DH");
        X509EncodedKeySpec x509KeySpec2 = new X509EncodedKeySpec
                (alicePubKeyEnc);
        PublicKey alicePubKey = bobKeyFac.generatePublic(x509KeySpec2);
      //  this.alicePubKey = alicePubKey;

        /*
         * Bob uses Alice's public key for the first (and only) phase
         * of his version of the DH
         * protocol.
         */

        byte[] dummy = {0,0};

        try {

            System.out.println("BOB: Execute PHASE1 ...");
            this.bobKeyAgree.doPhase(alicePubKey, true);

        }
        catch (Exception e)
        {
            System.out.println("bobKeyAgree.doPhase error: " + e);
        }


        /*
         * At this stage, both Alice and Bob have completed the DH key
         * agreement protocol.
         * Both generate the (same) shared secret.
         */

        try {

            byte[] bobSharedSecret = bobKeyAgree.generateSecret();

            System.out.println("Bob secret: " +
                    toHexString(bobSharedSecret));


            return bobSharedSecret;
        }
        catch (Exception e)
        {
            System.out.println("bobKeyAgree.generateSecret error: " + e);

        }


        return dummy;

    }

    public void compareSecrets(byte[] aliceSharedSecret, byte[] bobSharedSecret) throws Exception {
        if (!java.util.Arrays.equals(aliceSharedSecret, bobSharedSecret))
            throw new Exception("Shared secrets differ");

        System.out.println("Shared secrets are the same");

    }

    public static String toHex(byte[] array) throws NoSuchAlgorithmException
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

//    public void desTest() throws Exception {
//
//        /*
//         * Now let's return the shared secret as a SecretKey object
//         * and use it for encryption. First, we generate SecretKeys for the
//         * "DES" algorithm (based on the raw shared secret data) and
//         * then we use DES in ECB mode
//         * as the encryption algorithm. DES in ECB mode does not require any
//         * parameters.
//         *
//         * Then we use DES in CBC mode, which requires an initialization
//         * vector (IV) parameter. In CBC mode, you need to initialize the
//         * Cipher object with an IV, which can be supplied using the
//         * javax.crypto.spec.IvParameterSpec class. Note that you have to use
//         * the same IV for encryption and decryption: If you use a different
//         * IV for decryption than you used for encryption, decryption will
//         * fail.
//         *
//         * NOTE: If you do not specify an IV when you initialize the
//         * Cipher object for encryption, the underlying implementation
//         * will generate a random one, which you have to retrieve using the
//         * javax.crypto.Cipher.getParameters() method, which returns an
//         * instance of java.security.AlgorithmParameters. You need to transfer
//         * the contents of that object (e.g., in encoded format, obtained via
//         * the AlgorithmParameters.getEncoded() method) to the party who will
//         * do the decryption. When initializing the Cipher for decryption,
//         * the (reinstantiated) AlgorithmParameters object must be passed to
//         * the Cipher.init() method.
//         */
//        System.out.println("Return shared secret as SecretKey object ...");
//        // Bob
//        // NOTE: The call to bobKeyAgree.generateSecret above reset the key
//        // agreement object, so we call doPhase again prior to another
//        // generateSecret call
//        bobKeyAgree.doPhase(alicePubKey, true);
//        SecretKey bobDesKey = bobKeyAgree.generateSecret("DES");
//
//        // Alice
//        // NOTE: The call to aliceKeyAgree.generateSecret above reset the key
//        // agreement object, so we call doPhase again prior to another
//        // generateSecret call
//        aliceKeyAgree.doPhase(bobPubKey, true);
//        SecretKey aliceDesKey = aliceKeyAgree.generateSecret("DES");
//
//        /*
//         * Bob encrypts, using DES in ECB mode
//         */
//        Cipher bobCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
//        bobCipher.init(Cipher.ENCRYPT_MODE, bobDesKey);
//
//        byte[] cleartext = "This is just an example".getBytes();
//        byte[] ciphertext = bobCipher.doFinal(cleartext);
//
//        /*
//         * Alice decrypts, using DES in ECB mode
//         */
//        Cipher aliceCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
//        aliceCipher.init(Cipher.DECRYPT_MODE, aliceDesKey);
//        byte[] recovered = aliceCipher.doFinal(ciphertext);
//
//        if (!java.util.Arrays.equals(cleartext, recovered))
//            throw new Exception("DES in CBC mode recovered text is " +
//                    "different from cleartext");
//        System.out.println("DES in ECB mode recovered text is " +
//                "same as cleartext");
//
//        /*
//         * Bob encrypts, using DES in CBC mode
//         */
//        bobCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
//        bobCipher.init(Cipher.ENCRYPT_MODE, bobDesKey);
//
//        cleartext = "This is just an example".getBytes();
//        ciphertext = bobCipher.doFinal(cleartext);
//        // Retrieve the parameter that was used, and transfer it to Alice in
//        // encoded format
//        byte[] encodedParams = bobCipher.getParameters().getEncoded();
//
//        /*
//         * Alice decrypts, using DES in CBC mode
//         */
//        // Instantiate AlgorithmParameters object from parameter encoding
//        // obtained from Bob
//        AlgorithmParameters params = AlgorithmParameters.getInstance("DES");
//        params.init(encodedParams);
//        aliceCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
//        aliceCipher.init(Cipher.DECRYPT_MODE, aliceDesKey, params);
//        recovered = aliceCipher.doFinal(ciphertext);
//
//        if (!java.util.Arrays.equals(cleartext, recovered))
//            throw new Exception("DES in CBC mode recovered text is " +
//                    "different from cleartext");
//        System.out.println("DES in CBC mode recovered text is " +
//                "same as cleartext");
//    }



    /*
     * Converts a byte to hex digit and writes to the supplied buffer
     */
    public static void byte2hex(byte b, StringBuffer buf) {
        char[] hexChars = { '0', '1', '2', '3', '4', '5', '6', '7', '8',
                '9', 'A', 'B', 'C', 'D', 'E', 'F' };
        int high = ((b & 0xf0) >> 4);
        int low = (b & 0x0f);
        buf.append(hexChars[high]);
        buf.append(hexChars[low]);
    }

    /*
     * Converts a byte array to hex string
     */
    public static String toHexString(byte[] block) {
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
     * Prints the usage of this test.
     */
    private void usage() {
        System.err.print("DHKeyAgreement usage: ");
        System.err.println("[-gen]");
    }

    // The 1024 bit Diffie-Hellman modulus values used by SKIP
    private static final byte skip1024ModulusBytes[] = {
            (byte)0xF4, (byte)0x88, (byte)0xFD, (byte)0x58,
            (byte)0x4E, (byte)0x49, (byte)0xDB, (byte)0xCD,
            (byte)0x20, (byte)0xB4, (byte)0x9D, (byte)0xE4,
            (byte)0x91, (byte)0x07, (byte)0x36, (byte)0x6B,
            (byte)0x33, (byte)0x6C, (byte)0x38, (byte)0x0D,
            (byte)0x45, (byte)0x1D, (byte)0x0F, (byte)0x7C,
            (byte)0x88, (byte)0xB3, (byte)0x1C, (byte)0x7C,
            (byte)0x5B, (byte)0x2D, (byte)0x8E, (byte)0xF6,
            (byte)0xF3, (byte)0xC9, (byte)0x23, (byte)0xC0,
            (byte)0x43, (byte)0xF0, (byte)0xA5, (byte)0x5B,
            (byte)0x18, (byte)0x8D, (byte)0x8E, (byte)0xBB,
            (byte)0x55, (byte)0x8C, (byte)0xB8, (byte)0x5D,
            (byte)0x38, (byte)0xD3, (byte)0x34, (byte)0xFD,
            (byte)0x7C, (byte)0x17, (byte)0x57, (byte)0x43,
            (byte)0xA3, (byte)0x1D, (byte)0x18, (byte)0x6C,
            (byte)0xDE, (byte)0x33, (byte)0x21, (byte)0x2C,
            (byte)0xB5, (byte)0x2A, (byte)0xFF, (byte)0x3C,
            (byte)0xE1, (byte)0xB1, (byte)0x29, (byte)0x40,
            (byte)0x18, (byte)0x11, (byte)0x8D, (byte)0x7C,
            (byte)0x84, (byte)0xA7, (byte)0x0A, (byte)0x72,
            (byte)0xD6, (byte)0x86, (byte)0xC4, (byte)0x03,
            (byte)0x19, (byte)0xC8, (byte)0x07, (byte)0x29,
            (byte)0x7A, (byte)0xCA, (byte)0x95, (byte)0x0C,
            (byte)0xD9, (byte)0x96, (byte)0x9F, (byte)0xAB,
            (byte)0xD0, (byte)0x0A, (byte)0x50, (byte)0x9B,
            (byte)0x02, (byte)0x46, (byte)0xD3, (byte)0x08,
            (byte)0x3D, (byte)0x66, (byte)0xA4, (byte)0x5D,
            (byte)0x41, (byte)0x9F, (byte)0x9C, (byte)0x7C,
            (byte)0xBD, (byte)0x89, (byte)0x4B, (byte)0x22,
            (byte)0x19, (byte)0x26, (byte)0xBA, (byte)0xAB,
            (byte)0xA2, (byte)0x5E, (byte)0xC3, (byte)0x55,
            (byte)0xE9, (byte)0x2F, (byte)0x78, (byte)0xC7
    };

    // The SKIP 1024 bit modulus
    private static final BigInteger skip1024Modulus
            = new BigInteger(1, skip1024ModulusBytes);

    // The base used with the SKIP 1024 bit modulus
    private static final BigInteger skip1024Base = BigInteger.valueOf(2);
}

