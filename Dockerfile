## Run mvn clean package before making docker image
FROM openjdk:8-jre-alpine
ADD target/*.jar /app/
WORKDIR /app/
RUN mv *.jar startup.jar
ENTRYPOINT ["java", "-jar", "startup.jar"]