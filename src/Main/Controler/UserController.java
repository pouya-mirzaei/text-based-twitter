package Main.Controler;

import Main.Model.User;
import Main.Services.Authentication;
import Main.Services.Database;
import Main.Services.PasswordHasher;
import Main.Twitter;

import java.util.List;
import java.util.NoSuchElementException;
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

        } catch (IllegalAccessException e) {
            return false;
        }
    }


    public void login(String username, String password) {
        User mainUser = findUser(username);
        if (mainUser == null)
            throw new NoSuchElementException("Username '" + username + "' not found.");

        if (PasswordHasher.checkPassword(password, mainUser.getPassword())) {
            Authentication.currentUserId = mainUser.getId();
            Authentication.currentUserData = mainUser;
            return;
        }

        throw new SecurityException("Incorrect username or password.");
    }
}
