package com.server.springboot.encryption;

import org.bson.types.ObjectId;
import org.joda.time.DateTime;

/**
 * Created by hottelet on 9/30/15.
 */

import org.bson.types.ObjectId;
import org.joda.time.DateTime;

public class TokenInfo {
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

    private String iss;
    private String sub;
    private String aud;
    private DateTime expires;
    private DateTime issued;
    private String jti;
    private String firstName;
    private String lastName;
    private String admin;
    private String userId;

    public String getIss() {
        return iss;
    }

    public void setIss(String iss) {
        this.iss = iss;
    }

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    public String getAud() {
        return aud;
    }

    public void setAud(String aud) {
        this.aud = aud;
    }

    public DateTime getExpires() {
        return expires;
    }

    public void setExpires(DateTime expires) {
        this.expires = expires;
    }

    public DateTime getIssued() {
        return issued;
    }

    public void setIssued(DateTime issued) {
        this.issued = issued;
    }

    public String getJti() {
        return jti;
    }

    public void setJti(String jti) {
        this.jti = jti;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
