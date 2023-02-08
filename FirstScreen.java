package puhak.firstscreen;

import Helper.ConnectDatabase;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.ZoneId;

/**This class starts and runs the main program*/
public class FirstScreen extends Application {

    /**This is the start method. This loads the Login Screen GUI.*/
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(FirstScreen.class.getResource("/LoginScreen.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 425, 475);
        stage.setTitle("WGU Scheduling System");
        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();
    }

    /**This is the main method. This is the first method that gets called when program is ran.
     * This also connects to the Database upon launch and closes the connection upon exiting the program.*/
    public static void main(String[] args) throws Exception {
        //Locale.setDefault(new Locale("fr"));
        ConnectDatabase.openConnection();
        launch();
        ConnectDatabase.closeConnection();
    }
}