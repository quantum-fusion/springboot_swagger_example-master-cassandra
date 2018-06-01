FROM java:8
MAINTAINER info@technologyventureslimited.com
EXPOSE 8080 9042
ADD ./target/spring-boot-web-0.0.1-SNAPSHOT.jar .
ENTRYPOINT ["java", "-jar", "spring-boot-web-0.0.1-SNAPSHOT.jar"]
CMD java -jar spring-boot-web-0.0.1-SNAPSHOT.jar $@
