package Main.View;

import Main.Controller.CommentController;
import Main.Controller.TweetController;
import Main.Model.Comment;
import Main.Model.Tweet;
import Main.Model.User;
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
    private final UserPage userPage = Twitter.userPage;
    private final Typewriter tw = Twitter.tw;
    private final Scanner sc = Twitter.scanner;
    private static int commentCounter = 0;

    public void showPage(Tweet t) throws SQLException {

        tw.typeWithColor("***************************************************************************", Colors.BLUE, true);

        tw.typeWithColor("1. ", Colors.PURPLE, false);
        tw.typeWithColor("<= (Enter 1 to return )", Colors.BLUE, true);
        System.out.println();
        System.out.println();

        // constant tweet data (username , content and likes) :

        //first choice : user page
        tw.typeWithColor("2. ", Colors.PURPLE, false);
        tw.typeWithColor("@" + t.getUsername(), Colors.BLUE, false);
        tw.typeWithColor("\t\t" + new Timestamp(t.getCreatedAt()), Colors.WHITE, true);

        tw.typeWithColor("   Content : ", Colors.YELLOW, false);
        tw.type(Typewriter.splitLongString(t.getContent(), 100));

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


        commentCounter = 0;
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
                handleAuthorPage(t);
                break;
            case 3:
                handleLike(t);
                break;
            case 4:
                handleUserLiked(t);
                break;
            case 5:
                handleCommenting(t);
                break;
            default:
                if (choice < commentCounter + 6) {
                    handleReply(t, getCommentToReply(comments, choice));
                } else {
                    tw.typeWithColor("Wrong choice!", Colors.RED, true);
                    tw.typeWithColor("Press any key to try again!", Colors.RED, true);
                    sc.nextLine();
                    showPage(t);
                }
        }

    }

    private void handleAuthorPage(Tweet t) {
        userPage.showPage(Twitter.userController.getUserById(t.getUserId()));
    }

    private void handleUserLiked(Tweet t) throws SQLException {
        tw.typeWithColor("The users who liked this tweet : ", Colors.BLUE, true);
        List<User> users = tweetsManagerDb.getUsersWhoLikedTweet(t.getId());
        int userCounter = 1;
        for (User user : users) {
            tw.typeWithColor("  " + userCounter++ + ". ", Colors.PURPLE, false);
            tw.typeWithColor("@" + user.getUsername(), Colors.YELLOW, true);
        }
        tw.typeWithColor("Select your choice :", Colors.WHITE, true);
        int choice = Twitter.getIntegerInput();
        sc.nextLine();

        if (choice > 0 && choice <= users.size()) {
            userPage.showPage(users.get(choice - 1));
        } else {
            tw.typeWithColor("Wrong choice!", Colors.RED, true);
            tw.typeWithColor("Press any key to try again!", Colors.RED, true);
            sc.nextLine();
            showPage(t);
        }


    }

    private void handleReply(Tweet t, Comment commentToReply) throws SQLException {
        tw.typeWithColor("You are replying to " + commentToReply.getUsername() + "'s comment", Colors.PURPLE, true);
        tw.typeWithColor("Type your comment :", Colors.WHITE, true);
        String comment = sc.nextLine();
        tweetsManagerDb.replyTo(t, commentToReply, comment);
        showPage(t);
    }

    private Comment getCommentToReply(List<Comment> allComments, int num) throws SQLException {
        num -= 5;
        for (Comment comment : allComments) {
            if (comment.getDisplayingId() == num) return comment;

            List<Comment> replies = comment.getReplies();
            for (Comment reply : replies) {
                if (reply.getDisplayingId() == num) return reply;
            }
        }
        throw new SQLException("Comment didn't found !");
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
                System.out.print("\t");
            }
            // Display comment details
            tw.typeWithColor(commentCounter++ + 6 + ". ", Colors.PURPLE, false);
            tw.typeWithColor("@" + comment.getUsername() + " : ", Colors.YELLOW, false);
            comment.setDisplayingId(commentCounter);

            tw.type(Typewriter.splitLongString(comment.getCommentContent(), 100));

            // Display nested replies recursively
            displayComments(comment.getReplies(), depth + 1);
        }

    }
}
