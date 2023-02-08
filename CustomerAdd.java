package Controllers;

import Helper.CountryQuery;
import Helper.CustomerQuery;
import Helper.FirstLevelDivisionQuery;
import Models.Country;
import Models.Customer;
import Models.FirstLevelDivision;
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
import java.util.Optional;
import java.util.ResourceBundle;

import static Controllers.MainScreen.infoDialog;

/**This class adds new customers to the database. It also contains all FXML assignments and various methods.*/
public class CustomerAdd implements Initializable {
    /**This is assigned to the bottom right button labeled "Cancel".*/
    @FXML public Button CancelButton;
    /**This is assigned to the bottom right button labeled "Save".*/
    @FXML public Button SaveButton;
    /**This is the editable text-field located to the right of the label titled "Phone Number".*/
    @FXML public TextField customerNumberField;
    /**This is the editable text-field located to the right of the label titled "Customer Name".*/
    @FXML public TextField customerNameField;
    /**This is the disabled text-field located to the right of the label titled "Customer ID".*/
    @FXML public TextField customerIDField;
    /**This is the editable text-field located to the right of the label titled "Postal Code".*/
    @FXML public TextField customerPostalField;
    /**This is the editable text-field located to the right of the label titled "Address".*/
    @FXML public TextField customerAddressField;
    /**This is the editable combo box located to the right of the label titled "First-Level Division".*/
    @FXML public ComboBox<String> customerDivisionBox;
    /**This is the editable combo box located to the right of the label titled "Country".*/
    @FXML public ComboBox<String> customerCountryBox;

    /**This is the initialize method.
     * This retrieves a new customer ID and populates the country and division combo boxes.
     * @param url Path to FXML file.
     * @param resourceBundle Contains internationalization properties for GUI.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            int customerID = Customer.getCustomerIDCount();
            customerIDField.setText(String.valueOf(customerID));
            setDivisionCombo();
            setCountryCombo();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**This is the Cancel/Back to Main Screen (Customer View) method.
     * This produces an alert asking for the user to confirm they want to cancel adding a Customer.
     * If user clicks "cancel" on alert: user is redirected back to the Add Customer Screen.
     * If user clicks "OK" on alert: user is directed to the Customer Screen and additions are discarded.
     * This method also contains a Lambda expression that waits for a response from user.
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

    /**This is the To Main Screen Save method.
     * This gathers all values obtained from the text fields and combo boxes within the form.
     * It checks to see if any fields are left blank. If so, user is redirected back to form. If not,
     * then a query is made to add all data into the database as a new Customer. User is
     * then notified via an info pop up and redirected back to the Appointment view screen.
     * @param actionEvent The user clicks on the button titled "Save".
     * @throws IOException Throws and Input/Output error if failed.
     */
    public void toMainScreenSave(ActionEvent actionEvent) throws IOException {
        try {
            String newID = customerIDField.getText();
            String newName = customerNameField.getText();
            String newAddress = customerAddressField.getText();
            String newPostal = customerPostalField.getText();
            String newNumber = customerNumberField.getText();
            String Division_ID = customerDivisionBox.getValue();
            String Country = customerCountryBox.getValue();
            if (newName.isBlank()) {
                infoDialog("Warning!", "No input detected", "Customer name must contain a value");
                customerNameField.setPromptText("Enter value here!");
                return;
            }
            if (newAddress.isBlank()) {
                infoDialog("Warning!", "No input detected", "Customer address must contain a value");
                customerAddressField.setPromptText("Enter value here!");
                return;
            }
            if (newPostal.isBlank()) {
                infoDialog("Warning!", "No input detected", "Customer postal code must contain a value");
                customerPostalField.setPromptText("Enter value here!");
                return;
            }
            if (newNumber.isBlank()) {
                infoDialog("Warning!", "No input detected", "Customer phone number must contain a value");
                customerNumberField.setPromptText("Enter value here!");
                return;
            }
            if (Country == null) {
                infoDialog("Warning!", "No input detected", "Customer country must contain a value");
                customerCountryBox.setPromptText("Enter value here!");
                return;
            }
            if (Division_ID == null) {
                infoDialog("Warning!", "No input detected", "Customer division must contain a value");
                customerDivisionBox.setPromptText("Enter value here!");
                return;
            }

            CustomerQuery.createCustomer(newID, newName, newAddress, newPostal, newNumber, Division_ID);

            infoDialog("Success!", "Customer Added", "Customer has been added");

            Parent root = FXMLLoader.load(getClass().getResource("/CustomerView.fxml"));
            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 775, 500);
            stage.setTitle("WGU Scheduling System");
            stage.setScene(scene);
            stage.show();
            stage.centerOnScreen();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**This is the Set Division Combo box method.
     * This creates an observable list named divisionList. A query is made to gather all first level divisions within
     * the database and store within a separate observable list named divisions. All divisions are added to the divisionList
     * observable list. The Division combo box is set with the divisionList observable list.
     */
    private void setDivisionCombo(){
        ObservableList<String> divisionList = FXCollections.observableArrayList();

        try {
            ObservableList<FirstLevelDivision> divisions = FirstLevelDivisionQuery.getAllFirstLevelDivisions();
            if (divisions != null) {
                for (FirstLevelDivision division: divisions) {
                    divisionList.add(String.valueOf(division.getDivision()));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        customerDivisionBox.setItems(divisionList);
    }

    /**This is the Set Country Combo box method.
     * This creates an observable list named countryList. A query is made to gather all countries within
     * the database and store within a separate observable list named countries. All countries are added to the countryList
     * observable list. The Country combo box is set with the countryList observable list.
     */
    private void setCountryCombo(){
        ObservableList<String> countryList = FXCollections.observableArrayList();

        try {
            ObservableList<Country> countries = CountryQuery.getAllCountries();;
            if (countries != null) {
                for (Country country: countries) {
                    countryList.add(String.valueOf(country.getCountry()));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        customerCountryBox.setItems(countryList);
    }

    /**This is the Select Country method.
     * This creates an observable list named divisionList. A query is made to gather all divisions based off a
     * selected Country and store within a separate observable list named divisions. All divisions are added to the divisionList
     * observable list. The Division combo box is set with the divisionList observable list.
     * @param actionEvent The user selects a country from the Country combo box.
     */
    public void selectCountry(ActionEvent actionEvent) {
        ObservableList<String> divisionList = FXCollections.observableArrayList();
        try {
            ObservableList<FirstLevelDivision> divisions = FirstLevelDivisionQuery.getDivisionsByCountry(customerCountryBox.getSelectionModel().getSelectedItem());
            if (divisions != null) {
                for (FirstLevelDivision division: divisions) {
                    divisionList.add(String.valueOf(division.getDivision()));
                }
            }
            customerDivisionBox.setItems(divisionList);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

