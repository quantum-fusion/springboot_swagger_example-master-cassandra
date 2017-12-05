package guru.springframework.controllers;

/**
 * Created by hottelet on 9/4/15.
 */
public class Singleton {

    private String cassandraIpAddress = new String();
    private String cassandraPort = new String();

    private static Singleton singleInstance = new Singleton();
    private Singleton() {
        this.cassandraIpAddress = "";
        this.cassandraPort = "";
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

    public String getCassandraIpAddress()
    {
        return this.cassandraIpAddress;
    }

    public String getCassandraPort()
    {
        return this.cassandraPort;
    }

    /* Other methods protected by singleton-ness */
    protected static void HelloWorldMethod( ) {
        System.out.println("singleton hello world");
    }
}

