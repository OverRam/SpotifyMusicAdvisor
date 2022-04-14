package advisor.View;

import java.util.List;

public class MusicView {

    public void printList(List<String> music) {
        music.forEach(System.out::println);
    }
}
