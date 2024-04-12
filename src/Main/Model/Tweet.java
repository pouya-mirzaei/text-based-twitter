package Main.Model;

import Main.Model.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Tweet {

    private int id;
    private String content;
    private long createdAt;
    private int userId;
    private List<User> liked;
    private List<User> retweets;

    // Getters and Setters


    public Tweet(int userId, String content) {
        this.id = 0; // the main id will be created at database
        this.content = content;
        this.userId = userId;
        this.createdAt = new Date().getTime();
        this.liked = new ArrayList<>();
        this.retweets = new ArrayList<>();
    }

    public Tweet(int id, String content, long createdAt, int userId) {
        this.id = id;
        this.content = content;
        this.createdAt = createdAt;
        this.userId = userId;
        this.liked = new ArrayList<>();
        this.retweets = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public List<User> getLiked() {
        return liked;
    }

    public void setLiked(List<User> liked) {
        this.liked = liked;
    }

    public List<User> getRetweets() {
        return retweets;
    }

    public void setRetweets(List<User> retweets) {
        this.retweets = retweets;
    }
}
