package advisor;

import advisor.Controller.ApplicationController;
import advisor.Model.SpotifyAuth;
import advisor.View.UserView;

import java.util.HashMap;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ApplicationController controllerApplication = new ApplicationController(new UserView()
                , SpotifyAuth.getInstance());

        if (args.length >= 2) {
            HashMap<String, String> initParamsAuthAndResourceServer = new HashMap<>();
            for (int i = 0; i < args.length; i += 2) {
                initParamsAuthAndResourceServer.put(args[i], args[i + 1]);
            }
            controllerApplication.setInitParams(initParamsAuthAndResourceServer);
        }

        String inputOption = sc.nextLine();
        while (!inputOption.equalsIgnoreCase("exit")) {
            controllerApplication.mainMenu(inputOption);
            inputOption = sc.nextLine();
        }

    }

}