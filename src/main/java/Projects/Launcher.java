package Projects;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * is the main class of the application, serving as the entry point for the launcher GUI
 */
public class Launcher extends Application {
    private boolean lan_isEnglish = true; //A boolean field used to determine the language setting for the launcher (default is English)
    /**
     * A static method that launches the JavaFX application by calling the launch method.
     * This method serves as an entry point to start the application
     */
    public static void execute() {
        launch();
    }

    /**
     * An overridden method from the Application class.
     * This method is called when the application is started and is responsible for initializing and displaying the launcher window
     * @param stage
     * @throws Exception
     */
    @Override
    public  void start(Stage stage) throws Exception {

        int stageWidth = 500;
        int stageHeight = 125;
        Parent root = new Group();
        Scene scene = new Scene(root,stageWidth,stageHeight);
        VBox vbox = new VBox(10);
        vbox.setAlignment(Pos.TOP_LEFT);
        vbox.setStyle("-fx-padding: 10px");

        // Calculate the center of the screen
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        double centerX = (screenBounds.getWidth() - stage.getWidth()) / 2;
        double centerY = (screenBounds.getHeight() - stage.getHeight()) / 2;
        // Set the window's position to be centered
        stage.setX(centerX);
        stage.setY(centerY);

        ImageView logo = new ImageView("logo.png");
        logo.setLayoutX(0);
        logo.setLayoutY(0);

        Image icon = new Image("icons8.png");
        stage.getIcons().add(icon);
        stage.setTitle("Assignment Launcher by Alexey Rudoy");


        // Label
        Label label = new Label("Select an assignment: ");
        label.setLayoutY(150);
        label.setLayoutX(50);
        label.setMaxSize(150,10);

        // ComboBox
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.getItems().addAll("A12[CA] - Cellular Automata", "A22[GoL] - Game of Life", "A32[TM] - Turing Machine");
        comboBox.setValue("Choose");
        comboBox.setMaxSize(100,10);
        comboBox.setLayoutY(50);


        // Buttons
        Button selectButton = new Button("Select");
        selectButton.setLayoutY(50);
        Button closeButton = new Button("Close");

        HBox hboxTop = new HBox(10);
        hboxTop.setAlignment(Pos.TOP_LEFT);
        hboxTop.getChildren().addAll(logo,label, comboBox,selectButton,closeButton);

        Label lanLabel = new Label("Select a language:");
        ComboBox lanComboBox = new ComboBox<>();
        lanComboBox.getItems().addAll("English", "Russian");
        lanComboBox.setValue("English");


        HBox hboxBot = new HBox(10);
        hboxBot.setAlignment(Pos.CENTER);
        hboxBot.getChildren().addAll(lanLabel, lanComboBox);



        vbox.getChildren().addAll(hboxTop,hboxBot);


        // Event handler for the Select button
        selectButton.setOnAction(event -> {
            String selectedOption = comboBox.getValue();
            if (selectedOption != null) {
                if (selectedOption.equals("A12[CA] - Cellular Automata")) {
                    if(lanComboBox.getValue().equals("Russian"))
                    {
                        lan_isEnglish = false;
                    }
                    // Switch to the A12CA scene
                    A12CA a12ca = new A12CA(lan_isEnglish);
                    try {
                        a12ca.start(stage);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                } else if (selectedOption.equals("A22[GoL] - Game of Life")) {
                    A22_GL_Controller controller;
                    if(lanComboBox.getValue().equals("Russian")){
                        controller= new A22_GL_Controller("Russian");
                    }
                    else {
                        controller = new A22_GL_Controller("English");
                    }

                    controller.startView();
                    
                } else if (selectedOption.equals("A32[TM] - Turing Machine")) {
                    Alert alert = new Alert(Alert.AlertType.WARNING, "Turing Machine is coming soon (02/12/23)");
                    alert.showAndWait();
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
        rootGroup.getChildren().addAll(vbox);

        stage.setScene(scene);
        stage.show();
    }

    /**
     *The entry point for the JavaFX application. This method calls the execute method to start the application
     * @param args
     */
    public static void main(String[] args) {
        execute();
    }
}
