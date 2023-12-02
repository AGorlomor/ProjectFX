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

    public static void main(String[] args) {
        launch(args);
    }

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

    public String getServerPort() {
        return serverPort.getText();
    }
    public void appendToOutput(String text) {
        outputText.appendText(text + "\n");
    }
    public void showAlertAndWait(String alertText){
        Alert alert = new Alert(Alert.AlertType.WARNING, alertText);
        alert.showAndWait();
    }
    public MenuBar createMenuBar(){
        Scene scene = new Scene(new Group(),400,300);
        Menu fileMenu = new Menu("File");
        MenuItem exitMenuItem = new MenuItem("Exit");
        MenuItem closeMenuItem = new MenuItem("Close");
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
