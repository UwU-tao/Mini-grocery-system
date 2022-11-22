package application.controller;

public class UserController {
    private static int userid;
    private static String username;
    private static String password;
    private static String email;
    private static int admin;

    private static UserController instace;

    private UserController() {}

    public static UserController getInstance() {
        if (instace == null) {
            synchronized (UserController.class) {
                if (instace == null) {
                    instace = new UserController();
                }
            }
        }
        return instace;
    }

    public static int getUserid() {
        return userid;
    }

    public static void setUserid(int userid) {
        UserController.userid = userid;
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        UserController.username = username;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        UserController.password = password;
    }

    public static String getEmail() {
        return email;
    }

    public static void setEmail(String email) {
        UserController.email = email;
    }

    public static int getAdmin() {
        return admin;
    }

    public static void setAdmin(int admin) {
        UserController.admin = admin;
    }

    public static void clearSession() {
        userid = 0;
        username = null;
        password = null;
        email = null;
        admin = 0;
    }
}
