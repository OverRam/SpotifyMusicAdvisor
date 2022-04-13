package advisor.Controller;

import advisor.Model.CategoryHeaders;
import advisor.Model.MusicDataModel;
import advisor.OAuth.Authorization;
import advisor.OAuth.UrlData;
import advisor.View.MusicView;

import java.util.LinkedList;
import java.util.List;

public class Controller {
    private final MusicView musicView;
    private final MusicDataModel musicDataModel;

    public Controller(MusicView musicView, MusicDataModel musicDataModel) {
        this.musicView = musicView;
        this.musicDataModel = musicDataModel;
    }

    public void mainMenu(String inputOption) {
        String[] userChoice = inputOption.toLowerCase().split(" ");
        List<String> listToView = new LinkedList<>();

        if (userChoice[0].equalsIgnoreCase("auth")) {
            listToView.add(UrlData.getAuthSpotifyUrl());
            listToView.add(CategoryHeaders.SUCCESS.toString());
            Authorization.setIsAuthorized(true);

        } else if (Authorization.isAuthorized()) {

            switch (userChoice[0]) {
                case "new":
                    listToView.add(CategoryHeaders.NEW.toString());
                    listToView.addAll(musicDataModel.getNewMusic());
                    break;

                case "featured":
                    listToView.add(CategoryHeaders.FEATURED.toString());
                    listToView.addAll(musicDataModel.getFeaturedMusic());
                    break;

                case "categories":
                    listToView.add(CategoryHeaders.CATEGORIES.toString());
                    listToView.addAll(musicDataModel.getCategoriesMusic());
                    break;

                case "playlists":
                    listToView.add(CategoryHeaders.PLAYLISTS.toString());
                    listToView.addAll(musicDataModel.getPlaylistsMusic(userChoice[1]));
                    break;


                case "exit":
                    listToView.add(CategoryHeaders.EXIT_TEXT.toString());
                    break;

                default:
                    listToView.add("Wrong user request!");
            }

        } else {
            listToView.add("Please, provide access for application.");
        }
        musicView.printList(listToView);
    }

}
