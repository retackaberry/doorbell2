package home.doorbell2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class Doorbell2 extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Doorbell2.class.getResource("Doorbell2.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 480);
        scene.setCursor(Cursor.NONE);
        Image icon = new Image("logo.gif");
        stage.initStyle(StageStyle.UNDECORATED);
        //stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}