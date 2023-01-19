FROM openjdk:8-jdk-alpine
ARG JAR_FILE=target/supportportal-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} support-test.jar
ENTRYPOINT ["java","-jar","/support-test.jar"]