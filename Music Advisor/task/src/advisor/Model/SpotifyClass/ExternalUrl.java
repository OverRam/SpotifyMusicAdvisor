package advisor.Model.SpotifyClass;

public class ExternalUrl {
    private final String spotify;

    public ExternalUrl(String spotify) {
        this.spotify = spotify;
    }

    public String getUrl() {
        return spotify;
    }
}
