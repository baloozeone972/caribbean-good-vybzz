# Dockerfile pour Caribbean Good Vybzz
# Multi-stage build pour optimiser la taille de l'image

# Stage 1: Build de l'application
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copier les fichiers de configuration Maven
COPY pom.xml .
# Télécharger les dépendances (mise en cache)
RUN mvn dependency:go-offline -B

# Copier le code source
COPY src ./src

# Build du JAR
RUN mvn clean package -DskipTests

# Stage 2: Image de production légère
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Créer un utilisateur non-root pour la sécurité
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copier le JAR depuis le stage de build
COPY --from=build /app/target/*.jar app.jar

# Exposer le port
EXPOSE 8080

# Variables d'environnement par défaut
ENV SPRING_PROFILES_ACTIVE=prod
ENV SERVER_PORT=8080

# Point de santé pour les orchestrateurs
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

# Démarrer l'application
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}", "/app/app.jar"]
