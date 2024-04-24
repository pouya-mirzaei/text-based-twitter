package Main.View;

import Main.Model.Tweet;
import Main.Services.Authentication;
import Main.Services.TweetsManagerDb;
import Main.Twitter;

import java.sql.SQLException;
import java.util.List;

public class HomePage {
    private final Typewriter tw = Twitter.tw;
    private final TweetsManagerDb tweetsManagerDb = Twitter.tweetsManagerDb;

    public void showPage() throws SQLException {
        String[] mainMenu = {"For You", "Following", "Back"};


        int userInput = 0;
        boolean condition;
        do {
            Twitter.menu.displayMenu(mainMenu, "Select one :");
            userInput = Twitter.getIntegerInput();

            condition = userInput < 1 || userInput > mainMenu.length;

            if (condition) {
                tw.typeWithColor("Wrong choice, try again ...", Colors.RED, false);
                try {
                    Thread.sleep(1500);
                } catch (Exception e) {
                    //
                }
            }
        } while (condition);

        switch (userInput) {
            case 1:
                feed();
                break;
            case 2:
                following();
                break;
            case 3:
                Twitter.run();
                break;
            default:
                System.out.println("wrong");
        }
    }

    private void following() throws SQLException {
        List<Tweet> tweets = tweetsManagerDb.getFollowingUsersTweets(Authentication.currentUserId);
        Tweet.previewTweets(tweets);
    }

    private void feed() throws SQLException {
        List<Tweet> tweets = tweetsManagerDb.getTweetsOrderedByLikes();
        Tweet.previewTweets(tweets);
    }


}
