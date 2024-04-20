package Main.View;

import Main.Controller.TweetController;
import Main.Controller.UserController;
import Main.Model.Tweet;
import Main.Model.User;
import Main.Services.Authentication;
import Main.Services.TweetsManagerDb;
import Main.Twitter;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class UserPage {
    private final Typewriter tw = Twitter.tw;
    private final Scanner sc = Twitter.scanner;
    private final TweetController tweetController = Twitter.tweetController;
    private final UserController userController = Twitter.userController;
    private final TweetsManagerDb tweetsManagerDb = Twitter.tweetsManagerDb;
    private final int AUTHOR = 1;
    private final int NOT_FOLLOWED_PAGE = 2;
    private final int FOLLOWED_PAGE = 3;
    private int pageStatus; // 1: author , 2: not followed, 3: is following

    public void showPage(User user) throws SQLException {
        setPageStatus(user);
        tw.typeWithColor("***************************************************************************", Colors.BLUE, true);

        tw.typeWithColor("User page : ", Colors.BLUE, true);


        tw.typeWithColor("1. ", Colors.PURPLE, false);
        tw.typeWithColor("<= (Enter 1 to return )", Colors.BLUE, true);

        // user information
        tw.typeWithColor(user.getName(), Colors.GREEN, true);
        tw.typeWithColor("@" + user.getUsername(), Colors.WHITE, false);
        tw.typeWithColor("\t\t\t\t\t\t\t\t\t\t2. ", Colors.PURPLE, false);
        switch (pageStatus) {
            case 1:
                tw.typeWithColor("Edit profile", Colors.RESET, true);
                break;
            case 2:
                tw.typeWithColor("Follow", Colors.RESET, true);
                break;
            case 3:
                tw.typeWithColor("Unfollow", Colors.RESET, true);
                break;
        }

        System.out.println();

        //bio
        tw.typeWithColor("Bio : ", Colors.BLUE, false);
        tw.typeWithColor(Typewriter.splitLongString(user.getBio(), 50), Colors.YELLOW, true);
        System.out.println();

        // following things
        tw.typeWithColor("3. ", Colors.PURPLE, false);
        tw.typeWithColor(String.valueOf(user.getFollowingCount()), Colors.RESET, false);
        tw.typeWithColor(" Following", Colors.WHITE, false);

        tw.typeWithColor("\t\t4. ", Colors.PURPLE, false);
        tw.typeWithColor(String.valueOf(user.getFollowerCount()), Colors.RESET, false);
        tw.typeWithColor(" Followers", Colors.WHITE, true);

        // tweets
        tw.typeWithColor("5. ", Colors.PURPLE, false);
        tw.typeWithColor("@" + user.getUsername() + "'s Tweets", Colors.YELLOW, true);

        int choice = Twitter.getIntegerInput();

        switch (choice) {
            case 1:
                sc.nextLine();
                Twitter.run();
                break;
            case 2:
                sc.nextLine();
                handleFollowing(user);
                break;
            case 3:
                sc.nextLine();
                handleListOfFollowings(user);
                break;
            case 4:
                sc.nextLine();
                handleListOfFollowers(user);
                break;
            case 5:
                Tweet.previewTweets(tweetController.getUserTweets(user));
                break;
        }
    }

    private void handleListOfFollowers(User user) throws SQLException {
        List<User> followerUsers = tweetsManagerDb.getFollowers(user.getId());
        userController.printAListOfUsers(followerUsers);

        tw.typeWithColor("Select the user that you want to visit :(press -1 to return)", Colors.WHITE, true);
        int choice = Twitter.getIntegerInput();
        sc.nextLine();

        if (choice == -1) {
            showPage(user);
            return;
        }

        if (choice > 0 && choice <= followerUsers.size()) {
            showPage(followerUsers.get(choice - 1));
        } else {
            tw.typeWithColor("Wrong choice!", Colors.RED, true);
            tw.typeWithColor("Press any key to try again!", Colors.RED, true);
            sc.nextLine();
            showPage(user);
        }

    }

    private void handleListOfFollowings(User user) throws SQLException {
        List<User> followingUsers = tweetsManagerDb.getFollowings(user.getId());
        userController.printAListOfUsers(followingUsers);

        tw.typeWithColor("Select the user that you want to visit :(press -1 to return)", Colors.WHITE, true);
        int choice = Twitter.getIntegerInput();
        sc.nextLine();

        if (choice == -1) {
            showPage(user);
            return;
        }

        if (choice > 0 && choice <= followingUsers.size()) {
            showPage(followingUsers.get(choice - 1));
        } else {
            tw.typeWithColor("Wrong choice!", Colors.RED, true);
            tw.typeWithColor("Press any key to try again!", Colors.RED, true);
            sc.nextLine();
            showPage(user);
        }

    }

    private void handleFollowing(User user) throws SQLException {

        switch (pageStatus) {
            case 1:
                Authentication.currentUserData.editProfile();
                break;
            case 2:
                userController.followUser(user);
                tw.typeWithColor("You have Followed this user successfully!", Colors.GREEN, true);
                tw.typeWithColor("Press any key to continue...", Colors.WHITE, true);
                sc.nextLine();
                showPage(userController.getUserById(user.getId()));
                return;
            case 3:
                userController.unfollowUser(user);
                tw.typeWithColor("You have Unfollowed this user successfully!", Colors.GREEN, true);
                tw.typeWithColor("Press any key to continue...", Colors.WHITE, true);
                sc.nextLine();
                showPage(userController.getUserById(user.getId()));
                return;
        }
    }

    private void setPageStatus(User u) throws SQLException {
        if (isPageForCurrentUser(u)) {
            setStatus(AUTHOR);
        } else if (!userController.isFollowing(u.getId())) {
            setStatus(NOT_FOLLOWED_PAGE);
        } else {
            setStatus(FOLLOWED_PAGE);
        }
    }

    private boolean isPageForCurrentUser(User user) {
        return user.getId() == Authentication.currentUserId;

    }

    public void setStatus(int s) {
        this.pageStatus = s;
    }

}
