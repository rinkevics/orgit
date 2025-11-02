package lv.kauguri.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

public class MyController {

    @FXML
    private TextField textField;

    @FXML
    public void onAction() {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Clippy");
            alert.setContentText(textField.getText());
            alert.showAndWait();
        });
    }

}
