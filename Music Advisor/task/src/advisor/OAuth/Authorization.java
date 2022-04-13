package advisor.OAuth;

public class Authorization {
    private static boolean isAuthorized = false;

    public static boolean isAuthorized() {
        return isAuthorized;
    }

    public static void setIsAuthorized(boolean isAuthorized) {
        Authorization.isAuthorized = isAuthorized;
    }
}
