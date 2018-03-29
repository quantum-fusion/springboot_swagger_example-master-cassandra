package com.server.springboot.encryption;

/**
 * Created by hottelet on 9/30/15.
 */

import java.security.InvalidKeyException;
import java.security.SignatureException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import net.oauth.jsontoken.JsonToken;
import net.oauth.jsontoken.JsonTokenParser;
import net.oauth.jsontoken.crypto.HmacSHA256Signer;
import net.oauth.jsontoken.crypto.HmacSHA256Verifier;
import net.oauth.jsontoken.crypto.SignatureAlgorithm;
import net.oauth.jsontoken.crypto.Verifier;
import net.oauth.jsontoken.discovery.VerifierProvider;
import net.oauth.jsontoken.discovery.VerifierProviders;

import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.joda.time.DateTime;

import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import org.joda.time.Instant;
import org.joda.time.Period;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides static methods for creating and verifying access tokens and such.
 * @author davidm
 * http://stackoverflow.com/questions/23808460/jwt-json-web-token-library-for-java
 */
public class AuthHelper {

    private String SIGNING_KEY;

    private static final Logger LOG = LoggerFactory.getLogger(AuthHelper.class);

    public AuthHelper(String SigningKey) {

       this.SIGNING_KEY = SigningKey;

    }

    /**
     * Creates a json web token which is a digitally signed token that contains a payload (e.g. userId to identify
     * the user). The signing key is secret. That ensures that the token is authentic and has not been modified.
     * Using a jwt eliminates the need to store authentication session information in a database.
     */

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
    // "firstName": "John"                ; Maps to firstName from social login token extracted from social server @ runtime
    // "lastName" : "Doe"                 ; Maps to firstName from social login token extracted from social server @ runtime
    // "admin": true / false             ; False for all regular login customers @ runtime
    public String createJsonWebToken(String userId, Long durationDays, String iss, String sub, String jti, String aud, String firstName, String lastName, String admin) throws Exception {
        //Current time and signing algorithm
        Calendar cal = Calendar.getInstance();
        HmacSHA256Signer signer;
        try {
            signer = new HmacSHA256Signer(iss, null, SIGNING_KEY.getBytes());
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        }

        //Configure JSON token
        JsonToken token = new JsonToken(signer);
        token.setAudience(aud);
        token.setIssuedAt(new Instant(cal.getTimeInMillis()));
        token.setExpiration(new Instant(cal.getTimeInMillis() + 1000L * 60L * 60L * 24L * durationDays));


        //Configure request object, which provides information of the item
        JsonObject request = new JsonObject();
        request.addProperty("userId", userId);
        request.addProperty("sub",sub);
        request.addProperty("jti", jti);
        request.addProperty("firstName", firstName);
        request.addProperty("lastName", lastName);
        request.addProperty("admin", admin);

        JsonObject payload = token.getPayloadAsJsonObject();
        payload.add("info", request);


        try {
            return token.serializeAndSign();
        } catch (SignatureException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Verifies a json web token's validity and extracts the user id and other information from it.
     * @param token
     * @return
     * @throws SignatureException
     * @throws InvalidKeyException
     */
    public TokenInfo verifyToken(String token) throws Exception
    {
        try {
            final Verifier hmacVerifier = new HmacSHA256Verifier(SIGNING_KEY.getBytes());

            VerifierProvider hmacLocator = new VerifierProvider() {

                @Override
                public List<Verifier> findVerifier(String id, String key){
                    return Lists.newArrayList(hmacVerifier);
                }
            };
            VerifierProviders locators = new VerifierProviders();
            locators.setVerifierProvider(SignatureAlgorithm.HS256, hmacLocator);
            net.oauth.jsontoken.Checker checker = new net.oauth.jsontoken.Checker(){

                @Override
                public void check(JsonObject payload) throws SignatureException {
                    // don't throw - allow anything
                }

            };
            //Ignore Audience does not mean that the Signature is ignored
            JsonTokenParser parser = new JsonTokenParser(locators,
                    checker);
            JsonToken jt;
            try {
                jt = parser.verifyAndDeserialize(token);
            } catch (SignatureException e) {
                throw new RuntimeException(e);
            }
            JsonObject payload = jt.getPayloadAsJsonObject();
            TokenInfo t = new TokenInfo();

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

            String issuer = payload.getAsJsonPrimitive("iss").getAsString();
            String subject = payload.getAsJsonObject("info").getAsJsonPrimitive("sub").getAsString();
            String audience = payload.getAsJsonPrimitive("aud").getAsString();
            Instant exp = jt.getExpiration();
            Instant iat = jt.getIssuedAt();
            String jti = payload.getAsJsonObject("info").getAsJsonPrimitive("jti").getAsString();
            String firstName = payload.getAsJsonObject("info").getAsJsonPrimitive("firstName").getAsString();
            String lastName = payload.getAsJsonObject("info").getAsJsonPrimitive("lastName").getAsString();
            String admin = payload.getAsJsonObject("info").getAsJsonPrimitive("admin").getAsString();
            String userId =  payload.getAsJsonObject("info").getAsJsonPrimitive("userId").getAsString();

            //  String iat = payload.getAsJsonPrimitive("iat").getAsString();
            //  String exp = payload.getAsJsonPrimitive("exp").getAsString();
            //      System.out.println("iat = " + iat);
            //      System.out.println("exp = " + exp);

            LOG.debug("issuer = " + issuer);
            LOG.debug("subject = " + subject);
            LOG.debug("audience = " + audience);
            LOG.debug("exp: " + exp);
            LOG.debug("iat: " + iat);
            LOG.debug("jti: " + jti);
            LOG.debug("firstName: " + firstName);
            LOG.debug("lastName: " + lastName);
            LOG.debug("admin: " + admin);
            LOG.debug("userId = " + userId);

            LOG.debug("Authhelper:: right before setting tokenInfo fields");

            t.setIss(issuer);
            t.setSub(subject);
            t.setAud(audience);
            t.setExpires(exp.toDateTime());
            t.setIssued(iat.toDateTime());
            t.setJti(jti);
            t.setFirstName(firstName);
            t.setLastName(lastName);
            t.setAdmin(admin);
            t.setUserId(userId);

            return t;

        } catch (InvalidKeyException e1) {
            throw new RuntimeException(e1);
        }
    }


}