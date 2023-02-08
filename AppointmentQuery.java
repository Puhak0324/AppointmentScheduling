package Helper;

import Models.Appointment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.*;

/**This class controls the Appointment table within the database and contains all methods pertaining to.*/
public abstract class AppointmentQuery {



    /**
     * This is the Get Appointments method.
     * An observable list is created called appointments.
     * A database statement to select all from the Appointments table is created. The database is connected
     * to and statement is executed. A new Appointment is created including all specified columns. Each new
     * Appointment is added to the observable list appointments. Observable list is returned.
     *
     * @return returns an observable list called appointments.
     * @throws Exception Throws an error if failed.
     */
    public static ObservableList<Appointment> getAppointments() throws Exception {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        String searchStatement = "SELECT * FROM APPOINTMENTS";
        PreparedStatement ps = ConnectDatabase.getConnection().prepareStatement(searchStatement);
        ResultSet resultSet = ps.executeQuery();
        while (resultSet.next()) {
                Appointment newAppointment = new Appointment(
                        resultSet.getInt("Appointment_ID"),
                        resultSet.getString("Title"),
                        resultSet.getString("Description"),
                        resultSet.getString("Location"),
                        resultSet.getString("Type"),
                        resultSet.getDate("Start").toLocalDate(),
                        resultSet.getTimestamp("Start").toLocalDateTime(),
                        resultSet.getDate("End").toLocalDate(),
                        resultSet.getTimestamp("End").toLocalDateTime(),
                        resultSet.getInt("Customer_ID"),
                        resultSet.getInt("User_ID"),
                        resultSet.getString("Contact_ID"));
                appointments.add(newAppointment);
            }
            return appointments;
        }


