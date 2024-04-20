package Main.Services;


import Main.Model.Comment;
import Main.Model.Tweet;
import Main.Model.User;
import Main.Twitter;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TweetsManagerDb {
    public boolean likeTweet(int tweetId) throws SQLException {
        try (Connection conn = DriverManager.getConnection(Database.DATABASE_URL)) {
            if (isUserAlreadyLiked(Authentication.currentUserId, tweetId)) {
                return false;
            }

            // If not liked, insert a new like
            try (PreparedStatement insertStatement = conn.prepareStatement("INSERT INTO likes (user_id,tweet_id) " +
                    "VALUES (?,?) ")) {
                insertStatement.setInt(1, Authentication.currentUserId);
                insertStatement.setInt(2, tweetId);
                insertStatement.execute();
            }

            return true; // Like inserted successfully
        } catch (SQLException e) {
            throw e;
        }

    }

    public boolean unlikeTweet(int tweetId) throws SQLException {
        try (Connection conn = DriverManager.getConnection(Database.DATABASE_URL);
             PreparedStatement statement = conn.prepareStatement("" +
                     "DELETE FROM likes " +
                     "WHERE user_id = ? AND tweet_id = ?")) {

            statement.setInt(1, Authentication.currentUserData.getId());
            statement.setInt(2, tweetId);
            int result = statement.executeUpdate();

            return result == 1;
        } catch (SQLException e) {
            throw e;
        }

    }

    public int countLikes(int tweetId) throws SQLException {

        try (Connection conn = DriverManager.getConnection(Database.DATABASE_URL);
             PreparedStatement statement = conn.prepareStatement("" +
                     "SELECT COUNT(*) AS like_count " +
                     "FROM likes " +
                     "WHERE tweet_id = ?")) {

            statement.setInt(1, tweetId);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                return rs.getInt("like_count");
            } else {
                return 0;
            }

        } catch (SQLException e) {
            throw e;
        }
    }

    public boolean isUserAlreadyLiked(int userId, int tweetId) throws SQLException {

        try (Connection conn = DriverManager.getConnection(Database.DATABASE_URL);
             PreparedStatement statement = conn.prepareStatement("" +
                     "SELECT COUNT(*) > 0 AS already_liked " +
                     "FROM likes " +
                     "WHERE user_id = ? AND tweet_id = ?")) {

            statement.setInt(1, userId);
            statement.setInt(2, tweetId);

            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                return rs.getBoolean("already_liked");
            } else {
                return false;
            }

        } catch (SQLException e) {
            throw e;
        }
    }

    public void commentTo(Tweet t, String commentMessage) throws SQLException {
        try (Connection connection = DriverManager.getConnection(Database.DATABASE_URL);
             PreparedStatement stmt = connection.prepareStatement("INSERT INTO comments (tweet_id, user_id, comment_content, parent_comment_id) VALUES (?, ?, ?, ?)")) {
            stmt.setInt(1, t.getId());
            stmt.setInt(2, Authentication.currentUserId);
            stmt.setString(3, commentMessage);
            stmt.setInt(4, 0);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw e;
        }
    }

    public void replyTo(Tweet t, Comment c, String comment) throws SQLException {
        try (Connection connection = DriverManager.getConnection(Database.DATABASE_URL);
             PreparedStatement stmt = connection.prepareStatement("INSERT INTO comments (tweet_id, user_id, comment_content, parent_comment_id) VALUES (?, ?, ?, ?)")) {
            stmt.setInt(1, t.getId());
            stmt.setInt(2, Authentication.currentUserId);
            stmt.setString(3, comment);
            stmt.setInt(4, c.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw e;
        }
    }

    public List<User> getUsersWhoLikedTweet(long tweetId) throws SQLException {
        try (Connection connection = DriverManager.getConnection(Database.DATABASE_URL)) {
            PreparedStatement statement = connection.prepareStatement("" +
                    "SELECT u.* " +
                    "FROM users AS u " +
                    "INNER JOIN likes AS l ON u.id = l.user_id " +
                    "WHERE l.tweet_id = ?");
            statement.setLong(1, tweetId);

            ResultSet rs = statement.executeQuery();
            List<User> users = new ArrayList<>();
            while (rs.next()) {
                User user = new User(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("lastname"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role"),
                        rs.getString("bio"),
                        rs.getDate("created_at").getTime()
                );
                users.add(user);
            }

            return users;
        } catch (SQLException e) {
            throw e;
        }
    }

}
