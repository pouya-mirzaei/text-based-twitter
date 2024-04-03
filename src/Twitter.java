import View.Colors;
import View.Menu;
import View.Typewriter;

import java.util.Scanner;

public class Twitter {

    public static Scanner scanner;
    private static Typewriter tw = Menu.tw;


    public Twitter() {
        scanner = new Scanner(System.in);
    }

    public static void run() {
        Menu menu = new Menu();

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
        tw.typeWithColor("log in bitch ...", Colors.CYAN, false);
    }

    private static void handleSignUp() {
        tw.typeWithColor("Sign up bitch ...", Colors.GREEN, false);

    }


    public static int getIntegerInput() {
        while (!scanner.hasNextInt()) {
            scanner.nextLine(); // Clear invalid input
            tw.typeWithColor("Invalid input. Please enter a number: ", Colors.RED, true);
        }
        return scanner.nextInt();

    }
}
