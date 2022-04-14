package advisor.Model;

import java.util.LinkedList;
import java.util.List;

public class MusicDataModel {
    public List<String> getNewMusic() {
        return newMusicTest();
    }

    public List<String> getFeaturedMusic() {
        return featuredMusicTest();
    }

    public List<String> getCategoriesMusic() {
        return categoriesMusicTest();
    }

    public List<String> getPlaylistsMusic(String categoryName) {
        return playlistsMusicTest();
    }

    private List<String> newMusicTest() {
        List<String> list = new LinkedList<>();
        list.add("Mountains [Sia, Diplo, Labrinth]");
        list.add("Runaway [Lil Peep]");
        list.add("The Greatest Show [Panic! At The Disco]");
        list.add("All Out Life [Slipknot]");
        return list;
    }

    private List<String> featuredMusicTest() {
        List<String> list = new LinkedList<>();
        list.add("Mellow Morning");
        list.add("Wake Up and Smell the Coffee");
        list.add("Monday Motivation");
        list.add("Songs to Sing in the Shower");
        return list;
    }

    private List<String> categoriesMusicTest() {
        List<String> list = new LinkedList<>();
        list.add("Top Lists");
        list.add("Pop");
        list.add("Mood");
        list.add("Latin");
        return list;
    }

    private List<String> playlistsMusicTest() {
        List<String> list = new LinkedList<>();
        list.add("Walk Like A Badass");
        list.add("Rage Beats");
        list.add("Arab Mood Booster");
        list.add("Sunday Stroll");
        return list;
    }
}
