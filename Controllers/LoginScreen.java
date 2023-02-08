package Controllers;

import Helper.AppointmentQuery;
import Helper.UserQuery;
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
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.Locale;
import java.util.ResourceBundle;

import static Models.Appointment.getAppointmentIDCount;

interface LogActivity{
    String getFileName();
}
/**This class acquires and logs into the database. It also contains all FXML assignments and various methods.*/
public class LoginScreen implements Initializable {
    /**This is assigned to the bottom middle button labeled "Log In".*/
    @FXML public Button LoginButton;
    /**This is the editable text-field located above the Log-In button titled "Enter your password".*/
    @FXML public PasswordField PasswordField;
    /**This is the editable text-field located above the Log-In button titled "Enter your username".*/
    @FXML public TextField UsernameField;
    /**This is the label titled "LOGIN" located above the logo.*/
    @FXML public Label LoginLabel;
    /**This is the label titled "Time Zone" located below the "Log-In" button.*/
    @FXML public Label TimeZone;
    /**This is the label titled "Change Me" located below the "Log-In" button.*/
    @FXML public Label TimeZoneChangeMe;
    /**This is the label titled "Language" located below the "Log-In" button.*/
    @FXML public Label LanguageLabel;
    /**This is the label titled "English" located below the "Log-In" button.*/
    @FXML public Label EnglishLabel;
    /**
     * This assigns the log activity to the file named "login_activity.txt".
     */
    LogActivity logActivity = () -> "login_activity.txt";

