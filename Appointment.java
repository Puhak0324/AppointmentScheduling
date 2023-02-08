package Models;

import Helper.AppointmentQuery;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;

/**This class creates an Appointment as part of the Appointment table.*/
public class Appointment {

    private int appointmentID;
    private String title;
    private String description;
    private String location;
    private String contactID;
    private String type;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int customerID;
    private LocalDate startDate;
    private LocalDate endDate;
    private int userID;
    private static int appointmentIDCount = 1;

    /**This constructor sets up the order of the Appointment class.
     * All instances of the Appointment class will follow this order.
     * This constructor is used on the main Appointment table.*/
    public Appointment(int appointmentID, String title, String description, String location, String type,
                       LocalDate startDate, LocalDateTime startTime, LocalDate endDate, LocalDateTime endTime,
                       int customerID, int userID, String contactID) {
        this.appointmentID = appointmentID;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.startTime = startTime;
        this.endTime = endTime;
        this.startDate = startDate;
        this.endDate = endDate;
        this.contactID = contactID;
        this.customerID = customerID;
        this.userID = userID;
    }

    /**This constructor sets up the order of the Appointment class.
     * All instances of the Appointment class will follow this order.
     * This constructor is used on the Schedule Table located on the Reports Screen.*/
    public Appointment(int appointmentId, String title, String description, LocalDate start, LocalDateTime start1, LocalDateTime end, int customerId) {
        this.appointmentID = appointmentId;
        this.title = title;
        this.description = description;
        this.startDate = start;
        this.startTime = start1;
        this.endTime = end;
        this.customerID = customerId;
    }

    public Appointment(int appointmentId, String title, String description, String location, String type, LocalDate start, LocalDateTime start1, LocalDate end, LocalDateTime end1, int customerId, int userId, int contactId, String contactName) {
    }


    /**This is the accessor for Appointment ID. This returns the value of Appointment ID as an integer.*/
    public int getAppointmentID() {
        return this.appointmentID;
    }

    /**This is the accessor for Title. This returns the value of Title as a string.*/
    public String getTitle() {
        return this.title;
    }

    /**This is the accessor for Description. This returns the value of Description as a string.*/
    public String getDescription() {
        return this.description;
    }

    /**This is the accessor for Location. This returns the value of Location as a string.*/
    public String getLocation() {
        return this.location;
    }

    /**This is the accessor for Type. This returns the value of Type as a string.*/
    public String getType() {
        return this.type;
    }

    /**
     * This is the accessor for Start Time. This returns the value of Start Time as a LocalTime.
     */
    public LocalDateTime getStartTime() {
        return this.startTime;
    }

    /**
     * This is the accessor for End Time. This returns the value of End Time as a LocalTime.
     */
    public LocalDateTime getEndTime() {
        return this.endTime;
    }

    /**This is the accessor for Start Date. This returns the value of Start Date as a LocalDate.*/
    public LocalDate getStartDate() {
        return this.startDate;
    }

    /**This is the accessor for End Date. This returns the value of End Date as a LocalDate.*/
    public LocalDate getEndDate() {
        return this.endDate;
    }

    /**This is the accessor for Customer ID. This returns the value of Customer ID as an integer.*/
    public int getCustomerID() {
        return this.customerID;
    }

    /**This is the accessor for User ID. This returns the value of User ID as an integer.*/
    public int getUserID() {
        return this.userID;
    }

    /**This is the accessor for Contact ID. This returns the value of Contact ID as a string.*/
    public String getContactID() {
        return this.contactID;
    }

    /**This is the mutator for Appointment ID. This sets the value of Appointment ID as an integer.*/
    private void setAppointmentID(int appointmentID) {
        this.appointmentID = appointmentID;//auto generated
    }

    /**This is the mutator for Title. This sets the value of Title as a string.*/
    private void setTitle(String title) {
        this.title = title;
    }

    /**This is the mutator for Description. This sets the value of Description as a string.*/
    private void setDescription(String description) {
        this.description = description;
    }

    /**This is the mutator for Location. This sets the value of Location as a string.*/
    private void setLocation(String location) {
        this.location = location;
    }

    /**This is the mutator for Type. This sets the value of Type as a string.*/
    private void setType(String type) {
        this.type = type;
    }

    /**This is the mutator for Start Time. This sets the value of Start Time as a LocalTime.*/
    private void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    /**This is the mutator for End Time. This sets the value of End Time as a LocalTime.*/
    private void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    /**This is the mutator for Start Date. This sets the value of Start Date as a LocalDate.*/
    private void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    /**This is the mutator for End Date. This sets the value of End Date as a LocalDate.*/
    private void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    /**This is the mutator for Contact ID. This sets the value of Contact ID as a string.*/
    private void setContactID(String contactID) {
        this.contactID = contactID;
    }

    /**This is the mutator for Customer ID. This sets the value of Customer ID as an integer.*/
    private void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    /**This is the mutator for User ID. This sets the value of User ID as an integer.uj*/
    private void setUserID(int userID) {
        this.userID = userID;
    }

    /**This is the Get Appointment ID Count Method.
     * This queries to get all Appointments. It then enters a for loop to determine if the variable
     * appointmentIDCount is less than the current size of all Appointments. If it is smaller, The count is
     * increased by 1. Appointment ID Count is returned when it is not less than or equal to the size of
     * all Appointments.
     * @return Returns the current Appointment ID Count.
     * @throws Exception Throws an error if failed.
     */
    public static int getAppointmentIDCount() throws Exception {
        AppointmentQuery.getAppointments().size();
        for (int i = 0; i <= AppointmentQuery.getAppointments().size(); i++) {
            if (appointmentIDCount <= AppointmentQuery.getAppointments().size()) {
                appointmentIDCount++;
            }
        }
        return appointmentIDCount;
    }
}

