package Helper;

import Models.Customer;
import Models.FirstLevelDivision;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**This class controls the Customer table within the database and contains all methods pertaining to.*/
public abstract class CustomerQuery {

    /**
     * This is the Get Customers method.
     * An observable list is created called customers.
     * A database statement to select all from the Customers table is created. The database is connected
     * to and statement is executed. A new Customer is created including all specified columns. Each new
     * Customer is added to the observable list customers. Observable list is returned.
     *
     * @return returns an observable list called customers.
     * @throws Exception Throws an error if failed.
     */
    public static ObservableList<Customer> getCustomers() throws Exception {
        ObservableList<Customer> customers = FXCollections.observableArrayList();
        String searchStatement = "SELECT * FROM customers AS c INNER JOIN first_level_divisions AS d ON c.Division_ID =" +
                " d.Division_ID INNER JOIN countries AS co ON co.Country_ID=d.COUNTRY_ID;";
        PreparedStatement ps = ConnectDatabase.getConnection().prepareStatement(searchStatement);
        ResultSet resultSet = ps.executeQuery();
        while (resultSet.next()) {
            Customer newCustomer = new Customer(
                    resultSet.getInt("Customer_ID"),
                    resultSet.getString("Customer_Name"),
                    resultSet.getString("Address"),
                    resultSet.getString("Postal_Code"),
                    resultSet.getString("Phone"),
                    resultSet.getString("Division"),
                    resultSet.getString("Country"),
                    resultSet.getInt("Division_ID"));
            customers.add(newCustomer);
        }
        return customers;
    }

    /**This is the Create Customer method.
     * An observable list called newDivision is created that obtains the Division ID from Division name.
     * An Insert into Customer table statement is created. Statement is executed. If count is greater
     * than 0, return true. If count is less than 1, return false.
     * @param ID A variable that contains the Customer ID.
     * @param name A variable that contains the Customer name.
     * @param address A variable that contains the Customer address.
     * @param postalCode A variable that contains the Customer postal code.
     * @param phone A variable that contains the Customer phone number.
     * @param division A variable that contains the Customer division.
     * @return True if updated count is greater than 0 (should be 1) and false if none were affected.
     * @throws Exception Throws an error if failed.
     */
    public static boolean createCustomer(String ID, String name, String address, String postalCode, String phone, String division) throws Exception {

        FirstLevelDivision newDivision = FirstLevelDivisionQuery.getDivisionId(division);

        String insertStatement = "INSERT INTO customers(Customer_ID, Customer_Name, Address, Postal_Code, Phone, Division_ID) VALUES (?, ?, ?, ?, ?, ?)";

        DatabaseQuery.setPreparedStatement(ConnectDatabase.getConnection(), insertStatement);
        PreparedStatement preparedStatement = DatabaseQuery.getPreparedStatement();
        preparedStatement.setString(1, ID);
        preparedStatement.setString(2, name);
        preparedStatement.setString(3, address);
        preparedStatement.setString(4, postalCode);
        preparedStatement.setString(5, phone);
        preparedStatement.setInt(6, newDivision.getDivisionID());

        try {
            preparedStatement.execute();
            if (preparedStatement.getUpdateCount() > 0) {
                System.out.println("Rows affected: " + preparedStatement.getUpdateCount());
            } else {
                System.out.println("No change");
            }
            return true;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    /**This is the Update Customer method.
     * An observable list called newDivision is created that obtains the Division ID from Division name.
     * An Update Customer statement is created based off Customer ID.  Statement is executed.
     * Rows affected are returned (should be 1).
     * @param customerId A variable that contains the Customer ID.
     * @param name A variable that contains the Customer phone name.
     * @param address A variable that contains the Customer address.
     * @param postalCode A variable that contains the Customer postal code.
     * @param phone A variable that contains the Customer phone number.
     * @param division A variable that contains the Customer division.
     * @return Returns the rows affected.
     * @throws Exception Throws an error if failed.
     */
    public static int updateCustomer(int customerId, String name, String address, String postalCode, String phone, String division) throws Exception {
        FirstLevelDivision newDivision = FirstLevelDivisionQuery.getDivisionId(division);
        String sql = "UPDATE customers SET Customer_Name=?, Address=?, Postal_Code=?, Phone=?, Division_ID=? WHERE Customer_ID=?";
        PreparedStatement ps = ConnectDatabase.connection.prepareStatement(sql);
        ps.setString(1, name);
        ps.setString(2, address);
        ps.setString(3, postalCode);
        ps.setString(4, phone);
        ps.setInt(5, newDivision.getDivisionID());
        ps.setInt(6, customerId);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }

    /**This is the Delete Customer method.
     * It creates a database statement to Delete a row from the Customer table based off a specified
     * Customer ID. The database is connected to and executed. Rows affected are returned.
     *
     * @param customerID A variable that contains the Customer ID.
     * @return Returns the rows affected.
     * @throws SQLException Throws an error if any SQL rules are violated.
     */
    public static int deleteCustomer(int customerID) throws SQLException {
        String sql = "DELETE FROM CUSTOMERS WHERE Customer_ID = ?";
        PreparedStatement ps = ConnectDatabase.connection.prepareStatement(sql);
        ps.setInt(1, customerID);
        int rowsAffected = ps.executeUpdate();
        System.out.println("Customer Deleted");
        return rowsAffected;
    }
}