package advisor.Model.SpotifyClass;

public class Playlist {

    private final ExternalUrl external_urls;
    private final String name;

    public Playlist(ExternalUrl external_urls, String name) {
        this.external_urls = external_urls;
        this.name = name;
    }

    public ExternalUrl getExternal_urls() {
        return external_urls;
    }

    public String getName() {
        return name;
    }
}
