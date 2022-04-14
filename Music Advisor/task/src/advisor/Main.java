package advisor;

import advisor.Controller.Controller;
import advisor.Model.MusicDataModel;
import advisor.View.MusicView;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Controller controller = new Controller(new MusicView(), new MusicDataModel());

        String inputOption = "";
        while (!inputOption.equalsIgnoreCase("exit")) {
            inputOption = sc.nextLine();
            controller.mainMenu(inputOption);
        }
    }

}
