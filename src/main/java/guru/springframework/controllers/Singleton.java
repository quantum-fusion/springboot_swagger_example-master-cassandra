package guru.springframework.controllers;

/**
 * Created by hottelet on 9/4/15.
 */
public class Singleton {

    private String cassandraIpAddress = new String();
    private String cassandraPort = new String();
    private String login = new String();
    private String password = new String();

    private static Singleton singleInstance = new Singleton();
    private Singleton() {
        this.cassandraIpAddress = "";
        this.cassandraPort = "";
        this.login = "";
        this.password = "";
    }
    public static Singleton getInstance() {
        return singleInstance;
    }

    public void setCassandraIpAddress (String ipAddress)
    {
        this.cassandraIpAddress = ipAddress;
    }

    public void setCassandraPort (String port)
    {
        this.cassandraPort = port;
    }

    public void setLogin(String login) { this.login = login; }

    public void setPassword(String password) { this.password = password; }

    public String getCassandraIpAddress()
    {
        return this.cassandraIpAddress;
    }

    public String getCassandraPort()
    {
        return this.cassandraPort;
    }

    public String getLogin() { return this.login; }

    public String getPassword() { return this.password; }

    /* Other methods protected by singleton-ness */
    protected static void HelloWorldMethod( ) {
        System.out.println("singleton hello world");
    }
}

