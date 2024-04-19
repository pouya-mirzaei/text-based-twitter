package Main.Controller;

import Main.Model.Comment;
import Main.Services.Database;

import java.sql.Connection;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommentController {

    private List<Comment> getAllCommentsFor(int postId) throws SQLException {
        List<Comment> comments = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(Database.DATABASE_URL);
             PreparedStatement stmt = connection.prepareStatement("SELECT * FROM comments " +
                     "INNER JOIN users ON users.id = comments.user_id " +
                     "WHERE tweet_id = ? " +
                     "ORDER BY timestamp ASC")) {
            stmt.setInt(1, postId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                comments.add(new Comment(
                        rs.getInt("id"),
                        rs.getInt("tweet_id"),
                        rs.getInt("user_id"),
                        rs.getString("comment_content"),
                        rs.getDate("timestamp").getTime(),
                        rs.getInt("parent_comment_id"),
                        rs.getString("username")
                ));
            }
        }
        return comments;
    }


    public List<Comment> fetchCommentsWithReplies(int postId) throws SQLException {
        List<Comment> comments = getAllCommentsFor(postId); // Fetch comments from the database

        // Create a map to group comments by their parent comment IDs
        Map<Integer, List<Comment>> parentCommentMap = new HashMap<>();
        for (Comment comment : comments) {
            int parentCommentId = comment.getParentCommentId();
            if (!parentCommentMap.containsKey(parentCommentId)) {
                parentCommentMap.put(parentCommentId, new ArrayList<>());
            }
            parentCommentMap.get(parentCommentId).add(comment);
        }

        // Attach replies to each comment
        for (Comment comment : comments) {
            int commentId = comment.getId();
            List<Comment> replies = parentCommentMap.getOrDefault(commentId, new ArrayList<>());
            comment.setReplies(replies);
        }

        // Return the comments with replies
        return comments;
    }
}
