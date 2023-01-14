#
# Build stage
#
FROM maven:openjdk AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package

#
# Package stage
#
FROM openjdk:8-alpine
COPY --from=build /home/app/target/support-portal-backend-0.0.1-SNAPSHOT.jar /usr/local/lib/support-portal-backend.jar
EXPOSE ${PORT}
ENTRYPOINT ["java","-jar","/usr/local/lib/support-portal-backend.jar"]