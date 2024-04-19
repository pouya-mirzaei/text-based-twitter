package Main.View;

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

    public static String splitLongString(String longString, int maxLineLength) {
        StringBuilder result = new StringBuilder();
        StringBuilder currentLine = new StringBuilder();

        for (String word : longString.split(" ")) {
            if (currentLine.length() + word.length() <= maxLineLength) {
                currentLine.append(" ").append(word);
            } else {
                result.append(currentLine.toString().trim()).append("\n\t");
                currentLine = new StringBuilder(word);
            }
        }
        result.append(currentLine.toString().trim());
        return result.toString();
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
