package Controllers;

import Helper.AppointmentQuery;
import Helper.CustomerQuery;
import Models.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
import java.util.Optional;
import java.util.ResourceBundle;

import static Controllers.MainScreen.infoDialog;

/**This class controls, views, and deletes existing Customers in the database. It also contains all FXML assignments and various methods.*/
public class CustomerView implements Initializable {
    /**
     * This is the editable text-field located to the top left of the Customer Table.
     */
    public TextField customerTableQuery;
    /**
     * This is assigned to the bottom right button labeled "Log Out".
     */
    public Button LogOutButton;
    /**
     * This is assigned to the top right button labeled "Generate".
     */
    public Button ReportsGenerateButton;
    /**
     * This is assigned to the button on the right of the Customer Table labeled "Modify/Change".
     */
    public Button CustomerModifyButton;
    /**
     * This is assigned to the button on the right of the Customer Table labeled "Add New".
     */
    public Button CustomerAddButton;
    /**
     * This is assigned to the button on the right of the Customer Table labeled "Delete".
     */
    public Button CustomerDeleteButton;
    /**
     * This is assigned to the button on top right of the Customer Table labeled "View Appointments".
     */
    public Button appointmentsButton;
    /**
     * This is assigned to the Table View that contains all Customers in the database.
     */
    public TableView<Customer> CustomerTable;
    /**
     * This is assigned to the Table column within the Customer Table titled "ID".
     */
    public TableColumn<?, ?> customerIDColumn;
    /**
     * This is assigned to the Table column within the Customer Table titled "Customer Name".
     */
    public TableColumn<?, ?> customerNameColumn;
    /**
     * This is assigned to the Table column within the Customer Table titled "Address".
     */
    public TableColumn<?, ?> customerAddressColumn;
    /**
     * This is assigned to the Table column within the Customer Table titled "Postal Code".
     */
    public TableColumn<?, ?> customerPostalColumn;
    /**
     * This is assigned to the Table column within the Customer Table titled "Phone Number".
     */
    public TableColumn<?, ?> customerPhoneColumn;
    /**
     * This is assigned to the Table column within the Customer Table titled "Division ID".
     */
    public TableColumn<?, ?> customerStateColumn;
    /**
     * This assigns the log activity to the file named "login_activity.txt".
     */
    LogActivity logActivity = () -> "login_activity.txt";

    /**
     * This is the initialize method.
     * This sets all the table columns within the Customer Table and sorts the Table based
     * off of the "ID" column.
     * @param url Path to FXML file.
     * @param resourceBundle Contains internationalization properties for GUI.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            CustomerTable.refresh();
            CustomerQuery.getCustomers().size();
            CustomerTable.setItems(CustomerQuery.getCustomers());

            customerIDColumn.setCellValueFactory(new PropertyValueFactory<>("customerID"));
            customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
            customerAddressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
            customerPostalColumn.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
            customerPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
            customerStateColumn.setCellValueFactory(new PropertyValueFactory<>("divisionID"));

            CustomerTable.getSortOrder().add((TableColumn<Customer, ?>) customerIDColumn);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This is the Modify Customer method.
     * The user is directed to CustomerModify.FXML if a customer is selected.
     * If a customer has not been selected, user is redirected back to the CustomerView.FXML.
     * @param actionEvent User clicks on button titled "Modify/Change" to the right of Customer Table.
     * @throws Exception Throws error if failed.
     */
    public void toModifyCustomerScreen(ActionEvent actionEvent) throws Exception {
        if (CustomerTable.getSelectionModel().isEmpty()) {
            infoDialog("Warning!", "No Customer Selected", "Please choose a Customer from the above list");
        } else {
            CustomerModify.receiveSelectedCustomer(CustomerTable.getSelectionModel().getSelectedItem());
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/CustomerModify.fxml"));
            Parent CustomerModify = loader.load();
            Scene scene = new Scene(CustomerModify);
            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            stage.setTitle("WGU Scheduling System");
            stage.setScene(scene);
            stage.show();
            stage.centerOnScreen();
        }
    }

