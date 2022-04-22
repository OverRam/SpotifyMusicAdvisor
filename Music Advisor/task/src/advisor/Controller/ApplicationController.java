package advisor.Controller;

import advisor.Model.SpotifyApiConnectionModel;
import advisor.Model.TestMusicModel;
import advisor.View.UserView;

public class ApplicationController {
    private final UserView userView;
    private final SpotifyApiConnectionModel spotifyApiConnectionModel;

    public ApplicationController(UserView userView, SpotifyApiConnectionModel spotifyApiConnectionModel) {
        this.userView = userView;
        this.spotifyApiConnectionModel = spotifyApiConnectionModel;
    }

    public void setResourceUrl(String url) {
        spotifyApiConnectionModel.setServerResourceUrl(url);
    }

    public void mainMenu(String inputOption) {

        String[] userChoice = inputOption.toLowerCase().split(" ");

        if (userChoice[0].equalsIgnoreCase("auth")) {
            getAuthorizationCode();
            if (spotifyApiConnectionModel.isCode()) {
                getAccessToken();
            }

            if (spotifyApiConnectionModel.isCode() && spotifyApiConnectionModel.isAccessToken()) {
                userView.printMessage("---SUCCESS---");
            } else {
                userView.printMessage("---FAILED---");
            }
        } else if (spotifyApiConnectionModel.isCode()) {
            TestMusicModel musicDataModel = new TestMusicModel();
            switch (userChoice[0]) {
                case "new":
                    userView.printMessage("---NEW RELEASES---");
                    userView.printMessage(musicDataModel.getNewMusic());
                    break;

                case "featured":
                    userView.printMessage("---FEATURED---");
                    userView.printMessage(musicDataModel.getFeaturedMusic());
                    break;

                case "categories":
                    userView.printMessage("---CATEGORIES---");
                    userView.printMessage(musicDataModel.getCategoriesMusic());
                    break;

                case "playlists":
                    userView.printMessage("---" + userChoice[1] + " PLAYLISTS---");
                    userView.printMessage(musicDataModel.getPlaylistsMusic(userChoice[1]));
                    break;

                case "exit":
                    userView.printMessage("---GOODBYE!---");
                    break;

                default:
                    userView.printMessage("Wrong user request! Try again.");
            }

        } else {
            userView.printMessage("Please, provide access for application.");
        }
    }

    private void getAuthorizationCode() {

        userView.printMessage("use this link to request the access code:");

        userView.printMessage(spotifyApiConnectionModel.getLinkToRequestAccessCode());
        userView.printMessage("waiting for code...");

        spotifyApiConnectionModel.requestAccessCode();

        if (spotifyApiConnectionModel.isCode()) {
            userView.printMessage("code received");
        } else {
            userView.printMessage("code not received");
        }
    }

    private void getAccessToken() {

        userView.printMessage("making http request for access_token...");
        userView.printMessage("response:");

        spotifyApiConnectionModel.requestAccessToken();
        userView.printMessage(spotifyApiConnectionModel.getResponseAccessToken());
    }

}