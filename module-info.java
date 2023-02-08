module puhak.firstscreen {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens puhak.firstscreen to javafx.fxml;
    exports puhak.firstscreen;
    exports Controllers;
    opens Controllers to javafx.fxml;
    exports Helper;
    opens Helper to javafx.fxml;
    exports Models;
    opens Models to javafx.fxml;

}