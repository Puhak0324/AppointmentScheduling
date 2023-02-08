package Controllers;

import Helper.AppointmentQuery;
import Helper.CustomerQuery;
import Models.Appointment;
import Models.Customer;
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
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.util.ResourceBundle;

import static Controllers.MainScreen.infoDialog;

/**This class controls and views various reports obtains from the database. It also contains all FXML assignments and various methods.*/
public class ReportsGenerated implements Initializable {
    /**This is assigned to the Combo box next to label "Month" below the header "Customer Appointments By Month".*/
    @FXML public ComboBox<String> customerMonthComboBox;
    /**This is assigned to the Combo box next to label "Type" below the header "Customer Appointments By Type".*/
    @FXML public ComboBox<String> customerTypeComboBox;
    /**This is assigned to the Table View that contains all Appointments  in the database specified by Contact.*/
    @FXML public TableView<Appointment> scheduleTable;
    /**This is assigned to the Table column within the Schedule Table titled "ID".*/
    @FXML public TableColumn<?, ?> appointmentIDColumn;
    /**This is assigned to the Table column within the Schedule Table titled "Title"".*/
    @FXML public TableColumn<?, ?> titleColumn;
    /**This is assigned to the Table column within the Schedule Table titled "Description".*/
    @FXML public TableColumn<?, ?> descriptionColumn;
    /**This is assigned to the Table column within the Schedule Table titled "Start Time".*/
    @FXML public TableColumn<?, ?> startTimeColumn;
    /**This is assigned to the Table column within the Schedule Table titled "End Time".*/
    @FXML public TableColumn<?, ?> endTimeColumn;
    /**This is assigned to the Table column within the Schedule Table titled "Customer ID".*/
    @FXML public TableColumn<?, ?> customerIDColumn;
    /**This is assigned to the Combo box below the label "Contact".*/
    @FXML public ComboBox<String> contactScheduleComboBox;
    /**This is assigned to the Combo box next to label "Month" below the header "Contact Appointments By Month".*/
    @FXML public ComboBox<String> contactMonthComboBox;
    /**This is assigned to the Combo box next to label "Type" below the header "Contact Appointments By Type".*/
    @FXML public ComboBox<String> contactTypeComboBox;
    /**This is assigned to the button titled "Back to Appointments".*/
    @FXML public Button appointmentsButton;
    /**This is assigned to the button titled "Back to Customers".*/
    @FXML public Button customerButton;
    /**This is assigned to the button titled "Log Out".*/
    @FXML public Button logoutButton;
    /**This is assigned to the right of the label "Total" below the header "Contact Appointments By Type".*/
    @FXML public Label customerTotalLabelByType;
    /**This is assigned to the Combo box next to label "Customer ID" below the header "Customer Appointments By Type".*/
    @FXML public ComboBox<Integer> CustomerIDComboBoxType;
    /**This is assigned to the Combo box next to label "Customer ID" below the header "Customer Appointments By Month".*/
    @FXML public ComboBox<Integer> CustomerIDComboBoxMonth;
    /**This is assigned to the right of the label "Total" below the header "Customer Appointments By Month".*/
    @FXML public Label customerTotalLabelByMonth;
    /**This is assigned to the right of the label "Total" below the header "Contact Appointments By Type".*/
    @FXML public Label contactTotalLabelType;
    /**This is assigned to the Combo box next to label "Contact ID" below the header "Contact Appointments By Type".*/
    @FXML public ComboBox <String> ContactIDComboBoxType;
    /**This is assigned to the right of the label "Total" below the header "Contact Appointments By Month".*/
    @FXML public Label contactTotalLabelMonth;
    /**This is assigned to the Combo box next to label "Contact ID" below the header "Contact Appointments By Month".*/
    @FXML public ComboBox <String> ContactIDComboBoxMonth;
    /**
     * This assigns the log activity to the file named "login_activity.txt".
     */
    LogActivity logActivity = () -> "login_activity.txt";

