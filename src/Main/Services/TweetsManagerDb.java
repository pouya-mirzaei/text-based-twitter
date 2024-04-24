package Main.Services;


import Main.Model.Comment;
import Main.Model.Tweet;
import Main.Model.User;
import Main.Twitter;

import java.sql.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
                    "SELECT u.*, " +
                    "(SELECT COUNT(*) FROM follows WHERE follows.following_id = u.id) AS follower_count, " +
                    "(SELECT COUNT(*) FROM follows WHERE follows.follower_id = u.id) AS following_count " +
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
                        rs.getDate("created_at").getTime(),
                        rs.getInt("follower_count"),
                        rs.getInt("following_count")
                );
                users.add(user);
            }
            return users;
        } catch (SQLException e) {
            throw e;
        }
    }

    public List<User> getFollowers(int targetUserId) throws SQLException {
        try (Connection connection = DriverManager.getConnection(Database.DATABASE_URL);
             PreparedStatement statement = connection.prepareStatement("" +
                     "SELECT u.*, " +
                     "(SELECT COUNT(*) FROM follows WHERE follows.following_id = u.id) AS follower_count, " +
                     "(SELECT COUNT(*) FROM follows WHERE follows.follower_id = u.id) AS following_count " +
                     "FROM users AS u " +
                     "INNER JOIN follows AS f ON u.id = f.follower_id " +
                     "WHERE f.following_id = ?")) {
            statement.setInt(1, targetUserId);

            ResultSet rs = statement.executeQuery();
            List<User> followers = new ArrayList<>();
            while (rs.next()) {
                User follower = new User(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("lastname"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role"),
                        rs.getString("bio"),
                        rs.getDate("created_at").getTime(),
                        rs.getInt("follower_count"),
                        rs.getInt("following_count")
                );
                followers.add(follower);
            }
            return followers;
        } catch (SQLException e) {
            throw e;
        }
    }

    public List<User> getFollowings(int targetUserId) throws SQLException {
        try (Connection connection = DriverManager.getConnection(Database.DATABASE_URL);
             PreparedStatement statement = connection.prepareStatement("" +
                     "SELECT u.*, " +
                     "(SELECT COUNT(*) FROM follows WHERE follows.following_id = u.id) AS follower_count, " +
                     "(SELECT COUNT(*) FROM follows WHERE follows.follower_id = u.id) AS following_count " +
                     "FROM users AS u " +
                     "INNER JOIN follows AS f ON u.id = f.following_id " +
                     "WHERE f.follower_id = ?")) {
            statement.setInt(1, targetUserId);

            ResultSet rs = statement.executeQuery();
            List<User> followings = new ArrayList<>();
            while (rs.next()) {
                User following = new User(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("lastname"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role"),
                        rs.getString("bio"),
                        rs.getDate("created_at").getTime(),
                        rs.getInt("follower_count"),
                        rs.getInt("following_count")
                );
                followings.add(following);
            }
            return followings;
        } catch (SQLException e) {
            throw e;
        }
    }

    public List<Tweet> getTweetsOrderedByLikes() throws SQLException {
        List<Tweet> tweets = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(Database.DATABASE_URL)) {
            // Get all tweets
            PreparedStatement stmt1 = connection.prepareStatement("SELECT * FROM tweets " +
                    "INNER JOIN users on users.id = tweets.user_id");
            ResultSet rs1 = stmt1.executeQuery();
            while (rs1.next()) {
                int tweetId = rs1.getInt("id");

                // Count likes for each tweet (assuming a separate likes table)
                PreparedStatement stmt2 = connection.prepareStatement("SELECT COUNT(*) AS likes_count FROM likes WHERE tweet_id = ?");
                stmt2.setInt(1, tweetId);
                ResultSet rs2 = stmt2.executeQuery();

                int likesCount = 0;
                if (rs2.next()) {
                    likesCount = rs2.getInt("likes_count");
                }

                // Create Tweet object with likes count
                tweets.add(new Tweet(
                        tweetId,
                        rs1.getString("tweet_content"),
                        rs1.getTimestamp("created_at").getTime(),
                        rs1.getInt("user_id"),
                        rs1.getString("username"),
                        likesCount
                ));
            }
        }
        return tweets.stream().sorted(Comparator.comparingInt(Tweet::getLikeCounts).reversed()).collect(Collectors.toList());
    }


    public List<Tweet> getFollowingUsersTweets(int currentUserId) throws SQLException {
        List<Tweet> tweets = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(Database.DATABASE_URL)) {
            // Get all users followed by the current user
            PreparedStatement stmt1 = connection.prepareStatement("SELECT following_id FROM follows WHERE follower_id = ?");
            stmt1.setInt(1, currentUserId);
            ResultSet rs1 = stmt1.executeQuery();

            List<Integer> followingUserIds = new ArrayList<>();
            while (rs1.next()) {
                followingUserIds.add(rs1.getInt("following_id"));
            }

            // Check if any users are followed (avoid unnecessary query)
            if (!followingUserIds.isEmpty()) {
                // Construct a query string to fetch tweets from following users (using IN clause)
                StringBuilder tweetQuery = new StringBuilder("SELECT * FROM tweets " +
                        "INNER JOIN users on users.id = tweets.user_id " +
                        "WHERE user_id IN (");
                for (int i = 0; i < followingUserIds.size(); i++) {
                    tweetQuery.append("?");
                    if (i != followingUserIds.size() - 1) {
                        tweetQuery.append(",");
                    }
                }
                tweetQuery.append(")");
                tweetQuery.append("ORDER BY timestamp DESC");

                // Prepare and execute the tweet query
                PreparedStatement stmt2 = connection.prepareStatement(tweetQuery.toString());
                for (int i = 0; i < followingUserIds.size(); i++) {
                    stmt2.setInt(i + 1, followingUserIds.get(i));
                }
                ResultSet rs2 = stmt2.executeQuery();

                while (rs2.next()) {
                    tweets.add(new Tweet(
                            rs2.getInt("id"),
                            rs2.getString("tweet_content"),
                            rs2.getTimestamp("timestamp").getTime(),
                            rs2.getInt("user_id")
                    ));
                    tweets.get(tweets.size() - 1).setUsername(rs2.getString("username"));
                }
            }
        }
        return tweets;
    }


}
