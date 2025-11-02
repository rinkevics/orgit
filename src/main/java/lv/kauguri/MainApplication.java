package lv.kauguri;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("/lv/kauguri/views/index.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 547, 604);
        stage.setTitle("HelloWorld");
        stage.setScene(scene);
        stage.show();
    }

    static void main() {
        launch();
    }
}
