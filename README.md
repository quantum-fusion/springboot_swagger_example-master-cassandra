
# REST Spring Boot with Database and OpenAPI Swagger Interfaces:

## Get Started

git clone https://github.com/quantum-fusion/springboot_swagger_example-master-cassandra

mvn clean install

## Key Benefits of Spring Boot REST Server with OpenAPI standard

- OpenAPI standard supported by Swagger2 (https://swagger.io/blog/difference-between-swagger-and-openapi/)
- Spring Boot supports Cloud Native Computing Foundation standards (https://github.com/quantum-fusion/landscape)
- Docker supported and setup via argument configuration at command line (see https://github.com/quantum-fusion/springboot_swagger_example-master-cassandra/blob/master/runDockerApplication)
- AWS Serverless Lambda supported without Swagger2 (see https://github.com/quantum-fusion/aws-serverless-java-container/tree/master/samples/springboot/pet-store)
- Kubernetes supported and setup via REST remote configuration API (see https://github.com/quantum-fusion/springboot_swagger_example-master-cassandra/tree/master/Kubernetes)
- Spring Boot version spring-boot-starter-parent 1.5.3.RELEASE with Datastax Driver v3.3.2 (see https://github.com/quantum-fusion/springboot_swagger_example-master-cassandra/blob/master/runJar)
- Datastax Driver Protocol.V3
- REST API Controller interface supports multiple controllers
- Self documenting Swagger API interface (https://springframework.guru/spring-boot-restful-api-documentation-with-swagger-2/)
- Swagger In line tests from web browser
- Standard error handling interface for Log4j
- Supports AES256(PKCS5Padding) Encryption of Data at Rest, and Data Transport (https://github.com/quantum-fusion/aes-256-encryption-utility)
- Supports Session based management using JWT Tokens (https://github.com/quantum-fusion/aes-256-encryption-utility)
- Supports Diffie Hellman (https://github.com/quantum-fusion/aes-256-encryption-utility)
- Supports REST based Android integration using AES256(PKCS5Padding) (https://github.com/quantum-fusion/aes-256-encryption-utility)
- Mobile to REST Server End to End encryption (see https://www.whatsapp.com/security/WhatsApp-Security-Whitepaper.pdf)

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

## ScyllaDB Supported with Cassandra CQL:
https://hub.docker.com/r/scylladb/scylla/
https://www.scylladb.com/download/amazon/

Known Limitations:
- Do not currently support JPA based Cassandra CRUD calls, like Spring-Data for cassandra (https://projects.spring.io/spring-data-cassandra/#quick-start)
- Do not support MongoDB via JPA connector, but could consider integration test like spring-data MongoDB. (https://projects.spring.io/spring-data-mongodb/)
- Do not support MySQL via JPA connector, but could consider integration test. (https://spring.io/guides/gs/accessing-data-mysql/)
- Do not support Postgres via JPA connector, but could consider integration test. (https://springframework.guru/configuring-spring-boot-for-postgresql/)
- Do not support Redis, but could consider integration test. (https://projects.spring.io/spring-data-redis/)

Integration Tests to Consider:
- Consider Docker CQL test for 2 separate Docker images (https://yurisubach.com/2016/03/24/cassandra-docker-test-cluster/ )
- Consider creating Docker image for Spring Data Cassandra (https://projects.spring.io/spring-data-cassandra/#quick-start)



## AWS Cassandra Single Click(tested successfully):
- BitNami image was selected
- See instructions to get login and password for Bitnami image for Cassandra credentials (https://docs.bitnami.com/aws/faq/starting-bitnami-aws/find_credentials/#using-the-aws-marketplace)

## Other Cassandra multi-node cluster providers to Consider(not yet tested):
- Spotify Cassandra Quick start (https://github.com/spotify/docker-cassandra)
- Multi Node Cassandra Cluster on Kubernetes (https://github.com/vyshane/cassandra-kubernetes)

## Setup Options 1 or 2 or 3 or All:
- 1. Run Local SpringBoot and Cassandra single node.
- 2. Run Local Docker SpringBoot and Local Docker Cassandra single node.
- 3. Run Local Kubernetes SpringBoot and remote AWS Bitnami Cassandra single node, requires configurable IP and login credentials.
- All: SpringBoot will run on all platforms and support remote AWS Bitnami Cassandra single node, with configurable IP address and login credentials.


## Installation Instructions:

// spring-boot_swagger_example-master-cassandra Project

## Build project

mvn clean install

## Step1: get Apache Cassandra running locally or in Docker (see Step2).

cd apache-cassandra*

./run

cd ..

## Step2: Run service locally
java -jar ./target/spring-boot-web-0.0.1-SNAPSHOT.jar

## Step3: Run web browser to generate Swagger docs and tests

Execute localhost:8080/v2/api-docs in web browser.
Execute localhost:8080/swagger-ui.html in web browser.

## Step4: Configure service IP address for remote database ipaddress
java -jar ./target/spring-boot-web-0.0.1-SNAPSHOT.jar -Dcassandra_ip=<remoteIP> -Dcassandra_port=9042 -Dlogin=cassandra -Dpassword=GN1aJxMnsWOR

## Step5: REST based configuration for Kubernetes support of remote Cassandra nodes due to Google Kubernetes not supporting Templates. 
(Optional):
 curl -X POST --header 'Content-Type: application/json' --header 'Accept: text/plain' -d '{ \ 
   "cassandraIpAddress": "<remoteIP>", \ 
   "cassandraPort": "9042", \ 
   "login": "cassandra", \ 
   "password": "GN1aJxMnsWOR" \ 
 }' 'http://localhost:8080/restaurant/testarguments'
 

## Step6: Build Docker image (see ./dockerbuild/README.md)

cd ./dockerbuild

./build.script

./upload.script

cd ..

## Step7 (Optional): run Docker instances Locally

./runDockerApplication

./testDockerApplication

## Step8 (Optional): Install Kubernetes, and Launch REST service (see ./Kubernetes/README.md)

cd ./Kubernetes

./minikube.run

### must manually delete prior images and pods from ghost defunct pod services
./minikube.setup

### must check ip addresses for kubernetes master ip address
./kubectl.test

## Cleanup

./minikube.stop

