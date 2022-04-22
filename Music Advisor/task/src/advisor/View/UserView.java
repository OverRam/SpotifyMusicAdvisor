package advisor.View;

import java.util.List;

public class UserView {

    public void printMessage(List<String> messageList) {
        messageList.forEach(System.out::println);
    }

    public void printMessage(String message) {
        System.out.println(message);
    }
}
