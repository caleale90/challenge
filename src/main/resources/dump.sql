--
-- PostgreSQL database dump
--

-- Dumped from database version 17.2
-- Dumped by pg_dump version 17.2

-- Started on 2024-12-16 20:30:57 CET

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 217 (class 1259 OID 16389)
-- Name: movies; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.movies (
    movie_id integer NOT NULL,
    title character varying NOT NULL,
    genres character varying NOT NULL
);


ALTER TABLE public.movies OWNER TO postgres;

--
-- TOC entry 219 (class 1259 OID 16403)
-- Name: ratings; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.ratings (
    user_id integer NOT NULL,
    movie_id integer NOT NULL,
    rating integer,
    view_percentage integer,
    implicit_rating boolean
);


ALTER TABLE public.ratings OWNER TO postgres;

--
-- TOC entry 218 (class 1259 OID 16394)
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users (
    user_id integer NOT NULL,
    username character varying NOT NULL
);


ALTER TABLE public.users OWNER TO postgres;

--
-- TOC entry 3610 (class 0 OID 16389)
-- Dependencies: 217
-- Data for Name: movies; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.movies (movie_id, title, genres) FROM stdin;
1	Toy Story	Adventure|Animation|Children|Comedy|Fantasy
2	Grumpier Old Men	Comedy|Romance
3	Die Hard	Action|Thriller
4	Star Wars: Return of the Jedi	Action|Adventure|Fantasy|Sci-Fi
5	The Lion King	Adventure|Animation|Children|Drama|Musical
6	Pulp Fiction	Crime|Drama|Thriller
7	Forrest Gump	Comedy|Drama|Romance
8	The Matrix	Action|Sci-Fi
9	Goodfellas	Biography|Crime|Drama
10	Jurassic Park	Adventure|Sci-Fi|Thriller
\.


--
-- TOC entry 3612 (class 0 OID 16403)
-- Dependencies: 219
-- Data for Name: ratings; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.ratings (user_id, movie_id, rating, view_percentage, implicit_rating) FROM stdin;
1	7	5	100	f
\.


--
-- TOC entry 3611 (class 0 OID 16394)
-- Dependencies: 218
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.users (user_id, username) FROM stdin;
1	Alice
2	Bob
3	Charlie
\.


--
-- TOC entry 3458 (class 2606 OID 16402)
-- Name: movies movies_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.movies
    ADD CONSTRAINT movies_pkey PRIMARY KEY (movie_id);


--
-- TOC entry 3462 (class 2606 OID 16417)
-- Name: ratings user_movie_pk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ratings
    ADD CONSTRAINT user_movie_pk PRIMARY KEY (user_id, movie_id);


--
-- TOC entry 3460 (class 2606 OID 16400)
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (user_id);


--
-- TOC entry 3463 (class 2606 OID 16411)
-- Name: ratings movie_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ratings
    ADD CONSTRAINT movie_id FOREIGN KEY (movie_id) REFERENCES public.movies(movie_id);


--
-- TOC entry 3464 (class 2606 OID 16406)
-- Name: ratings user_id; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.ratings
    ADD CONSTRAINT user_id FOREIGN KEY (user_id) REFERENCES public.users(user_id);


-- Completed on 2024-12-16 20:30:57 CET

--
-- PostgreSQL database dump complete
--

