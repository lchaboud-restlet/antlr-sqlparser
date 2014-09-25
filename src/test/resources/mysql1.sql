
ddd;

create database test;
use test;

DROP table IF EXISTS Contact;
DROP table IF EXISTS Company;

CREATE TABLE Company (
    id INT NOT NULL AUTO_INCREMENT, 
    duns VARCHAR(9) UNIQUE,
    name VARCHAR(255),
    address VARCHAR(255), 
    zip_code VARCHAR(5), 
    company_creation Date, 
    website VARCHAR(255), 
    phone_number VARCHAR(255), 
    city VARCHAR(255), 
    PRIMARY KEY(id)
);

CREATE TABLE Contact (
    id VARCHAR(255), 
    email VARCHAR (255) NOT NULL UNIQUE,
    age INT,
    name VARCHAR (255),
    firstname VARCHAR (255),
    company_id INT,
    PRIMARY KEY(id),
    FOREIGN KEY (company_id) REFERENCES Company(id)
); 

CREATE USER 'test'@'localhost' IDENTIFIED BY 'test';
GRANT ALL PRIVILEGES ON *.* TO 'test'@'localhost' WITH GRANT OPTION;
FLUSH PRIVILEGES;


CREATE TABLE table3
(
    id INTEGER PRIMARY KEY NOT NULL,
    nom VARCHAR(100)
);

CREATE TABLE table2
(
    id INTEGER PRIMARY KEY NOT NULL,
    nom VARCHAR(100),
	id_table3 INTEGER,
	nom_table3 VARCHAR(100)
);

CREATE TABLE `table1` (
  `id` int(11) NOT NULL,
  `nom` varchar(100) DEFAULT NULL,
  `dt` date DEFAULT NULL,
  `num` int(11) DEFAULT NULL,
  `id_table2` int(11) DEFAULT NULL,
  `id_table3` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `id_table2` (`id_table2`),
  KEY `id_table3` (`id_table3`),
  CONSTRAINT `table1_ibfk_1` FOREIGN KEY (`id_table2`) REFERENCES `table2` (`id`),
  CONSTRAINT `table1_ibfk_2` FOREIGN KEY (`id_table3`) REFERENCES `table3` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

ALTER TABLE table3
ADD CONSTRAINT u_table3
UNIQUE (id, nom);

ALTER TABLE table2
ADD CONSTRAINT fk_table2_table3
FOREIGN KEY (id_table3, nom_table3)
REFERENCES table3(id, nom)
ON UPDATE CASCADE
ON DELETE RESTRICT;