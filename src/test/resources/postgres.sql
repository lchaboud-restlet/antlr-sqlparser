CREATE SEQUENCE rank_id_seq;
CREATE SEQUENCE rank_id_seq2;
CREATE SEQUENCE id_article_keyword;
CREATE SEQUENCE id_keyword;

CREATE TABLE users (
    email VARCHAR (100) PRIMARY KEY ,
    name VARCHAR (255),
    password VARCHAR (255),
    isadmin BOOLEAN
);

CREATE TABLE article (
  id INTEGER PRIMARY KEY default nextval('rank_id_seq'),
  name varchar(100),
  url varchar(200)
) ;

create table Comment (
  id                         bigint not null,
  comment                     varchar(255),
  author                      varchar(100),
  constraint pk_Comment primary key (id));

CREATE TABLE Link (
  id INTEGER PRIMARY KEY default nextval('rank_id_seq2'),
  Article1 INTEGER,
  Article2 INTEGER,
  Weight FLOAT
);

CREATE TABLE keyword (
  id INTEGER PRIMARY KEY default nextval('id_keyword'),
  name varchar(100)
);

CREATE TABLE article_keyword(
  id INTEGER PRIMARY KEY default nextval('id_article_keyword'),
  article_id INTEGER references article(id),
  keyword_id INTEGER references keyword(id)
);

INSERT into article (name,url) values
('tablette','tablette.jpg'),
('ordinateur','ordi.jpg'),
('table','table.jpg'),
('guitare','guitare.jpg') ,
('retroprojecteur','retropro.jpg'),
('bouteille','bouteille.jpg'),
('goblet','goblet.jpg'),
('souris','souris.jpg'),
('barquettes','barquettes.jpg'),
('ecolier','ecolier.jpg'),
('ecran','ecran.jpg');

INSERT INTO  users (email, name, password, isAdmin) values ('blondeau.gui@gmail.com', 'Guillaume Blondeau', 'test', true);

INSERT INTO Link (Article1, Article2, Weight) values
((SELECT id FROM Article  where name ='tablette' limit 1), (SELECT id FROM Article where name ='ordinateur' limit 1), 0.5),
((SELECT id FROM Article where name ='tablette' limit 1), (SELECT id FROM Article where name ='table' limit 1), 0.8),
((SELECT id FROM Article where name ='table' limit 1), (SELECT id FROM Article where name ='guitare' limit 1), 0.1);

insert into keyword(name) values('delicieux');
insert into keyword(name) values('sexy');

insert into article_keyword(article_id, keyword_id) values(
  (SELECT id FROM Article  where name ='tablette' limit 1),
  (SELECT id FROM Keyword where name = 'delicieux' limit 1)
);

insert into article_keyword(article_id, keyword_id) values(
  (SELECT id FROM Article  where name ='table' limit 1),
  (SELECT id FROM Keyword where name = 'sexy' limit 1)
);

-- Table: table3

-- DROP TABLE table3;

CREATE TABLE table3
(
  id integer NOT NULL,
  nom character varying(100),
  CONSTRAINT table3_pkey PRIMARY KEY (id),
  CONSTRAINT u_table3 UNIQUE (id, nom)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE table3
  OWNER TO postgres;

-- Table: table2

-- DROP TABLE table2;

CREATE TABLE table2
(
  id integer NOT NULL,
  nom character varying(100),
  id_table3 integer,
  nom_table3 character varying(100),
  CONSTRAINT table2_pkey PRIMARY KEY (id),
  CONSTRAINT fk_table2_table3 FOREIGN KEY (id_table3, nom_table3)
      REFERENCES table3 (id, nom) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE RESTRICT
)
WITH (
  OIDS=FALSE
);
ALTER TABLE table2
  OWNER TO postgres;

-- Table: table1

-- DROP TABLE table1;

CREATE TABLE table1
(
  id integer NOT NULL,
  nom character varying(100),
  dt date,
  num integer,
  id_table2 integer,
  id_table3 integer,
  CONSTRAINT table1_pkey PRIMARY KEY (id),
  CONSTRAINT table1_id_table2_fkey FOREIGN KEY (id_table2)
      REFERENCES table2 (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT table1_id_table3_fkey FOREIGN KEY (id_table3)
      REFERENCES table3 (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE
)
WITH (
  OIDS=FALSE
);
ALTER TABLE table1
  OWNER TO postgres;
