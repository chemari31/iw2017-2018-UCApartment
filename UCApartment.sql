DROP DATABASE IF EXISTS Ucapartament;
CREATE DATABASE Ucapartament DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE Ucapartament;
CREATE USER IF NOT EXISTS 'springuser'@'localhost' IDENTIFIED BY 'ThePassword';
GRANT ALL PRIVILEGES ON Ucapartament.* TO 'springuser'@'localhost';
