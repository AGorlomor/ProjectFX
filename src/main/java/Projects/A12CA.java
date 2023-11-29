package Projects;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class A12CA extends Application {

    //Keywords:
    private int cellSize = 10; // An integer representing the size of each cell in the cellular automata grid
    private int gridWidth = 75; // An integer representing the number of cells in width
    private int gridHeight = 45; // An integer representing the number of cells in height
    private int[][] grid; // A 2D array of integers representing the grid to store cell states
    private int [] familyGrid; //An integer array representing the families of cells

    //GUI elements:
    private Label seedLabel; //A JavaFX Label for displaying a label before the seed input field
    private TextField seedField; //A JavaFX TextField for entering the seed value
    private Button generateButton; //A JavaFX Button for generating the cellular automata
    private Button stopButton; //  A JavaFX Button for stopping the execution
    private Button clearButton; //A JavaFX Button for clearing the canvas
    private Label decimalLabel; //A JavaFX Label for displaying the decimal value of the seed

    private Button backButton; //A JavaFX Button for going back to the previous screen
    //GUI elements: Canvas:
    private Canvas canvas; //A JavaFX Canvas for displaying the cellular automata grid
    private GraphicsContext gc; //A GraphicsContext variable for interpreting a 2D grid
    //Supporting variables
    private boolean stop = false; // A boolean flag to indicate whether the stop button was pressed
    private boolean clear = false; // A boolean flag to indicate whether the clear button was pressed
    private String lan_seedLabel;
    private String lan_decLabel;
    private String lan_generate;
    private String lan_stop;
    private  String lan_clear;
    private String lan_back;
/*
Strings representing labels and button text in different languages
 */


    private int bufferHeight = 60; // Number of rows to buffer
    private Alert alert = new Alert(Alert.AlertType.WARNING,""); //Alert object
    private Stage primaryStage;

    /**
     * Constructor that takes a boolean flag to determine the language (English or non-English) for setting labels and button text
     * @param
     */
    public A12CA(boolean lan_isEnglish) {

        if(lan_isEnglish){
            lan_seedLabel = "seed: ";
            lan_decLabel = "Model: ";
            lan_generate = "Generate";
            lan_stop = "Stop";
            lan_clear = "Clear";
            lan_back = "Back";
        } else if (!lan_isEnglish) {
            lan_seedLabel = "зерно: ";
            lan_decLabel = "Модель: ";
            lan_generate = "Генерировать";
            lan_stop = "стоп";
            lan_clear = "Вычищить";
            lan_back = "назад";

        }
    }

    /**
     *
     * The entry point to the application
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * The start method for initializing and displaying the JavaFX application
     * @param primaryStage
     */
    @Override
    public void start(Stage primaryStage) {

        this.primaryStage = primaryStage;
        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, 900, 750);
        scene.setFill(Color.LIGHTBLUE);
        primaryStage.setScene(scene);

        // Calculate the center of the screen
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        double centerX = (screenBounds.getWidth() - primaryStage.getWidth()) / 2;
        double centerY = (screenBounds.getHeight() - primaryStage.getHeight()) / 2;
        // Set the window's position to be centered
        primaryStage.setX(centerX);
        primaryStage.setY(centerY);

        

        ImageView bannerImg = new ImageView("banner.png");
        bannerImg.setLayoutX(0);
        bannerImg.setLayoutY(0);

        HBox bannerBox = new HBox(10);
        bannerBox.setAlignment(Pos.CENTER);
        bannerBox.getChildren().addAll(bannerImg);
        bannerBox.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));


        // Create a VBox to center the canvas and input elements
        VBox centerBox = new VBox(10);
        centerBox.setAlignment(Pos.CENTER);
        centerBox.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        centerBox.setPadding(new Insets(10));
        centerBox.setStyle("-fx-border-width: 4px; -fx-border-color: black;");



        // Create a canvas for drawing the cellular automata grid
        canvas = new Canvas(gridWidth * cellSize, gridHeight * cellSize);
        gc = canvas.getGraphicsContext2D();
        canvas.setStyle("-fx-border-width: 4px; -fx-border-color: black;");
        // Add the canvas to the VBox
        centerBox.getChildren().addAll(canvas);

        // Create a HBox to hold the input elements
        HBox inputBox = new HBox(10);
        inputBox.setAlignment(Pos.CENTER);
        inputBox.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY)));

        seedLabel = new Label(lan_seedLabel);
        seedLabel.setMaxSize(200, 10);
        seedLabel.setStyle("-fx-font-size: 14px; -fx-padding: 5px; -fx-border-color: black; -fx-border-width: 2;");

        // Create input elements
        seedField = new TextField();
        seedField.setPromptText("Enter seed");
        seedField.setMaxSize(100, 10);
        seedField.setStyle("-fx-font-size: 14px;");

        generateButton = new Button(lan_generate);
        generateButton.setMaxSize(125, 10);
        generateButton.setStyle("-fx-font-size: 14px;");
        generateButton.setOnAction(event -> {
            generateAutomata();
        });

        decimalLabel = new Label(lan_decLabel);
        decimalLabel.setMaxSize(200, 10);
        decimalLabel.setStyle("-fx-font-size: 14px; -fx-padding: 5px; -fx-border-color: black; -fx-border-width: 2;");

        stopButton = new Button(lan_stop);
        stopButton.setMaxSize(50, 10);
        stopButton.setStyle("-fx-font-size: 14px;");
        stopButton.setOnAction(actionEvent -> stop = true);

        clearButton = new Button(lan_clear);
        clearButton.setMaxSize(150, 10);
        clearButton.setStyle("-fx-font-size: 14px;");
        clearButton.setOnAction(actionEvent -> clearCanvas());


        backButton = new Button(lan_back);
        backButton.setMaxSize(100, 10);
        backButton.setStyle("-fx-font-size: 14px;");
        backButton.setOnAction(event -> {
            Launcher launcher = new Launcher();
            try {
                launcher.start(primaryStage);
            } catch (Exception e) {

            }
        });


        inputBox.getChildren().addAll(seedLabel, seedField, generateButton, decimalLabel, stopButton, clearButton, backButton);


        // Set the center of the BorderPane to centerBox
        root.setCenter(centerBox);
        root.setTop(bannerBox);
        root.setBottom(inputBox);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Cellular Automata");
        primaryStage.show();
    }

    /**
     * Method for generating the cellular automata based on the provided seed
     */
    private void generateAutomata() {
        AtomicInteger rowCount = new AtomicInteger();
        // Get the seed from the input field
        String seedText = seedField.getText();
        if(isValidbinary(seedText)) {
            decimalLabel.setText("Seed: " + String.valueOf(binaryToDecimal(seedField.getText())));

            grid = new int[bufferHeight][gridWidth];

            grid = fillFirstRow(grid);


            // Create a scheduled executor service to periodically update the canvas
            ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);


            executor.scheduleAtFixedRate(() -> {
                generateButton.setDisable(true);
                clearButton.setDisable(true);
                if (stop) {
                    executor.shutdown();
                    stop = false;
                    generateButton.setDisable(false);
                    clearButton.setDisable(false);
                    return;
                }
                if (clear)
                {
                    gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
                    updateCanvas();
                    executor.shutdown();
                    clear = false;
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
                int[] newRow = generateRow(familyGrid, seedText);
                grid[rowCount.get()] = newRow;


                // Use Platform.runLater to update the canvas on the JavaFX thread
                Platform.runLater(this::updateCanvas);
            }, 0, 75, TimeUnit.MILLISECONDS);
        }
        else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Wrong input: input must be and 8 digit binary number (for example: try 01011010)");
            alert.showAndWait();
        }
    }

    /**
     *  Helper method to check if the input is a valid 8-digit binary number
     * @param input
     * @return
     */
    private boolean isValidbinary(String input) {
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

    /**
     * Helper method to convert a binary string to a decimal integer
     * @param binary
     * @return
     */
    public static int binaryToDecimal(String binary) {
        int decimal = 0;
        int binaryLength = binary.length();

        for (int i = 0; i < binaryLength; i++) {
            char digit = binary.charAt(binaryLength - 1 - i);

            // Check if the character is '0' or '1'
            if (digit != '0' && digit != '1') {
                throw new IllegalArgumentException("Invalid binary input: " + binary);
            }

            int bitValue = (digit == '1') ? 1 : 0;
            decimal += bitValue * Math.pow(2, i);
        }

        return decimal;
    }

    /**
     * Method for updating the canvas to display the cellular automata grid
     */
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

    /**
     * Method for filling the first row of the grid with initial values
     * @param grid
     * @return
     */
    private int[][] fillFirstRow(int[][] grid) {
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

    /**
     *  Method for retrieving the families of cells based on the previous row
     * @param grid
     * @param row
     * @return
     */
    private int[] getFamilies(int[][] grid, int row) {
        int[] generatedRow = new int[gridWidth * 3];
        for (int i = 1; i < gridWidth - 1; i++) {
            generatedRow[i] = grid[row - 1][i - 1];
            generatedRow[i + 1] = grid[row - 1][i];
            generatedRow[i + 2] = grid[row - 1][i + 1];
        }
        return generatedRow;
    }

    /**
     * Method for generating a new row of cells based on the seed and family of cells
     * @param familyGrid
     * @param seed
     * @return
     */
    private int[] generateRow(int[] familyGrid,String seed) {
        int[] newRow = new int[gridWidth];
        for (int i = 0; i < gridWidth; i++) {
            int left = familyGrid[i];
            int top = familyGrid[i + 1];
            int right = familyGrid[i + 2];

            if (left == 0 && top == 0 && right == 0) {
                newRow[i] = Integer.parseInt(String.valueOf(seed.charAt(7)));
            } else if (left == 0 && top == 0 && right == 1) {
                newRow[i] = Integer.parseInt(String.valueOf(seed.charAt(6)));
            } else if (left == 0 && top == 1 && right == 0) {
                newRow[i] = Integer.parseInt(String.valueOf(seed.charAt(5)));
            } else if (left == 0 && top == 1 && right == 1) {
                newRow[i] = Integer.parseInt(String.valueOf(seed.charAt(4)));
            } else if (left == 1 && top == 0 && right == 0) {
                newRow[i] = Integer.parseInt(String.valueOf(seed.charAt(3)));
            } else if (left == 1 && top == 0 && right == 1) {
                newRow[i] = Integer.parseInt(String.valueOf(seed.charAt(2)));
            } else if (left == 1 && top == 1 && right == 0) {
                newRow[i] = Integer.parseInt(String.valueOf(seed.charAt(1)));
            } else if (left == 1 && top == 1 && right == 1) {
                newRow[i] = Integer.parseInt(String.valueOf(seed.charAt(0)));
            }
        }
        return newRow;
    }

    /**
     * Method for clearing the canvas
     */
    private void clearCanvas() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }



}