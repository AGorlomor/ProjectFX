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
/**
 * A32_Client_TMView is a JavaFX application that provides a graphical user interface
 * for simulating the operation of a Turing Machine. It allows users to input a model
 * and tape, and then simulates the execution of the Turing Machine based on the provided
 * model and initial tape. The simulation results are displayed in a TextArea.
 *
 * @author Alexey Rudoy
 * @version 1.4
 */
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
    HashMap<String, String> modelMap;
    /**
     * Constructor for A32_Client_TMView class.
     *
     * @param model The initial model for the Turing Machine.
     */
    public A32_Client_TMView(String model) {
        this.model = model;
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
        this.stage.setHeight(stageHeight);
        this.stage.setWidth(stageWidth);
        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, stageHeight, stageWidth);

        outPutTextArea = new TextArea();
        modelTextField = new TextField();
        tapeTextField = new TextField();

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
    /**
     * Simulates the operation of the Turing Machine based on the provided model and tape.
     * The simulation results are displayed in the output TextArea.
     */
    public void simulate() {
        modelTextField.setText(model);
        String currentTape = tape;
        int currentState = 1;
        int currentPos = tape.length() / 2;
        HashMap<String, String> map = createMap();
        appendToOutput("Initial State:");
        appendToOutput(displayTape(currentTape, currentPos));
        do {
            String nextStep = map.get(("" + currentState) + readTape(currentPos));
            if (nextStep != null) { // check for invalid combinations
                currentTape = writeToTape(currentTape, currentPos, nextStep.charAt(1));
                currentPos += (nextStep.charAt(2) == '0') ? -1 : 1;
                appendToOutput(displayTape(currentTape, currentPos));

                if (currentPos < 0 || currentPos > tape.length()) { // check for bounds
                    appendToOutput("Error: out of Bounds ");
                    currentState = 0;
                }
            } else {
                appendToOutput("Error: missing states ");
                currentState = 0;
            }
            if (currentState == 0) {
                break;
            }
            currentState = Integer.parseInt(String.valueOf(nextStep.charAt(0)));
        } while (true);
    }
    /**
     * Displays the current state of the tape, highlighting the position of the tape head.
     *
     * @param tape     The current tape content.
     * @param position The position of the tape head.
     * @return A formatted string representing the tape with brackets around the tape head.
     */
    private String displayTape(String tape, int position) {
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
    /**
     * Writes a symbol to the tape at the specified position.
     *
     * @param currentTape The current tape content.
     * @param position    The position to write the symbol.
     * @param numToWrite  The symbol to write.
     * @return The updated tape after writing the symbol.
     */
    private String writeToTape(String currentTape, int position, char numToWrite) {
        String nextTape;
        nextTape = currentTape.substring(0, position) + numToWrite + currentTape.substring(position + 1);
        return nextTape;
    }
    /**
     * Reads the symbol at the specified position on the tape.
     *
     * @param position The position to read the symbol from.
     * @return The symbol read from the tape.
     */
    private char readTape(int position) {
        if (position < 0 || position >= tape.length()) {
            // Invalid position, return a default value or handle the error
            return ' ';
        }
        return tape.charAt(position);
    }
    /**
     * Appends the given text to the output TextArea.
     *
     * @param text The text to append to the output.
     */
    public void appendToOutput(String text) {
        outPutTextArea.appendText(text + "\n");
    }
    /**
     * Creates a mapping of states and transitions from the provided model.
     *
     * @return A HashMap representing the transitions defined in the model.
     */
    public HashMap<String, String> createMap() {
        HashMap<String, String> map = new HashMap<>();
        int n = model.length();
        this.model = model.replace(" ", "");
        int nTokens = n / 5;
        for (int i = 0; i < nTokens; i++) {
            String temp;
            temp = model.substring(i * 5, (i * 5) + 5);
            map.putIfAbsent(temp.substring(0, 2), temp.substring(2));
        }
        return map;
    }
}
