package Controllers;

import Helper.AppointmentQuery;
import Helper.CustomerQuery;
import Helper.UserQuery;
import Models.Appointment;
import Models.Customer;
import Models.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.*;
import java.util.ResourceBundle;

import static Controllers.MainScreen.infoDialog;
import static Models.Appointment.getAppointmentIDCount;

/**This class adds new appointments to the database. It also contains all FXML assignments and various methods.*/
public class AppointmentAdd implements Initializable {
    /**
     * This is assigned to the bottom right button labeled "Cancel".
     */
    @FXML
    public Button CancelButton;
    /**
     * This is assigned to the bottom right button labeled "Save".
     */
    @FXML
    public Button SaveButton;
    /**
     * This is the disabled text-field located to the right of the label titled "Appointment ID".
     */
    @FXML
    public TextField appointmentIDField;
    /**
     * This is the editable text-field located to the right of the label titled "Title".
     */
    @FXML
    public TextField appointmentTitleField;
    /**
     * This is the editable text-field located to the right of the label titled "Description".
     */
    @FXML
    public TextField appointmentDescriptionField;
    /**
     * This is the editable date picker located to the right of the label titled "Start Date".
     */
    @FXML
    public DatePicker appointmentDatePickerStart;
    /**
     * This is the editable date picker located to the right of the label titled "End Date".
     */
    @FXML
    public DatePicker appointmentDatePickerEnd;
    /**
     * This is the editable text field located to the right of the label titled "Location".
     */
    @FXML
    public TextField appointmentLocationField;
    /**
     * This is the editable combo box located to the right of the label titled "Start Time".
     */
    @FXML
    public ComboBox<LocalTime> appointmentStartComboBox;
    /**
     * This is the editable combo box located to the right of the label titled "End Time".
     */
    @FXML
    public ComboBox<LocalTime> appointmentEndComboBox;
    /**
     * This is the editable combo box located to the right of the label titled "Type".
     */
    @FXML
    public ComboBox<String> TypeCombo;
    /**
     * This is the editable combo box located to the right of the label titled "User ID".
     */
    @FXML
    public ComboBox<Integer> UserIDCombo;
    /**
     * This is the editable combo box located to the right of the label titled "Customer ID".
     */
    @FXML
    public ComboBox<Integer> CustomerIDCombo;
    /**
     * This is the editable combo box located to the right of the label titled "Contact".
     */
    @FXML
    public ComboBox<String> ContactCombo;

    /**
     * This is the initialize method.
     * This retrieves a new appointment ID and populates all combo boxes.
     *
     * @param url            Path to FXML file.
     * @param resourceBundle Contains internationalization properties for GUI.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            getAppointmentIDCount();
            int appointmentID = Appointment.getAppointmentIDCount();
            appointmentIDField.setText(String.valueOf(appointmentID));
            populateTimeComboBoxes();
            populateContactComboBox();
            populateCustomerIDComboBox();
            populateUserIDComboBox();
            populateTypeComboBox();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This is the Convert to EST method.
     * This returns the EST of the entered time.
     *
     * @param time takes a LocalDateTime as a variable.
     * @return Zoned DateTime of entered time.
     */
    private ZonedDateTime convertToEST(LocalDateTime time) {
        return ZonedDateTime.of(time, ZoneId.of("America/New_York"));
    }

    /**
     * This is the Populate Type Combo box method.
     * This creates an observable list titled "typeList" and adds all specified types. All types are
     * then set in the Type Combo box.
     */
    private void populateTypeComboBox() {
        ObservableList<String> typeList = FXCollections.observableArrayList();
        typeList.addAll("Planning Session", "De-Briefing", "Follow-up", "Pre-Briefing", "Open Session");
        TypeCombo.setItems(typeList);
    }

