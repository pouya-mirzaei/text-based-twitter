package Main.View;

import Main.Controler.TweetController;
import Main.Model.Tweet;
import Main.Twitter;

public class TweetPage {
    private static final TweetController tweetController = Twitter.tweetController;

    public static void tweetPage(Tweet t) {
        System.out.println(t.getContent() + 1 + " liked users count");

        // temp code
        Twitter.scanner.nextLine();
        Twitter.run();
    }
}
