package Main.Controler;

import Main.Services.Database;
import Main.Twitter;
import Main.View.Colors;
import Main.View.Typewriter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
            Twitter.run();


        } catch (SQLException e) {
            throw new SQLException(e);
        }

    }


    // ----------------------------------------------------------------- DATABASE OPERATIONS

    // READ

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

    // Update

    // DELETE
}
