package advisor.Model.SpotifyClass;

public class Song {
    private final String name;
    private final Artist[] artists;
    private final ExternalUrl external_urls;

    public Song(Artist[] artists, ExternalUrl external_urls, String name) {
        this.artists = artists;
        this.external_urls = external_urls;
        this.name = name;
    }

    public Artist[] getArtists() {
        return artists;
    }

    public ExternalUrl getExternal_urls() {
        return external_urls;
    }

    public String getName() {
        return name;
    }

}
