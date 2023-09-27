package Projects;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class A12CA extends Application {
    private Canvas canvas;
    private GraphicsContext gc;
    private int cellSize = 10; // Size of each cell
    private int gridWidth = 75; // Number of cells in width
    private int gridHeight = 45; // Number of cells in height
    private int[][] grid; // Grid to store cell states
    private int [] familyGrid; //Gri to store the families of cells

    private Label label1;
    private Label label2;
    private Button stopButton;
    private boolean stop = false;
    private TextField seedField;
    private Button generateButton;
    private int bufferHeight = 60; // Number of rows to buffer
    private Alert alert = new Alert(Alert.AlertType.WARNING,"");


    public A12CA(SceneController sceneController) {
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Group root = new Group();
        Scene scene = new Scene(root, 900, 575);

        // Create a canvas for drawing the cellular automata grid
        canvas = new Canvas(gridWidth * cellSize, gridHeight * cellSize);
        gc = canvas.getGraphicsContext2D();
        canvas.setLayoutX(75);
        canvas.setLayoutY(25);
        canvas.setStyle("-fx-border-color: black; -fx-border-width: 2;");

        root.getChildren().add(canvas);

        label1 = new Label("Enter seed");
        label1.setLayoutX(75);
        label1.setLayoutY(500);
        label1.setMaxSize(75, 10);
        label1.setStyle("-fx-border-color: black; -fx-padding: 2 2 2 2; -fx-border-width: 2;");

        // Create input elements
        seedField = new TextField();
        seedField.setPromptText("Enter seed");
        seedField.setLayoutX(175);
        seedField.setLayoutY(500);
        seedField.setMaxSize(100, 10);

        generateButton = new Button("Generate");
        generateButton.setLayoutX(300);
        generateButton.setLayoutY(500);
        generateButton.setOnAction(event -> {
            generateAutomata();
        });

        label2 = new Label("Seed:");
        label2.setLayoutX(375);
        label2.setLayoutY(500);
        label2.setMaxSize(75, 10);
        label2.setStyle("-fx-border-color: black; -fx-padding: 2 2 2 2; -fx-border-width: 2;");

        stopButton = new Button("Stop");
        stopButton.setLayoutX(475);
        stopButton.setLayoutY(500);
        stopButton.setOnAction(actionEvent -> stop = true);

        root.getChildren().addAll(label1, seedField, generateButton, label2, stopButton);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Cellular Automata");
        primaryStage.show();
    }


    private void generateAutomata() {
        AtomicInteger rowCount = new AtomicInteger();
        // Get the seed from the input field
        String seedText = seedField.getText();
        if(isValidbinary(seedText)) {
            label2.setText("Seed: " + seedField.getText());
            long seed = 0;

            // Parse the seed as a long (you can use any appropriate method)
            try {
                seed = Long.parseLong(seedText);
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Input must be only integers");
                return;
            }


            grid = new int[bufferHeight][gridWidth];
            grid = fillFirstRow(grid);


            // Create a scheduled executor service to periodically update the canvas
            ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);


            executor.scheduleAtFixedRate(() -> {
                if (stop) {
                    executor.shutdown();
                    return;
                }
                // Clear the canvas
                gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
                rowCount.set(rowCount.get() + 1);

                //Check if we need to scroll down
                if (Integer.parseInt(String.valueOf(rowCount.get())) > gridHeight) {
                    for (int i = 0; i < gridHeight - 1; i++) {
                        grid[i] = grid[i + 1];

                    }
                    rowCount.set(gridHeight - 1);


                }
                familyGrid = getFamilies(grid, rowCount.get());
                // Generate the new row and set it in the grid
                int[] newRow = generateRow(familyGrid);
                grid[rowCount.get()] = newRow;


                // Use Platform.runLater to update the canvas on the JavaFX thread
                Platform.runLater(this::updateCanvas);
            }, 0, 100, TimeUnit.MILLISECONDS);
        }
        else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Wrong input");
            alert.showAndWait();
        }
    }
    private boolean isValidbinary(String input)
    {
        if(input.length() != 8)
        {
            return false;
        }
        for(int i = 0; i < input.length(); i++)
        {
            if(input.charAt(i) != '0' && input.charAt(i) !='1')
            {

                return false;
            }
        }

        return true;
    }

    private void updateCanvas() {
        // Draw the grid based on the cell states
        for (int i = 0; i < gridHeight; i++) {
            for (int j = 0; j < gridWidth; j++) {
                if (grid[i][j] == 1) {
                    // Fill cell with white color
                    gc.fillRect(j * cellSize, i * cellSize, cellSize, cellSize);
                }
            }
        }
    }

    private int[][] fillFirstRow(int[][] grid)
    {
        for(int i = 0; i < gridWidth; i++)
        {
            if(i == gridWidth/2+1)
            {
                grid[0][i] = 1;
            }
            else
            {
                grid[0][i] = 0;
            }
        }
        return grid;
    }
    private int[] getFamilies(int[][] grid, int row) {
        int[] generatedRow = new int[gridWidth * 3];
        for (int i = 1; i < gridWidth - 1; i++) {
            generatedRow[i] = grid[row - 1][i - 1];
            generatedRow[i + 1] = grid[row - 1][i];
            generatedRow[i + 2] = grid[row - 1][i + 1];
        }
        return generatedRow;
    }

    private int[] generateRow(int[] familyGrid) {
        int[] newRow = new int[gridWidth];
        for (int i = 0; i < gridWidth; i++) {
            int left = familyGrid[i];
            int top = familyGrid[i + 1];
            int right = familyGrid[i + 2];

            if (left == 0 && top == 0 && right == 0) {
                newRow[i] = 0;
            } else if (left == 0 && top == 0 && right == 1) {
                newRow[i] = 1;
            } else if (left == 0 && top == 1 && right == 0) {
                newRow[i] = 0;
            } else if (left == 0 && top == 1 && right == 1) {
                newRow[i] = 1;
            } else if (left == 1 && top == 0 && right == 0) {
                newRow[i] = 1;
            } else if (left == 1 && top == 0 && right == 1) {
                newRow[i] = 0;
            } else if (left == 1 && top == 1 && right == 0) {
                newRow[i] = 1;
            } else if (left == 1 && top == 1 && right == 1) {
                newRow[i] = 0;
            }
        }
        return newRow;
    }


}