package Main.Services;

import Main.Model.User;
import Main.Twitter;

import java.sql.*;

public class Database {

    public static String DATABASE_URL = "jdbc:sqlite:twitter.db";


    public Database() {
        initDatabase();
    }

    public void initDatabase() {


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

            stmn.executeUpdate("CREATE TABLE IF NOT EXISTS tweets (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "user_id INTEGER NOT NULL," +
                    "tweet_content TEXT NOT NULL," +
                    "timestamp DATETIME DEFAULT CURRENT_TIMESTAMP," +
                    "FOREIGN KEY (user_id) REFERENCES users(id)" +
                    ")");

            stmn.executeUpdate("CREATE TABLE IF NOT EXISTS likes (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "tweet_id INTEGER NOT NULL," +
                    "user_id INTEGER NOT NULL," +
                    "FOREIGN KEY (tweet_id) REFERENCES tweets(id)," +
                    "FOREIGN KEY (user_id) REFERENCES users(id)" +
                    ")");

            stmn.executeUpdate("CREATE TABLE IF NOT EXISTS comments (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "tweet_id INTEGER NOT NULL," +
                    "user_id INTEGER NOT NULL," +
                    "parent_comment_id INTEGER," +
                    "comment_content TEXT NOT NULL," +
                    "timestamp DATETIME DEFAULT CURRENT_TIMESTAMP," +
                    "FOREIGN KEY (tweet_id) REFERENCES tweets(id)," +
                    "FOREIGN KEY (user_id) REFERENCES users(id)," +
                    "FOREIGN KEY (parent_comment_id) REFERENCES comments(id)" +
                    ")");


            connection.close();
        } catch (Exception e) {
            System.out.println("error : " + e.getMessage());
        }

        try {
            Twitter.userController.createAdmin("Shayan", "Chaleshi", "admin", "admin", "I'm a bitch");
        } catch (SQLException e) {
            //
        }
    }


}
