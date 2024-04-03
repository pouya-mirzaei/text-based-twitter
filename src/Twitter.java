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
        String[] items = {"hello", "blue"};
        menu.displayMenu(items, "Welcome to my" + Colors.CYAN + " Twitter app " + Colors.RESET + "Please Select an option to start");

    }


    public static int getIntegerInput() {
        while (!scanner.hasNextInt()) {
            scanner.nextLine(); // Clear invalid input
            tw.typeWithColor("Invalid input. Please enter a number: ", Colors.RED, true);
        }
        return scanner.nextInt();

    }
}
