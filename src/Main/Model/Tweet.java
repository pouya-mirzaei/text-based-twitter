package Main.Model;

import Main.Twitter;
import Main.View.Colors;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Tweet {

    private int id;
    private String content;
    private long createdAt;
    private int userId;
    private String username;
    private static final int paginationCount = 3;
    private int likeCounts;

    // Getters and Setters


    public Tweet(int userId, String content) {
        this.id = 0; // the main id will be created at database
        this.content = content;
        this.userId = userId;
        this.createdAt = new Date().getTime();
    }

    public Tweet(int id, String content, long createdAt, int userId) {
        this.id = id;
        this.content = content;
        this.createdAt = createdAt;
        this.userId = userId;
    }

    public Tweet(int id, String content, long createdAt, int userId, String username, int likeCounts) {
        this.id = id;
        this.content = content;
        this.createdAt = createdAt;
        this.userId = userId;
        this.username = username;
        this.likeCounts = likeCounts;
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


    public static void previewTweets(List<Tweet> tweets) throws SQLException {

        Twitter.scanner.nextLine();

        if (tweets.isEmpty()) {
            Twitter.tw.typeWithColor("You have not posted any tweet yet!", Colors.RED, true);
            Twitter.tw.typeWithColor("Try tweet a post !", Colors.RED, true);
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

                if (i % paginationCount == paginationCount - 1 && i != tweets.size() - 1) {
                    Twitter.tw.typeWithColor("Type 'more' to see more tweets", Colors.YELLOW, true);
                    Twitter.tw.typeWithColor("Or Enter the number of the tweet that you want to see", Colors.YELLOW, true);
                    Twitter.tw.typeWithColor("Or Press -1 to return to the main menu", Colors.YELLOW, true);
                    String text = null;


                    int id = 0;
                    do {
                        text = Twitter.scanner.nextLine();
                        if (Objects.equals(text, "-1")) {
                            Twitter.run();
                            return;
                        }
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
                        i -= paginationCount;
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
            throw e;
        }
    }


    private static void tweetPreview(Tweet tweet) throws SQLException {
        Twitter.tw.typeWithColor("\t@" + tweet.getUsername(), Colors.BLUE, false);
        Twitter.tw.typeWithColor("\t\t" + new Timestamp(tweet.getCreatedAt()), Colors.WHITE, true);
        Twitter.tw.typeWithColor("\tContent : ", Colors.YELLOW, false);
        Twitter.tw.type(tweet.getContent());


        try {
            Twitter.tw.typeWithColor("\t[Likes] : ", Colors.PURPLE, false);
            Twitter.tw.type(String.valueOf(Twitter.tweetsManagerDb.countLikes(tweet.getId())));
        } catch (SQLException e) {
            throw e;
        }

    }

    public int getLikeCounts() {
        return likeCounts;
    }
}

