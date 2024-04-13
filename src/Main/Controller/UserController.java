package Main.Controller;

import Main.Model.User;
import Main.Services.Authentication;
import Main.Services.Database;
import Main.Services.PasswordHasher;
import Main.Twitter;

import java.sql.*;
import java.util.NoSuchElementException;

public class UserController {


    // READ
    public User getUser(String username) {
        try (Connection connection = DriverManager.getConnection(Database.DATABASE_URL);
             PreparedStatement stmt = connection.prepareStatement("SELECT * FROM users WHERE username = ?")) {
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
                        rs.getDate("created_at").getTime()
                );
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public User getUserById(int id) {
        try (Connection connection = DriverManager.getConnection(Database.DATABASE_URL);
             PreparedStatement stmt = connection.prepareStatement("SELECT * FROM users WHERE id = ?")) {
            stmt.setInt(1, id); // Set username parameter securely
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
                        rs.getDate("created_at").getTime()
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
            try {
                Twitter.tweetController.getCurrentUserTweets();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
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

}
