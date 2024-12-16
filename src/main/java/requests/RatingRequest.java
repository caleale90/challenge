package requests;

public class RatingRequest {

    public String user;
    public String movie;
    public int rating;

    public RatingRequest(String user, String movie, int rating){
        this.user = user;
        this.movie = movie;
        this.rating = rating;
    }
}
