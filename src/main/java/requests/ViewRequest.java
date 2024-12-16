package requests;

public class ViewRequest {

    public String user;
    public String movie;
    public int percentage;

    public ViewRequest(String user, String movie, int percentage){
        this.user = user;
        this.movie = movie;
        this.percentage = percentage;
    }
}
