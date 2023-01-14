FROM openjdk:8-alpine
MAINTAINER mmacedoaraujo
COPY target/supportportal-0.0.1-SNAPSHOT.jar supportportal-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/supportportal-0.0.1-SNAPSHOT.jar"]