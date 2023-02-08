package Controllers;

import Helper.AppointmentQuery;
import Models.Appointment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.TimeZone;

/**This class controls, views, and deletes existing Appointments in the database. It also contains all FXML assignments and various methods.*/
public class MainScreen implements Initializable {
    /**This is assigned to the bottom left button titled "Generate".*/
    @FXML public Button ReportsGenerateButton;
    /**This is assigned to the bottom middle button titled "Modify/Change".*/
    @FXML public Button AppointmentModifyButton;
    /**This is assigned to the bottom left button titled "Add New".*/
    @FXML public Button AppointmentAddButton;
    /**This is assigned to the bottom right button titled "Delete".*/
    @FXML public Button AppointmentDeleteButton;
    /**This is assigned to the Table View that contains all Appointments in the database.*/
    @FXML public TableView <Appointment> AppointmentTable;
    /**This is assigned to the bottom right button titled "Log Out".*/
    @FXML public Button LogOutButton;
    /**This is the editable text-field located to the top right of the Appointment Table.*/
    @FXML public TextField appointmentTableQuery;
    /**This is assigned to the Table column within the Appointment Table titled "ID".*/
    @FXML public TableColumn <?, ?> appointmentIDColumn;
    /**This is assigned to the Table column within the Appointment Table titled "Title".*/
    @FXML public TableColumn <?, ?> appointmentTitleColumn;
    /**This is assigned to the Table column within the Appointment Table titled "Description".*/
    @FXML public TableColumn <?, ?> appointmentDescriptionColumn;
    /**This is assigned to the Table column within the Appointment Table titled "Location".*/
    @FXML public TableColumn <?, ?> appointmentLocationColumn;
    /**This is assigned to the Table column within the Appointment Table titled "Type".*/
    @FXML public TableColumn <?, ?> appointmentTypeColumn;
    /**This is assigned to the top right button titled "View Customers".*/
    @FXML public Button customerButton;
    /**This is assigned to the Table column within the Appointment Table titled "Contact".*/
    @FXML public TableColumn <?, ?> appointmentContactIDColumn;
    /**This is assigned to the Table column within the Appointment Table titled "Start Time".*/
    @FXML public TableColumn <?, ?> appointmentStartTimeColumn;
    /**This is assigned to the Table column within the Appointment Table titled "End Time".*/
    @FXML public TableColumn <?, ?> appointmentEndTimeColumn;
    /**This is assigned to the Table column within the Appointment Table titled "Start Date".*/
    @FXML public TableColumn <?, ?> appointmentStartDateColumn;
    /**This is assigned to the Table column within the Appointment Table titled "End Date".*/
    @FXML public TableColumn <?, ?> appointmentEndDateColumn;
    /**This is assigned to the Table column within the Appointment Table titled "Customer ID".*/
    @FXML public TableColumn <?, ?> appointmentCustomerIDColumn;
    /**This is assigned to the Table column within the Appointment Table titled "User ID".*/
    @FXML public TableColumn <?, ?> appointmentUserIDColumn;
    /**This is assigned to the radio button titled "View All".*/
    @FXML public RadioButton viewAllRadioButton;
    /**This is assigned to the radio button titled "View by Week".*/
    @FXML public RadioButton viewByWeekRadioButton;
    /**This is assigned to the radio button titled "View by Month".*/
    @FXML public RadioButton viewByMonthRadioButton;
    /**This is an observable list named appointments.*/
    static ObservableList<Appointment> appointments;
    /**
     * This assigns the log activity to the file named "login_activity.txt".
     */
    LogActivity logActivity = () -> "login_activity.txt";

