package Projects;

import javafx.application.Application;
import javafx.event.Event;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

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

    VBox outputbox;
    TextArea outputText;
    A32_TM_ServerController serverController;

    public A32_TM_ServerView() {
        this.serverController = new A32_TM_ServerController();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setHeight(stageHeight);
        stage.setWidth(stageWidth);
        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, stageWidth, stageHeight);

        bannerBox =new VBox(10);
        bannerImage = new ImageView("tm-server.png");
        bannerBox.setAlignment(Pos.CENTER);
        bannerBox.getChildren().add(bannerImage);

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
        inputLine = new HBox(10);
        inputLine.setAlignment(Pos.CENTER);
        inputLine.getChildren().addAll(serverPort, startButton, modelButton, finalizeCheckBox,endButton);
        inputContainer.getChildren().add(inputLine);

        outputbox = new VBox(10);
        outputText = new TextArea();
        outputbox.setAlignment(Pos.CENTER);
        outputbox.getChildren().add(outputText);

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
}
