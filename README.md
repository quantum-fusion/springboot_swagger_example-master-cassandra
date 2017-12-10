

#Key Benefits of Spring Boot REST Server with Swagger

- Spring Boot version spring-boot-starter-parent 1.5.3.RELEASE with Datastax Driver v3.3.2
- Datastax Driver Protocol.V3
- REST API Controller interface supports multiple controllers
- Self documenting Swagger API interface (https://springframework.guru/spring-boot-restful-api-documentation-with-swagger-2/)
- Swagger In line tests from web browser
- Standard error handling interface for Log4j

- Docker support with Kubernetes supports scaling REST Spring Boot on AWS, Azure, IBM.
- Spring Boot supports Cloud Foundry CLI for scaling on AWS, Azure, IBM.

- Application.properties supports changing port numbers and defaults
- Command line arguments for changing IP address and Ports for Cassandra
- Configurable REST API supports dynamic changing IP address and Ports for Cassandra Connection Pooling

- Databases currently supported
- ORM Layer Design Pattern to Cassandra
- JPA Layer Design Pattern (CRUD operations to H2)
- Cassandra Datastax support  (https://www.datastax.com/products/datastax-enterprise)
- H2 in memory database

Tools:
DataStax CQL IDE Editor for MACOS (http://docs.datastax.com/en/dse/5.1/dse-dev/datastax_enterprise/devcenter/dcToc.html )

Known Limitations:
- Do not currently support Datastax driver with REST inside a Docker container, due to port restrictions on local 127.0.0.1 and port binding conflicts. 
- Do not currently support JPA based Cassandra CRUD calls, like Spring-Data for cassandra (https://projects.spring.io/spring-data-cassandra/#quick-start)


 .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::        (v1.4.2.RELEASE)


# spring-boot_swagger_example-master-cassandra Project

#Step1 or Step2: get Apache Cassandra running

cd apache-cassandra*

./run

cd ..

#Step2: Run cassandra in Docker
./runDocker-apache-cassandra-latest 

#Step3: Build project

mvn clean install

#Step4: Run service locally
java -jar ./target/spring-boot-web-0.0.1-SNAPSHOT.jar

#Step5: Run web browser to generate Swagger docs and tests

Execute localhost:8080/v2/api-docs in web browser.
Execute localhost:8080/swagger-ui.html in web browser.

# Step6: configure service IP address for remote database ipaddress
java -jar ./target/spring-boot-web-0.0.1-SNAPSHOT.jar -Dcassandra_ip=<remoteIP> -Dcassandra_port=9042

#Step7(Optional):
Configure remote IPaddress and Port using REST API
curl -X POST --header 'Content-Type: application/json' --header 'Accept: text/plain' -d '{ \ 
   "cassandraIpAddress": "<remoteIP>", \ 
   "cassandraPort": "9042" \ 
 }' 'http://localhost:8080/restaurant/testarguments'

#Step8(Not currently supported due to limitation in datastax driver v3): Build Docker image (see ./dockerbuild/README.md)

cd ./dockerbuild

./build.script

./upload.script

# current limitation on running REST service in Docker, and pointing to local 127.0.0.1 for cassandra node, and exposing Cassandra node to Docker.

docker run -d -p 8080:8080 -p 9042:9042 joethecoder2/spring-boot-web cassandra_ip=127.0.0.1 cassandra_port=9042

#Step9(Not currently supported due to limitation in Docker with datastax driver v3): Install Kubernetes, and Launch REST service (see ./Kubernetes/README.md)

cd ./Kubernetes

./minikube.run

./minikube.setup

./kubectl.test

# Cleanup

./minikube.stop

