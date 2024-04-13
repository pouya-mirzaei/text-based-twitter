package Main.Controler;

import Main.Model.Tweet;
import Main.Services.Authentication;
import Main.Services.Database;
import Main.Twitter;
import Main.View.Colors;
import Main.View.TweetPage;
import Main.View.Typewriter;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TweetController {
    private Typewriter tw = Twitter.tw;
    private Scanner sc = Twitter.scanner;


    public void tweet(int id) throws SQLException {
        String message = "Want to tweet ? " +
                "\nEnter your tweet message:";
        tw.typeWithColor(message, Colors.CYAN, true);
        sc.nextLine(); // clear the input

        String tweet_content = sc.nextLine();

        try {
            createTweet(id, tweet_content);
            tw.typeWithColor("You have tweeted a new post !", Colors.CYAN, true);
            tw.typeWithColor(" Press any key to see your tweet", Colors.CYAN, true);
            sc.nextLine();
            TweetPage.tweetPage(getCurrentUserTweets().get(2));


        } catch (SQLException e) {
            throw new SQLException(e);
        }

    }


    // ----------------------------------------------------------------- DATABASE OPERATIONS

    // READ
    public List<Tweet> getCurrentUserTweets() throws SQLException {
        try {
            Connection conn = DriverManager.getConnection(Database.DATABASE_URL);
            PreparedStatement stmn = conn.prepareStatement("SELECT * FROM tweets " +
                    "INNER JOIN users ON tweets.user_id = users.id " +
                    "WHERE username = ?");

            stmn.setString(1, Authentication.currentUserData.getUsername());

            ResultSet resultSet = stmn.executeQuery();
            List<Tweet> userTweets = new ArrayList<>();
            while (resultSet.next()) {
                Tweet newTweet = new Tweet(
                        resultSet.getInt("id"),
                        resultSet.getString("tweet_content"),
                        resultSet.getTimestamp("timestamp").getTime(),
                        resultSet.getInt("user_id")
                );
                newTweet.setUsername(resultSet.getString("username"));
                userTweets.add(newTweet);
            }

            return userTweets;

        } catch (SQLException e) {
            throw e;
        }
    }


    // CREATE
    public void createTweet(int userId, String tweenContent) throws SQLException {
        try (Connection connection = DriverManager.getConnection(Database.DATABASE_URL);
             PreparedStatement prepareStatement = connection.prepareStatement("INSERT " +
                     "INTO tweets (user_id, tweet_content) " +
                     "VALUES (?,?);");
        ) {
            prepareStatement.setInt(1, userId);
            prepareStatement.setString(2, tweenContent);

            prepareStatement.executeUpdate();

        } catch (SQLException e) {
            throw e;
        }
    }

    public void showMyTweets() {

        List<Tweet> tweets;
        String test = sc.nextLine();
        try {
            tweets = getCurrentUserTweets();
            for (int i = 0; i < tweets.size(); i++) {
                tw.typeWithColor(i + 1 + ".", Colors.PURPLE, true);

                tw.typeWithColor("------------------------------------------------------------------------------------------", Colors.GREEN, true);
                tweetPreview(tweets.get(i));
                tw.typeWithColor("------------------------------------------------------------------------------------------", Colors.GREEN, true);

                if (i % 3 == 2 && i != tweets.size() - 1) {
                    tw.typeWithColor("Type 'more' to see more tweets", Colors.YELLOW, true);
                    tw.typeWithColor("Or Enter the number of the tweet that you want to see", Colors.YELLOW, true);
                    String text;

                    int id = 0;
                    do {
                        text = sc.nextLine();
                        if (text.equals("more")) {
                            break;
                        }
                        id = -1;
                        try {
                            id = Integer.parseInt(text);
                        } catch (NumberFormatException e) {
                            tw.typeWithColor("You should pass an number or type 'more'", Colors.RED, true);
                        }
                        if (id > 0 && id <= i + 1) {
                            TweetPage.tweetPage(tweets.get(i));
                            return;
                        }

                    } while (!text.equals("more") || id < 0 || id >= i + 1);

                    if (text.equals("more")) {
                        continue;
                    }

                    if (id > 0 && id <= i + 1) {
                        TweetPage.tweetPage(tweets.get(i));
                        return;
                    } else {
                        tw.typeWithColor("wrong choice, try again...", Colors.RED, true);
                        i -= 3;
                    }

                }
            }
            int id = 0;
            do {
                id = Twitter.getIntegerInput();
                TweetPage.tweetPage(tweets.get(id - 1));
            } while (id < 0 || id >= tweets.size());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        Twitter.run();// -------------------------------------------------------------


    }


    private static void tweetPreview(Tweet tweet) {
        System.out.print("\t@");
        System.out.println(tweet.getUsername() + "\t\t" + new Timestamp(tweet.getCreatedAt()));
        System.out.println("\tContent : " + tweet.getContent());

    }


    // Update

    // DELETE
}
