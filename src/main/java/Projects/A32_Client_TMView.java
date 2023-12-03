package Projects;

import javafx.application.Application;
import javafx.event.Event;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.HashMap;

public class A32_Client_TMView extends Application {
    int stageHeight = 600;
    int stageWidth = 400;
    Stage stage;

    TextArea outPutTextArea;
    TextField modelTextField;
    TextField tapeTextField;
    VBox inputArea;
    HBox buttonBox;
    Button backButton;
    Label modelLabel = new Label("Model:");
    Label tapeLabel = new Label("Tape:");
    

    A32_TM_ClientView clientView;
    String tape = "0000000000000000000000000";
    String model;
    HashMap<String,String> modelMap;

    public A32_Client_TMView(String model) {
        this.model = model;


    }

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        this.stage.setHeight(stageHeight);
        this.stage.setWidth(stageWidth);
        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, stageHeight, stageWidth);

        outPutTextArea = new TextArea();
        modelTextField = new TextField();
        tapeTextField = new TextField();

        // Initialize tapeTextField with the initial value of tape
        tapeTextField.setText(tape);

        backButton = new Button("Back");
        backButton.setOnAction(event -> {
            stage.close();
        });

        buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(backButton);
        inputArea = new VBox(10);
        inputArea.getChildren().addAll(modelLabel, modelTextField, tapeLabel, tapeTextField, buttonBox);

        root.setCenter(outPutTextArea);
        root.setBottom(inputArea);
        stage.setScene(scene);
        stage.show();
    }

    public void appendToOutput(String text) {
        this.outPutTextArea.appendText(text + "\n");
    }
    public HashMap<String,String> createMap(){
        HashMap<String, String> map = new HashMap<>();
        int n = model.length();
        this.model = model.replace(" ","");
        int nTokens = n / 5;
        for (int i = 0; i < nTokens; i++) {
            String temp;
            temp = model.substring(i * 5, (i * 5) + 5);
            map.putIfAbsent(temp.substring(0, 2), temp.substring(2));

        }
        return map;
    }
    public void simulate(){
        modelTextField .setText(model);
        String currentTape = tape;
        int currentState = 1;
        int currentPos = tape.length() / 2;
        HashMap<String, String> map = createMap();
        appendToOutput(displayTape(currentTape,currentPos));
        do {
            String nextStep = map.get(("" + currentState) + readTape(currentPos));
            if (nextStep != null) {                                                                   //check for unvalid combinations
                currentTape = writeToTape(currentTape, currentPos, nextStep.charAt(1));
                currentPos += (nextStep.charAt(2) == '0') ? -1 : 1;
                appendToOutput(displayTape(currentTape, currentPos));

                if (currentPos < 0 || currentPos > tape.length()) {                                       //check for bounds
                    appendToOutput("PROBLEM OCCURRED: Simulation out of Bounds ");
                    currentState = 0;
                }
            } else {
                appendToOutput("PROBLEM OCCURRED: There are missing states ");
                currentState = 0;
            }
            if (currentState == 0) {
                break;
            }
            currentState = Integer.parseInt(String.valueOf(nextStep.charAt(0)));
        }
        while (true);
    }
    private static String displayTape(String tape, int position) {
        if (position < 0 || position >= tape.length()) {
            // Invalid position, return the original tape
            return tape;
        }

        // Insert brackets and spaces around the specified position
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < tape.length(); i++) {
            if (i == position) {
                result.append(String.format("[%c] ", tape.charAt(i)));
            } else {
                result.append(tape.charAt(i)).append(" ");
            }
        }

        // Remove trailing space
        result.deleteCharAt(result.length() - 1);

        return result.toString();
    }
    private String writeToTape(String currentTape, int position, char numToWrite) {
        String nextTape;
        nextTape = currentTape.substring(0, position) + numToWrite + currentTape.substring(position + 1);
        return nextTape;
    }
    private char readTape(int position) {
        if (position < 0 || position >= tape.length()) {
            // Invalid position, return a default value or handle the error
            return ' ';
        }
        return tape.charAt(position);
    }
}
