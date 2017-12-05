package guru.springframework;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import guru.springframework.controllers.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@SpringBootApplication
public class SpringBootWebApplication {
    public static void main(String[] args) {
        String cassandraIpaddress = new String("");
        String cassandraPort = new String("");

        final Logger logger = LoggerFactory.getLogger(SpringBootWebApplication.class);

        Singleton cassandraProperties = Singleton.getInstance();

        if (args.length > 0)
        {

            for (String s: args) {

                if (s.contains("-Dcassandra.ip")) {
                    System.out.println("found cassandra ip");

                    cassandraIpaddress = s.substring(s.indexOf("=") + 1, s.length());

                    cassandraProperties.setCassandraIpAddress(cassandraIpaddress);

                    logger.info(cassandraIpaddress);
                }

                if (s.contains("-Dcassandra.port")) {
                    System.out.println("found cassandra port");

                    cassandraPort = s.substring(s.indexOf("=") + 1, s.length());

                    cassandraProperties.setCassandraPort(cassandraPort);

                    logger.info(cassandraPort);
                }
            }
        }




        SpringApplication.run(SpringBootWebApplication.class, args);
    }
}
