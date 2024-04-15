package Main.Model;

import Main.Twitter;
import Main.View.Colors;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Tweet {

    private int id;
    private String content;
    private long createdAt;
    private int userId;
    private String username;
    private List<User> liked;
    private List<User> retweets;

    // Getters and Setters


    public Tweet(int userId, String content) {
        this.id = 0; // the main id will be created at database
        this.content = content;
        this.userId = userId;
        this.createdAt = new Date().getTime();
        this.liked = new ArrayList<>();
        this.retweets = new ArrayList<>();
    }

    public Tweet(int id, String content, long createdAt, int userId) {
        this.id = id;
        this.content = content;
        this.createdAt = createdAt;
        this.userId = userId;
        this.liked = new ArrayList<>();
        this.retweets = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public List<User> getLiked() {
        return liked;
    }

    public void setLiked(List<User> liked) {
        this.liked = liked;
    }

    public List<User> getRetweets() {
        return retweets;
    }

    public void setRetweets(List<User> retweets) {
        this.retweets = retweets;

    }

    public static void previewTweets(List<Tweet> tweets) {

        Twitter.scanner.nextLine();

        if (tweets.isEmpty()) {
            Twitter.tw.typeWithColor("You have not posted any tweet yet!", Colors.YELLOW, true);
            Twitter.tw.typeWithColor("Try tweet a post !", Colors.YELLOW, true);
            Twitter.tw.typeWithColor("Press any key to continue ...", Colors.WHITE, true);
            Twitter.scanner.nextLine();
            Twitter.run();
            return;
        }

        try {
            for (int i = 0; i < tweets.size(); i++) {
                Twitter.tw.typeWithColor(i + 1 + ".", Colors.PURPLE, true);

                Twitter.tw.typeWithColor("------------------------------------------------------------------------------------------", Colors.GREEN, true);
                tweetPreview(tweets.get(i));
                Twitter.tw.typeWithColor("------------------------------------------------------------------------------------------", Colors.GREEN, true);

                if (i % 3 == 2 && i != tweets.size() - 1) {
                    Twitter.tw.typeWithColor("Type 'more' to see more tweets", Colors.YELLOW, true);
                    Twitter.tw.typeWithColor("Or Enter the number of the tweet that you want to see", Colors.YELLOW, true);
                    String text;

                    int id = 0;
                    do {
                        text = Twitter.scanner.nextLine();
                        if (text.equals("more")) {
                            break;
                        }
                        try {
                            id = Integer.parseInt(text);
                        } catch (NumberFormatException e) {
                            Twitter.tw.typeWithColor("You should pass an number or type 'more'", Colors.RED, true);
                        }
                        if (id > 0 && id <= i + 1) {
                            Twitter.tweetPage.showPage(tweets.get(id - 1));
                            return;
                        }

                    } while (id < 0 || id >= i + 1);

                    if (text.equals("more")) {
                        continue;
                    }

                    if (id > 0 && id <= i + 1) {
                        Twitter.tweetPage.showPage(tweets.get(i));
                        return;
                    } else {
                        Twitter.tw.typeWithColor("wrong choice, try again...", Colors.RED, true);
                        i -= 3;
                    }

                }
            }
            Twitter.tw.typeWithColor("That's all you got!", Colors.PURPLE, true);
            Twitter.tw.typeWithColor("Select one to see the details(-1 to return)", Colors.PURPLE, true);
            int id = 0;
            do {
                id = Twitter.getIntegerInput();
                if (id == -1) {
                    Twitter.run();
                    return;
                }
                if (id > tweets.size() || id < 1) continue;
                Twitter.tweetPage.showPage(tweets.get(id - 1));
            } while (id < 1 || id >= tweets.size());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    private static void tweetPreview(Tweet tweet) {
        System.out.print("\t@");
        System.out.println(tweet.getUsername() + "\t\t" + new Timestamp(tweet.getCreatedAt()));
        System.out.println("\tContent : " + tweet.getContent());
        try {
            System.out.println("\t[Likes] : " + Twitter.tweetsManagerDb.countLikes(tweet.getId()));
        } catch (SQLException e) {
            //
        }

    }
}

