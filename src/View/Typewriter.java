package View;

public class Typewriter {

    private final int delay;

    public Typewriter(int delay) {
        this.delay = delay;
    }


    public void type(String message) {
        try {
            for (char c : message.toCharArray()) {
                System.out.print(c);
                Thread.sleep(delay);
            }

            System.out.println(); // Move to the next line after typing the complete message

        } catch (InterruptedException ie) {
            System.out.println(message);
        }

    }

    public void typeWithColor(String message, String color, boolean goToNextLine) {
        try {
            for (char c : message.toCharArray()) {
                System.out.print(color + c + Colors.RESET);
                Thread.sleep(delay);
            }

            if (goToNextLine) System.out.println(); // Move to the next line after typing the complete message

        } catch (InterruptedException ie) {
            System.out.println(message);
        }

    }

}
