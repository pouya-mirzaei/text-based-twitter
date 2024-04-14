package Main.Services;


import java.sql.*;

public class TweetsManagerDb {
    public boolean likeTweet(int tweetId) throws SQLException {
        try {
            Connection conn = DriverManager.getConnection(Database.DATABASE_URL);
            PreparedStatement statement = conn.prepareStatement("" +
                    "SELECT * FROM likes " +
                    "WHERE user_id = ? AND tweet_id = ?");
            statement.setInt(1, Authentication.currentUserId);
            statement.setInt(2, tweetId);
            statement.execute();

            ResultSet rs = statement.getResultSet();

            if (rs.next()) {
                return false;
            }
            statement = conn.prepareStatement("INSERT INTO likes (user_id,tweet_id) " +
                    "VALUES (?,?) ");
            statement.setInt(1, Authentication.currentUserId);
            statement.setInt(2, tweetId);
            statement.execute();


            return true;
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
}
