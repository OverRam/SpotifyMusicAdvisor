package advisor.Model;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;
import java.util.HashMap;

public class SpotifyApiConnectionModel {
    private HttpServer httpServer;
    private static SpotifyApiConnectionModel SERVER_MODEL;
    private final Integer portUrl = 8081;
    private final String localHostUrl = "http://localhost:" + portUrl;
    private String serverResourceUrl = "https://accounts.spotify.com";
    private boolean isAccessCode = false;
    private boolean isErrorConnection = false;
    private boolean isAccessToken = false;
    private String responseAccessToken;
    private final HashMap<String, String> accessCodeRequestResp = new HashMap<>();
    private final HashMap<String, String> tokenRequestResp = new HashMap<>();
    private final HashMap<String, String> dataPKCE = new HashMap<>();

    private SpotifyApiConnectionModel() {
        dataPKCE.put("clientID", "48af67d4f4b649019f5205330f92a462");
        dataPKCE.put("method", "S256");
    }

    public static SpotifyApiConnectionModel getInstance() {
        if (SERVER_MODEL == null) {
            SERVER_MODEL = new SpotifyApiConnectionModel();
        }
        return SERVER_MODEL;
    }

    public void setServerResourceUrl(String url) {
        serverResourceUrl = url;
    }

    public String getLinkToRequestAccessCode() {
        return serverResourceUrl + "/authorize?client_id=" + dataPKCE.get("clientID") + "&" +
                "redirect_uri=" + localHostUrl + "/callback&" +
                "response_type=code";
    }

    private HttpServer createServer() {
        try {
            httpServer = HttpServer.create();
            httpServer.bind(new InetSocketAddress(portUrl), 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return httpServer;
    }

    public void requestAccessCode() {
        httpServer = createServer();
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
            sleepThread();
        }

        httpServer.stop(1);
    }

    private void sleepThread() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void requestAccessToken() {

        createServer();
        httpServer.createContext("/callback",
                exchange -> {
                }
        );
        httpServer.start();

        String secret = "f7ad0bed1627483dab14e3bc48e5f7fd";

        HttpRequest request = HttpRequest.newBuilder()
                .headers("Content-Type", "application/x-www-form-urlencoded",
                        "Authorization", "Basic " + stringTo64Byte((dataPKCE.get("clientID") + ":" + secret))
                )
                .uri(URI.create(serverResourceUrl + "/api/token"))
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
            responseAccessToken = response.body();

            String[] codeArr = responseAccessToken
                    .replaceAll("\\{", "")
                    .replaceAll("}", "")
                    .replaceAll("\"", "")
                    .split(",");


            for (String e : codeArr) {
                String[] arr = e.split(":");
                if (arr.length % 2 == 0){
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
            sleepThread();
        }
        httpServer.stop(1);
    }

    public Boolean isCode() {
        return isAccessCode;
    }

    public boolean isAccessToken() {
        return isAccessToken;
    }

    public String getResponseAccessToken() {
        return responseAccessToken;
    }

    private String stringTo64Byte(String s) {
        return Base64.getEncoder().encodeToString(s.getBytes());
    }
}