    /**
     * This is the Add Customer method.
     * The user is directed to CustomerAdd.FXML.
     * @param actionEvent User clicks on button titled "Add" to the right of Customer Table.
     * @throws Exception Throws error if failed.
     */
    public void toAddCustomerScreen(ActionEvent actionEvent) throws Exception {
        Customer.getCustomerIDCount();
        Parent root = FXMLLoader.load(getClass().getResource("/CustomerAdd.fxml"));
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 600, 400);
        stage.setTitle("WGU Scheduling System");
        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();
    }

    /**
     * This is the Delete Customer method.
     * If user has not selected a customer, user is shown error message and redirected back to Customer View.
     * If user has selected a customer, user is prompted to continue. If user clicks cancel, changes are aborted.
     * If user clicks "OK", existing appointments are checked first. If chosen customer has no appointments,
     * changes are saved and chosen customer is deleted from the database. Customer Table is refreshed to reflect changes.
     * @param actionEvent User clicks on button titled "Delete" to the right of Customer Table.
     * @throws Exception Throws error if failed.
     */
    public void onDeleteCustomer(ActionEvent actionEvent) throws Exception {
        Customer SelectedCustomer = CustomerTable.getSelectionModel().getSelectedItem();
        if (CustomerTable.getSelectionModel().isEmpty()) {
            infoDialog("Warning!", "No Customer Selected", "Please choose a customer from the above list");
            return;
        }
        if (CustomerTable.getSelectionModel().getSelectedItem() != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "This will delete selected customer, proceed?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && (result.get() == ButtonType.OK)) {
                try {
                    boolean valid = validateCustomerDelete(SelectedCustomer);
                    if (valid) {
                        infoDialog("Success!", "Customer Deleted", "The following customer has been deleted: " + "\n"
                        + "Customer ID: " + CustomerTable.getSelectionModel().getSelectedItem().getCustomerID() + "\n"
                        + "Customer Name: " + CustomerTable.getSelectionModel().getSelectedItem().getCustomerName() + "\n"
                        + "Customer Address: " + CustomerTable.getSelectionModel().getSelectedItem().getAddress() + "\n"
                        + "Customer Postal Code: " + CustomerTable.getSelectionModel().getSelectedItem().getPostalCode() + "\n"
                        + "Customer Phone Number: " + CustomerTable.getSelectionModel().getSelectedItem().getPhoneNumber());
                        int selectedCustomer = CustomerTable.getSelectionModel().getSelectedItem().getCustomerID();
                        CustomerQuery.deleteCustomer(selectedCustomer);
                        CustomerTable.setItems(CustomerQuery.getCustomers());
                        CustomerTable.refresh();
                        CustomerTable.getSortOrder().add((TableColumn<Customer, ?>) customerIDColumn);
                    } else {
                        infoDialog("Error", "Customer Not Deleted", "Must delete all appointments first");
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
     * This is the Validate Customer Delete method.
     * This creates an observable list by gathering all appointments based off Customer ID.
     * If customer has no existing appointments, function returns true.
     * If customer has existing appointments, function returns false.
     *
     * @throws Exception Throws error if failed.
     */
    public static boolean validateCustomerDelete(Customer Selectedcustomer) throws Exception {
        try {
            ObservableList appointments = AppointmentQuery.getAppointmentsByCustomerID(Selectedcustomer.getCustomerID());
            if (appointments != null && appointments.size() < 1) {
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This is the Lookup by Customer ID Method.
     * All Customers within the database are obtained through a query.
     * A for loop is entered that compares all Customer ID's with the user entered text.
     * If a match is found, return the Customer.
     * If loop is exited without a match, return nothing.
     * @param searchedCustomerID Any number the user entered.
     * @return Return a Customer with matching Customer ID, or return nothing.
     * @throws Exception Throws error if failed.
     */
    public static Customer lookupByCustomerID(int searchedCustomerID) throws Exception {
        ObservableList<Customer> customers = CustomerQuery.getCustomers();
        for (int i = 0; i < customers.size(); i++) {
            Customer ct = customers.get(i);
            if (ct.getCustomerID() == searchedCustomerID) {
                return ct;
            }
        }
        return null;
    }

    /**
     * This is the Lookup by Customer Name method.
     * If user does not type anything and hits "Enter", all Customers in database will display.
     * If user types in characters, for loop is entered that compares the entered text versus all Customer Names.
     * For ease, everything is compared in lower case, not requiring user to capitalize searches.
     * If match is found, Customer is added to namedCustomers observable list.
     * @param partialName Any set of characters the user types into the text-field.
     * @return Any Customers within the database that match the characters the user entered.
     * @throws Exception Throws error if failed.
     */
    public static ObservableList<Customer> lookupByCustomerName(String partialName) throws Exception {
        ObservableList<Customer> namedCustomers = FXCollections.observableArrayList();
        ObservableList<Customer> allCustomers = CustomerQuery.getCustomers();

        if (partialName.length() == 0) {
            namedCustomers = allCustomers;
        } else {
            for (int i = 0; i < allCustomers.size(); i++) {
                if (allCustomers.get(i).getCustomerName().toLowerCase().contains(partialName.toLowerCase())) {
                    namedCustomers.add(allCustomers.get(i));
                }
            }
        }
        return namedCustomers;
    }

    /**
     * This is the Get Customer Results method.
     * It gathers the text entered into the text field and runs the "lookupByCustomerName" method first.
     * If no matches are found, a loop is entered that compares the entered text to all Customer ID's.
     * If a match is found, result is displayed within the Customer Table.
     * If no matches are found, an alert pops up indicating no matches were found.
     * Search bar auto-resets to allow user to reattempt to locate another part.
     * @param actionEvent typing enter after desired characters have been entered in text-field.
     * @throws Exception Throws error if failed.
     */
    public void getCustomerResultsHandler(ActionEvent actionEvent) throws Exception {
        String ct = customerTableQuery.getText();

        ObservableList<Customer> customers = lookupByCustomerName(ct);

        if (customers.size() == 0) {
            try {
                int cust = Integer.parseInt(ct);
                Customer A = lookupByCustomerID(cust);
                if (A != null)
                    customers.add(A);
            } catch (NumberFormatException e) {
            }
        }
        CustomerTable.setItems(customers);
        customerTableQuery.setText("");
        if (customers.size() == 0) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Input not valid");
            errorAlert.setContentText("Unable to locate customer");
            errorAlert.showAndWait();
            CustomerTable.setItems(CustomerQuery.getCustomers());
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
     * This is the View Appointments method.
     * The user is asked to confirm screen change. If User clicks "OK", User is directed to MainScreen.FXML.
     * This method also contains a Lambda expression that waits for a response from user.
     * @param actionEvent User clicks on button titled "View Appointments" to the top right of Customer Table.
     * @throws IOException Throws Input/Output error if failed.
     */
    public void toViewAppointments(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Confirm");
        alert.setHeaderText("Confirm Change");
        alert.setContentText("Are you sure you want to View Appointments?");
        alert.showAndWait().ifPresent((response -> {
            if (response == ButtonType.OK) {
                System.out.println("Alerting!");
                Parent main = null;
                try {
                    Parent root = FXMLLoader.load(getClass().getResource("/MainScreen.fxml"));
                    Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                    Scene scene = new Scene(root, 1000, 500);
                    stage.setTitle("Home Screen");
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
     * @param actionEvent User clicks on button titled "Log Out" to the bottom right of Customer Table.
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
}