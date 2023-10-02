package Projects;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * is the main class of the application
 */
public class Main extends Application {
    /**
     * The entry point for the JavaFX application. It is the main method that gets executed when the application is launched.
     * This method is used to start the JavaFX application by calling the launch method with the provided arguments
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        launch(args);
    }
    /**
     * The start method is an overridden method from the Application class.
     * It is called when the application is started and is responsible for initializing and launching the main application window. Within this method:
     * An instance of the Launcher class is created. The Launcher class appears to be responsible for setting up and displaying the main application window
     * @param stage
     * @throws Exception
     */
    @Override
    public void start(Stage stage) throws Exception {
        Launcher launcher = new Launcher();
        launcher.start(stage);

    }
}