    /**
     * This is the initialize method.
     * This sets all the table columns within the Appointment Table and sorts the Table based
     * off of the "ID" column.
     * @param url Path to FXML file.
     * @param resourceBundle Contains internationalization properties for GUI.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            ObservableList<Appointment> appointments = AppointmentQuery.getAppointments();
            AppointmentTable.setItems(appointments);
            appointmentIDColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
            appointmentTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
            appointmentDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
            appointmentLocationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
            appointmentTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
            appointmentStartTimeColumn.setCellValueFactory(new PropertyValueFactory<>("startTime"));
            appointmentEndTimeColumn.setCellValueFactory(new PropertyValueFactory<>("endTime"));
            appointmentCustomerIDColumn.setCellValueFactory(new PropertyValueFactory<>("customerID"));
            appointmentContactIDColumn.setCellValueFactory(new PropertyValueFactory<>("contactID"));
            appointmentUserIDColumn.setCellValueFactory(new PropertyValueFactory<>("userID"));


            AppointmentTable.refresh();
            AppointmentTable.getSortOrder().add((TableColumn<Appointment, ?>) appointmentIDColumn);
            Appointment.getAppointmentIDCount();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**This is the To Change View method.
     * If View All radio button is selected, all Appointments in database are displayed in table.
     * If View by Month radio button is selected, Appointments now to one month forward are displayed.
     * If View by Week radio button is selected, Appointments now to one week forward are displayed.
     * @param actionEvent Selection of one of the three radio buttons.
     * @throws Exception Throws an error if failed.
     */
    public void toChangeView(ActionEvent actionEvent) throws Exception {
        if(viewAllRadioButton.isSelected()){
            appointments = AppointmentQuery.getAppointments();
            AppointmentTable.setItems(appointments);
            AppointmentTable.refresh();
        }
        if(viewByMonthRadioButton.isSelected()){
            appointments = AppointmentQuery.getAppointmentsByMonth();
            AppointmentTable.setItems(appointments);
            AppointmentTable.refresh();
        }
        if(viewByWeekRadioButton.isSelected()){
            appointments = AppointmentQuery.getAppointmentsByWeek();
            AppointmentTable.setItems(appointments);
            AppointmentTable.refresh();
        }
    }

