package Main.View;

import Main.Model.User;
import Main.Twitter;

public class UserPage {
    public void showPage(User user) {
        System.out.println(user.getUsername());
        Twitter.scanner.nextLine();
        Twitter.run();
    }
}
