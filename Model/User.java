package Models;

/**This class creates a User as part of the User Table.*/
public class User {
    private int userId;
    private String userName;
    private String password;

    /**This constructor sets up the order of the User class.
     * All instances of the User class will follow this order.*/
    public User(int userId, String userName, String password) {
        this.userId = userId;
        this.userName = userName;
        this.password = password;

    }

    /**This is the accessor for User ID. This returns User ID as an integer value.*/
    public int getUserId() {
        return userId;
    }

   /**This is the mutator for User ID. This sets the value of User ID as an integer.*/
    public void setUserId(int userId) {
        this.userId = userId;
    }

   /**This is the accessor for Username. This returns Username as a string value.*/
    public String getUserName() {
        return userName;
    }

    /**This is the mutator for Username. This sets the value of username as a string.*/
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**This is the accessor for Password. This returns Password as a string value.*/
    public String getPassword() {
        return password;
    }

    /**This is the mutator for Password. This sets the value of Password as a string.*/
    public void setPassword(String password) {
        this.password = password;
    }
}

