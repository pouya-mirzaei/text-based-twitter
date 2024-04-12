package Main.Model;

import Main.Services.Authentication;
import Main.Services.PasswordHasher;
import Main.Twitter;
import Main.View.Colors;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class User {
    private int id;
    private String name;
    private String lastName;
    private String username;
    private String password;
    private String role;
    private String bio;
    private final long createAt;

    public User(String name, String lastName, String username, String password, String bio) {
        this.id = 0; // this will be changed in the db
        this.name = name;
        this.lastName = lastName;
        this.username = username;
        this.password = PasswordHasher.hashPassword(password);
        this.bio = bio;
        this.role = "USER";
        this.createAt = new Date().getTime();
    }

    public User(int id, String name, String lastName, String username, String password, String role, String bio, long createAt) {
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
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
            Twitter.userController.editUser(username, this);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
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
            Twitter.userController.editUser(username, this);

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public long getCreateAt() {
        return createAt;
    }


    public void changePassword() throws Exception {
        String password;
        String confirmPassword;
        boolean condition = true;
        do {
            Twitter.tw.type("Create a new password for your account (Your password cannot contains spaces)=>");
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
            Twitter.userController.editUser(username, this);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

    }

    private void showUserInformation() {

        Timestamp time = new Timestamp(new Date(this.createAt).getTime());

        Twitter.tw.typeWithColor("Name : " + name + " " + lastName, Colors.BLUE, true);
        Twitter.tw.typeWithColor("Username : @" + username, Colors.BLUE, true);
        Twitter.tw.typeWithColor("Bio : " + bio, Colors.BLUE, true);
        Twitter.tw.typeWithColor("Joined : " + time, Colors.BLUE, true);

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
        String[] menuItems = {"Change your name", "Change password", "Change bio", "Back"};

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
                    changePassword();
                    break;
                case 3:
                    changeBio();
                case 4:
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

    public void tweet() {
        try {
            Twitter.tweetController.tweet(this.getId());
        } catch (SQLException e) {
            Twitter.tw.typeWithColor("Error : " + e.getMessage(), Colors.RED, true);
            Twitter.tw.typeWithColor("Press any key to go back to the main menu " + e.getMessage(), Colors.RED, true);
            Twitter.scanner.nextLine();
            Twitter.run();
        }
    }
}
