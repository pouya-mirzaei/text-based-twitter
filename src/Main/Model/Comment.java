package Main.Model;

import Main.Services.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Comment {

    private int id;
    private int tweetId;
    private int userId;
    private String commentContent;
    private long createdAt;
    private int parentCommentId;
    private String username;
    private List<Comment> replies;
    private int displayingId;

    public Comment(int id, int tweetId, int userId, String commentContent, long createdAt, int parentCommentId) {
        this.id = id;
        this.tweetId = tweetId;
        this.userId = userId;
        this.commentContent = commentContent;
        this.createdAt = createdAt;
        this.parentCommentId = parentCommentId;

    }

    public Comment(int id, int postId, int userId, String commentContent, long createdAt, int parentCommentId, String username) {
        this(id, postId, userId, commentContent, createdAt, parentCommentId);
        this.username = username;

    }

    // Getters and setters (omitted for brevity)

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTweetId() {
        return tweetId;
    }

    public void setTweetId(int tweetId) {
        this.tweetId = tweetId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public int getParentCommentId() {
        return parentCommentId;
    }

    public void setParentCommentId(int parentCommentId) {
        this.parentCommentId = parentCommentId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Comment> getReplies() {
        return replies;
    }

    public void setReplies(List<Comment> replies) {
        this.replies = replies;
    }

    public int getDisplayingId() {
        return displayingId;
    }

    public void setDisplayingId(int displayingId) {
        this.displayingId = displayingId;
    }
}
