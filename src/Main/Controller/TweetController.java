package Main.Controller;

import Main.Model.Tweet;
import Main.Model.User;
import Main.Services.Authentication;
import Main.Services.Database;
import Main.Twitter;
import Main.View.Colors;
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
            Twitter.tweetPage.showPage(getCurrentUserLastTweet());


        } catch (SQLException e) {
            throw new SQLException(e);
        }

    }


    // ----------------------------------------------------------------- DATABASE OPERATIONS

    // READ
    public List<Tweet> getUserTweets(User u) throws SQLException {
        try {
            Connection conn = DriverManager.getConnection(Database.DATABASE_URL);
            PreparedStatement stmn = conn.prepareStatement("SELECT * FROM tweets " +
                    "INNER JOIN users ON tweets.user_id = users.id " +
                    "WHERE username = ? " +
                    "ORDER BY timestamp DESC");

            stmn.setString(1, u.getUsername());

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

            conn.close();
            return userTweets;

        } catch (SQLException e) {
            throw e;
        }
    }

    public List<Tweet> getAllTweets() throws SQLException {
        try {
            Connection conn = DriverManager.getConnection(Database.DATABASE_URL);

            Statement stmn = conn.createStatement();
            String sql = "SELECT * from tweets " +
                    "INNER JOIN users ON tweets.user_id = users.id " +
                    "ORDER BY timestamp DESC";

            ResultSet resultSet = stmn.executeQuery(sql);
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

            conn.close();
            return userTweets;

        } catch (SQLException e) {
            throw e;
        }
    }

    public Tweet getTweetById(int id) throws SQLException {
        try {
            Connection conn = DriverManager.getConnection(Database.DATABASE_URL);
            PreparedStatement stmn = conn.prepareStatement("SELECT * FROM tweets " +
                    "INNER JOIN users ON tweets.user_id = users.id " +
                    "WHERE tweets.id = ? ");

            stmn.setInt(1, id);

            ResultSet resultSet = stmn.executeQuery();
            Tweet newTweet = new Tweet(
                    resultSet.getInt("id"),
                    resultSet.getString("tweet_content"),
                    resultSet.getTimestamp("timestamp").getTime(),
                    resultSet.getInt("user_id")
            );
            newTweet.setUsername(resultSet.getString("username"));
            conn.close();
            return newTweet;

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

    public Tweet getCurrentUserLastTweet() throws SQLException {
        try (Connection connection = DriverManager.getConnection(Database.DATABASE_URL);
             PreparedStatement prepareStatement = connection.prepareStatement("" +
                     "SELECT * FROM tweets " +
                     "INNER JOIN users ON tweets.user_id = users.id " +
                     "WHERE user_id = ? " +
                     "ORDER BY timestamp DESC");
        ) {
            prepareStatement.setInt(1, Authentication.currentUserId);

            ResultSet rs = prepareStatement.executeQuery();

            if (rs.next()) {
                Tweet t = new Tweet(
                        rs.getInt("id"),
                        rs.getString("tweet_content"),
                        rs.getTimestamp("timestamp").getTime(),
                        rs.getInt("user_id")
                );
                t.setUsername(rs.getString("username"));
                return t;
            } else {
                throw new SQLException("There was an error finding the tweet, please try again later...");
            }
        } catch (SQLException e) {
            throw e;
        }
    }


    // Update

    // DELETE
}
