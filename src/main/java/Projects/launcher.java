package Projects;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class launcher extends Application {
    private SceneController sceneController;

    public static void execute() {
        launch();
    }

    @Override
    public  void start(Stage stage) throws Exception {
        this.sceneController = new SceneController(stage);

        int stageWidth = 450;
        int stageHeight = 150;
        Parent root = createLauncherScene();
        Scene scene = new Scene(root,stageWidth,stageHeight);

        Image icon = new Image("icons8.png");
        stage.getIcons().add(icon);
        stage.setTitle("Assignment Launcher by Alexey Rudoy");
        //stage.setWidth(stageWidth);
        //stage.setHeight(stageHeight);

        // Label
        Label label = new Label("Select an assignment: ");

        // ComboBox
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.getItems().addAll("A12[CA] - Cellular Automata", "A22[GoL]", "A32[TM]");
        comboBox.setValue("Choose");

        // Buttons
        Button selectButton = new Button("Select");
        Button closeButton = new Button("Close");

        // Event handler for the Select button
        selectButton.setOnAction(event -> {
            String selectedOption = comboBox.getValue();
            if (selectedOption != null) {
                if (selectedOption.equals("A12[CA] - Cellular Automata")) {
                    // Switch to the A12CA scene
                    Projects.A12CA a12ca = new Projects.A12CA(sceneController);
                    try {
                        a12ca.start(stage);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    System.out.println("Selected: " + selectedOption);
                }
            } else {
                System.out.println("No option selected.");
            }
        });

        // Event handler for the Close button
        closeButton.setOnAction(event -> {
            stage.close();
        });

        // Layout
        label.setLayoutX(stageWidth / 12);
        label.setLayoutY(stageHeight / 22.5);
        comboBox.setLayoutX(stageWidth / 2.8125);
        comboBox.setLayoutY(stageHeight / 22.5);
        selectButton.setLayoutX((stageWidth / 2) - 65);
        selectButton.setLayoutY(stageHeight / 2.5);
        closeButton.setLayoutX((stageWidth / 2) + 85);
        closeButton.setLayoutY(stageHeight / 2.5);

        // Add nodes to root
        Group rootGroup = (Group) root;
        rootGroup.getChildren().addAll(label, comboBox, selectButton, closeButton);

        stage.setScene(scene);
        stage.show();
    }

    private Parent createLauncherScene() {
        // Create and return the root node for the launcher scene
        // You can customize this method to create the scene as you need it
        return new Group(); // Example: Creating an empty Group as the root
    }

    public static void main(String[] args) {
        execute();
    }
}
