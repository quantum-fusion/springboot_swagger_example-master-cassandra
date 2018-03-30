package com.server.springboot.encryption;

import org.junit.Before;
import org.junit.Test;

public class MainAppTest {

    private String initialIV;

    @Before
    public void setup() {
        createSystemProperties();
        initialIV = "0000000000000000";
    }
    

    @Test
    public void showKey() {
        MainApp.main(createArguments("showKey"));
    }

    @Test
    public void encrypt() {
        MainApp.main(createArguments("encrypt", "'this is the message that should be decrypted'", initialIV));
    }

    @Test
    public void decrypt() {
        MainApp.main(createArguments("decrypt", "gwJB/ke1X6kLQP8df3RjS7X7P2WbswqML+FcXqCsElPzMyo54oIj4Ef+glovFk3T", initialIV));
    }

    @Test
    public void decryptWithBadIV() {
        MainApp.main(createArguments("decrypt", "gwJB/ke1X6kLQP8df3RjS7X7P2WbswqML+FcXqCsElPzMyo54oIj4Ef+glovFk3T", "1111111111111111"));
    }

    private String[] createArguments(String... args) {
        return args;
    }

    private void createSystemProperties() {
        System.setProperty("keystore","src/test/resources/client-aes-keystore.jck");
        System.setProperty("storepass","mystorepass");
        System.setProperty("alias","jceksaes");
        System.setProperty("keypass", "mykeypass");
    }
}
