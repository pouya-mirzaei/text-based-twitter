package Main.Model;

import Main.Services.Authentication;
import Main.Services.Database;
import Main.Services.PasswordHasher;
import Main.Twitter;
import Main.View.Colors;

import javax.xml.crypto.Data;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public class User {
    private String id;
    private String name;
    private String lastName;
    private String username;
    private String password;
    private String role;
    private String bio;
    private final long createAt;

    public User(String name, String lastName, String username, String password, String bio) {
        this.id = String.valueOf(UUID.randomUUID());
        this.name = name;
        this.lastName = lastName;
        this.username = username;
        this.password = PasswordHasher.hashPassword(password);
        this.bio = bio;
        this.role = "USER";
        this.createAt = new Date().getTime();
    }

    public User(String id, String name, String lastName, String username, String password, String role, String bio, long createAt) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.role = role;
        this.bio = bio;
        this.createAt = createAt;
    }

    // Getters and setters
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void changeName() throws Exception {
        Twitter.tw.type("Enter your new name =>");
        String name = Twitter.scanner.nextLine();

        // last name
        Twitter.tw.type("Enter your new last name =>");
        String lastName = Twitter.scanner.nextLine();
        this.name = name;
        this.lastName = lastName;

        try {
            Twitter.db.updateUsersDb(Database.users);
        } catch (Exception e) {
            throw new Exception("There was an error while changing your name :(");
        }
    }

    public String getLastName() {
        return lastName;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getBio() {
        return bio;
    }

    public void changeBio() throws Exception {
        String bio;
        do {
            Twitter.tw.type("Please write a biography about yourself. Keep it within 200 characters.\" =>");
            bio = Twitter.scanner.nextLine();
        } while (bio.length() > 200);

        this.bio = bio;

        try {
            Twitter.db.updateUsersDb(Database.users);
        } catch (Exception e) {
            throw new Exception("There was an error while changing your bio :(");
        }
    }

    public long getCreateAt() {
        return createAt;
    }

    public void changeUsername() throws Exception {
        String username;
        boolean flag;
        do {
            // Prompt the user for a username
            Twitter.tw.type("Create a username for yourself => ");
            username = Twitter.scanner.nextLine();

            flag = Twitter.userController.findUser(username) != null;

            if (flag) {
                do {
                    Twitter.tw.typeWithColor("The username \"" + username + "\" is already in use. Please try a different username.", Colors.RED, true);
                    username = Twitter.scanner.nextLine();
                    flag = Twitter.userController.findUser(username) != null;

                } while (flag);
            }

            // Validate the username format using regex
            if (!Authentication.isInputValid(username, "^[a-zA-Z0-9_-]{3,16}$")) {
                Twitter.tw.typeWithColor("Invalid username format.\nUsername must contain only alphanumeric characters, underscores, or hyphens, and be between 3 and 16 characters long.", Colors.RED, true);
            }
        } while (!Authentication.isInputValid(username, "^[a-zA-Z0-9_-]{3,16}$"));

        this.username = username;

        try {
            Twitter.db.updateUsersDb(Database.users);
        } catch (Exception e) {
            throw new Exception("There was an error while changing your username :(");
        }
    }

    public void changePassword() throws Exception {
        String password;
        String confirmPassword;
        boolean condition = true;
        do {
            Twitter.tw.type("Create a password for your account (Your password cannot contains spaces)=>");
            password = Twitter.scanner.nextLine();

            if (!Authentication.isInputValid(password, "^(?=.*\\d)(?=.*[a-zA-Z]).{6,}$")) {
                Twitter.tw.typeWithColor("Your password is weak and easy to guess. Please choose a stronger one.", Colors.RED, true);
                Twitter.tw.typeWithColor("  Your password must contain at least 6 characters or more.", Colors.YELLOW, true);
                Twitter.tw.typeWithColor("  And it must also include both uppercase and lowercase letters and at least one number for added security.", Colors.YELLOW, true);
                continue;
            }

            Twitter.tw.type("Confirm your password =>");
            confirmPassword = Twitter.scanner.nextLine();

            condition = !Objects.equals(password, confirmPassword) || password.contains(" ");
            if (condition) {
                Twitter.tw.typeWithColor("Your password didn't match, try again ...", Colors.RED, true);
            }

        } while (condition);

        this.password = PasswordHasher.hashPassword(password);

        try {
            Twitter.db.updateUsersDb(Database.users);
        } catch (Exception e) {
            throw new Exception("There was an error while changing your password :(");
        }

    }

    private void showUserInformation() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE");
        String day = dateFormat.format(new Date(createAt));

        dateFormat = new SimpleDateFormat("MMMM");
        String month = dateFormat.format(new Date(createAt));

        dateFormat = new SimpleDateFormat("YYYY");
        String year = dateFormat.format(new Date(createAt));

        Twitter.tw.typeWithColor("Name : " + name + " " + lastName, Colors.BLUE, true);
        Twitter.tw.typeWithColor("Username : @" + username, Colors.BLUE, true);
        Twitter.tw.typeWithColor("Bio : " + bio, Colors.BLUE, true);
        Twitter.tw.typeWithColor("Joined : " + new Date(createAt), Colors.BLUE, true);

    }


    public void profileSettings() {
        showUserInformation();

        String[] menuItems = {"Edit profile", "Back"};

        int userInput = 0;

        boolean condition;
        do {
            Twitter.menu.displayMenu(menuItems, "Select an option to continue");

            userInput = Twitter.getIntegerInput();

            condition = userInput < 1 || userInput > menuItems.length;

            if (condition) {
                Twitter.tw.typeWithColor("Wrong choice, try again ...", Colors.RED, false);
                try {
                    Thread.sleep(1500);
                } catch (Exception e) {

                }
            }
        } while (condition);

        switch (userInput) {
            case 1:
                editProfile();
                break;
            case 2:
                Twitter.run();
        }
    }

    private void editProfile() {
        String[] menuItems = {"Change your name", "Change username", "Change password", "Change bio", "Back"};

        int userInput = 0;

        boolean condition;
        do {
            Twitter.menu.displayMenu(menuItems, "Which one do you want to edit ?");

            userInput = Twitter.getIntegerInput();

            condition = userInput < 1 || userInput > menuItems.length;

            if (condition) {
                Twitter.tw.typeWithColor("Wrong choice, try again ...", Colors.RED, false);
                try {
                    Thread.sleep(1500);
                } catch (Exception e) {

                }
            }
        } while (condition);

        Twitter.scanner.nextLine();

        try {
            switch (userInput) {
                case 1:
                    changeName();
                    break;
                case 2:
                    changeUsername();
                    break;
                case 3:
                    changePassword();
                    break;
                case 4:
                    changeBio();
                case 5:
                    Twitter.run();
                    return;
            }
            Twitter.tw.typeWithColor("You edited your profile successfully :)", Colors.GREEN, true);
            Twitter.tw.typeWithColor("Press any key to continue", Colors.CYAN, true);
            Twitter.scanner.nextLine();
            Authentication.currentUserData.profileSettings();

        } catch (Exception e) {
            Twitter.tw.typeWithColor(e.getMessage(), Colors.RED, true);
        }
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                ", bio='" + bio + '\'' +
                ", createAt=" + createAt +
                '}';
    }
}