    /**
     * This is the Add Appointment method.
     * It translates the Contact strings into an integer. A database statement to Insert
     * into the Appointments table is created. The database is connected to and statement is executed.
     * The table columns are set with the passed in variables and a new Appointment is added into the Table.
     *
     * @param newId          A variable that contains the Appointment ID.
     * @param newTitle       A variable that contains the Appointment title.
     * @param newDescription A variable that contains the Appointment description.
     * @param newLocation    A variable that contains the Appointment location.
     * @param newType        A variable that contains the Appointment type.
     * @param newStart   A variable that contains the Appointment start date and time.
     * @param newEnd     A variable that contains the Appointment end date and time.
     * @param newCustomerID  A variable that contains the Customer ID.
     * @param newUserID      A variable that contains the User ID.
     * @param newContact     A variable that contains the Contact.
     */
    public static void addAppointment(int newId, String newTitle, String newDescription, String newLocation, String newType,
                                      LocalDateTime newStart, LocalDateTime newEnd, Integer newCustomerID, Integer newUserID, String newContact) {
        try {
            if (newContact.equals("Li Lee")) {
                newContact = "3";
            }
            if (newContact.equals("Daniel Garcia")) {
                newContact = "2";
            }
            if (newContact.equals("Anika Costa")) {
                newContact = "1";
            }
            String sqlaa = "INSERT INTO appointments(Appointment_ID, Title, Description, Location, Type, Start, End, Customer_ID, User_ID, Contact_ID) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = ConnectDatabase.connection.prepareStatement(sqlaa);

            preparedStatement.setInt(1, newId);
            preparedStatement.setString(2, newTitle);
            preparedStatement.setString(3, newDescription);
            preparedStatement.setString(4, newLocation);
            preparedStatement.setString(5, newType);
            preparedStatement.setTimestamp(6, Timestamp.valueOf(newStart));
            preparedStatement.setTimestamp(7, Timestamp.valueOf(newEnd));
            preparedStatement.setInt(8, newCustomerID);
            preparedStatement.setInt(9, newUserID);
            preparedStatement.setString(10, newContact);

            preparedStatement.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * This is the Update Appointment method.
     * It translates the Contact strings into an integer. A database statement to Update
     * Appointments table is created. The database is connected to and statement is executed.
     * The table columns are set with the passed in variables and an updated Appointment replaces the original where
     * the specified Appointment ID was located.
     *
     * @param appointmentID A variable that contains the Appointment ID.
     * @param title         A variable that contains the Appointment title.
     * @param description   A variable that contains the Appointment description.
     * @param location      A variable that contains the Appointment location.
     * @param type          A variable that contains the Appointment type.
     * @param newStart  A variable that contains the Appointment start date and time.
     * @param newEnd    A variable that contains the Appointment end date and time.
     * @param newCustomerID A variable that contains the Customer ID.
     * @param newUserID     A variable that contains the User ID.
     * @param newContact    A variable that contains the Contact.
     * @return Returns the rows affected by the statement.
     * @throws Exception Throws an error if failed.
     */
    public static int updateAppointment(int appointmentID, String title, String description, String location,
                                        String type, LocalDateTime newStart, LocalDateTime newEnd,
                                        Integer newCustomerID, Integer newUserID, String newContact) throws Exception {
        if (newContact.equals("Li Lee")) {
            newContact = "3";
        }
        if (newContact.equals("Daniel Garcia")) {
            newContact = "2";
        }
        if (newContact.equals("Anika Costa")) {
            newContact = "1";
        }
        String updateStatement = "UPDATE appointments SET Title=?, Description=?, Location=?, Type=?, Start=?, End=?, " +
                "Customer_ID=?, User_ID=?, Contact_ID=? WHERE Appointment_ID = ?;";

        PreparedStatement preparedStatement = ConnectDatabase.connection.prepareStatement(updateStatement);

        preparedStatement.setString(1, title);
        preparedStatement.setString(2, description);
        preparedStatement.setString(3, location);
        preparedStatement.setString(4, type);
        preparedStatement.setTimestamp(5, Timestamp.valueOf(newStart));
        preparedStatement.setTimestamp(6, Timestamp.valueOf(newEnd));
        preparedStatement.setInt(7, newCustomerID);
        preparedStatement.setInt(8, newUserID);
        preparedStatement.setString(9, newContact);
        preparedStatement.setInt(10, appointmentID);
        int rowsAffected = preparedStatement.executeUpdate();
        return rowsAffected;
    }

    /**
     * This is the Delete Appointment method.
     * It creates a database statement to Delete a row from the Appointment table based off a specified
     * Appointment ID. The database is connected to and executed. Rows affected are returned.
     *
     * @param appointmentID A variable that contains the Appointment ID.
     * @return Returns the rows affected by the statement.
     * @throws SQLException Throws an error if any SQL rules are violated.
     */
    public static int deleteAppointment(int appointmentID) throws SQLException {
        String sql = "DELETE FROM APPOINTMENTS WHERE Appointment_ID = ?";
        PreparedStatement ps = ConnectDatabase.connection.prepareStatement(sql);
        ps.setInt(1, appointmentID);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }

    /**
     * This is the Get Appointments By Month method.
     * An observable list is created called appointments. Two variables are created of the current
     * LocalDateTime and LocalDateTime plus 30 days. A database Select statement is created to find Appointments
     * that are between the two variables. Statement is executed and any Appointments matching the criteria
     * are placed into the observable list appointments. Observable list appointments is returned.
     *
     * @return Returns the appointments observable list.
     * @throws SQLException Throws an error if any SQL rules are violated.
     */
    public static ObservableList<Appointment> getAppointmentsByMonth() throws SQLException {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();

        LocalDateTime currentDate = LocalDateTime.now();
        LocalDateTime upcomingMonth = currentDate.plusDays(30);

        String queryStatement = "Select * FROM appointments WHERE Start < ? AND Start > ?";

        PreparedStatement preparedStatement = ConnectDatabase.connection.prepareStatement(queryStatement);
        preparedStatement.setDate(1, Date.valueOf(upcomingMonth.toLocalDate()));
        preparedStatement.setDate(2, Date.valueOf(currentDate.toLocalDate()));
        try {
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();
            while (resultSet.next()) {
                Appointment appointment = new Appointment(
                        resultSet.getInt("Appointment_ID"),
                        resultSet.getString("Title"),
                        resultSet.getString("Description"),
                        resultSet.getString("Location"),
                        resultSet.getString("Type"),
                        resultSet.getDate("Start").toLocalDate(),
                        resultSet.getTimestamp("Start").toLocalDateTime(),
                        resultSet.getDate("End").toLocalDate(),
                        resultSet.getTimestamp("End").toLocalDateTime(),
                        resultSet.getInt("Customer_ID"),
                        resultSet.getInt("User_ID"),
                        resultSet.getString("Contact_ID"));
                appointments.add(appointment);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return appointments;
    }

    /**
     * This is the Get Appointments By Week method.
     * An observable list is created called appointments. Two variables are created of the current
     * LocalDateTime and LocalDateTime plus 7 days. A database Select statement is created to find Appointments
     * that are between the two variables. Statement is executed and any Appointments matching the criteria
     * are placed into the observable list appointments. Observable list appointments is returned.
     *
     * @return Returns the appointments observable list.
     * @throws SQLException Throws an error if any SQL rules are violated.
     */
    public static ObservableList<Appointment> getAppointmentsByWeek() throws SQLException {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();

        LocalDateTime currentDate = LocalDateTime.now();
        LocalDateTime nextWeek = currentDate.plusDays(7);

        String queryStatement = "Select * FROM appointments WHERE Start < ? AND Start > ?";

        PreparedStatement preparedStatement = ConnectDatabase.connection.prepareStatement(queryStatement);
        preparedStatement.setDate(1, Date.valueOf(nextWeek.toLocalDate()));
        preparedStatement.setDate(2, Date.valueOf(currentDate.toLocalDate()));
        try {
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();
            while (resultSet.next()) {
                Appointment appointment = new Appointment(
                        resultSet.getInt("Appointment_ID"),
                        resultSet.getString("Title"),
                        resultSet.getString("Description"),
                        resultSet.getString("Location"),
                        resultSet.getString("Type"),
                        resultSet.getDate("Start").toLocalDate(),
                        resultSet.getTimestamp("Start").toLocalDateTime(),
                        resultSet.getDate("End").toLocalDate(),
                        resultSet.getTimestamp("End").toLocalDateTime(),
                        resultSet.getInt("Customer_ID"),
                        resultSet.getInt("User_ID"),
                        resultSet.getString("Contact_ID"));
                appointments.add(appointment);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return appointments;
    }

    /**
     * This is the Get Appointments By Customer ID method.
     * This creates an Observable list called appointments. A database Select statement is created that searches
     * for all appointments containing a specified Customer ID. Statement is executed. Any matching
     * Appointments are added to the observable list appointments.
     * Observable list appointments is returned.
     *
     * @param CustomerID A variable that contains the Customer ID.
     * @return ObservableList List of Appointments.
     * @throws Exception Throws error if failed.
     */
    public static ObservableList<Appointment> getAppointmentsByCustomerID(int CustomerID) throws Exception {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();

        String queryStatement = "SELECT * FROM appointments WHERE Customer_ID=?;";

        DatabaseQuery.setPreparedStatement(ConnectDatabase.getConnection(), queryStatement);
        PreparedStatement preparedStatement = DatabaseQuery.getPreparedStatement();

        preparedStatement.setInt(1, CustomerID);

        try {
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();

            // Forward scroll resultSet
            while (resultSet.next()) {
                Appointment newAppointment = new Appointment(
                        resultSet.getInt("Appointment_ID"),
                        resultSet.getString("Title"),
                        resultSet.getString("Description"),
                        resultSet.getString("Location"),
                        resultSet.getString("Type"),
                        resultSet.getDate("Start").toLocalDate(),
                        resultSet.getTimestamp("Start").toLocalDateTime(),
                        resultSet.getDate("End").toLocalDate(),
                        resultSet.getTimestamp("End").toLocalDateTime(),
                        resultSet.getInt("Customer_ID"),
                        resultSet.getInt("User_ID"),
                        resultSet.getString("Contact_ID"));
                appointments.add(newAppointment);
            }
            return appointments;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());

        }
        return appointments;
    }

    /** This method gets an Appointment by Appointment ID
     * @param AppointmentID Int value of Appointment ID
     * @return Appointment Appointment
     * @throws SQLException Catches SQLException, prints stacktrace, and error message.
     */
    public static Appointment getAppointmentByAppointmentID(int AppointmentID) throws Exception {

        String queryStatement = "SELECT * FROM appointments AS a INNER JOIN contacts AS c ON a.Contact_ID=c.Contact_ID WHERE Appointment_ID=?;";

        DatabaseQuery.setPreparedStatement(ConnectDatabase.getConnection(), queryStatement);
        PreparedStatement preparedStatement = DatabaseQuery.getPreparedStatement();

        preparedStatement.setInt(1, AppointmentID);

        try {
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();

            // Forward scroll resultSet
            while (resultSet.next()) {
                Appointment newAppointment = new Appointment(
                        resultSet.getInt("Appointment_ID"),
                        resultSet.getString("Title"),
                        resultSet.getString("Description"),
                        resultSet.getString("Location"),
                        resultSet.getString("Type"),
                        resultSet.getDate("Start").toLocalDate(),
                        resultSet.getTimestamp("Start").toLocalDateTime(),
                        resultSet.getDate("End").toLocalDate(),
                        resultSet.getTimestamp("End").toLocalDateTime(),
                        resultSet.getInt("Customer_ID"),
                        resultSet.getInt("User_ID"),
                        resultSet.getInt("Contact_ID"),
                        resultSet.getString("Contact_Name")
                );

                return newAppointment;
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return null;
    }

    /**
     * This is the Get Appointments By Contact ID method.
     * This creates an Observable list called appointments. If contactID contains a string, it is converted
     * to an integer. A database Select statement is created that searches
     * for all appointments containing a specified Contact ID. Statement is executed. Any matching
     * Appointments are added to the observable list appointments.
     * Observable list appointments is returned.
     *
     * @param contactID A variable that contains the Contact ID.
     * @return ObservableList List of Appointments.
     * @throws Exception Throws error if failed.
     */
    public static ObservableList<Appointment> getAppointmentsByContactID(String contactID) throws Exception {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        if (contactID.equals("Li Lee")) {
            contactID = "3";
        }
        if (contactID.equals("Daniel Garcia")) {
            contactID = "2";
        }
        if (contactID.equals("Anika Costa")) {
            contactID = "1";
        }
        String queryStatement = "SELECT * FROM appointments WHERE Contact_ID=?;";

        DatabaseQuery.setPreparedStatement(ConnectDatabase.getConnection(), queryStatement);
        PreparedStatement preparedStatement = DatabaseQuery.getPreparedStatement();

        preparedStatement.setString(1, contactID);

        try {
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();

            // Forward scroll resultSet
            while (resultSet.next()) {
                Appointment newAppointment = new Appointment(
                        resultSet.getInt("Appointment_ID"),
                        resultSet.getString("Title"),
                        resultSet.getString("Description"),
                        resultSet.getString("Location"),
                        resultSet.getString("Type"),
                        resultSet.getDate("Start").toLocalDate(),
                        resultSet.getTimestamp("Start").toLocalDateTime(),
                        resultSet.getDate("End").toLocalDate(),
                        resultSet.getTimestamp("End").toLocalDateTime(),
                        resultSet.getInt("Customer_ID"),
                        resultSet.getInt("User_ID"),
                        resultSet.getString("Contact_ID"));
                appointments.add(newAppointment);
            }
            return appointments;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return appointments;
    }
}