    /**
     * This is the initialize method.
     * This populates all combo boxes and sets all the table columns within the Schedule Table
     * and sorts the Table based off of the "ID" column.
     * @param url Path to FXML file.
     * @param resourceBundle Contains internationalization properties for GUI.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            populateContactComboBox();
            populateTypeComboBox();
            populateCustomerIDComboBox();
            populateMonthComboBox();
            appointmentIDColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
            titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
            descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
            startTimeColumn.setCellValueFactory(new PropertyValueFactory<>("startTime"));
            endTimeColumn.setCellValueFactory(new PropertyValueFactory<>("endTime"));
            customerIDColumn.setCellValueFactory(new PropertyValueFactory<>("customerID"));
            scheduleTable.refresh();
            scheduleTable.getSortOrder().add((TableColumn<Appointment, ?>) appointmentIDColumn);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**This is the Populate Contact Combo box method.
     * This creates an observable list named contactComboList, then adds all specified Contacts.
     * All Contacts are then set in the Contact Combo boxes.
     */
    private void populateContactComboBox() {
        ObservableList<String> contactComboList = FXCollections.observableArrayList();
        contactComboList.addAll("Anika Costa", "Daniel Garcia", "Li Lee");
        contactScheduleComboBox.setItems(contactComboList);
        ContactIDComboBoxType.setItems(contactComboList);
        ContactIDComboBoxMonth.setItems(contactComboList);
    }

    /**This is the Populate Type Combo box method.
     * This creates an observable list titled "typeList" and adds all specified types. All types are
     * then set in the Type Combo boxes.
     */
    private void populateTypeComboBox() {
        ObservableList<String> typeList = FXCollections.observableArrayList();
        typeList.addAll("Planning Session", "De-Briefing", "Follow-up", "Pre-Briefing", "Open Session");
        customerTypeComboBox.setItems(typeList);
        contactTypeComboBox.setItems(typeList);
    }

    /**This is the Populate Customer ID Combo box method.
     * This creates an observable list named customerIDComboList, then queries all Customers in
     * the database. All Customer IDs are added to the customerIDComboList. The Combo boxes are then
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
        CustomerIDComboBoxType.setItems(customerIDComboList);
        CustomerIDComboBoxMonth.setItems(customerIDComboList);
    }

    /**This is the Populate Month Combo box method.
     * This creates an observable list titled "MonthList" and adds all specified months. All months are
     * then set in the Month Combo boxes.
     */
    private void populateMonthComboBox() {
        ObservableList<String> MonthList = FXCollections.observableArrayList();
        MonthList.addAll("January", "February", "March", "April", "May", "June", "July", "August",
                "September", "October", "November", "December");
        customerMonthComboBox.setItems(MonthList);
        contactMonthComboBox.setItems(MonthList);
    }

