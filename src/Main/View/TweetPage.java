package Main.View;

import Main.Controller.CommentController;
import Main.Controller.TweetController;
import Main.Model.Comment;
import Main.Model.Tweet;
import Main.Services.Authentication;
import Main.Services.TweetsManagerDb;
import Main.Twitter;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

public class TweetPage {
    private final TweetController tweetController = Twitter.tweetController;
    private final TweetsManagerDb tweetsManagerDb = Twitter.tweetsManagerDb;
    private final CommentController commentController = Twitter.commentController;
    private final Typewriter tw = Twitter.tw;
    private final Scanner sc = Twitter.scanner;

    public void showPage(Tweet t) throws SQLException {

        tw.typeWithColor("***************************************************************************", Colors.BLUE, true);

        tw.typeWithColor("1. ", Colors.PURPLE, false);
        tw.typeWithColor("<= (Enter 1 to return )", Colors.BLUE, true);
        System.out.println();
        System.out.println();

        // constant tweet data (username , content and likes) :

        //first choice : user page
        tw.typeWithColor("2. ", Colors.PURPLE, false);
        Twitter.tw.typeWithColor("@" + t.getUsername(), Colors.BLUE, false);
        Twitter.tw.typeWithColor("\t\t" + new Timestamp(t.getCreatedAt()), Colors.WHITE, true);

        Twitter.tw.typeWithColor("   Content : ", Colors.YELLOW, false);
        Twitter.tw.type(Typewriter.splitLongString(t.getContent(), 100));

        // second choice : likes
        tw.typeWithColor("3. ", Colors.PURPLE, false);
        if (tweetsManagerDb.isUserAlreadyLiked(Authentication.currentUserId, t.getId())) {
            tw.typeWithColor("♥", Colors.RED, false);
        } else {
            tw.typeWithColor("♥", Colors.RESET, false);
        }
        System.out.print(" (" + tweetsManagerDb.countLikes(t.getId()) + ")");

        tw.typeWithColor("\t4. ", Colors.PURPLE, false);
        System.out.print("[Users that liked this tweet]");

        tw.typeWithColor("\t5. ", Colors.PURPLE, false);
        System.out.println("Comment on this post :");


        // comments :
        System.out.println();
        tw.typeWithColor("Comments section : ", Colors.YELLOW, true);

        List<Comment> comments = commentController.fetchCommentsWithReplies(t.getId());
        displayComments(comments, 0);


        tw.typeWithColor("Select your choice :", Colors.WHITE, true);
        int choice = Twitter.getIntegerInput();
        sc.nextLine(); // clearing scanner

        switch (choice) {
            case 1:
                Twitter.run();
                return;
            case 2:
                //user page
                break;
            case 3:
                handleLike(t);
                break;
            case 4:
                showLikedUsers(t);
                break;
            case 5:
                handleCommenting(t);
                break;
            default:
                showPage(t);
        }

    }

    private void handleLike(Tweet t) throws SQLException {
        if (tweetsManagerDb.isUserAlreadyLiked(Authentication.currentUserId, t.getId())) {
            if (tweetsManagerDb.unlikeTweet(t.getId())) {
                tw.typeWithColor("Unliked !", Colors.GREEN, true);
                showPage(t);
            } else {
                throw new SQLException("There was an error while unliking the tweet");
            }
        } else {
            if (tweetsManagerDb.likeTweet(t.getId())) {
                tw.typeWithColor("Liked !", Colors.GREEN, true);
                showPage(t);
            } else {
                throw new SQLException("There was an error while liking the tweet");
            }
        }
    }


    private void showLikedUsers(Tweet t) {
        System.out.println("handling ... ");
        Twitter.scanner.nextLine();
        Twitter.run();
    }

    private void handleCommenting(Tweet t) throws SQLException {
        tw.typeWithColor("Write your comment and then press enter", Colors.BLUE, true);
        String comment = sc.nextLine();
        try {
            tweetsManagerDb.commentTo(t, comment);
            tw.typeWithColor("Your comment had been submitted!", Colors.GREEN, true);
            tw.typeWithColor("Press any key to continue...", Colors.BLUE, true);
            sc.nextLine();
            showPage(t);
        } catch (SQLException e) {
            throw e;
        }
    }

    public void displayComments(List<Comment> comments, int depth) {
        if (comments == null || comments.isEmpty()) {
            return;
        }

        for (Comment comment : comments) {
            if (depth == 0) {
                if (comment.getParentCommentId() != 0) {
                    continue;
                }
            }
            // Indentation based on depth for better visualization
            for (int i = 0; i < depth; i++) {
                System.out.print("  ");
            }
            // Display comment details
            System.out.println("User: " + comment.getUsername() + ", Comment: " + comment.getCommentContent());

            // Display nested replies recursively
            displayComments(comment.getReplies(), depth + 1);
        }

    }
}
