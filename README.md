

#Key Benefits of Spring Boot REST Server with Swagger

- REST API Controller interface supports multiple controllers

- Self documenting Swagger API interface (https://springframework.guru/spring-boot-restful-api-documentation-with-swagger-2/)
- In line tests from web browser
- Standard error handling interface for Log4j

- Application.properties for standard Cassandra defaults
- Application.properties supports changing port numbers

- Databases currently supported
- Cassandra Datastax support
- H2 in memory database
- JPA layer support

 .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::        (v1.4.2.RELEASE)


# spring-boot_swagger_example-master-cassandra Project

#Step1: get Apache Cassandra running

cd apache-cassandra*

./run

cd ..

#Step2: Build project

mvn clean install


#Step3: Run service
java -jar ./target/spring-boot-web-0.0.1-SNAPSHOT.jar

#Step4: Run web browser to generate Swagger docs and tests
localhost:8080/v2/api-docs
localhost:8080/swagger-ui.html