    /**
     * This is the To Main Screen Cancel method.
     * The user is asked to confirm screen change. If User clicks "OK", User is directed to MainScreen.FXML.
     * This method also contains a Lambda expression that waits for a response from user.
     * @param actionEvent User clicks on button titled "Back to Appointments".
     * @throws IOException Throws Input/Output error if failed.
     */
    public void toMainScreenCancel(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Confirm Exit");
        alert.setHeaderText("Confirm Exit");
        alert.setContentText("Are you sure you want to exit to Appointments?");
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
     * @param actionEvent User clicks on button titled "Log Out" to the bottom right of Schedule Table.
     * @throws IOException Throws Input/Output error if failed.
     */
    public void toLoginScreen(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Confirm Exit");
        alert.setHeaderText("Confirm Exit");
        alert.setContentText("Are you sure you want to log out?");
        alert.showAndWait().ifPresent((response -> {
            if (response == ButtonType.OK) {
                System.out.println("Alerting!");
                Parent main = null;
                try {
                    logoutSuccess();
                    Parent root = FXMLLoader.load(getClass().getResource("/LoginScreen.fxml"));
                    Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                    Scene scene = new Scene(root, 425, 475);
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

    /**This is the To Customer Screen Cancel method.
     * The user is asked to confirm screen change. If User clicks "OK", User is directed to CustomerView.FXML.
     * This method also contains a Lambda expression that waits for a response from user.
     * @param actionEvent The User clicks on button titled "Back to Customers".
     * @throws IOException Throws an Input/Output error if failed.
     */
    public void toCustomerScreenCancel(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Confirm Exit");
        alert.setHeaderText("Confirm Exit");
        alert.setContentText("Are you sure you want to exit to Customers?");
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

    /**This is the Generate Appointments method.
     * This gathers the selection from the contactScheduleCombBox. A query is made that searches for all
     * appointments from that selected Contact ID. The Schedule Table is set with any matching appointments.
     * @param event User clicks on the button titled "GO" to the right of the Schedule Table.
     * @throws Exception Throws an error if failed.
     */
    @FXML void GenerateAppointments(ActionEvent event) throws Exception {
        String contactID = String.valueOf(contactScheduleComboBox.getSelectionModel().getSelectedItem());
        ObservableList<Appointment> appointments = AppointmentQuery.getAppointmentsByContactID(contactID);
        scheduleTable.setItems(appointments);
        scheduleTable.refresh();
    }

    /**This is the Generate Customer Total Appointments by Type method.
     * This gathers both values retrieved from the Customer ID combo box and Customer Type combo box.
     * If either combo box was left empty, an info dialog box displays alerting the User. A query is made
     * to retrieve all appointments based off Customer ID. A loop is entered that sets the Customer Total
     * by Type label to the total number of appointments based off Type.
     * @param actionEvent The user clicks on button titled "Go!" below the header "Customer
     *                    Appointments By Type".
     */
    public void GenerateCustomerTotalApptsType(ActionEvent actionEvent) {
        Integer newID = CustomerIDComboBoxType.getValue();
        String newType = customerTypeComboBox.getValue();
        if (newID == null) {
            infoDialog("Warning!", "No input detected", "Customer ID must contain a value");
            CustomerIDComboBoxType.setPromptText("Enter value here!");
            return;
        }
        if (newType == null) {
            infoDialog("Warning!", "No input detected", "Customer type must contain a value");
            customerTypeComboBox.setPromptText("Enter value here!");
            return;
        }
        ObservableList<String> planning = FXCollections.observableArrayList();
        ObservableList<String> debriefing = FXCollections.observableArrayList();
        ObservableList<String> followup = FXCollections.observableArrayList();
        ObservableList<String> prebriefing = FXCollections.observableArrayList();
        ObservableList<String> open = FXCollections.observableArrayList();

        String customerID = String.valueOf(CustomerIDComboBoxType.getSelectionModel().getSelectedItem());
        try {
            ObservableList<Appointment> appointments = AppointmentQuery.getAppointmentsByCustomerID(Integer.parseInt(customerID));
            if (appointments != null) {
                for (Appointment appointment : appointments) {
                    String type = appointment.getType();

                    if (type.equals("Planning Session")) {
                        planning.add(type);
                    }

                    if (type.equals("De-Briefing")) {
                        debriefing.add(type);
                    }

                    if (type.equals("Follow-up")) {
                        followup.add(type);
                    }

                    if (type.equals("Pre-Briefing")) {
                        prebriefing.add(type);
                    }

                    if (type.equals("Open Session")) {
                        open.add(type);
                    }

                    if (customerTypeComboBox.getValue().equals("Planning Session")) {
                        customerTotalLabelByType.setText(String.valueOf(planning.size()));
                    }
                    if (customerTypeComboBox.getValue().equals("De-Briefing")) {
                        customerTotalLabelByType.setText(String.valueOf(debriefing.size()));
                    }
                    if (customerTypeComboBox.getValue().equals("Follow-up")) {
                        customerTotalLabelByType.setText(String.valueOf(followup.size()));
                    }
                    if (customerTypeComboBox.getValue().equals("Pre-Briefing")) {
                        customerTotalLabelByType.setText(String.valueOf(prebriefing.size()));
                    }
                    if (customerTypeComboBox.getValue().equals("Open Session")) {
                        customerTotalLabelByType.setText(String.valueOf(open.size()));
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**This is the Generate Customer Total Appointments by Month method.
     * This gathers both values retrieved from the Customer ID combo box and Customer Month combo box.
     * If either combo box was left empty, an info dialog box displays alerting the User. A query is made
     * to retrieve all appointments based off Customer ID. A loop is entered that sets the Customer Total
     * by Month label to the total number of appointments based off Month.
     * @param actionEvent The user clicks on button titled "Go!" below the header "Customer
     *                    Appointments By Month".
     */
    public void GenerateCustomerTotalApptsMonth(ActionEvent actionEvent) throws Exception {
        Integer newID = CustomerIDComboBoxMonth.getValue();
        String newMonth = customerMonthComboBox.getValue();
        if (newID == null) {
            infoDialog("Warning!", "No input detected", "Customer ID must contain a value");
            CustomerIDComboBoxMonth.setPromptText("Enter value here!");
            return;
        }
        if (newMonth == null) {
            infoDialog("Warning!", "No input detected", "Customer month must contain a value");
            customerMonthComboBox.setPromptText("Enter value here!");
            return;
        }
        ObservableList<Integer> January = FXCollections.observableArrayList();
        ObservableList<Integer> February = FXCollections.observableArrayList();
        ObservableList<Integer> March = FXCollections.observableArrayList();
        ObservableList<Integer> April = FXCollections.observableArrayList();
        ObservableList<Integer> May = FXCollections.observableArrayList();
        ObservableList<Integer> June = FXCollections.observableArrayList();
        ObservableList<Integer> July = FXCollections.observableArrayList();
        ObservableList<Integer> August = FXCollections.observableArrayList();
        ObservableList<Integer> September = FXCollections.observableArrayList();
        ObservableList<Integer> October = FXCollections.observableArrayList();
        ObservableList<Integer> November = FXCollections.observableArrayList();
        ObservableList<Integer> December = FXCollections.observableArrayList();

        String customerID = String.valueOf(CustomerIDComboBoxMonth.getSelectionModel().getSelectedItem());
        try {
            ObservableList<Appointment> appointments = AppointmentQuery.getAppointmentsByCustomerID(Integer.parseInt(customerID));
            if (appointments != null) {
                for (Appointment appointment : appointments) {
                    LocalDate date = appointment.getStartDate();

                    if (date.getMonth().equals(Month.of(1))) {
                        January.add(date.getMonthValue());
                    }

                    if (date.getMonth().equals(Month.of(2))) {
                        February.add(date.getMonthValue());
                    }

                    if (date.getMonth().equals(Month.of(3))) {
                        March.add(date.getMonthValue());
                    }

                    if (date.getMonth().equals(Month.of(4))) {
                        April.add(date.getMonthValue());
                    }

                    if (date.getMonth().equals(Month.of(5))) {
                        May.add(date.getMonthValue());
                    }

                    if (date.getMonth().equals(Month.of(6))) {
                        June.add(date.getMonthValue());
                    }

                    if (date.getMonth().equals(Month.of(7))) {
                        July.add(date.getMonthValue());
                    }

                    if (date.getMonth().equals(Month.of(8))) {
                        August.add(date.getMonthValue());
                    }

                    if (date.getMonth().equals(Month.of(9))) {
                        September.add(date.getMonthValue());
                    }

                    if (date.getMonth().equals(Month.of(10))) {
                        October.add(date.getMonthValue());
                    }

                    if (date.getMonth().equals(Month.of(11))) {
                        November.add(date.getMonthValue());
                    }

                    if (date.getMonth().equals(Month.of(12))) {
                        December.add(date.getMonthValue());
                    }
                    if (customerMonthComboBox.getValue().equals("January")) {
                        customerTotalLabelByMonth.setText(String.valueOf(January.size()));
                    }
                    if (customerMonthComboBox.getValue().equals("February")) {
                        customerTotalLabelByMonth.setText(String.valueOf(February.size()));
                    }
                    if (customerMonthComboBox.getValue().equals("March")) {
                        customerTotalLabelByMonth.setText(String.valueOf(March.size()));
                    }
                    if (customerMonthComboBox.getValue().equals("April")) {
                        customerTotalLabelByMonth.setText(String.valueOf(April.size()));
                    }
                    if (customerMonthComboBox.getValue().equals("May")) {
                        customerTotalLabelByMonth.setText(String.valueOf(May.size()));
                    }
                    if (customerMonthComboBox.getValue().equals("June")) {
                        customerTotalLabelByMonth.setText(String.valueOf(June.size()));
                    }
                    if (customerMonthComboBox.getValue().equals("July")) {
                        customerTotalLabelByMonth.setText(String.valueOf(July.size()));
                    }
                    if (customerMonthComboBox.getValue().equals("August")) {
                        customerTotalLabelByMonth.setText(String.valueOf(August.size()));
                    }
                    if (customerMonthComboBox.getValue().equals("September")) {
                        customerTotalLabelByMonth.setText(String.valueOf(September.size()));
                    }
                    if (customerMonthComboBox.getValue().equals("October")) {
                        customerTotalLabelByMonth.setText(String.valueOf(October.size()));
                    }
                    if (customerMonthComboBox.getValue().equals("November")) {
                        customerTotalLabelByMonth.setText(String.valueOf(November.size()));
                    }
                    if (customerMonthComboBox.getValue().equals("December")) {
                        customerTotalLabelByMonth.setText(String.valueOf(December.size()));
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**This is the Generate Contact Total Appointments by Type method.
     *This gathers both values retrieved from the Contact ID combo box and Contact Type combo box.
     * If either combo box was left empty, an info dialog box displays alerting the User. A query is made
     * to retrieve all appointments based off Contact ID. A loop is entered that sets the Contact Total
     * by Type label to the total number of appointments based off Type.
     * @param actionEvent The user clicks on button titled "Go!" below the header "Contact
     *                    Appointments By Type".
     */
    public void GenerateContactTotalApptsType(ActionEvent actionEvent) {
        String newID = ContactIDComboBoxType.getValue();
        String newType = contactTypeComboBox.getValue();
        if (newID == null) {
            infoDialog("Warning!", "No input detected", "Contact ID must contain a value");
            ContactIDComboBoxType.setPromptText("Enter value here!");
            return;
        }
        if (newType == null) {
            infoDialog("Warning!", "No input detected", "Contact type must contain a value");
            contactTypeComboBox.setPromptText("Enter value here!");
            return;
        }
        ObservableList<String> planning = FXCollections.observableArrayList();
        ObservableList<String> debriefing = FXCollections.observableArrayList();
        ObservableList<String> followup = FXCollections.observableArrayList();
        ObservableList<String> prebriefing = FXCollections.observableArrayList();
        ObservableList<String> open = FXCollections.observableArrayList();

        String contactID = String.valueOf(ContactIDComboBoxType.getSelectionModel().getSelectedItem());
        try {
            ObservableList<Appointment> appointments = AppointmentQuery.getAppointmentsByContactID(contactID);
            if (appointments != null) {
                for (Appointment appointment : appointments) {
                    String type = appointment.getType();

                    if (type.equals("Planning Session")) {
                        planning.add(type);
                    }

                    if (type.equals("De-Briefing")) {
                        debriefing.add(type);
                    }

                    if (type.equals("Follow-up")) {
                        followup.add(type);
                    }

                    if (type.equals("Pre-Briefing")) {
                        prebriefing.add(type);
                    }

                    if (type.equals("Open Session")) {
                        open.add(type);
                    }
                    if (contactTypeComboBox.getValue().equals("Planning Session")) {
                        contactTotalLabelType.setText(String.valueOf(planning.size()));
                    }
                    if (contactTypeComboBox.getValue().equals("De-Briefing")) {
                        contactTotalLabelType.setText(String.valueOf(debriefing.size()));
                    }
                    if (contactTypeComboBox.getValue().equals("Follow-up")) {
                        contactTotalLabelType.setText(String.valueOf(followup.size()));
                    }
                    if (contactTypeComboBox.getValue().equals("Pre-Briefing")) {
                        contactTotalLabelType.setText(String.valueOf(prebriefing.size()));
                    }
                    if (contactTypeComboBox.getValue().equals("Open Session")) {
                        contactTotalLabelType.setText(String.valueOf(open.size()));
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**This is the Generate Contact Total Appointments by Month method.
     * This gathers both values retrieved from the Contact ID combo box and Customer Month combo box.
     * If either combo box was left empty, an info dialog box displays alerting the User. A query is made
     * to retrieve all appointments based off Contact ID. A loop is entered that sets the Contact Total
     * by Month label to the total number of appointments based off Month.
     * @param actionEvent The user clicks on button titled "Go!" below the header "Contact
     *                    Appointments By Month".
     */
    public void GenerateContactTotalApptsMonth(ActionEvent actionEvent) {
        String newID = ContactIDComboBoxMonth.getValue();
        String newMonth = contactMonthComboBox.getValue();
        if (newID == null) {
            infoDialog("Warning!", "No input detected", "Contact ID must contain a value");
            ContactIDComboBoxMonth.setPromptText("Enter value here!");
            return;
        }
        if (newMonth == null) {
            infoDialog("Warning!", "No input detected", "Contact month must contain a value");
            contactMonthComboBox.setPromptText("Enter value here!");
            return;
        }
        ObservableList<Integer> January = FXCollections.observableArrayList();
        ObservableList<Integer> February = FXCollections.observableArrayList();
        ObservableList<Integer> March = FXCollections.observableArrayList();
        ObservableList<Integer> April = FXCollections.observableArrayList();
        ObservableList<Integer> May = FXCollections.observableArrayList();
        ObservableList<Integer> June = FXCollections.observableArrayList();
        ObservableList<Integer> July = FXCollections.observableArrayList();
        ObservableList<Integer> August = FXCollections.observableArrayList();
        ObservableList<Integer> September = FXCollections.observableArrayList();
        ObservableList<Integer> October = FXCollections.observableArrayList();
        ObservableList<Integer> November = FXCollections.observableArrayList();
        ObservableList<Integer> December = FXCollections.observableArrayList();

        String contactID = ContactIDComboBoxMonth.getSelectionModel().getSelectedItem();
        if (contactID.equals("Li Lee")){
            contactID = "3";
        }
        if(contactID.equals("Daniel Garcia")){
            contactID = "2";
        }
        if(contactID.equals("Anika Costa")){
            contactID = "1";
        }
        try {
            ObservableList<Appointment> appointments = AppointmentQuery.getAppointmentsByContactID(contactID);
            if (appointments != null) {
                for (Appointment appointment : appointments) {
                    LocalDate date = appointment.getStartDate();

                    if (date.getMonth().equals(Month.of(1))) {
                        January.add(date.getMonthValue());
                    }

                    if (date.getMonth().equals(Month.of(2))) {
                        February.add(date.getMonthValue());
                    }

                    if (date.getMonth().equals(Month.of(3))) {
                        March.add(date.getMonthValue());
                    }

                    if (date.getMonth().equals(Month.of(4))) {
                        April.add(date.getMonthValue());
                    }

                    if (date.getMonth().equals(Month.of(5))) {
                        May.add(date.getMonthValue());
                    }

                    if (date.getMonth().equals(Month.of(6))) {
                        June.add(date.getMonthValue());
                    }

                    if (date.getMonth().equals(Month.of(7))) {
                        July.add(date.getMonthValue());
                    }

                    if (date.getMonth().equals(Month.of(8))) {
                        August.add(date.getMonthValue());
                    }

                    if (date.getMonth().equals(Month.of(9))) {
                        September.add(date.getMonthValue());
                    }

                    if (date.getMonth().equals(Month.of(10))) {
                        October.add(date.getMonthValue());
                    }

                    if (date.getMonth().equals(Month.of(11))) {
                        November.add(date.getMonthValue());
                    }

                    if (date.getMonth().equals(Month.of(12))) {
                        December.add(date.getMonthValue());
                    }
                    if (contactMonthComboBox.getValue().equals("January")) {
                        contactTotalLabelMonth.setText(String.valueOf(January.size()));
                    }
                    if (contactMonthComboBox.getValue().equals("February")) {
                        contactTotalLabelMonth.setText(String.valueOf(February.size()));
                    }
                    if (contactMonthComboBox.getValue().equals("March")) {
                        contactTotalLabelMonth.setText(String.valueOf(March.size()));
                    }
                    if (contactMonthComboBox.getValue().equals("April")) {
                        contactTotalLabelMonth.setText(String.valueOf(April.size()));
                    }
                    if (contactMonthComboBox.getValue().equals("May")) {
                        contactTotalLabelMonth.setText(String.valueOf(May.size()));
                    }
                    if (contactMonthComboBox.getValue().equals("June")) {
                        contactTotalLabelMonth.setText(String.valueOf(June.size()));
                    }
                    if (contactMonthComboBox.getValue().equals("July")) {
                        contactTotalLabelMonth.setText(String.valueOf(July.size()));
                    }
                    if (contactMonthComboBox.getValue().equals("August")) {
                        contactTotalLabelMonth.setText(String.valueOf(August.size()));
                    }
                    if (contactMonthComboBox.getValue().equals("September")) {
                        contactTotalLabelMonth.setText(String.valueOf(September.size()));
                    }
                    if (contactMonthComboBox.getValue().equals("October")) {
                        contactTotalLabelMonth.setText(String.valueOf(October.size()));
                    }
                    if (contactMonthComboBox.getValue().equals("November")) {
                        contactTotalLabelMonth.setText(String.valueOf(November.size()));
                    }
                    if (contactMonthComboBox.getValue().equals("December")) {
                        contactTotalLabelMonth.setText(String.valueOf(December.size()));
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**This is the Reset Table View method.
     * This sets the Schedule Table to null, which clears it of any contents.
     * @param actionEvent The User clicks on the button titled "Reset".
     */
    public void resetTableView(ActionEvent actionEvent) {
        scheduleTable.setItems(null);
    }
}
