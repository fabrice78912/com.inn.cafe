# Étape 1: Builder avec Maven (plus léger)
FROM maven:3.8.6-openjdk-17-slim AS builder

WORKDIR /app

# Copier uniquement les fichiers essentiels pour le build
COPY pom.xml .
COPY src ./src

# Construire sans les tests pour gagner du temps
RUN mvn clean package -DskipTests

# Étape 2: Image runtime plus légère avec Eclipse Temurin JRE Alpine
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copier uniquement le jar produit par la build
COPY --from=builder /app/target/cafe-app.jar .

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "cafe-app.jar"]

#
## Step 1: Builder le projet avec maven
#FROM maven:3.8.3-openjdk-17-slim AS maven-builder
#WORKDIR /app
#COPY . /app
#RUN mvn -f pom.xml clean package -DskipTests
#
## Step 2: Copier et lancer le .jar file
#FROM openjdk:17-alpine
#WORKDIR /app
#COPY --from=maven-builder ./app/target/cafe-app.jar .
#
#EXPOSE 8080
#
#ENTRYPOINT ["java", "-jar", "cafe-app.jar"]