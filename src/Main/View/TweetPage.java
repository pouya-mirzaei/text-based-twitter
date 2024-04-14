package Main.View;

import Main.Controller.TweetController;
import Main.Model.Tweet;
import Main.Twitter;

import java.sql.SQLException;

public class TweetPage {
    private static final TweetController tweetController = Twitter.tweetController;

    public static void tweetPage(Tweet t) {
        try {
            System.out.println("like counter : " + Twitter.tweetsManagerDb.countLikes(t.getId()));
            System.out.println(t.getContent());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // temp code
        Twitter.scanner.nextLine();
        Twitter.run();
    }
}
