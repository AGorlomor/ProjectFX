package Projects;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.util.Optional;
/**
 * A32_TM_ServerView is a JavaFX application that provides a graphical user interface
 * for controlling and monitoring a Turing Machine server. It allows users to start and
 * stop the server, view the current Turing Machine model, and manage server connections.
 *
 * The view includes a menu bar with File options, controls for starting and ending the server,
 * displaying the current model, and handling server disconnection. Additionally, it features
 * an output area for displaying server-related information.
 *
 * @author Alexey Rudoy
 * @version 3.5
 */
public class A32_TM_ServerView extends Application {
    int stageHeight = 400;
    int stageWidth = 600;

    VBox bannerBox;
    ImageView bannerImage;

    VBox inputContainer;
    HBox inputLine;
    TextField serverPort;
    Button startButton;
    Button modelButton;
    CheckBox finalizeCheckBox;
    Button endButton;
    Button closeButton;

    VBox outputbox;
    TextArea outputText;
    A32_TM_ServerController serverController;
    Stage stage;
    MenuBar menuBar;


    public A32_TM_ServerView() {
        this.serverController = new A32_TM_ServerController();
    }
    /**
     * Constructor for A32_TM_ServerView class. Initializes the server controller.
     */
    public static void main(String[] args) {
        launch(args);
    }
    /**
     * The main entry point for the JavaFX application. It sets up the GUI components
     * and initializes the stage.
     *
     * @param stage The primary stage for this application.
     * @throws Exception If an error occurs during the application startup.
     */
    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        stage.setHeight(stageHeight);
        stage.setWidth(stageWidth);
        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, stageWidth, stageHeight);

        bannerBox =new VBox(10);
        menuBar = createMenuBar();
        bannerImage = new ImageView("tm-server.png");
        bannerBox.setAlignment(Pos.CENTER);
        bannerBox.getChildren().addAll(menuBar,bannerImage);

        inputContainer = new VBox(10);
        inputContainer.setAlignment(Pos.CENTER);
        serverPort = new TextField("3000");
        startButton = new Button("Start");
        startButton.setOnAction(Event->{
            startButton.setDisable(true);
            serverController.startServer(getServerPort());
        });
        modelButton = new Button("Model");
        modelButton.setOnAction(Event->{
            appendToOutput(serverController.modelTM);
        });
        finalizeCheckBox = new CheckBox("Finalize");
        endButton = new Button("End");
        endButton.setOnAction(Event ->{
            if(finalizeCheckBox.isSelected()){
                Alert lastAlert = new Alert(Alert.AlertType.CONFIRMATION,"Are you sure you want to disconnect the server");
                Optional<ButtonType> result = lastAlert.showAndWait();

                if (result.isPresent() && result.get() == ButtonType.OK) {
                    // serverController.closeServer();
                    startButton.setDisable(false);
                    closeButton.setDisable(false);
                }

            }
            else {
                showAlertAndWait("To finalize disconnecting the server check the Finalize checkbox");
            }
        });
        closeButton = new Button("Close");
        closeButton.setDisable(true);
        closeButton.setOnAction(Event->{
            stage.close();
        });
        inputLine = new HBox(10);
        inputLine.setAlignment(Pos.CENTER);
        inputLine.getChildren().addAll(serverPort, startButton, modelButton, finalizeCheckBox,endButton,closeButton);
        inputContainer.getChildren().add(inputLine);

        outputbox = new VBox(10);
        outputText = new TextArea();
        outputbox.setAlignment(Pos.CENTER);
        outputbox.getChildren().add(outputText);

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                showAlertAndWait("Close button functionality disabled please use the close button.");
                windowEvent.consume();
            }
        });

        root.setTop(bannerBox);
        root.setCenter(outputText);
        root.setBottom(inputContainer);
        stage.setScene(scene);
        stage.show();
    }
    /**
     * Retrieves the server port specified in the input field.
     *
     * @return The server port as a string.
     */
    public String getServerPort() {
        return serverPort.getText();
    }
    /**
     * Appends the given text to the output TextArea.
     *
     * @param text The text to append to the output.
     */
    public void appendToOutput(String text) {
        outputText.appendText(text + "\n");
    }
    /**
     * Shows a warning alert with the given text and waits for user acknowledgment.
     *
     * @param alertText The text for the warning alert.
     */
    public void showAlertAndWait(String alertText){
        Alert alert = new Alert(Alert.AlertType.WARNING, alertText);
        alert.showAndWait();
    }
    /**
     * Creates a MenuBar with File menu options.
     *
     * @return The created MenuBar.
     */
    public MenuBar createMenuBar(){
        Scene scene = new Scene(new Group(),400,300);
        Menu fileMenu = new Menu("File");
        MenuItem exitMenuItem = new MenuItem("Exit");
        MenuItem closeMenuItem = new MenuItem("Close");

        Menu networkMenu = new Menu("Network");
        MenuItem connectMenuItem = new MenuItem("Connect");
        MenuItem disconnect = new MenuItem("Disconnect");

        Menu controls = new Menu("Controls");
        MenuItem modelMenuItem = new MenuItem("Model");
        exitMenuItem.setOnAction(Event -> {
            Platform.exit();
            System.exit(0);
        });
        closeMenuItem.setOnAction(Event ->{
            stage.close();
        });
        fileMenu.getItems().addAll(exitMenuItem,closeMenuItem);
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(fileMenu);
        return menuBar;
    }
}
