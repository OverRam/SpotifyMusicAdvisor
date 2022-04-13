package advisor.Model;

public enum CategoryHeaders {
    NEW("---NEW RELEASES---"),
    FEATURED("---FEATURED---"),
    CATEGORIES("---CATEGORIES---"),
    PLAYLISTS("---MOOD PLAYLISTS---"),
    EXIT_TEXT("---GOODBYE!---"),
    SUCCESS("---SUCCESS---");


    private final String heading;

    CategoryHeaders(String heading) {
        this.heading = heading;
    }

    @Override
    public String toString(){
        return heading;
    }
}
