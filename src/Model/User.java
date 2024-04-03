package Model;

import java.util.Date;

public class User {
    private String id;
    private String name;
    private String lastName;
    private final String username;
    private final String password;
    private String role;
    private String bio;
    private final long createAt;

    public User(String id, String name, String lastName, String username, String password, String bio) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
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

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public void setBio(String bio) {
        this.bio = bio;
    }

    public long getCreateAt() {
        return createAt;
    }

}
