# Recommendation System


### Getting started

To launch the application for the first time, you need to run the bash script `provision.sh` under the scripts folder.
It compiles the project using Maven, then it builds the Spring services and launches everything needed.
Moreover, this script adds sample data into the Postgres database.


### Start and stop the application

There are 2 more scripts, named `start.sh` and `stop.sh` that are utilities scripts to conveniently start and stop the
application.


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

### View a movie

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
This enpoint transforms also the view percentage into a rating.
This rating is stored into the DB only if the user has not rated the movie explicitly before.
For example, if Alice already rated this movie explicitly, the value "100" is added to the `view_percentage` column.
If Alice has not rated already the movie, the value "100" will be addded to the `view_percentage` column and a rating of 
5 will be added to the `rating` column. A boolean value of true is also set to `implicit_rating` column.


### Search for a movie

`GET /search` allows to list movies.
Optionally it accepts 3 parameters that can be used alone or in combination.

* `genre`
* `minRating`
* `maxRating`





    @GetMapping("/search")
    public ResponseEntity<List<Object[]>> getMovies(@RequestParam(required = false) String genre,
                                                    @RequestParam(required = false) Integer minRating,
                                                    @RequestParam(required = false) Integer maxRating) {

        List<Object[]> movies = ratingRepository.findMovies(genre, minRating, maxRating);
        return ResponseEntity.status(HttpStatus.OK).body(movies);
    }

    @GetMapping("/{username}")
    public ResponseEntity<List<UserInteraction>> getUserInteractions(
            @PathVariable String username,
            @RequestParam(value = "type", required = false) String type) {

        return ResponseEntity.status(HttpStatus.OK).body(userInteractionService.getUserInteractions(username, type));
    }

    @GetMapping("/recommend")
    public ResponseEntity<Set<String>> getRecommendationFor(@RequestParam String username) {
        return ResponseEntity.status(HttpStatus.OK).body(recommendationSystem.recommendMovies(username));
    }


