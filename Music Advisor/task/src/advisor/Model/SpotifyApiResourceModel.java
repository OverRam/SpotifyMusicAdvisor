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
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class SpotifyApiResourceModel {
    private static final SpotifyApiResourceModel SPOTIFY_API_RESOURCE_MODEL = new SpotifyApiResourceModel();
    private String hostApiResource = "https://api.spotify.com";
    private final Gson gson = new Gson();
    private ErrorSpotify errorSpotify;
    private List<String> returnedList;

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

    private JsonObject getJsonObjectFromHost(String urlPatch, String catalog) {
        HttpResponse<String> httpResponse = requestGETToServer(urlPatch);
        JsonObject jsonObject = JsonParser.parseString(httpResponse.body()).getAsJsonObject();

        errorSpotify = gson.fromJson(jsonObject.get("error"), ErrorSpotify.class);

        if (errorSpotify == null) {
            jsonObject = jsonObject.getAsJsonObject(catalog);
        } else {
            returnedList.add(errorSpotify.getMessage());
        }

        return jsonObject;
    }

    public List<String> getCategories(String spotifyID) {
        returnedList = new LinkedList<>();

        JsonObject jsonObject = getJsonObjectFromHost(spotifyID, "categories");

        if (errorSpotify == null) {
            DataBase dataBase = DataBase.geInstance();
            jsonObject.getAsJsonArray("items").iterator().forEachRemaining(x -> {
                Category category = gson.fromJson(x, Category.class);
                dataBase.addCategory(category.getName(), category.getId());
                returnedList.add(category.getName());
            });
        } else {
            returnedList.add(errorSpotify.getMessage());
        }


        return returnedList;
    }

    public List<String> getNewSongs(String spotifyID) {
        returnedList = new LinkedList<>();

        JsonObject jsonObject = getJsonObjectFromHost(spotifyID, "albums");

        if (errorSpotify == null) {
            jsonObject.getAsJsonArray("items").iterator().forEachRemaining(element -> {
                Song s = gson.fromJson(element, Song.class);
                returnedList.add(s.getName());

                List<String> artists = new LinkedList<>();

                Arrays.stream(s.getArtists()).iterator().forEachRemaining(a -> artists.add(a.getName()));

                returnedList.add(Arrays.toString(artists.toArray()));
                returnedList.add(s.getExternal_urls().getUrl() + "\n");
            });
        } else {
            returnedList.add(errorSpotify.getMessage());
        }

        return returnedList;
    }

    public List<String> getPlaylistByCategoryID(String spotifyID) {
        returnedList = new LinkedList<>();

        if (spotifyID == null) {
            return List.of("Unknown category name.");
        }
        return getFeaturedPlaylists("categories/" + spotifyID + "/playlists");
    }

    public List<String> getFeaturedPlaylists(String spotifyID) {
        returnedList = new LinkedList<>();

        JsonObject jsonObject = getJsonObjectFromHost(spotifyID, "playlists");

        if (errorSpotify == null) {
            jsonObject.getAsJsonArray("items").iterator().forEachRemaining(x -> {
                Playlist playlistObject = gson.fromJson(x, Playlist.class);
                returnedList.add(playlistObject.getName() + "\n" + playlistObject.getExternal_urls().getUrl() + "\n");
            });
        }
        return returnedList;
    }
}
