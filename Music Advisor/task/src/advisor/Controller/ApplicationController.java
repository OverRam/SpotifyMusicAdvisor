package advisor.Controller;

import advisor.Model.DataModel;
import advisor.Model.SpotifyApiResourceModel;
import advisor.Model.SpotifyAuth;
import advisor.View.UserView;

import java.util.HashMap;
import java.util.Scanner;

public class ApplicationController {
    private final UserView userView = new UserView();
    private final SpotifyAuth spotifyAuth = SpotifyAuth.getInstance();
    private final DataModel dataModel = new DataModel();
    private String userInputOption;
    private boolean isAccess = false;
    private boolean isNotExit = true;

    public void runApplication() {
        setUserInputOption();
        String[] userCategoryChoice;

        while (isNotExit) {
            isNotExit = !userInputOption.equalsIgnoreCase("exit");
            userCategoryChoice = userInputOption.split(" ");

            if (!isAccess) {
                checkParamsAuth(userInputOption);
                setUserInputOption();
            } else {
                menu(userCategoryChoice);
                setUserInputOption();
            }
        }
    }

    public void setUserInputOption() {
        this.userInputOption = new Scanner(System.in).nextLine();
    }

    public void setInitParams(HashMap<String, String> params) {
        if (params.containsKey("-access")) {
            spotifyAuth.setUrlAccessServer(params.get("-access"));
        }

        if (params.containsKey("-resource")) {
            SpotifyApiResourceModel.getInstance().setUrlHostApiResource(params.get("-resource"));
        }

        if (params.containsKey("-page")) {
            try {
                int numOfEntries = Integer.parseInt(params.get("-page"));
                dataModel.setEntriesPerPage(numOfEntries);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

    }

    public void checkParamsAuth(String inputOption) {
        if (inputOption.equalsIgnoreCase("auth")) {
            setAuthorizationCode();

            if (spotifyAuth.isCode()) {
                setAccessToken();
            }
            if (spotifyAuth.isAccessToken()) {
                userView.printListOfMessage("Success!");
                isAccess = true;
                dataModel.setCategories();
            } else {
                userView.printListOfMessage("Failed!");
            }
        } else {
            userView.printListOfMessage("Please, provide access for application.");
        }
    }

    void menu(String[] userCategoryChoice) {
        String categoryName = "";

        if (userCategoryChoice.length > 1) {
            categoryName = userInputOption.replaceAll(userCategoryChoice[0] + " ", "");
        }

        switch (userCategoryChoice[0].toLowerCase()) {
            case "new":
                userView.printListOfMessage(dataModel.getNewSongs());
                naviPage();
                break;

            case "featured":
                userView.printListOfMessage(dataModel.getPlaylistsPage("featured-playlists"));
                naviPage();
                break;

            case "categories":
                userView.printListOfMessage(dataModel.getCategoryPage());
                naviPage();
                break;

            case "playlists":
                String categoryId = dataModel.getCategoryID(categoryName);

                if (categoryId != null) {
                    String url = "categories/" + categoryId + "/playlists";
                    userView.printListOfMessage(dataModel.getPlaylistsPage(url));
                    naviPage();
                } else {
                    userView.printListOfMessage("Wrong spotify ID.");
                    setUserInputOption();
                }
                break;

            case "exit":
                userView.printListOfMessage(" ---GOODBYE!---");
                break;

            default: {
                userView.printListOfMessage("Wrong user request! Try again.");
            }

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

    private void naviPage() {
        boolean isNextOrPrev;
        boolean isExit = false;
        do {
            setUserInputOption();
            isNextOrPrev = userInputOption.equals("next") || userInputOption.equals("prev");

            if (isNextOrPrev) {
                userView.printListOfMessage(dataModel.getOtherPage(userInputOption));
            } else if (userInputOption.equalsIgnoreCase("exit")) {
                isExit = true;
                userInputOption = "";
            }
        } while (!isExit);
    }
}