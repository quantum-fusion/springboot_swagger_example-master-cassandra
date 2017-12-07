package guru.springframework;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.ConfigurableApplicationContext;
import guru.springframework.controllers.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Arrays;


@SpringBootApplication
public class SpringBootWebApplication {
    public static void main(String[] args) {
        String cassandraIpaddress = new String("");
        String cassandraPort = new String("");

        final Logger logger = LoggerFactory.getLogger(SpringBootWebApplication.class);

        Singleton controllerProperties = Singleton.getInstance();

        if (args.length > 0)
        {

            for (String s: args) {

                if (s.contains("cassandra_ip")) {
                    System.out.println("found cassandra ip");

                    cassandraIpaddress = s.substring(s.indexOf("=") + 1, s.length());

                    controllerProperties.setCassandraIpAddress(cassandraIpaddress);

                    logger.info(cassandraIpaddress);
                }

                if (s.contains("cassandra_port")) {
                    System.out.println("found cassandra port");

                    cassandraPort = s.substring(s.indexOf("=") + 1, s.length());

                    controllerProperties.setCassandraPort(cassandraPort);

                    logger.info(cassandraPort);
                }
            }
        }




        // SpringApplication.run(SpringBootWebApplication.class, args);

        ConfigurableApplicationContext context = SpringApplication.run(SpringBootWebApplication.class, args);

        logger.info("Beans that are registered by Spring Boot:");
        String[] beanNamesApplicationContext = context.getBeanDefinitionNames();
        Arrays.sort(beanNamesApplicationContext);
        for (String beanName : beanNamesApplicationContext) {
            logger.info(beanName);
        }



    }
}
