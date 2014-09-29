--
-- PostgreSQL database dump
--

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: company; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE company (
    id integer NOT NULL,
    duns character varying(9),
    name character varying(255),
    address character varying(255),
    zip_code character varying(5),
    company_creation date,
    website character varying(255),
    phone_number character varying(255),
    city character varying(255)
);


ALTER TABLE public.company OWNER TO postgres;

--
-- Name: contact; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE contact (
    id character varying(255) NOT NULL,
    email character varying(255) NOT NULL,
    age integer,
    name character varying(255),
    firstname character varying(255),
    company_id integer
);


ALTER TABLE public.contact OWNER TO postgres;

--
-- Name: table1; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE table1 (
    id integer NOT NULL,
    nom character varying(100) DEFAULT NULL::character varying,
    dt date,
    num integer,
    id_table2 integer,
    id_table3 integer
);


ALTER TABLE public.table1 OWNER TO postgres;

--
-- Name: table2; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE table2 (
    id integer NOT NULL,
    nom character varying(100),
    id_table3 integer,
    nom_table3 character varying(100)
);


ALTER TABLE public.table2 OWNER TO postgres;

--
-- Name: table3; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE table3 (
    id integer NOT NULL,
    nom character varying(100)
);


ALTER TABLE public.table3 OWNER TO postgres;

--
-- Name: company_duns_key; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY company
    ADD CONSTRAINT company_duns_key UNIQUE (duns);


--
-- Name: company_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY company
    ADD CONSTRAINT company_pkey PRIMARY KEY (id);


--
-- Name: contact_email_key; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY contact
    ADD CONSTRAINT contact_email_key UNIQUE (email);


--
-- Name: contact_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY contact
    ADD CONSTRAINT contact_pkey PRIMARY KEY (id);


--
-- Name: table1_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY table1
    ADD CONSTRAINT table1_pkey PRIMARY KEY (id);


--
-- Name: table2_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY table2
    ADD CONSTRAINT table2_pkey PRIMARY KEY (id);


--
-- Name: table3_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY table3
    ADD CONSTRAINT table3_pkey PRIMARY KEY (id);


--
-- Name: u_table3; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY table3
    ADD CONSTRAINT u_table3 UNIQUE (id, nom);


--
-- Name: contact_company_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY contact
    ADD CONSTRAINT contact_company_id_fkey FOREIGN KEY (company_id) REFERENCES company(id);


--
-- Name: fk_table2_table3; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY table2
    ADD CONSTRAINT fk_table2_table3 FOREIGN KEY (id_table3, nom_table3) REFERENCES table3(id, nom) ON UPDATE CASCADE ON DELETE RESTRICT;


--
-- Name: table1_ibfk_1; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY table1
    ADD CONSTRAINT table1_ibfk_1 FOREIGN KEY (id_table2) REFERENCES table2(id);


--
-- Name: table1_ibfk_2; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY table1
    ADD CONSTRAINT table1_ibfk_2 FOREIGN KEY (id_table3) REFERENCES table3(id) ON DELETE CASCADE;


--
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


--
-- PostgreSQL database dump complete
--

