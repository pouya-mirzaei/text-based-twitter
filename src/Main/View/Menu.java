package Main.View;

import java.io.IOException;

public class Menu {
    public static Typewriter tw = new Typewriter(5);

    public void displayMenu(String[] menuItems, String message) {
        clearScreen();

        tw.type(message);

        int counter = 1;

        for (String item : menuItems) {
            tw.typeWithColor(" " + counter + ". " + item, Colors.YELLOW, true);
            counter++;
        }


    }

    public static void clearScreen() {
    }

}
