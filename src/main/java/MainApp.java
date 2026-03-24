import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        HomeController homeController = new HomeController();

        Scene scene = new Scene(homeController.getRoot(), 1100, 680);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

        primaryStage.setTitle("Apex Auto");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(750);
        primaryStage.setMinHeight(520);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

