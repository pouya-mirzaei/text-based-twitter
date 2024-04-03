package Main.Services;

import Main.Controler.UserController;
import Main.Model.User;
import Main.Twitter;
import Main.View.Colors;
import Main.View.Menu;
import Main.View.Typewriter;

import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        String username;
        do {
            // Prompt the user for a username
            tw.typeWithColor("Create a username for yourself => ", Colors.PURPLE, true);
            username = scanner.nextLine();

            // Validate the username format using regex
            if (!isValidUsername(username)) {
                tw.typeWithColor("Invalid username format.\nUsername must contain only alphanumeric characters, underscores, or hyphens, and be between 3 and 16 characters long.", Colors.RED, true);
            }
        } while (!isValidUsername(username));


        if (userController.findUser(username) != null) {
            do {
                tw.typeWithColor("The username \"" + username + "\" is already in use. Please try a different username.", Colors.RED, true);
                username = scanner.next();

            } while (userController.findUser(username) != null);
        }

        // password
        String password;
        String confirmPassword;
        boolean condition;
        do {
            tw.typeWithColor("Create a password for your account (Your password cannot contains spaces)=>", Colors.PURPLE, true);
            password = scanner.nextLine();

            tw.typeWithColor("Confirm your password =>", Colors.PURPLE, true);
            confirmPassword = scanner.nextLine();

            condition = !Objects.equals(password, confirmPassword) || password.contains(" ");
            if (condition) {
                tw.typeWithColor("Your password didn't match or it contained spaces, try again ...", Colors.RED, true);
            }

        } while (condition);

        // bio
        tw.typeWithColor("Please write a biography about yourself. Keep it within 200 characters.\" =>", Colors.PURPLE, true);
        String bio = scanner.nextLine();


        userController.signup(name, lastName, username, password, bio);


    }

    private boolean isValidUsername(String username) {
        // Regex pattern for username
        String regexPattern = "^[a-zA-Z0-9_-]{3,16}$";

        // Compile the regex pattern
        Pattern pattern = Pattern.compile(regexPattern);

        // Match the username against the regex pattern
        Matcher matcher = pattern.matcher(username);

        // Return true if the username matches the regex pattern
        return matcher.matches();
    }

}
