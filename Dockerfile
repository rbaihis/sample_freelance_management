#FROM ubuntu:latest
#LABEL authors="rbaih"
#
#ENTRYPOINT ["top", "-b"]

FROM openjdk:17-jdk-alpine

# Expose le port de l'application Spring Boot
EXPOSE 8087
# Ajoute le livrable Spring Boot dans l'image
ADD target/freelancing-0.0.1-SNAPSHOT.jar freelancing-0.0.1-SNAPSHOT.jar
# Commande d'exécution de l'application Spring Boot
ENTRYPOINT ["java", "-jar", "/freelancing-0.0.1-SNAPSHOT.jar"]


