package com.server.springboot.encryption;

/**
 * Created by quantum-fusion on 10/21/15.
 */
public class JWTHelper {

    private TokenInfo t;
    private String JWT;

    public TokenInfo getT() {
        return t;
    }

    public void setT(TokenInfo t) {
        this.t = t;
    }

    public String getJWT() {
        return JWT;
    }

    public void setJWT(String JWT) {
        this.JWT = JWT;
    }
}
