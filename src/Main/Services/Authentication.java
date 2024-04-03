package Main.Services;

import Main.Model.User;

public class Authentication {
    public static String currentUserId = null;
    public static User currentUserData = null;

    public static boolean isUserLoggedIn() {
        return currentUserId != null;
    }
}
