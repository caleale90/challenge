package requests;

public class ViewRequest {

    public String user;
    public String movie;
    public int viewPercentage;

    public ViewRequest(String user, String movie, int viewPercentage){
        this.user = user;
        this.movie = movie;
        this.viewPercentage = viewPercentage;
    }
}
