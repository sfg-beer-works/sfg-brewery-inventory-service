DROP DATABASE IF EXISTS beersinventoryervice;
DROP USER IF EXISTS `beer_service`@`%`;
CREATE DATABASE IF NOT EXISTS beersinventoryervice CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER IF NOT EXISTS `beer__inventory_service`@`%` IDENTIFIED WITH mysql_native_password BY 'password';
GRANT SELECT, INSERT, UPDATE, DELETE, CREATE, DROP, REFERENCES, INDEX, ALTER, EXECUTE, CREATE VIEW, SHOW VIEW,
CREATE ROUTINE, ALTER ROUTINE, EVENT, TRIGGER ON `beersinventoryervice`.* TO `beer__inventory_service`@`%`;
FLUSH PRIVILEGES;