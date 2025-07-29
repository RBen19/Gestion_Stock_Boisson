#!/bin/bash

# Configuration du port depuis la variable d'environnement
export CATALINA_OPTS="-Dserver.port=${PORT:-8080}"

# Modifier server.xml pour écouter sur 0.0.0.0
sed -i "s|<Connector port=\"8080\"|<Connector address=\"0.0.0.0\" port=\"${PORT:-8080}\"|g" /usr/local/tomcat/conf/server.xml

echo "Starting Tomcat on 0.0.0.0:${PORT:-8080}"

# Démarrer Tomcat
exec catalina.sh run