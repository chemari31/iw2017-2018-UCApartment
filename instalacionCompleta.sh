#!/bin/bash

printf "Se van a instalar los programas necesarios para desplegar la aplicación\n"
sudo apt-get install openjdk-8-jdk openjdk-8-doc openjdk-8-jre
sudo apt-get install git mysql-server tomcat8 maven

printf "# Se va a clonar el proyecto de github en la carpeta /var/www\n"
cd /var/www
git clone https://github.com/chemari31/iw2017-2018-UCApartment

# Nos movemos al directorio del proyecto
cd /var/www/iw2017-2018-UCApartment

# Se crea la base de datos y el usuario de esta
printf "# Introduzca su clave de mysql para importar la base de datos\n"
mysql -u root -p < UCApartment.sql

printf "A continuación se parará el servicio tomcat8 por si está activo...\n"
sudo service tomcat8 stop

printf "# Se procede a desplegar la aplicación...\n"
mvn spring-boot:run
