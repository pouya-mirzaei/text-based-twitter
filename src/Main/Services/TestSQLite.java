package Main.Services;

import Main.Model.User;

import java.sql.*;

public class TestSQLite {

    public static String DATABASE_URL = "jdbc:sqlite:twitter.db";

    public static void main(String[] args) {
        initDatabase();


    }

    public static void initDatabase() {
        try {
            Connection connection = DriverManager.getConnection(DATABASE_URL);
            Statement stmn = connection.createStatement();

            String sql = "CREATE TABLE IF NOT EXISTS users(" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT NOT NULL," +
                    "lastname TEXT NOT NULL," +
                    "username TEXT UNIQUE NOT NULL," +
                    "password TEXT NOT NULL," +
                    "role NOT NULL DEFAULT 'USER'," +
                    "bio TEXT," +
                    "created_at DATETIME DEFAULT CURRENT_TIMESTAMP" +
                    ")";
            stmn.execute(sql);


            connection.close();
            System.out.println("database initiated successfully");
        } catch (Exception e) {
            System.out.println("error : " + e);
        }
    }

    public static void createUser(String name, String lastname, String username, String password, String bio) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL);
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

    public static void createAdmin(String name, String lastname, String username, String password, String bio) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL);
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

    public static User getUser(int id) throws SQLException {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL);
             PreparedStatement stmt = connection.prepareStatement("SELECT * FROM users WHERE id = ?")) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("lastname"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role"),
                        rs.getString("bio"),
                        new Date(rs.getDate("created_at").getTime()).getTime()
                );
            }
            return null;
        }
    }


}
