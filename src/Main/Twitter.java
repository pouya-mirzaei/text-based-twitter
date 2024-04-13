package Main;

import Main.Controler.TweetController;
import Main.Controler.UserController;
import Main.Model.Tweet;
import Main.Services.Authentication;
import Main.Services.Database;
import Main.View.Colors;
import Main.View.Menu;
import Main.View.Typewriter;

import java.sql.SQLException;
import java.util.Scanner;

public class Twitter {

    public static Scanner scanner;
    public static Database db;
    public static Typewriter tw = Menu.tw;
    public static Menu menu;
    public static UserController userController;
    public static TweetController tweetController;
    public static Authentication auth;


    public Twitter() throws IllegalAccessException {
        scanner = new Scanner(System.in);
        userController = new UserController();
        tweetController = new TweetController();
        menu = new Menu();
        auth = new Authentication();
        db = new Database();
    }

    public static void run() {

        if (Authentication.isUserLoggedIn()) {
            if (Authentication.isAdmin()) {
                adminMenu();
            } else {
                userMenu();
            }
        } else {
            authMenu();
        }


    }

    private static void adminMenu() {
        tw.typeWithColor("Admin menu", Colors.CYAN, true);

    }

    private static void userMenu() {

        String[] mainMenu = {"Home", "My tweets", "Search", "Tweet a post", "Profile Settings", "Log out"};
        String message = "Hi " + auth.currentUserData.getName();

        int userInput = 0;
        boolean condition;
        do {
            menu.displayMenu(mainMenu, message);
            userInput = getIntegerInput();

            condition = userInput < 1 || userInput > mainMenu.length;

            if (condition) {
                tw.typeWithColor("Wrong choice, try again ...", Colors.RED, false);
                try {
                    Thread.sleep(1500);
                } catch (Exception e) {
                    //
                }
            }
        } while (condition);

        switch (userInput) {
            case 1:
                // home page
                break;
            case 2:
                Twitter.tweetController.showMyTweets();
                break;
            case 3:
                // search
                break;
            case 4:
                Authentication.currentUserData.tweet();
                break;
            case 5:
                // profile
                Authentication.currentUserData.profileSettings();
                break;
            case 6:
                userController.logout();
                Twitter.run();
                break;

        }

    }

    private static void authMenu() {

        String[] mainMenu = {"Login", "Sign Up", "Exit"};
        String message = "Welcome to my" + Colors.CYAN + " Twitter app " + Colors.RESET;
        message += "\nYou need to log into your account. If you don't have one, create one :)";
        int userInput = 0;

        boolean condition;
        do {
            menu.displayMenu(mainMenu, message);
            userInput = getIntegerInput();

            condition = userInput < 1 || userInput > mainMenu.length;

            if (condition) {
                tw.typeWithColor("Wrong choice, try again ...", Colors.RED, false);
                try {
                    Thread.sleep(1500);
                } catch (Exception e) {

                }
            }
        } while (condition);

        switch (userInput) {
            case 1:
                handleLogIn();
                break;
            case 2:
                handleSignUp();
                break;
            case 3:
                System.exit(0);
        }

    }

    private static void handleLogIn() {
        auth.loginMenu();
    }

    private static void handleSignUp() {
        auth.signupMenu();
    }


    public static int getIntegerInput() {
        while (!scanner.hasNextInt()) {
            scanner.nextLine(); // Clear invalid input
            tw.typeWithColor("Invalid input. Please enter a number: ", Colors.RED, true);
        }
        return scanner.nextInt();

    }
}