    /**This is the To View Customers method.
     * The user is asked to confirm screen change. If User clicks "OK", User is directed to CustomerView.FXML.
     * This method also contains a Lambda expression that waits for a response from user.
     * @param actionEvent The User clicks on button titled "View Customers".
     * @throws IOException Throws an Input/Output error if failed.
     */
    public void toViewCustomers(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Confirm");
        alert.setHeaderText("Confirm Change");
        alert.setContentText("Are you sure you want to View Customers?");
        alert.showAndWait().ifPresent((response -> {
            if (response == ButtonType.OK) {
                System.out.println("Alerting!");
                Parent main = null;
                try {
                    Parent root = FXMLLoader.load(getClass().getResource("/CustomerView.fxml"));
                    Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                    Scene scene = new Scene(root, 775, 500);
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
     * This is the Logout Success method.
     * The text file "login_activity.txt" is obtained.
     * Successful logout with timestamp is written to text file.
     */
    private void logoutSuccess() {
        try {
            FileWriter fileWriter = new FileWriter(logActivity.getFileName(), true);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            Date date = new Date(System.currentTimeMillis());
            fileWriter.write("Successful Logout: TimeStamp: " + simpleDateFormat.format(date) + "\n");
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This is the Login Screen method.
     * User is prompted with a confirmation dialog. If user clicks "OK", user is directed to LoginScreen.FXML.
     * If user clicks "Cancel", user is redirected back to Customer View.
     * @param actionEvent User clicks on button titled "Log Out" to the bottom right of Appointment Table.
     * @throws IOException Throws Input/Output error if failed.
     */
    public void ToLoginScreen(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Confirm Exit");
        alert.setHeaderText("Confirm Exit");
        alert.setContentText("Are you sure you want to log out?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            logoutSuccess();
            Parent root = FXMLLoader.load(getClass().getResource("/LoginScreen.fxml"));
            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 425, 475);
            stage.setTitle("WGU Scheduling System");
            stage.setScene(scene);
            stage.show();
            stage.centerOnScreen();
        }
    }

    /**
     * This is the Reports Screen method.
     * The user is asked to confirm screen change. If User clicks "OK", User is directed to ReportsScreen.FXML.
     * This method also contains a Lambda expression that waits for a response from user.
     * @param actionEvent User clicks on button titled "Generate" to the right of Customer Table.
     * @throws IOException Throws Input/Output error if failed.
     */
    public void toReportsScreen(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Confirm");
        alert.setHeaderText("Confirm Change");
        alert.setContentText("Are you sure you want to View Reports?");
        alert.showAndWait().ifPresent((response -> {
            if (response == ButtonType.OK) {
                System.out.println("Alerting!");
                Parent main = null;
                try {
                    Parent root = FXMLLoader.load(getClass().getResource("/ReportsScreen.fxml"));
                    Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                    Scene scene = new Scene(root, 775, 550);
                    stage.setTitle("Reports");
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
     * This is the Add Appointment method.
     * The user is directed to AppointmentAdd.FXML.
     * @param actionEvent User clicks on button titled "Add" to the right of Appointment Table.
     * @throws Exception Throws error if failed.
     */
    public void toAddAppointmentScreen(ActionEvent actionEvent) throws Exception {
        Appointment.getAppointmentIDCount();
        Parent root = FXMLLoader.load(getClass().getResource("/AppointmentAdd.fxml"));
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 600, 400);
        stage.setTitle("WGU Scheduling System");
        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();
    }

    /**
     * This is the Modify Appointment method.
     * The user is directed to AppointmentModify.FXML if an appointment is selected.
     * If an appointment has not been selected, user is redirected back to the MainScreen.FXML.
     * @param actionEvent User clicks on button titled "Modify/Change" to the right of Appointment Table.
     * @throws Exception Throws error if failed.
     */
    public void toModifyAppointmentScreen(ActionEvent actionEvent) throws Exception {
        if (AppointmentTable.getSelectionModel().isEmpty()) {
            infoDialog("Warning!", "No Appointment Selected", "Please choose an appointment from the above list");
        } else {
            AppointmentModify.receiveSelectedAppointment(AppointmentTable.getSelectionModel().getSelectedItem());
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/AppointmentModify.fxml"));
            Parent modifyAppts = loader.load();
            Scene scene = new Scene(modifyAppts);
            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            stage.setTitle("WGU Scheduling System");
            stage.setScene(scene);
            stage.show();
            stage.centerOnScreen();
        }
    }

    /**
     * This is the Delete Appointment method.
     * If user has not selected an appointment, user is shown error message and redirected back to Main Screen.
     * If user has selected an appointment, user is prompted to continue. If user clicks cancel, changes are aborted.
     * If user clicks "OK", changes are saved and chosen appointment is deleted from the database. Appointment Table
     * is refreshed to reflect changes.
     * @param actionEvent User clicks on button titled "Delete" to the bottom right of Appointment Table.
     * @throws Exception Throws error if failed.
     */
    public void onDeleteAppointment(ActionEvent actionEvent) throws Exception {
        if (AppointmentTable.getSelectionModel().isEmpty()) {
            infoDialog("Warning!", "No Appointment Selected", "Please choose an appointment from the above list");
            return;
        }
        if (confirmDialog("Warning", "Would you like to delete this Appointment?")) {
            infoDialog("Success!", "Appointment Deleted", "The following appointment has been deleted: " + "\n"
            + "Appointment ID: " + AppointmentTable.getSelectionModel().getSelectedItem().getAppointmentID() + "\n"
            + "Appointment Title: " + AppointmentTable.getSelectionModel().getSelectedItem().getTitle() + "\n"
            + "Appointment Description: " + AppointmentTable.getSelectionModel().getSelectedItem().getDescription() + "\n"
            + "Appointment Location: " + AppointmentTable.getSelectionModel().getSelectedItem().getLocation() + "\n"
            + "Appointment Type: " + AppointmentTable.getSelectionModel().getSelectedItem().getType() + "\n"
            + "Appointment Start: " + AppointmentTable.getSelectionModel().getSelectedItem().getStartTime() + "\n"
            + "Appointment End: " + AppointmentTable.getSelectionModel().getSelectedItem().getEndTime());
            int selectedAppointment = AppointmentTable.getSelectionModel().getSelectedItem().getAppointmentID();
            AppointmentQuery.deleteAppointment(selectedAppointment);
            AppointmentTable.setItems(AppointmentQuery.getAppointments());
            AppointmentTable.refresh();
            AppointmentTable.getSortOrder().add((TableColumn<Appointment, ?>) appointmentIDColumn);
        }
    }

    /**
     * This is the Lookup by Appointment ID Method.
     * All Appointments within the database are obtained through a query.
     * A for loop is entered that compares all Appointment ID's with the user entered text.
     * If a match is found, return the Appointment.
     * If loop is exited without a match, return nothing.
     * @param searchedAppointmentID Any number the user entered.
     * @return Return an Appointment with matching Appointment ID, or return nothing.
     * @throws Exception Throws error if failed.
     */
    public static Appointment lookupByAppointmentID(int searchedAppointmentID) throws Exception {
        ObservableList<Appointment> appointments = AppointmentQuery.getAppointments();
        for (int i = 0; i < appointments.size(); i++) {
            Appointment at = appointments.get(i);
            if (at.getAppointmentID() == searchedAppointmentID) {
                return at;
            }
        }
        return null;
    }

    /**
     * This is the Lookup by Appointment Title method.
     * If user does not type anything and hits "Enter", all Appointments in database will display.
     * If user types in characters, for loop is entered that compares the entered text versus all Appointment titles.
     * For ease, everything is compared in lower case, not requiring user to capitalize searches.
     * If match is found, Appointment is added to namedAppointments observable list.
     * @param partialTitle Any set of characters the user types into the text-field.
     * @return Any Appointments within the database that match the characters the user entered.
     * @throws Exception Throws error if failed.
     */
    public static ObservableList<Appointment> lookupByAppointmentTitle(String partialTitle) throws Exception {
        ObservableList<Appointment> namedAppointments = FXCollections.observableArrayList();
        ObservableList<Appointment> allAppointments = AppointmentQuery.getAppointments();

        if (partialTitle.length() == 0) {
            namedAppointments = allAppointments;
        } else {
            for (int i = 0; i < allAppointments.size(); i++) {
                if (allAppointments.get(i).getTitle().toLowerCase().contains(partialTitle.toLowerCase())) {
                    namedAppointments.add(allAppointments.get(i));
                }
            }
        }
        return namedAppointments;
    }

    /**
     * This is the Get Appointment Results method.
     * It gathers the text entered into the text field and runs the "lookupByAppointmentTitle" method first.
     * If no matches are found, a loop is entered that compares the entered text to all Appointment ID's.
     * If a match is found, result is displayed within the Appointment Table.
     * If no matches are found, an alert pops up indicating no matches were found.
     * Search bar auto-resets to allow user to reattempt to locate another part.
     * @param actionEvent typing enter after desired characters have been entered in text-field.
     * @throws Exception Throws error if failed.
     */
    public void getAppointmentResultsHandler(ActionEvent actionEvent) throws Exception {
        String at = appointmentTableQuery.getText();

        ObservableList<Appointment> appointments = lookupByAppointmentTitle(at);

        if (appointments.size() == 0) {
            try {
                int appt = Integer.parseInt(at);
                Appointment A = lookupByAppointmentID(appt);
                if (A != null)
                    appointments.add(A);
            } catch (NumberFormatException e) {
            }
        }
        AppointmentTable.setItems(appointments);
        appointmentTableQuery.setText("");
        if (appointments.size() == 0) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Input not valid");
            errorAlert.setContentText("Unable to locate appointment");
            errorAlert.showAndWait();
            AppointmentTable.setItems(AppointmentQuery.getAppointments());
        }
    }

    /**This is the Confirmation Dialog method.
     * This displays a confirmation alert if method is called.
     * @param title Allows for the Title to be set within other methods.
     * @param content Allows for the content to be set within other methods.
     * @return If true, alert is displayed. If false, nothing is displayed.
     */
    static boolean confirmDialog (String title, String content){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText("Confirm");
        alert.setContentText(content);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * This is the Information Dialog method.
     * This displays an information alert when the method is called.
     * @param title   This allows for the title to be set within other methods.
     * @param header  This allows for the header to be set within other methods.
     * @param content Allows for content to be set within other methods.
     * @return Returns nothing.
     */
    static Optional<ButtonType> infoDialog(String title, String header, String content){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
        return null;
    }
}
