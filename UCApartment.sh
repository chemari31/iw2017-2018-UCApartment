#!/bin/bash
printf "# Se procede con la eliminación de la batería de test\n"
rm -r /src/test

# Se crea la base de datos y el usuario de esta
printf "# Introduzca su clave de mysql para importar la base de datos\n"
mysql -u root -p < UCApartment.sql

printf "A continuación se parará todo proceso escuchando por el puerto 8080...\n"
sudo service tomcat8 stop

printf "# Se procede a desplegar la aplicación...\n"
mvn spring-boot:run
