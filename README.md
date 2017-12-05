

#Key Benefits of Spring Boot REST Server with Swagger

- REST API Controller interface supports multiple controllers

- Self documenting Swagger API interface (https://springframework.guru/spring-boot-restful-api-documentation-with-swagger-2/)
- In line tests from web browser
- Standard error handling interface for Log4j

- Application.properties for standard Cassandra defaults
- Application.properties supports changing port numbers

- Databases currently supported
- DAO Layer Design Pattern (CRUD operations to Cassandra)
- JPA Layer Design Pattern (CRUD operations to H2)
- Cassandra Datastax support (https://www.datastax.com/products/datastax-enterprise)
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


#Step3: Run service
java -jar ./target/spring-boot-web-0.0.1-SNAPSHOT.jar

#Step4: Run web browser to generate Swagger docs and tests

Execute localhost:8080/v2/api-docs in web browser.
Execute localhost:8080/swagger-ui.html in web browser.

#Step5(Optional): Install Docker for MACOS

https://docs.docker.com/docker-for-mac/install/

#Step5(Optional): Build Docker image

Start Docker on MACOS

cd dockerbuild

./build.script

#Step6(Optional): Setup DockerHub account, and then Authenticate with DockerHub

cd dockerbuild

./upload.script

 #Step7(Optional): Install KubeCtl Kubernetes on MAC/OS

 https://kubernetes.io/docs/tasks/tools/install-kubectl/


#Step8: Install Kubernetes Minikube instead of Kube-Solo to Start Kubernetes Cluster on MAC/OS

 https://deis.com/docs/workflow/quickstart/provider/minikube/boot/

#Step9: Run a Hello World Application using the running Kubernetes cluster

 https://kubernetes.io/docs/tasks/access-application-cluster/service-access-application-cluster/

 Execute Steps 1-9

  kubectl run hello-world --replicas=2 --labels="run=load-balancer-example" --image=gcr.io/google-samples/node-hello:1.0  --port=8080

#Step10(Optional): Run the Docker Image for the REST Service

 https://kubernetes.io/docs/tasks/access-application-cluster/service-access-application-cluster/

 Execute Steps 1-9

kubectl run spring-boot-web --replicas=2 --labels="run=load-balancer-example" --image=joethecoder2/spring-boot-web  --port=8080


#StepAppendix(Deprecated from 2016 Do Not Use): Install Microsoft's Deis Kube-Solo for MACOS

   https://deis.com/blog/2016/run-kubernetes-on-a-mac-with-kube-solo/
