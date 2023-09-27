package Projects;

import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class SceneController {
    private final Stage stage;
    private Scene launcherScene; // Store the launcher scene

    public SceneController(Stage stage) {
        this.stage = stage;
    }

    public void setScene(Parent root) {
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void showLauncher() {
        if (launcherScene == null) {
            // Create the launcher scene only if it hasn't been created yet
            Group launcherRoot = new Group();
            launcherScene = new Scene(launcherRoot);

            // Create UI elements for the launcher scene
            Label label = new Label("Welcome to the Launcher!");
            Button launchButton = new Button("Launch Another Scene");

            // Define layout for UI elements
            label.setLayoutX(50);
            label.setLayoutY(50);
            launchButton.setLayoutX(50);
            launchButton.setLayoutY(100);

            // Add UI elements to the launcherRoot
            launcherRoot.getChildren().addAll(label, launchButton);
        }

        // Set the launcher scene as the active scene for the stage
        setScene(launcherScene.getRoot());
    }
}
