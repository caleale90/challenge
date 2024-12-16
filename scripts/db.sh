#!/bin/bash

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

docker exec -it postgres bash -c "psql -U postgres -d challenge"

# Query di utilità

# Tutte le tabelle assieme
# SELECT * FROM public.ratings JOIN public.movies ON movies.movie_id = ratings.movie_id JOIN public.users ON users.user_id = ratings.user_id;

# Movies
# SELECT * FROM public.movies;

# Users
# SELECT * FROM public.user;

# Rating
# SELECT * FROM public.ratings;