    /**This is the initialize method.
     * This locates the language bundle. If computer settings are in english, all specified fields are set
     * to English. If computer settings are set to French, all specified fields are set to French.
     * @param url Path to FXML file.
     * @param resources Contains internationalization properties for GUI.
     */
    @Override
    public void initialize(URL url, ResourceBundle resources) {
        try {
            ResourceBundle resourceBundle = ResourceBundle.getBundle("language/language", Locale.getDefault());
            if (Locale.getDefault().getLanguage().equals("fr") || Locale.getDefault().getLanguage().equals("en")) {
                UsernameField.setPromptText(resourceBundle.getString("Username"));
                PasswordField.setPromptText(resourceBundle.getString("Password"));
                LoginLabel.setText(resourceBundle.getString("Title"));
                LoginButton.setText(resourceBundle.getString("Button"));
                TimeZone.setText(resourceBundle.getString("TimeZone"));
                LanguageLabel.setText(resourceBundle.getString("Language"));
                TimeZoneChangeMe.setText(String.valueOf(ZoneId.systemDefault()));
                SelectLanguage();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**This is the To Main Screen method.
     * This executes a User Query that checks if a matching Username and Password were entered. If not then
     * an error dialog pops up notifying user, either in English or French depending on computer settings.
     * If a matching Username and Password were entered, the attempt is logged into the "login_activity.txt" file,
     * a pop-up is displayed showing any upcoming appointments for the User currently logged in, and the User
     * is redirected to the Main Screen (Appointment View).
     * @param actionEvent The user clicks on the button titled "Log In".
     * @throws Exception Throws an error if failed.
     */
    public void toMainScreen(ActionEvent actionEvent) throws Exception {
        getAppointmentIDCount();
        createFile();
        try {
            boolean valid = UserQuery.checkUsernamePassword(UsernameField.getText(), PasswordField.getText());
            if (!valid) {
                loginFailure();
                if (Locale.getDefault().getLanguage().equals("en")) {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setHeaderText("Input not valid");
                    errorAlert.setContentText("Check Username and Password entries");
                    errorAlert.showAndWait();
                    return;
                }
                if(Locale.getDefault().getLanguage().equals("fr")){
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setHeaderText("Entrée non valide");
                    errorAlert.setContentText("Vérifier les entrées de nom d’utilisateur et de mot de passe");
                    errorAlert.showAndWait();
                    return;
                }
            }
            if (valid) {
                loginSuccess();
                alertUpcomingAppointment();
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
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**This is the Login Success method.
     * This logs activity by stamping the Username, Password and timestamp to a selected file.
     */
    private void loginSuccess() {
        try {
            FileWriter fileWriter = new FileWriter(logActivity.getFileName(), true);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            Date date = new Date(System.currentTimeMillis());
            fileWriter.write("Successful Login: Username=" + UsernameField.getText() + " Password=" + PasswordField.getText() + " Timestamp: " + simpleDateFormat.format(date) + "\n");
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**This is the Login Failure method.
     * This logs activity by stamping the Username, Password and timestamp to a selected file.
     */
    private void loginFailure() {
        try {
            FileWriter fileWriter = new FileWriter(logActivity.getFileName(), true);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            Date date = new Date(System.currentTimeMillis());
            fileWriter.write("Failed Login: Username=" + UsernameField.getText() + " Password=" + PasswordField.getText() + " Timestamp: " + simpleDateFormat.format(date) + "\n");
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**This is the Create file method.
     * If a file does not exist, a file is created. If a file exists, user is notified of the path to file.
     */
    private void createFile() {
        try {
            File newfile = new File(logActivity.getFileName());
            if (newfile.createNewFile()) {
                System.out.println("File created:" + newfile.getName());
            } else {
                System.out.println("File exists within: " + newfile.getPath());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**This is the Select Language method.
     * If computer settings are set to English, the English label is set to display English.
     * If computer settings are set to French, the English label is set to display Francais.
     */
    public void SelectLanguage() {
        if (Locale.getDefault().getLanguage().equals("en")) {
            EnglishLabel.setText("English");
        }
        if (Locale.getDefault().getLanguage().equals("fr")) {
            EnglishLabel.setText("Francais");
        }
    }

    /**This is the Alert Upcoming Appointment method.
     * This creates two variables of the LocalDateTime now and LocalDateTime plus 15 minutes. Two observable
     * lists are created-upcomingAppointments and appointments. A query is made to gather all Appointments and
     * store into appointments list. A For loop is entered that checks each appointment. If the appointment start and end
     * date are today, an embedded if loop is entered. If the appointment is after the LocalDateTime now and before the LocalDateTime plus 15 minutes, then it is added to the upcomingAppointments
     * list. If settings are in English, an alert is displayed in English notifying User of the Appointment. If settings
     * are in French, an alert is displayed in French notifying User of the Appointment. If the For loop is exited with
     * the upcomingAppointments list less than one (meaning no Upcoming Appointments were located), then the User
     * is notified via alert displayed in either English or French based on computer settings.
     * @throws Exception Throws an error if failed.
     */
    private void alertUpcomingAppointment() throws Exception {
        try {
            LocalDateTime localDateTime = LocalDateTime.now();
            LocalDateTime localDateTimePlus15 = localDateTime.plusMinutes(15);
            LocalDate localDate = LocalDate.now();

            ObservableList<Appointment> upcomingAppointments = FXCollections.observableArrayList();
            try {
                ObservableList<Appointment> appointments = AppointmentQuery.getAppointments();

                if (appointments != null) {
                    for (Appointment appointment : appointments) {
                        if (appointment.getStartDate().isEqual(localDate) && appointment.getEndDate().isEqual(localDate)) {
                            if (appointment.getStartTime().isAfter(localDateTime) && appointment.getStartTime().isBefore(localDateTimePlus15)) {
                                upcomingAppointments.add(appointment);

                                if (Locale.getDefault().getLanguage().equals("en")) {
                                    Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);
                                    infoAlert.setTitle("Appointment Alert");
                                    infoAlert.setHeaderText("Appointment Coming Up!");
                                    infoAlert.setContentText("Appointment ID: " + appointment.getAppointmentID() + "\n" +
                                            "Appointment Title: " + appointment.getTitle() + "\n" +
                                            "Start Date: " + appointment.getStartDate() + "\n" +
                                            "Start Time: " + appointment.getStartTime() + "\n" +
                                            "End Time: " + appointment.getEndTime());
                                    infoAlert.setResizable(true);
                                    infoAlert.showAndWait();
                                }
                                if (Locale.getDefault().getLanguage().equals("fr")) {
                                    Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);
                                    infoAlert.setTitle("Alerte de rendez-vous");
                                    infoAlert.setHeaderText("Rendez-vous à venir!");
                                    infoAlert.setContentText("ID de rendez-vous: " + appointment.getAppointmentID() + "\n" +
                                            "Date de début: " + appointment.getStartDate() + "\n" +
                                            "Heure de début: " + appointment.getStartTime() + "\n" +
                                            "Heure de fin: " + appointment.getEndTime());
                                    infoAlert.setResizable(true);
                                    infoAlert.showAndWait();
                                }
                            }
                        }
                    }

                    if (upcomingAppointments.size() < 1) {
                        if (Locale.getDefault().getLanguage().equals("en")) {
                            Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);
                            infoAlert.setHeaderText("Appointment Alert");
                            infoAlert.setContentText("No upcoming appointments");
                            infoAlert.showAndWait();
                        }
                        if(Locale.getDefault().getLanguage().equals("fr")){
                            Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);
                            infoAlert.setHeaderText("Alerte de rendez-vous");
                            infoAlert.setContentText("Aucun rendez-vous à venir");
                            infoAlert.showAndWait();
                        }
                    }
                }
            } catch (RuntimeException e) {
                throw new RuntimeException(e);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}