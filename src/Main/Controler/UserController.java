package Main.Controler;

import Main.Model.User;
import Main.Services.Database;

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
}
