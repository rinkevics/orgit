module lv.kauguri {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    exports lv.kauguri;
    exports lv.kauguri.controllers;

    opens lv.kauguri to javafx.fxml;
    opens lv.kauguri.controllers to javafx.fxml;
}
