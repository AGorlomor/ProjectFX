package Projects;

import javafx.application.Application;
import javafx.event.Event;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class A32_TM_ClientView extends Application {
    Stage stage;
    int stageHight = 400;
    int stageWidth = 600;
    VBox bannerContainer;
    ImageView bannerImage;

    VBox inputContainer;

    HBox inputLine1;
    TextField userName;
    TextField serverAddress;
    TextField serverPort;
    Button connectButton;
    Button endButton;

    HBox inputLine2;
    TextField model;
    Button validateButton;
    Button sendButton;
    Button receiveButton;
    Button runButton;

    TextArea outputText;

    A32_TM_ClientController clientController;

    public A32_TM_ClientView( A32_TM_ClientController clientController){
        this.clientController = clientController;


    }

    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage stage) throws Exception {

        stage.setHeight(stageHight);
        stage.setWidth(stageWidth);
        BorderPane root = new BorderPane();
        Scene scene = new Scene(root,stageHight, stageWidth);

        bannerContainer = new VBox(10);
        bannerImage = new ImageView("tm-client.png");
        bannerContainer.setAlignment(Pos.CENTER);
        bannerContainer.getChildren().add(bannerImage);


        outputText = new TextArea();

        inputContainer = new VBox(10);

        inputLine1 = new HBox(10);
        userName = new TextField("user");
        serverAddress = new TextField("localhost");
        serverPort = new TextField("3000");
        connectButton = new Button("Connect");
        connectButton.setOnAction(Event ->{
            if(isInt(serverPort.getText())) {
                clientController.connectToServer(serverAddress.getText(), serverPort.getText());
                connectButton.setDisable(true);
            }
            else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Port number is not an integer, please enter an valid port number");
            alert.showAndWait();
            }


        });
        endButton = new Button("End");
        endButton.setOnAction(Event->{
            clientController.disconnect();
            connectButton.setDisable(false);
        });
        inputLine1.setAlignment(Pos.CENTER);
        inputLine1.getChildren().addAll(userName,serverAddress,serverPort,connectButton,endButton);

        inputLine2 = new HBox(10);
        model = new TextField();
        validateButton = new Button("validate");
        sendButton = new Button("send");
        sendButton.setOnAction(Event->{
            try {
                clientController.sendModel(model.getText());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        receiveButton = new Button("receive");
        receiveButton.setOnAction(Event->{
            try {
                clientController.recieveModel();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        runButton = new Button("run");
        inputLine2.setAlignment(Pos.CENTER);
        inputLine2.getChildren().addAll(model,validateButton,sendButton,receiveButton,runButton);
        inputContainer.setAlignment(Pos.CENTER);
        inputContainer.getChildren().addAll(inputLine1,inputLine2);

        root.setTop(bannerContainer);
        root.setCenter(outputText);
        root.setBottom(inputContainer);


        stage.setScene(scene);
        stage.show();


    }
    public void setmodelTM(String modelTM){
        this.model.setText(modelTM);

    }
    public void appendToOutput(String text) {
        outputText.appendText(text + "\n");
    }
    public boolean isInt(String num){
        try {
            Integer.parseInt(num);
            return true;
        }catch (Exception e) {
            return false;
        }


    }

}
