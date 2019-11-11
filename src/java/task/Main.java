package task;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(Main.class.getResource("resources/javafx.fxml"));
        primaryStage.setTitle("Homework");
        primaryStage.setScene(new Scene(root, 500, 150));
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
