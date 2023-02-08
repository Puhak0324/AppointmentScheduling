package Helper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import Models.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**This is the User Query Class. It contains all methods pertaining to the User table.*/
public abstract class UserQuery {

    /**This is the Get Users method.
     * An observable list users is created.
     * A database statement to select all from the users table is created. The database is connected
     * to and statement is executed. A new User is created including all specified columns. Each new
     * User is added to the observable list. Observable list users is returned.
     *
     * @return Returns the observable list users.
     * @throws SQLException Throws an error if failed.
     */
    public static ObservableList<User> getUsers() throws SQLException {
        ObservableList<User> users = FXCollections.observableArrayList();
        String sql = "SELECT * FROM USERS";
        PreparedStatement ps = ConnectDatabase.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            User newUser = new User(
                    rs.getInt("User_ID"),
                    rs.getString("User_Name"),
                    rs.getString("Password")
            );
            users.add(newUser);
        }
        return users;
    }

    /**This is the Check Username and Password method.
     * This creates a Select All statement specifying a Username and Password.
     * Statement is executed. If no matches are found, returns false.
     * @param username A variable that contains the Username.
     * @param password A variable that contains the Password.
     * @return Returns false if no matches are found within database.
     * @throws Exception throws an error if failed.
     */
    public static boolean checkUsernamePassword(String username, String password) throws Exception {
        String searchStatement = "SELECT * FROM users WHERE User_Name=? AND Password=?";

        DatabaseQuery.setPreparedStatement(ConnectDatabase.getConnection(), searchStatement);
        PreparedStatement preparedStatement = DatabaseQuery.getPreparedStatement();

        preparedStatement.setString(1, username);
        preparedStatement.setString(2, password);

        try {
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();
            return (resultSet.next());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }
}
