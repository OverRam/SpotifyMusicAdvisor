package advisor.Model;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;
import java.util.HashMap;

public class SpotifyAuth {

    private final static SpotifyAuth SPOTIFY_AUTH = new SpotifyAuth();
    private final String localHostUrl = "http://localhost:" + ServerHttp.PORT_URL;
    private String accessServerResourceUrl = "https://accounts.spotify.com";
    private boolean isAccessCode = false;
    private boolean isErrorConnection = false;
    private boolean isAccessToken = false;
    private final String clientId = "48af67d4f4b649019f5205330f92a462";
    private final HashMap<String, String> accessCodeRequestResp = new HashMap<>();
    private final HashMap<String, String> tokenRequestResp = new HashMap<>();

    private SpotifyAuth() {
    }

    public static SpotifyAuth getInstance() {
        return SPOTIFY_AUTH;
    }

    public void setUrlAccessServer(String accessServerResourceUrl) {
        this.accessServerResourceUrl = accessServerResourceUrl;
    }

    /**
     * @return a link that the user must click and then grant authorization
     */
    public String getLinkToRequestAccessCode() {
        return accessServerResourceUrl + "/authorize?client_id=" + clientId + "&" +
                "redirect_uri=" + localHostUrl + "/callback&" +
                "response_type=code";
    }

    /**
     * Before starting this method, the user should click on the link granting authorization
     * from getLinkToRequestAccessCode()
     */
    public void getAccessCodeFromServer() {
        HttpServer httpServer = ServerHttp.createServer();
        httpServer.createContext("/callback",
                exchange -> {
                    String response = exchange.getRequestURI().getQuery();

                    if (response == null) {
                        response = "error= Query is null";
                    }

                    String[] codeArr = response.split("&");

                    for (String e : codeArr) {
                        String[] arr = e.split("=");
                        accessCodeRequestResp.put(arr[0], arr[1]);
                    }

                    isAccessCode = accessCodeRequestResp.containsKey("code");
                    isErrorConnection = accessCodeRequestResp.containsKey("error");

                    String answerInTheBrowser = isAccessCode ?
                            "Got the code. Return back to your program." : "Authorization code not found. Try again.";

                    exchange.sendResponseHeaders(200, answerInTheBrowser.length());
                    exchange.getResponseBody().write(answerInTheBrowser.getBytes());
                    exchange.getResponseBody().close();
                }
        );

        httpServer.start();

        for (int i = 0; i < 10; i++) {
            if (isErrorConnection || isAccessCode) {
                break;
            }
            ServerHttp.sleepThread();
        }

        httpServer.stop(1);
    }

    public void getAccessTokenFromServer() {
        HttpServer httpServer = ServerHttp.createServer();

        httpServer.createContext("/callback");
        httpServer.start();

        String clientSec = "f7ad0bed1627483dab14e3bc48e5f7fd";

        HttpRequest request = HttpRequest.newBuilder()
                .headers("Content-Type", "application/x-www-form-urlencoded",
                        "Authorization", "Basic " +
                                Base64.getEncoder().encodeToString((clientId + ":" + clientSec).getBytes())
                )
                .uri(URI.create(accessServerResourceUrl + "/api/token"))
                .POST(HttpRequest.BodyPublishers.ofString(
                        "redirect_uri=http://localhost:8081/callback&" +
                                "grant_type=authorization_code&" +
                                "code=" + accessCodeRequestResp.get("code")
                ))
                .build();

        HttpClient httpClient = HttpClient.newBuilder().build();

        isAccessToken = false;
        isErrorConnection = false;

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            String responseWithAccessToken = response.body();

            String[] codeArr = responseWithAccessToken
                    .replaceAll("\\{", "")
                    .replaceAll("}", "")
                    .replaceAll("\"", "")
                    .split(",");


            for (String e : codeArr) {
                String[] arr = e.split(":");
                if (arr.length % 2 == 0) {
                    tokenRequestResp.put(arr[0], arr[1]);
                }
            }

            isAccessToken = tokenRequestResp.containsKey("access_token");
            isErrorConnection = tokenRequestResp.containsKey("error");

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            isErrorConnection = true;
        }

        for (int i = 0; i < 10; i++) {
            if (isAccessToken || isErrorConnection) {
                break;
            }
            ServerHttp.sleepThread();
        }
        httpServer.stop(1);
    }

    public boolean isCode() {
        return isAccessCode;
    }

    public boolean isAccessToken() {
        return isAccessToken;
    }

    public String getAccessToken() {
        return tokenRequestResp.get("access_token");
    }

}