package Main.Services;

import Main.Model.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Database {

    public static List<User> users;

    public Database() {
        // initiate the database
        users = getUsersFromDb();
    }

    public List<User> getUsersFromDb() {

        List<User> users = new ArrayList<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader("users.txt"));
            String line;

            while ((line = reader.readLine()) != null) {
                String[] userData = line.split("::");

                User user = new User(userData[0], userData[1], userData[2], userData[3], userData[4], userData[5], userData[6], Long.parseLong(userData[7]));

                users.add(user);
            }
            reader.close();
            return users;

        } catch (IOException e) {
            users = new ArrayList<>();
            User mainUser = new User("pouya", "mizaei", "admin", "admin", "admin", "The official account of the admin");
            mainUser.setRole("ADMIN");
            users.add(mainUser);


            updateUsersDb(users);
            return users;
        }

    }

    private void updateUsersDb(List<User> users) {
        BufferedWriter writer;

        try {
            writer = new BufferedWriter(new FileWriter("users.txt"));

            for (User user : users) {
                if (user == null) continue;

                writer.write(user.getId() + "::");
                writer.write(user.getName() + "::");
                writer.write(user.getLastName() + "::");
                writer.write(user.getUsername() + "::");
                writer.write(user.getPassword() + "::");
                writer.write(user.getRole() + "::");
                writer.write(user.getBio() + "::");
                writer.write(user.getCreateAt() + "\n");
            }
            writer.close();

        } catch (IOException e) {
            // Do Nothing
        }


    }
}
