package advisor.OAuth;

public class UrlData {
    private static final String AUTH_SPOTIFY_URL = "https://accounts.spotify.com/authorize";
    private static final String REQUEST_PARAMETERS_CLIENT_ID = "client_id=48af67d4f4b649019f5205330f92a462";
    private static final String REDIRECT_URI = "redirect_uri=http://localhost:8080";
    private static final String RESPONSE_TYPE = "response_type=code";

    public static String getAuthSpotifyUrl() {
        return AUTH_SPOTIFY_URL.concat("?")
                .concat(REQUEST_PARAMETERS_CLIENT_ID)
                .concat("&")
                .concat(REDIRECT_URI)
                .concat("&")
                .concat(RESPONSE_TYPE);
    }
}
