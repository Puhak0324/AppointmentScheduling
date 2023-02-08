package Helper;

import java.sql.Connection;
import java.sql.DriverManager;

/**Thisn is the Connect to Database class. This contains all methods pertaining to Database Connection.*/
public abstract class ConnectDatabase {
    private static final String protocol = "jdbc";
    private static final String vendor = ":mysql:";
    private static final String location = "//localhost/";
    private static final String databaseName = "client_schedule";
    private static final String jdbcUrl = protocol + vendor + location + databaseName + "?connectionTimeZone = SERVER"; // LOCAL
    private static final String driver = "com.mysql.cj.jdbc.Driver"; // Driver reference
    private static final String userName = "sqlUser"; // Username
    private static String password = "Passw0rd!"; // Password
    public static Connection connection;  // Connection Interface

    /**This is the openConnection method.
     * This locates the driver, references the URL with attached Username and Password, and if successful prints
     * out "Connection Successful". If connection fails, prints out "Error".
     * @return Returns nothing.
     */
    public static Connection openConnection()
    {
        try {
            Class.forName(driver); // Locate Driver
            connection = DriverManager.getConnection(jdbcUrl, userName, password); // Reference Connection object
            System.out.println("Connection successful!");
        }
        catch(Exception e)
        {
            System.out.println("Error:" + e.getMessage());
        }
        return null;
    }

    /**This is the Get Connection method. This returns the connection.*/
    public static Connection getConnection(){
        return connection;
    }

    /**This is the Close Connection method. This closes the connection and prints "Connection closed!".
     * If failed, prints out "Error".*/
    public static void closeConnection() {
        try {
            connection.close();
            System.out.println("Connection closed!");
        }
        catch(Exception e)
        {
            System.out.println("Error:" + e.getMessage());
        }
    }
}
