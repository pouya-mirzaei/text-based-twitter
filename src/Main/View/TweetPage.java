package Main.View;

import Main.Controller.TweetController;
import Main.Model.Tweet;
import Main.Services.TweetsManagerDb;
import Main.Twitter;

import java.sql.SQLException;

public class TweetPage {
    private final TweetController tweetController = Twitter.tweetController;
    private final TweetsManagerDb tweetsManagerDb = Twitter.tweetsManagerDb;
    private final Typewriter tw = Twitter.tw;

    public void showPage(Tweet t) throws SQLException {
        try {
            System.out.println("like counter : " + Twitter.tweetsManagerDb.countLikes(t.getId()));
            System.out.println(t.getContent());
            String[] menu = {"like", "unlike", "back"};
            Twitter.menu.displayMenu(menu, "select :");
            int test = Twitter.getIntegerInput();

            switch (test) {
                case 1:
                    if (tweetsManagerDb.likeTweet(t.getId())) {
                        tw.typeWithColor("success !", Colors.GREEN, true);
                    } else {
                        tw.typeWithColor("You have already liked this tweet !", Colors.RED, true);
                    }
                    showPage(t);
                    break;
                case 2:
                    if (tweetsManagerDb.unlikeTweet(t.getId())) {
                        tw.typeWithColor("success !", Colors.GREEN, true);
                    } else {
                        tw.typeWithColor("You have not liked this tweet yet !", Colors.RED, true);
                    }
                    showPage(t);
                    break;
                case 3:
                    Twitter.run();
                    return;

            }

        } catch (SQLException e) {
            throw e;
        }

        // temp code
        Twitter.scanner.nextLine();
        Twitter.run();
    }
}
