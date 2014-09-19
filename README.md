ANTLR-SQLPARSER
===============

Main
====

Run : ```java com.restlet.sqlimport.Main "[input file]" "[output file]"```
, with : 
 - [input file] : file path to the SQL input file
 - [output file] : file path to the output file

Tests
=====

Run tests : "SqlImportTest.java"

SQL scripts for databases tests :
- mysql.sql
- oracle1.sql
- oracle2.sql
- postgres.sql
- standard.sql

Source code
===========

- grammar : src/main/antlr4/com/restlet/sqlimport/parser/Sql.g4
- Main class : com.restlet.sqlimport.Main
- Tests :
  - Java test : src/test/java/SqlImportTest.java
  - SQL file : src/test/resources/import1.sql
