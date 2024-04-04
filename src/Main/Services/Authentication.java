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


public class Authentication {
    public static String currentUserId = null;
    public static User currentUserData = null;
    private static Typewriter tw = Twitter.tw;
    private static Scanner scanner = Twitter.scanner;
    private final UserController userController = Twitter.userController;


    public static boolean isUserLoggedIn() {
        return currentUserId != null;
    }

    public static boolean isAdmin() {
        return Objects.equals(currentUserData.getRole(), "ADMIN");
    }

    public void signupMenu() {
        Menu.clearScreen();
        scanner.nextLine();
        tw.typeWithColor("Signup Menu :)", Colors.CYAN, true);

        // name
        tw.type("Tell us your name =>");
        String name = scanner.nextLine();

        // last name
        tw.type("Hey  " + name + "! what is your last name ? =>");
        String lastName = scanner.nextLine();

        // username
        String username;
        do {
            // Prompt the user for a username
            tw.type("Create a username for yourself => ");
            username = scanner.nextLine();

            // Validate the username format using regex
            if (!isInputValid(username, "^[a-zA-Z0-9_-]{3,16}$")) {
                tw.typeWithColor("Invalid username format.\nUsername must contain only alphanumeric characters, underscores, or hyphens, and be between 3 and 16 characters long.", Colors.RED, true);
            }
        } while (!isInputValid(username, "^[a-zA-Z0-9_-]{3,16}$"));


        if (userController.findUser(username) != null) {
            do {
                tw.typeWithColor("The username \"" + username + "\" is already in use. Please try a different username.", Colors.RED, true);
                username = scanner.next();

            } while (userController.findUser(username) != null);
        }

        // password
        String password;
        String confirmPassword;
        boolean condition = true;
        do {
            tw.type("Create a password for your account (Your password cannot contains spaces)=>");
            password = scanner.nextLine();

            if (!isInputValid(password, "^(?=.*\\d)?(?=.*[a-zA-Z]).{6,}$")) {
                tw.typeWithColor("Your password is weak and easy to guess. Please choose a stronger one.", Colors.RED, true);
                tw.typeWithColor("  Your password must contain at least 6 characters or more.", Colors.YELLOW, true);
                tw.typeWithColor("  And it must also include both uppercase and lowercase letters for added security.", Colors.YELLOW, true);
                continue;
            }

            tw.type("Confirm your password =>");
            confirmPassword = scanner.nextLine();

            condition = !Objects.equals(password, confirmPassword) || password.contains(" ");
            if (condition) {
                tw.typeWithColor("Your password didn't match, try again ...", Colors.RED, true);
            }

        } while (condition);

        // bio
        String bio;
        do {

            tw.type("Please write a biography about yourself. Keep it within 200 characters.\" =>");
            bio = scanner.nextLine();
        } while (bio.length() > 200);

        if (userController.signup(name, lastName, username, password, bio)) {
            tw.typeWithColor("You have been registered into the system!", Colors.CYAN, true);
            tw.type(" Press any key to continue...");
        } else {
            tw.typeWithColor("There was an error in registration:(", Colors.CYAN, true);
            tw.type(" Press any key to try again...");
        }
        scanner.nextLine();
        Twitter.run();
    }

    public void loginMenu() {
        Menu.clearScreen();
        scanner.nextLine();
        tw.typeWithColor("Login Menu :)", Colors.CYAN, true);

        //username
        tw.type("Enter your username");
        String username = scanner.nextLine();

        //password
        tw.type("Enter your password");
        String password = scanner.nextLine();


        try {
            userController.login(username, password);
            tw.typeWithColor("You have logged in into your account successfully:)", Colors.CYAN, true);
            tw.type(" Press any key to continue...");

        } catch (Exception e) {
            tw.typeWithColor("Error : " + e.getMessage(), Colors.RED, true);
            tw.type(" Press any key to try again...");
        } finally {
            scanner.nextLine();
            Twitter.run();
        }

    }


    private boolean isInputValid(String input, String regexPattern) {
        // Compile the regex pattern
        Pattern pattern = Pattern.compile(regexPattern);

        // Match the input against the regex pattern
        Matcher matcher = pattern.matcher(input);

        // Return true if the username matches the regex pattern
        return matcher.matches();
    }

}
