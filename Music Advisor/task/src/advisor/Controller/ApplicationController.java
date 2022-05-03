package advisor.Controller;

import advisor.Model.DataBase;
import advisor.Model.SpotifyApiResourceModel;
import advisor.Model.SpotifyAuth;
import advisor.View.UserView;

import java.util.HashMap;

public class ApplicationController {
    private final UserView userView;
    private final SpotifyAuth spotifyAuth;
    private boolean firstRun = true;

    public ApplicationController(UserView userView, SpotifyAuth spotifyAuth) {
        this.userView = userView;
        this.spotifyAuth = spotifyAuth;
    }

    public void setInitParams(HashMap<String, String> params) {
        if (params.containsKey("-access")) {
            spotifyAuth.setUrlAccessServer(params.get("-access"));
        }

        if (params.containsKey("-resource")) {
            SpotifyApiResourceModel.getInstance().setUrlHostApiResource(params.get("-resource"));
        }
    }

    public void mainMenu(String inputOption) {
        SpotifyApiResourceModel apiResourceModel = SpotifyApiResourceModel.getInstance();
        String[] userCategoryChoice = inputOption.split(" ");
        String categoryName = "";

        if (userCategoryChoice.length > 1) {
            categoryName = inputOption.replaceAll(userCategoryChoice[0] + " ", "");
        }

        if (userCategoryChoice[0].equalsIgnoreCase("auth")) {
            setAuthorizationCode();
            if (spotifyAuth.isCode()) {
                setAccessToken();
            }

            if (spotifyAuth.isAccessToken()) {
                userView.printListOfMessage("Success!");
            } else {
                userView.printListOfMessage("Failed!");
            }
        } else if (spotifyAuth.isAccessToken()) {

            if (firstRun) {
                apiResourceModel.getCategories("categories");
                firstRun = false;
            }

            switch (userCategoryChoice[0].toLowerCase()) {
                case "new":
                    userView.printListOfMessage(apiResourceModel.getNewSongs("new-releases"));
                    break;

                case "featured":
                    userView.printListOfMessage(apiResourceModel.getFeaturedPlaylists("featured-playlists"));
                    break;

                case "categories":
                    userView.printListOfMessage(apiResourceModel.getCategories("categories"));
                    break;

                case "playlists":
                    String categoryId = DataBase.geInstance().getCategoryID(categoryName);
                    userView.printListOfMessage(apiResourceModel.getPlaylistCategoryID(categoryId));
                    break;

                case "exit":
                    break;

                default:
                    userView.printListOfMessage("Wrong user request! Try again.");
            }

        } else {
            userView.printListOfMessage("Please, provide access for application.");
        }
    }

    private void setAuthorizationCode() {

        userView.printListOfMessage("use this link to request the access code:");
        userView.printListOfMessage(spotifyAuth.getLinkToRequestAccessCode());

        spotifyAuth.getAccessCodeFromServer();
        userView.printListOfMessage("waiting for code...");

        if (spotifyAuth.isCode()) {
            userView.printListOfMessage("code received");
        } else {
            userView.printListOfMessage("code not received");
        }
    }

    private void setAccessToken() {
        userView.printListOfMessage("Making http request for access_token...");
        spotifyAuth.getAccessTokenFromServer();
    }
}