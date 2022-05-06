package advisor;

import advisor.Controller.ApplicationController;

import java.util.HashMap;

public class Main {
    public static void main(String[] args) {

        ApplicationController controllerApplication = new ApplicationController();

        if (args.length >= 2) {
            HashMap<String, String> initParamsAuthAndResourceServer = new HashMap<>();
            for (int i = 0; i < args.length; i += 2) {
                initParamsAuthAndResourceServer.put(args[i], args[i + 1]);
            }
            controllerApplication.setInitParams(initParamsAuthAndResourceServer);
        }

        controllerApplication.runApplication();

    }
}