# Recommendation System Challenge

### Getting started

To launch the application for the first time, you need to run the bash script `recommendation_system.sh` under the
scripts folder with the parameter `initialize`.
It compiles the project using Maven, then it builds the Spring services and launches everything needed.
Moreover, this script adds sample data into the Postgres database.

### Start and stop the application

To start and stop the application, you need to run `recommendation_system.sh` with parameters `start` or `stop`.

### Inspect the underlying DB

Another script useful to perform query on the underlying Postgres DB is `db.sh`.
It enters into the postgres running container and runs bash command.

### API Docs

The application offers many endpoints to perform various operations.

#### Rate a movie

`POST /rating` allows a user to rate a movie.

It requires a JSON body like

```
{
    "user": "Bob",
    "movie": "Goodfellas",
    "rating": 4
}
```

In this case it will add a rating of 4 to the "Goodfellas" movie for the user Bob.

#### View a movie

`POST /view` allows perform a view event by a user.

It requires a JSON body like

```
{
    "user": "Alice",
    "movie": "Forrest Gump",
    "viewPercentage": 100
}
```

This means tha Alice saw the "Forrest Gump" movie until 100%.
This endpoint transforms also the view percentage into a rating implicitly.
This rating is stored into the DB only if the user has not rated the movie explicitly before.
For example, if Alice already rated this movie explicitly, only the value "100" is added to the `view_percentage`
column.
If Alice has not rated already the movie, the value "100" will be addded to the `view_percentage` column and a rating of
5 will be added to the `rating` column. A boolean value of true is also set to `implicit_rating` column.

#### Search for a movie

`GET /search` to list movies.

Optionally, it accepts 3 parameters that can be used alone or in combination.

* `genre`
* `minRating`
* `maxRating`

**Note:** consider that `minRating` and `maxRating` parameters act on the average of movies' ratings.
The underlying query calculates the average rating for each rated movie. Subsequently, the optional parameters mentioned
above are applied to filter movies with at least or at most a certain rating.

##### Examples

```
http://localhost:8080/search
http://localhost:8080/search?minRating=2
http://localhost:8080/search?maxRating=4
http://localhost:8080/search?genres=Action&minRating=2&maxRating=4
```

#### History of user interactions

`GET /{username}` to see all ratings given by a user

Using the optional parameters `view` or `ratings` it is possible to see all movies seen or rated by a user.
For the `view` parameter, the query selects only ratings with `viewPercentage` set.
For the `ratings` parameter, the query selects only ratings where `implicit_rating` is set to `false`. 

##### Examples

```
http://localhost:8080/Alice
http://localhost:8080/Alice?type=views
http://localhost:8080/search?type=ratings
```

#### Movies recommendation for a user

`GET /recommend` to get movies recommended for a user.

It starts from the `ratings` table, taking genres of movies rated with 4 or 5 by a user.
Then it selects movies of those genres from `movies` table and finally removes from the obtained set movies already rated.

##### Examples

```
http://localhost:8080/recommend?username=Bob
```

