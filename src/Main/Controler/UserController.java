package Main.Controler;

import Main.Model.User;
import Main.Services.Database;
import Main.Twitter;

import java.util.List;
import java.util.Objects;

public class UserController {
    public User findUser(String username) {
        List<User> users = Database.users;
        for (User user : users) {
            if (Objects.equals(user.getUsername(), username)) {
                return user;
            }
        }
        return null;
    }

    public boolean signup(String name, String lastName, String username, String password, String bio) {

        try {
            User newUser = new User(name, lastName, username, password, bio);
            Database.users.add(newUser);
            Twitter.db.updateUsersDb(Database.users);
            return true;

        } catch (Exception e) {
            return false;
        }
    }


}
