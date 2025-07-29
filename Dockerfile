# Multi-stage build pour optimiser la taille de l'image
FROM maven:3.9.9-eclipse-temurin-21 AS build

# Définir le répertoire de travail
WORKDIR /app

# Copier les fichiers de configuration Maven
COPY pom.xml .
COPY mvnw .
COPY mvnw.cmd .
COPY .mvn .mvn

# Télécharger les dépendances (cache Docker layer)
RUN mvn dependency:resolve

# Copier le code source
COPY src ./src

# Build l'application (skip tests pour accélérer le build)
RUN mvn clean package -DskipTests

# Stage 2: Runtime avec Tomcat
FROM tomcat:10.1-jdk21-temurin

# Supprimer les applications par défaut de Tomcat
RUN rm -rf /usr/local/tomcat/webapps/*

# Copier le fichier WAR depuis le stage de build
COPY --from=build /app/target/gestionBoisson-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war

# Copier le script de démarrage
COPY start-tomcat.sh /usr/local/bin/start-tomcat.sh
RUN chmod +x /usr/local/bin/start-tomcat.sh

# Variables d'environnement par défaut (overridées par Render)
ENV DATABASE_URL=""
ENV PORT=8080

# Exposer le port
EXPOSE $PORT

# Démarrer Tomcat avec notre script
CMD ["/usr/local/bin/start-tomcat.sh"]