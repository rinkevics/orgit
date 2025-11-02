package lv.kauguri.controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class MyController {

    @FXML
    private TextField textField;

    @FXML
    private ListView<String> itemListView;

    private ObservableList<String> items;

    @FXML
    public void initialize() {
        // Create a list of random text items
        items = FXCollections.observableArrayList(
            "Project Alpha - Active",
            "Project Beta - In Development",
            "Project Gamma - Testing",
            "Project Delta - Completed",
            "Project Epsilon - Planning",
            "Project Zeta - On Hold",
            "Project Eta - Review",
            "Project Theta - Archived"
        );

        // Set the items to the ListView
        itemListView.setItems(items);
    }

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
