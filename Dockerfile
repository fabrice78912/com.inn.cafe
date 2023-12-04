#FROM openjdk:18
#EXPOSE 8080
#COPY target/server-app.jar ./
#RUN mvn -f pom.xml clean package -DskipTests
#ADD target/server-app.jar server-app.jar
#ENTRYPOINT ["java","-jar","/server-app.jar"]

FROM eclipse-temurin:18-alpine
VOLUME /tmp
COPY target/*.jar server-app.jar
RUN mvn -f pom.xml clean package -DskipTests
#COPY --chown=appuser:appuser target/**.jar /home/appuser/server-app.jar
ENTRYPOINT ["java","-jar","/cafe-app.jar"]