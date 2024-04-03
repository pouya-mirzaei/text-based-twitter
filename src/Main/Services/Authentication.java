package Main.Services;

import Main.Controler.UserController;
import Main.Model.User;
import Main.Twitter;
import Main.View.Colors;
import Main.View.Menu;
import Main.View.Typewriter;

import java.util.Objects;
import java.util.Scanner;

import static Main.Twitter.auth;
import static Main.Twitter.getIntegerInput;

public class Authentication {
    public static String currentUserId = null;
    public static User currentUserData = null;
    private static Typewriter tw = Twitter.tw;
    private static Scanner scanner = Twitter.scanner;
    private final UserController userController = Twitter.userController;


    public static boolean isUserLoggedIn() {
        return currentUserId != null;
    }

    public void signupMenu() {
        Menu.clearScreen();
        scanner.nextLine();
        tw.typeWithColor("Signup Menu Menu", Colors.CYAN, true);

        // name
        tw.typeWithColor("Tell us your name =>", Colors.PURPLE, true);
        String name = scanner.nextLine();

        // last name
        tw.typeWithColor("Hey  " + name + "! what is your last name ? =>", Colors.PURPLE, true);
        String lastName = scanner.nextLine();

        // username
        tw.typeWithColor("Create a username for your self =>", Colors.PURPLE, true);
        String username = scanner.next();

        if (userController.findUser(username) != null) {
            do {
                tw.typeWithColor("The username \"" + username + "\" is already in use. Please try a different username.", Colors.PURPLE, true);
                username = scanner.next();

            } while (userController.findUser(username) != null);
        }

        // password
        String password;
        String confirmPassword;
        boolean condition;
        do {
            tw.typeWithColor("Create a password for your account =>", Colors.PURPLE, true);
            password = scanner.next();

            tw.typeWithColor("Confirm your password =>", Colors.PURPLE, true);
            confirmPassword = scanner.next();

            condition = !Objects.equals(password, confirmPassword);
            if (condition) {
                tw.typeWithColor("Your password didn't math, try again ...", Colors.RED, true);
            }

        } while (condition);


//        auth.signup(name, lastName, username, password, age, gender);


    }

}
