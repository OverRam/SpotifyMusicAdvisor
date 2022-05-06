package advisor.Model;


import advisor.Model.SpotifyClass.Category;
import advisor.Model.SpotifyClass.ErrorSpotify;
import advisor.Model.SpotifyClass.Playlist;
import advisor.Model.SpotifyClass.Song;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SpotifyApiResourceModel {
    private static final SpotifyApiResourceModel SPOTIFY_API_RESOURCE_MODEL = new SpotifyApiResourceModel();
    private String hostApiResource = "https://api.spotify.com";
    private final Gson gson = new Gson();
    private ErrorSpotify errorSpotify;

    private SpotifyApiResourceModel() {
    }

    public static SpotifyApiResourceModel getInstance() {
        return SPOTIFY_API_RESOURCE_MODEL;
    }

    public void setUrlHostApiResource(String urlHost) {
        hostApiResource = urlHost;
    }

    private HttpResponse<String> requestGETToServer(String urlPatch) {
        String serverResp = "";

        HttpServer httpServer = ServerHttp.createServer();

        httpServer.createContext("/callback");

        httpServer.start();

        String patchBasicUrl = "/v1/browse/";


        URI uri = URI.create(hostApiResource + patchBasicUrl + urlPatch);

        HttpRequest request = HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + SpotifyAuth.getInstance().getAccessToken())
                .uri(uri)
                .GET()
                .build();

        HttpClient httpClient = HttpClient.newBuilder().build();
        HttpResponse<String> response = null;

        try {
            response = httpClient
                    .send(request, HttpResponse.BodyHandlers.ofString());

            serverResp = response.body();

            if (response.statusCode() == 401) {
                SpotifyAuth.getInstance().getAccessTokenFromServer();
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < 15; i++) {
            if (!serverResp.equals("")) {
                break;
            }
            ServerHttp.sleepThread();
        }

        httpServer.stop(1);
        return response;
    }

    /**
     * @param urlPatch the rest of the path after "/v1/browse/"
     * @param catalog  main object in Json structure
     * @return null if there is an error, otherwise JsonObject
     */
    private JsonObject getJsonObjectFromHost(String urlPatch, String catalog) {
        HttpResponse<String> httpResponse = requestGETToServer(urlPatch);
        JsonObject jsonObject = JsonParser
                .parseString(httpResponse.body())
                .getAsJsonObject();

        errorSpotify = gson.fromJson(jsonObject.get("error"), ErrorSpotify.class);

        if (errorSpotify == null) {
            jsonObject = jsonObject.getAsJsonObject(catalog);
        } else {
            System.out.println(errorSpotify.getMessage());
        }

        return jsonObject;
    }

    public List<Playlist> getPlaylistByCategoryID(String spotifyID) {
        List<Playlist> returnedList = new LinkedList<>();
        JsonObject jsonObject = getJsonObjectFromHost(spotifyID, "playlists");

        if (errorSpotify == null) {
            jsonObject.getAsJsonArray("items").iterator().forEachRemaining(x ->
                    returnedList.add(gson.fromJson(x, Playlist.class))
            );
        }
        return returnedList;
    }

    public List<Category> getCategoriesFromServer() {
        List<Category> categories = new ArrayList<>();

        JsonObject jsonObject = getJsonObjectFromHost("categories", "categories");

        if (errorSpotify == null) {
            jsonObject.getAsJsonArray("items").iterator().forEachRemaining(x ->
                    categories.add(gson.fromJson(x, Category.class))
            );
        }

        return categories;
    }

    public List<Song> getNewSongsFromServer() {
        List<Song> returnedList = new LinkedList<>();

        JsonObject jsonObject = getJsonObjectFromHost("new-releases", "albums");

        if (errorSpotify == null) {
            jsonObject.getAsJsonArray("items").iterator().forEachRemaining(element ->
                    returnedList.add(gson.fromJson(element, Song.class)));
        }
        return returnedList;
    }

}
