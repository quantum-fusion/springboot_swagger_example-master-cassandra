

#Key Benefits of Spring Boot REST Server with Swagger

- REST API Controller interface supports multiple controllers
- Docker support with Kubernetes supports scaling on AWS, Azure, IBM.
- Spring Boot supports Cloud Foundry CLI for scaling on AWS, Azure, IBM.
- Self documenting Swagger API interface (https://springframework.guru/spring-boot-restful-api-documentation-with-swagger-2/)
- In line tests from web browser
- Standard error handling interface for Log4j

- Application.properties for standard Cassandra defaults
- Application.properties supports changing port numbers

- Databases currently supported
- ORM Layer Design Pattern (CRUD operations to Cassandra)
- JPA Layer Design Pattern (CRUD operations to H2)
- Cassandra Datastax support  (https://www.datastax.com/products/datastax-enterprise)
- Datastax Driver v3.0
- H2 in memory database

Tools:
DataStax CQL IDE Editor for MACOS (http://docs.datastax.com/en/dse/5.1/dse-dev/datastax_enterprise/devcenter/dcToc.html )


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


#Step3: Run service locally
java -jar ./target/spring-boot-web-0.0.1-SNAPSHOT.jar

#Step4: Run web browser to generate Swagger docs and tests

Execute localhost:8080/v2/api-docs in web browser.
Execute localhost:8080/swagger-ui.html in web browser.

# Step5: configure service IP address for remote database ipaddress
java -jar ./target/spring-boot-web-0.0.1-SNAPSHOT.jar -Dcassandra_ip=<remoteIP> -Dcassandra_port=9042


#Step5(Not currently supported due to limitation in datastax driver v3): Build Docker image (see ./dockerbuild/README.md)

cd ./dockerbuild

./build.script

./upload.script

# current limitation on running REST service in Docker, and pointing to local 127.0.0.1 for cassandra node, and exposing Cassandra node to Docker.

docker run -d -p 8080:8080 -p 9042:9042 joethecoder2/spring-boot-web cassandra_ip=127.0.0.1 cassandra_port=9042

#Step6(Not currently supported due to limitation in Docker with datastax driver v3): Install Kubernetes, and Launch REST service (see ./Kubernetes/README.md)

cd ./Kubernetes

./minikube.run

./minikube.setup

./kubectl.test

# Cleanup

./minikube.stop

