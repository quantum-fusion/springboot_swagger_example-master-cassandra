package com.server.springboot.encryption;

import java.io.Serializable;

/**
 * Created by quantum-fusion on 3/29/18.
 */
public class PublicKeyEnc implements Serializable {

    static final long serialVersionUID = 1L; //assign a long value

    public byte[] publicKeyEnc;

    public byte[] getPublicKeyEnc() {
        return publicKeyEnc;
    }

    public void setPublicKeyEnc(byte[] publicKeyEnc) {
        this.publicKeyEnc = publicKeyEnc;
    }
}
