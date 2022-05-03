package advisor.View;

import java.util.List;

public class UserView {

    public void printListOfMessage(List<String> messageList) {
        messageList.forEach(System.out::println);
    }

    public void printListOfMessage(String message) {
        System.out.println(message);
    }

    public void printPage(List<String> contentList, int numOfPage, int numberOfPages) {
        System.out.println("---PAGE " + numOfPage + " OF " + numberOfPages + "---");
        printListOfMessage(contentList);
    }
}
