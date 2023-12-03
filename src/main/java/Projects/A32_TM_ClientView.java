package Projects;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
    TextField modelTextField;
    Button validateButton;
    Button sendButton;
    Button receiveButton;
    Button runButton;

    TextArea outputText;
    MenuBar menuBar;

    A32_TM_ClientController clientController;
    A32_Client_TMView clientTMView;

    public A32_TM_ClientView( A32_TM_ClientController clientController){
        this.clientController = clientController;



    }

    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage stage) throws Exception {
        this.stage =stage;
        this.stage.setHeight(stageHight);
        this.stage.setWidth(stageWidth);
        BorderPane root = new BorderPane();
        Scene scene = new Scene(root,stageHight, stageWidth);

        bannerContainer = new VBox(10);
        menuBar = createMenuBar();
        bannerImage = new ImageView("tm-client.png");
        bannerContainer.setAlignment(Pos.CENTER);
        bannerContainer.getChildren().addAll(menuBar,bannerImage);


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
                showAlertAndWait("Port number is not an integer, please enter an valid port number");
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
        modelTextField = new TextField();
        validateButton = new Button("validate");
        validateButton.setOnAction(Event->{
            try {
                if(clientController.validateModel(modelTextField.getText())){
                    showAlertAndWait("Valid Model! \n you may send the model to the server");
                }
                else {
                    showAlertAndWait("Invalid Model!");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        sendButton = new Button("send");
        sendButton.setOnAction(Event->{
            try {
                clientController.sendModel(modelTextField.getText());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        receiveButton = new Button("receive");
        receiveButton.setOnAction(Event->{
            try {
                clientController.receiveModel();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        runButton = new Button("run");
        runButton.setOnAction(Event->{
            try {
                clientController.receiveModel();
                //clientController.receiveModel();
                clientController.model = modelTextField.getText();
                this.clientTMView = new A32_Client_TMView(modelTextField.getText().replace(" ",""));
                Stage newStage = new Stage();
                clientTMView.start(newStage);
                newStage.show();
                clientTMView.appendToOutput("Model: "+modelTextField.getText());
                clientTMView.simulate();

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        inputLine2.setAlignment(Pos.CENTER);
        inputLine2.getChildren().addAll(modelTextField,validateButton,sendButton,receiveButton,runButton);
        inputContainer.setAlignment(Pos.CENTER);
        inputContainer.getChildren().addAll(inputLine1,inputLine2);

        root.setTop(bannerContainer);
        root.setCenter(outputText);
        root.setBottom(inputContainer);




        this.stage.setScene(scene);
        this.stage.show();


    }
    public void setmodelTM(String modelTM){
        this.modelTextField.setText(modelTM);

    }
    public void appendToOutput(String text) {
        this.outputText.appendText(text + "\n");
    }
    public boolean isInt(String num){
        try {
            Integer.parseInt(num);
            return true;
        }catch (Exception e) {
            return false;
        }
    }
    public void showAlertAndWait(String alertText){
        Alert alert = new Alert(Alert.AlertType.WARNING, alertText);
        alert.showAndWait();
    }
    public MenuBar createMenuBar(){
        Menu fileMenu = new Menu("File");
        MenuItem exitMenuItem = new MenuItem("Exit");
        MenuItem closeMenuItem = new MenuItem("Close");

        Menu controlsMenu = new Menu("Controls");
        MenuItem sentMenuItem = new MenuItem("Send");
        MenuItem receiveMenuItem = new MenuItem("Receive");
        MenuItem validateMenuItem = new MenuItem("Validate");
        MenuItem runMenuItem = new MenuItem("Run");

        Menu networkMenu = new Menu("Network");
        MenuItem connectMenuItem = new MenuItem("Connect");
        MenuItem disconnectMenuItem = new MenuItem("Disconnect");


        exitMenuItem.setOnAction(Event -> {
            Platform.exit();
            System.exit(0);
        });
        closeMenuItem.setOnAction(Event ->{
            stage.close();
        });
        sentMenuItem.setOnAction(Event->{
            try {
                clientController.sendModel(modelTextField.getText());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        receiveMenuItem.setOnAction(Event->{
            try {
                clientController.receiveModel();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        validateMenuItem.setOnAction(Event->{

        });
        runMenuItem.setOnAction(Event->{

        });
        connectMenuItem.setOnAction(Event->{
            if(isInt(serverPort.getText())) {
                clientController.connectToServer(serverAddress.getText(), serverPort.getText());
                connectButton.setDisable(true);
            }
            else {
                showAlertAndWait("Port number is not an integer, please enter an valid port number");
            }
        });
        disconnectMenuItem.setOnAction(Event->{
            clientController.disconnect();
            connectButton.setDisable(false);
        });



        networkMenu.getItems().addAll(connectMenuItem,disconnectMenuItem);
        controlsMenu.getItems().addAll(sentMenuItem,receiveMenuItem,validateMenuItem,runMenuItem);
        fileMenu.getItems().addAll(exitMenuItem,closeMenuItem);
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(fileMenu,controlsMenu,networkMenu);
        return menuBar;
    }
    private void openResWindow() throws Exception {

    }
    public void appendTMWindow(String msg){
        clientTMView.appendToOutput(msg);
    }


}
class TM{
    public TM(){

    }
}