package View;

import java.io.IOException;
import java.util.Date;

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
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            }

            // Now your console is cleared
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

}
