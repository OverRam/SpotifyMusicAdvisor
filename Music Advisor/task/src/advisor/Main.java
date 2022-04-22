package advisor;

import advisor.Controller.ApplicationController;
import advisor.Model.SpotifyApiConnectionModel;
import advisor.View.UserView;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ApplicationController controllerApplication = new ApplicationController(new UserView()
                , SpotifyApiConnectionModel.getInstance());

        if (args.length == 2 && args[0].equalsIgnoreCase("-access")) {
            controllerApplication.setResourceUrl(args[1]);
        }

        String inputOption = "";
        while (!inputOption.equalsIgnoreCase("exit")) {
            inputOption = sc.nextLine();
            controllerApplication.mainMenu(inputOption);
        }

    }

}