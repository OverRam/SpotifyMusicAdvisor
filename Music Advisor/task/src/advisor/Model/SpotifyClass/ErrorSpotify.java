package advisor.Model.SpotifyClass;

public class ErrorSpotify {
    private final Integer status;
    private final String message;

    public ErrorSpotify(Integer status, String message) {
        this.status = status;
        this.message = message;
    }

    public Integer getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