    /**
     * This is the Populate User ID Combo box method.
     * This creates an observable list named userIDComboList, then queries all Users from database.
     * All Users are added to the userIDComboList. The Combo box is then set with the userIDComboList.
     */
    private void populateUserIDComboBox() {
        ObservableList<Integer> userIDComboList = FXCollections.observableArrayList();
        try {
            ObservableList<User> users = UserQuery.getUsers();
            for (User user : users) {
                userIDComboList.add(user.getUserId());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        UserIDCombo.setItems(userIDComboList);
    }

    /**
     * This is the Populate Customer ID Combo box method.
     * This creates an observable list named customerIDComboList, then queries all Customers in
     * the database. All Customer IDs are added to the customerIDComboList. The Combo box is then
     * set with the customerIDComboList.
     */
    private void populateCustomerIDComboBox() {
        ObservableList<Integer> customerIDComboList = FXCollections.observableArrayList();

        try {
            ObservableList<Customer> customers = CustomerQuery.getCustomers();
            for (Customer customer : customers) {
                customerIDComboList.add(customer.getCustomerID());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        CustomerIDCombo.setItems(customerIDComboList);
    }

    /**
     * This is the Populate Contact Combo box method.
     * This creates an observable list named contactComboList, then adds all specified Contacts.
     * All Contacts are then set in the Contact Combo box.
     */
    private void populateContactComboBox() {
        ObservableList<String> contactComboList = FXCollections.observableArrayList();
        contactComboList.addAll("Anika Costa", "Daniel Garcia", "Li Lee");
        ContactCombo.setItems(contactComboList);
    }

    /**
     * This is the Populate Time Combo boxes method.
     * This creates an observable list named time, and specifies a start time of 0700 hours and an end
     * time of 2300 hours. Start time is incrementally increased by 15 minutes and added to the observable list,
     * while it is before the specified End time. The observable list is then set in both Start and End time combo boxes.
     */
    private void populateTimeComboBoxes() {
        ObservableList<LocalTime> time = FXCollections.observableArrayList();
        LocalTime startTime = LocalTime.of(7, 0);
        LocalTime endTime = LocalTime.of(23, 0);

        time.add(startTime);
        while (startTime.isBefore(endTime)) {
            startTime = startTime.plusMinutes(15);
            time.add(startTime);
        }
        appointmentStartComboBox.setItems(time);
        appointmentEndComboBox.setItems(time);
    }

    /**
     * This is the Cancel/Back to Main Screen (Schedule View) method.
     * This produces an alert asking for the user to confirm they want to cancel adding an appointment.
     * If user clicks "cancel" on alert: user is redirected back to the Add Appointment Screen.
     * If user clicks "OK" on alert: user is directed to the Main Screen and additions are discarded.
     * This method also contains a Lambda expression that waits for a response from user.
     *
     * @param actionEvent The user clicking on "Cancel".
     * @throws IOException Throws and Input/Output error if failed.
     */
    public void toMainScreenCancel(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Confirm Cancel");
        alert.setHeaderText("Confirm Cancel");
        alert.setContentText("Are you sure you want to Cancel?");
        alert.showAndWait().ifPresent((response -> {
            if (response == ButtonType.OK) {
                System.out.println("Alerting!");
                Parent main = null;
                try {
                    Parent root = FXMLLoader.load(getClass().getResource("/MainScreen.fxml"));
                    Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                    Scene scene = new Scene(root, 1000, 500);
                    stage.setTitle("WGU Scheduling System");
                    stage.setScene(scene);
                    stage.show();
                    stage.centerOnScreen();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }));
    }

    /**
     * This is the To Main Screen Save method.
     * This gathers all values obtained from the text fields and combo boxes within the form.
     * It checks to see if any fields are left blank. If so, user is redirected back to form. If not,
     * then appointment overlap is checked by Customer ID. If overlap is detected, user is redirected back
     * to form. If not, then appointment scheduled outside of business hours (8AM-10PM) is checked. If
     * appointment is outside of business hours, user is redirected back to form. If appointment is within
     * business hours, then a query is made to add all data into the database as a new Appointment. User is
     * then notified via an info pop up and redirected back to the Appointment view screen.
     *
     * @param actionEvent The user clicks on the button titled "Save".
     * @throws IOException Throws and Input/Output error if failed.
     */
    public void toMainScreenSave(ActionEvent actionEvent) throws IOException {
        try {
            int newID = Integer.parseInt(appointmentIDField.getText());
            String newTitle = appointmentTitleField.getText();
            String newType = TypeCombo.getValue();
            String newDescription = appointmentDescriptionField.getText();
            String newLocation = appointmentLocationField.getText();
            LocalDate newStartDate = appointmentDatePickerStart.getValue();
            LocalDate newEndDate = appointmentDatePickerEnd.getValue();
            LocalTime newStartTime = LocalTime.from(appointmentStartComboBox.getValue());
            LocalTime newEndTime = LocalTime.from(appointmentEndComboBox.getValue());
            String newContact = ContactCombo.getValue();
            Integer newCustomerID = CustomerIDCombo.getValue();
            Integer newUserID = UserIDCombo.getValue();

            if (newType == null) {
                infoDialog("Warning!", "No input detected", "Appointment type must contain a value");
                TypeCombo.setPromptText("Enter value here!");
                return;
            }
            if (newTitle.isBlank()) {
                infoDialog("Warning!", "No input detected", "Appointment title must contain a value");
                appointmentTitleField.setPromptText("Enter value here!");
                return;
            }
            if (newDescription.isBlank()) {
                infoDialog("Warning!", "No input detected", "Appointment description must contain a value");
                appointmentDescriptionField.setPromptText("Enter value here!");
                return;
            }
            if (newLocation.isBlank()) {
                infoDialog("Warning!", "No input detected", "Appointment location must contain a value");
                appointmentLocationField.setPromptText("Enter value here!");
                return;
            }
            if (newStartDate == null) {
                infoDialog("Warning!", "No input detected", "Appointment start date must contain a value");
                appointmentDatePickerStart.setPromptText("Enter value here!");
                return;
            }
            if (newEndDate == null) {
                infoDialog("Warning!", "No input detected", "Appointment end date must contain a value");
                appointmentDatePickerEnd.setPromptText("Enter value here!");
                return;
            }
            if (newStartTime == null) {
                infoDialog("Warning!", "No input detected", "Appointment start time must contain a value");
                appointmentStartComboBox.setPromptText("Enter value here!");
                return;
            }
            if (newEndTime == null) {
                infoDialog("Warning!", "No input detected", "Appointment end time must contain a value");
                appointmentEndComboBox.setPromptText("Enter value here!");
                return;
            }
            if (newContact == null) {
                infoDialog("Warning!", "No input detected", "Appointment contact must contain a value");
                ContactCombo.setPromptText("Enter value here!");
                return;
            }
            if (newCustomerID == null) {
                infoDialog("Warning!", "No input detected", "Appointment customer ID must contain a value");
                CustomerIDCombo.setPromptText("Enter value here!");
                return;
            }
            if (newUserID == null) {
                infoDialog("Warning!", "No input detected", "Appointment user ID must contain a value");
                UserIDCombo.setPromptText("Enter value here!");
                return;
            }
            if (appointmentDatePickerEnd.getValue().isBefore(appointmentDatePickerStart.getValue())) {
                infoDialog("Warning!", "Logical Error Detected", "Start date must come before end date");
                return;
            }
            if (newEndTime.isBefore(newStartTime)) {
                infoDialog("Warning!", "Logical Error Detected", "Start time must come before end time");
                return;
            }
            if (newEndTime.equals(newStartTime)) {
                infoDialog("Warning!", "Logical Error Detected", "Start time must come before end time");
                return;
            }
            if (!newStartDate.equals(newEndDate)) {
                infoDialog("Warning!", "Logical Error Detected", "Appointment must start and end on the same date");
                return;
            }
            LocalDateTime selectedStart = newStartDate.atTime(newStartTime);
            LocalDateTime selectedEnd = newEndDate.atTime(newEndTime);
            LocalDateTime proposedStart;
            LocalDateTime proposedEnd;
            try {
                ObservableList<Appointment> appointments = AppointmentQuery.getAppointmentsByCustomerID(CustomerIDCombo.getSelectionModel().getSelectedItem());
                for (Appointment appointment : appointments) {
                    proposedStart = appointment.getStartDate().atTime(appointment.getStartTime().toLocalTime());
                    proposedEnd = appointment.getEndDate().atTime(appointment.getEndTime().toLocalTime());

                    if (selectedStart.isEqual(proposedStart) && selectedEnd.isBefore(proposedEnd)) {
                        infoDialog("Warning!", "Appointment Overlap Detected", "1. Appointment overlap detected");
                        return;
                    }
                    if (selectedStart.isAfter(proposedStart) && selectedEnd.isBefore(proposedEnd)) {
                        infoDialog("Warning!", "Appointment Overlap Detected", "2. Appointment overlap detected");
                        return;
                    }
                    if (selectedStart.isAfter(proposedStart) && selectedEnd.isEqual(proposedEnd)) {
                        infoDialog("Warning!", "Appointment Overlap Detected", "3. Appointment overlap detected");
                        return;
                    }
                    if (selectedStart.isEqual(proposedStart) && selectedEnd.isAfter(proposedEnd)) {
                        infoDialog("Warning!", "Appointment Overlap Detected", "4. Appointment overlap detected");
                        return;
                    }
                    if (selectedStart.isEqual(proposedStart) && selectedEnd.isEqual(proposedEnd)) {
                        infoDialog("Warning!", "Appointment Overlap Detected", "5. Appointment overlap detected");
                        return;
                    }
                    if (selectedStart.isBefore(proposedStart) && selectedEnd.isEqual(proposedEnd)) {
                        infoDialog("Warning!", "Appointment Overlap Detected", "6. Appointment overlap detected");
                        return;
                    }
                    if (selectedStart.isBefore(proposedStart) && selectedEnd.isAfter(proposedEnd)) {
                        infoDialog("Warning!", "Appointment Overlap Detected", "7. Appointment overlap detected");
                        return;
                    }
                    if (selectedStart.isBefore(proposedStart) && selectedEnd.isAfter(proposedStart)) {
                        infoDialog("Warning!", "Appointment Overlap Detected", "8. Appointment overlap detected");
                        return;
                    }
                    if (selectedStart.isBefore(proposedEnd) && selectedEnd.isAfter(proposedEnd)) {
                        infoDialog("Warning!", "Appointment Overlap Detected", "9. Appointment overlap detected");
                        return;
                    }
                }

                ZonedDateTime startLDTToZDT = ZonedDateTime.of(newStartDate, newStartTime, ZoneId.systemDefault());
                ZonedDateTime startZDTToZDTEST = startLDTToZDT.withZoneSameInstant(ZoneId.of("America/New_York"));
                LocalTime startAppointmentTimeToCheck = startZDTToZDTEST.toLocalTime();

                ZonedDateTime endLDTToZDT = ZonedDateTime.of(newEndDate, newEndTime, ZoneId.systemDefault());
                ZonedDateTime endZDTToZDTEST = endLDTToZDT.withZoneSameInstant(ZoneId.of("America/New_York"));
                LocalTime endAppointmentTimeToCheck = endZDTToZDTEST.toLocalTime();

                LocalTime startOfBusinessHours = LocalTime.of(8, 0, 0);
                LocalTime endOfBusinessHours = LocalTime.of(22, 0, 0);

                if (startAppointmentTimeToCheck.isBefore(startOfBusinessHours) ||
                        startAppointmentTimeToCheck.isAfter(endOfBusinessHours) ||
                        endAppointmentTimeToCheck.isBefore(startOfBusinessHours) ||
                        endAppointmentTimeToCheck.isAfter(endOfBusinessHours)) {
                    infoDialog("Warning!", "Appointment Outside of Business Hours", "Business hours are 8AM-10PM EST" + "\n"
                            + "Please adjust hours to conform to constraints.");
                    return;
                }

                AppointmentQuery.addAppointment(Integer.parseInt(appointmentIDField.getText()),
                appointmentTitleField.getText(), appointmentDescriptionField.getText(), appointmentLocationField.getText(),
                TypeCombo.getValue(),
                LocalDateTime.of(appointmentDatePickerStart.getValue(), LocalTime.from(appointmentStartComboBox.getValue())),
                LocalDateTime.of(appointmentDatePickerEnd.getValue(), LocalTime.from(appointmentEndComboBox.getValue())),
                        CustomerIDCombo.getValue(), UserIDCombo.getValue(), ContactCombo.getValue());


                infoDialog("Success!", "Appointment Added", "Appointment has been added");
                Parent root = FXMLLoader.load(getClass().getResource("/MainScreen.fxml"));
                Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                Scene scene = new Scene(root, 1000, 500);
                stage.setTitle("WGU Scheduling System");
                stage.setScene(scene);
                stage.show();
                stage.centerOnScreen();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
}



