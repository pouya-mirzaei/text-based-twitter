package Main.Controller;

import Main.Model.User;
import Main.Services.Authentication;
import Main.Services.Database;
import Main.Services.PasswordHasher;
import Main.Twitter;
import Main.View.Colors;

import java.sql.*;
import java.util.List;
import java.util.NoSuchElementException;

public class UserController {


    // READ
    public User getUser(String username) {
        try (Connection connection = DriverManager.getConnection(Database.DATABASE_URL);
             PreparedStatement stmt = connection.prepareStatement("" +
                     "SELECT u.*, " +
                     "(SELECT COUNT(*) FROM follows WHERE follows.following_id = u.id) AS follower_count, " +
                     "(SELECT COUNT(*) FROM follows WHERE follows.follower_id = u.id) AS following_count " +
                     "FROM users u " +
                     "WHERE username = ?")) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("lastname"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role"),
                        rs.getString("bio"),
                        rs.getDate("created_at").getTime(),
                        rs.getInt("follower_count"),
                        rs.getInt("following_count"));
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public User getUserById(int id) {
        try (Connection connection = DriverManager.getConnection(Database.DATABASE_URL);
             PreparedStatement stmt = connection.prepareStatement("" +
                     "SELECT u.*, " +
                     "(SELECT COUNT(*) FROM follows WHERE follows.following_id = u.id) AS follower_count, " +
                     "(SELECT COUNT(*) FROM follows WHERE follows.follower_id = u.id) AS following_count " +
                     "FROM users u " +
                     "WHERE id = ?")) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(
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
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    //CREATE
    public boolean signup(String name, String lastName, String username, String password, String bio) {

        try {
            createUser(name, lastName, username, password, bio);
            return true;

        } catch (SQLException e) {
            return false;
        }
    }


    public void login(String username, String password) {
        User mainUser = getUser(username);
        if (mainUser == null)
            throw new NoSuchElementException("Username '" + username + "' not found.");

        if (PasswordHasher.checkPassword(password, mainUser.getPassword())) {
            Authentication.currentUserId = mainUser.getId();
            Authentication.currentUserData = mainUser;
            return;
        }

        throw new SecurityException("Incorrect username or password.");
    }

    public void logout() {
        Authentication.currentUserId = 0;
        Authentication.currentUserData = null;
    }


    public void createUser(String name, String lastname, String username, String password, String bio) throws SQLException {
        try (Connection connection = DriverManager.getConnection(Database.DATABASE_URL);
             PreparedStatement stmt = connection.prepareStatement("INSERT INTO users (name, lastname, username, password, bio) VALUES (?, ?, ?, ?, ?)")) {
            stmt.setString(1, name);
            stmt.setString(2, lastname);
            stmt.setString(3, username);
            stmt.setString(4, PasswordHasher.hashPassword(password));
            stmt.setString(5, bio);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }

    public void createAdmin(String name, String lastname, String username, String password, String bio) throws SQLException {
        try (Connection connection = DriverManager.getConnection(Database.DATABASE_URL);
             PreparedStatement stmt = connection.prepareStatement("INSERT INTO users (name, lastname, username, password, bio, role) VALUES (?, ?, ?, ?, ?, ?)")) {
            stmt.setString(1, name);
            stmt.setString(2, lastname);
            stmt.setString(3, username);
            stmt.setString(4, PasswordHasher.hashPassword(password));
            stmt.setString(5, bio);
            stmt.setString(6, "ADMIN");
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException(e);
        }
    }

    public void followUser(User user) throws SQLException {
        if (user.getId() == Authentication.currentUserId) {
            throw new SQLException("You can not follow yourself!");
        }

        try (Connection connection = DriverManager.getConnection(Database.DATABASE_URL);
             PreparedStatement stmt = connection.prepareStatement("" +
                     "INSERT into follows (follower_id,following_id) " +
                     "VALUES (?,?)")) {
            stmt.setInt(1, Authentication.currentUserId);
            stmt.setInt(2, user.getId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw e;
        }
    }

    public boolean isFollowing(int targetUserId) throws SQLException {
        try (Connection connection = DriverManager.getConnection(Database.DATABASE_URL);
             PreparedStatement statement = connection.prepareStatement("" +
                     "SELECT COUNT(*) > 0 AS is_following " +
                     "FROM follows " +
                     "WHERE follower_id = ? AND following_id = ?")) {
            statement.setInt(1, Authentication.currentUserId);
            statement.setInt(2, targetUserId);

            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                return rs.getBoolean("is_following");
            } else {
                return false; // No results, user is not following
            }
        } catch (SQLException e) {
            throw e;
        }
    }


    //UPDATE
    public void editUser(String username, User newUserData) throws SQLException {
        try (Connection connection = DriverManager.getConnection(Database.DATABASE_URL);
             PreparedStatement stmt = connection.prepareStatement("UPDATE users SET bio = ?, name = ?, lastname = ?, password = ? WHERE username = ?")) {
            stmt.setString(1, newUserData.getBio());
            stmt.setString(2, newUserData.getName());
            stmt.setString(3, newUserData.getLastName());
            stmt.setString(4, newUserData.getPassword());
            stmt.setString(5, username);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw e;
        }
    }


    //DELETE
    public void deleteUser(String username) throws IllegalAccessException {
    }

    public void unfollowUser(User user) throws SQLException {

        try (Connection connection = DriverManager.getConnection(Database.DATABASE_URL);
             PreparedStatement statement = connection.prepareStatement("" +
                     "DELETE FROM follows " +
                     "WHERE follower_id = ? AND following_id = ?")) {

            statement.setInt(1, Authentication.currentUserId);
            statement.setInt(2, user.getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw e;
        }
    }


    public void printAListOfUsers(List<User> users) {
        int userCounter = 1;
        for (User user : users) {
            Twitter.tw.typeWithColor("  " + userCounter++ + ". ", Colors.PURPLE, false);
            Twitter.tw.typeWithColor("@" + user.getUsername(), Colors.YELLOW, true);
        }
    }


}
