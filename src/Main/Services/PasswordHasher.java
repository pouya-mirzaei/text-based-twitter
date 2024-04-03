package Main.Services;


import org.mindrot.jbcrypt.BCrypt;

public class PasswordHasher {

    // Method to hash a password
    public static String hashPassword(String password) {
        String salt = BCrypt.gensalt();
        return BCrypt.hashpw(password, salt);
    }

    // Method to check if a password matches a hashed password
    public static boolean checkPassword(String password, String hashedPassword) {
        return BCrypt.checkpw(password, hashedPassword);
    }

}
