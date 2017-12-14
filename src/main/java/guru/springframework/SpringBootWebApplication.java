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
        String login = new String("");
        String password = new String("");

        final Logger logger = LoggerFactory.getLogger(SpringBootWebApplication.class);

        Singleton controllerProperties = Singleton.getInstance();

        if (args.length > 0)
        {

            for (String s: args) {

                if (s.contains("cassandra_ip")) {

                    cassandraIpaddress = s.substring(s.indexOf("=") + 1, s.length());

                    controllerProperties.setCassandraIpAddress(cassandraIpaddress);

                    logger.info("found cassandra ip " + cassandraIpaddress);
                }

                if (s.contains("cassandra_port")) {

                    cassandraPort = s.substring(s.indexOf("=") + 1, s.length());

                    controllerProperties.setCassandraPort(cassandraPort);

                    logger.info("found cassandra port " + cassandraPort);
                }

                if(s.contains("login")) {
                    login = s.substring(s.indexOf("=") + 1, s.length());

                    controllerProperties.setLogin(login);

                    logger.info("found login " + login);
                }

                if(s.contains("password")) {
                    password = s.substring(s.indexOf("=") + 1, s.length());

                    controllerProperties.setPassword(password);

                    logger.info("found password " + password);
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
