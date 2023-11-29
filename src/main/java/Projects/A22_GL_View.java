package Projects;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.control.ColorPicker;

import java.util.Random;

public class A22_GL_View extends Application {
    /**
     * Constructor fot view
     */
    public A22_GL_View() {
    }




    private int stageHight = 825;
    private int stageWidth = 1020;
    private int gridRowNum ;
    private int gridColumnNum ;
    GridPane gridPane = new GridPane();
    int[][] grid;
    private A22_GL_Controller controller;
    ImageView bannerImg;
    HBox bannerBox;
    Button randomButton;
    Button manualButton;
    Label moduleLabel;
    TextField moduleTextField;

    CheckBox multicolorCheck;
    Button multicolourButton;
    Label multicolorLabel;
    Button startButton;
    Label stepsLabel;
    TextField stepsTextField;
    Label executeLabel;
    Button stopButton;
    Button closeButton;
    VBox inputContainer;
    HBox inputLine1;
    HBox inputLine2;
    MenuBar menuBar;
    boolean isMultiColored = false;
    int stepCount = 0;
    Color color;
    public A22_GL_View(A22_GL_Controller controller) {
        this.controller = controller;
        this.gridRowNum = controller.getGridRowNum();
        this.gridColumnNum = controller.getGridColumnNum();
        this.grid = new int[gridRowNum][gridColumnNum];
        gridPane.setVgap(1);
        gridPane.setHgap(1);
        //Module Text field
        moduleTextField = new TextField();
        moduleTextField.setText("10010001101000111");
    }
    public void setController(A22_GL_Controller controller){
        this.controller = controller;
    }


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {

        ColorPicker colorPicker = new ColorPicker(Color.BLACK);

        Stage colorPickerStage = new Stage();
        colorPickerStage.initOwner(stage);
        colorPickerStage.initModality(Modality.APPLICATION_MODAL);
        colorPickerStage.setScene(new Scene(colorPicker));
        colorPicker.setOnAction(event -> {
            Color selectedColor = colorPicker.getValue();
            controller.setColor(selectedColor.toString());
            colorPickerStage.hide();
        });

        stage.setHeight(stageHight);
        stage.setWidth(stageWidth);
        BorderPane root = new BorderPane();
        Scene scene = new Scene(root,stageHight, stageWidth);
        menuBar = createMenuBar();




        //Image to set as a banner
        bannerImg = new ImageView("GOLBAnner.png");
        bannerImg.setLayoutX(0);
        bannerImg.setLayoutY(0);

        //HBox for the top banner
        bannerBox = new HBox(10);
        bannerBox.setAlignment(Pos.CENTER);
        bannerBox.getChildren().addAll(bannerImg);
        bannerBox.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        bannerBox.setMaxHeight(100);
        VBox topContainer = new VBox(menuBar, bannerBox);



        //Random button
        randomButton = new Button(controller.lan_random);

        //Manual button
        manualButton =  new Button(controller.lan_manual);
        manualButton.setOnAction(Event -> {

        });

        //Module Label
        moduleLabel = new Label(controller.lan_model);



        //MultiColor checkBox
        multicolorCheck = new CheckBox();
        multicolorCheck.selectedProperty().addListener((observable,oldS,newS) -> {
            if(newS){
                System.out.println("Checked");
                isMultiColored = true;
                multicolourButton.setDisable(false);

            }else {
                isMultiColored = false;
                System.out.println("Deselected");
            }
        });

        //Multi colour button
        multicolourButton = new Button(controller.lan_color);
        multicolourButton.setDisable(true);
        multicolourButton.setOnAction(event -> {
            colorPicker.setValue(Color.web(controller.getColor()));
            colorPickerStage.showAndWait(); // Show the color picker dialog
            color = colorPicker.getValue();
            System.out.println(color.toString());
        });

        //Multicolor Checkbox label
        multicolorLabel = new Label(controller.lan_mulicolor);

        //Start button
        startButton = new Button(controller.lan_start);
        startButton.setDisable(true);
        startButton.setOnAction(Event ->{

            grid = buildGrid(gridPane);
            controller.clickStart();
        });


        //Steps label
        stepsLabel = new Label(controller.lan_steps);

        //Steps text field
        stepsTextField = new TextField();
        stepsTextField.setText("Blank = infinite loop");

        //Executed label
        executeLabel = new Label(controller.lan_executed + this.stepCount);
        executeLabel.setStyle("-fx-font-size: 14px; -fx-padding: 5px; -fx-border-color: black; -fx-border-width: 2;");

        //Stop Button
        stopButton = new Button(controller.lan_stop);
        stopButton.setDisable(true);
        stopButton.setOnAction(Event ->{
            controller.clickStop();
        });

        //close button
        closeButton = new Button(controller.lan_close);
        closeButton.setOnAction(Event -> {
            controller.clickStop();
            stage.close();
        });
        manualButton.setOnAction(Event -> {
            updateGridPane(new int[gridColumnNum][gridRowNum]);
            startButton.setDisable(false);
        });
        randomButton.setOnAction(Event -> {
            updateGridPane(new int[gridColumnNum][gridRowNum]);
            updateGridPane(controller.genRandomGrid());
            startButton.setDisable(false);


        });


        // VBox for user input GUI elements with two lines
        inputContainer = new VBox(10);
        inputContainer.setAlignment(Pos.CENTER);

        // First line of input elements
        inputLine1 = new HBox(10);
        inputLine1.setAlignment(Pos.CENTER);
        inputLine1.getChildren().addAll(randomButton, manualButton, moduleLabel, moduleTextField, multicolorCheck, multicolorLabel, multicolourButton);
        inputContainer.getChildren().add(inputLine1);

        // Second line of input elements
        inputLine2 = new HBox(10);
        inputLine2.setAlignment(Pos.CENTER);
        inputLine2.getChildren().addAll(startButton, stepsLabel, stepsTextField, executeLabel, stopButton, closeButton);
        inputContainer.getChildren().add(inputLine2);



        //Create a grid pane for a grid of buttons
        fillGridWithButtons();




        System.out.println("Reached");

        gridPane.setAlignment(Pos.CENTER);
        stage.setScene(scene);
        root.setCenter(gridPane);
        root.setTop(topContainer);
        root.setBottom(inputContainer);
        stage.show();
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                controller.clickStop();
                Platform.exit();
                System.exit(0);
            }
        });

    }

    /***
     * Method to create a button
     * @param row
     * @param col
     * @return
     */
    public Button createButton(int row, int col)
    {

        Button cell = new Button();
        cell.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-min-width: 2px; -fx-min-height: 2px;");



        cell.setOnAction(Event -> {
            System.out.println(Integer.toString(row)+" "+Integer.toString(col));

            if(cell.getStyle().contains("-fx-background-color: white;"))
            {
                cell.setStyle("-fx-background-color: #" + color.toString().substring(2) + "; -fx-border-color: black; -fx-min-width: 2px; -fx-min-height: 2px;");
            } else {
                cell.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-min-width: 2px; -fx-min-height: 2px;");
            }

        });
        return cell;
    }

    /***
     * method to create a menu bar
     * @return
     */
    public MenuBar createMenuBar() {
        Scene scene = new Scene(new Group(),400,300);
        KeyCombination rightShiftQ = new KeyCodeCombination(KeyCode.Q, KeyCombination.SHIFT_ANY);
        KeyCombination rightShiftW = new KeyCodeCombination(KeyCode.W, KeyCombination.SHIFT_ANY);
        Menu fileMenu = new Menu("File");
        MenuItem exitMenuItem = new MenuItem("Exit");
        MenuItem closeMenuItem = new MenuItem("Close");

        Menu simulationMenu = new Menu("Simulation");
        MenuItem randomMenuItem = new MenuItem("Random input");
        MenuItem menualMenuItem = new MenuItem("Manual input");
        MenuItem multiMenuItem = new MenuItem("Multicolor mode");
        MenuItem colorMenuItem = new MenuItem("Color select");
        MenuItem startMenuItem = new MenuItem("Start");
        MenuItem stopMenuItem = new MenuItem("Stop");

        startMenuItem.setAccelerator(rightShiftQ);
        stopMenuItem.setAccelerator(rightShiftW);

        exitMenuItem.setOnAction(Event -> {
            Platform.exit();
            System.exit(0);
        });
        closeMenuItem.setOnAction(Event ->{
            controller.stage.close();
        });
        randomMenuItem.setOnAction(Event->{
            updateGridPane(new int[gridColumnNum][gridRowNum]);
            updateGridPane(controller.genRandomGrid());
            startButton.setDisable(false);

        });
        menualMenuItem.setOnAction(Event->{
            updateGridPane(new int[gridColumnNum][gridRowNum]);
            startButton.setDisable(false);

        });
        startMenuItem.setOnAction(Event ->{
            if(startButton.isDisabled()){
                return;
            }
            else {
                controller.clickStart();
            }
        });
        stopMenuItem.setOnAction(Event ->{
            if(stopButton.isDisabled()){
                return;
            }
            else {
                controller.clickStop();
            }
        });

        scene.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (rightShiftQ.match(event)) {
                if(startButton.isDisabled()){
                    return;
                }
                else {
                    controller.clickStart();
                }
            }
            if (rightShiftW.match(event)) {
                if(stopButton.isDisabled()){
                    return;
                }
                else {
                    controller.clickStop();
                }
            }
            event.consume();
        });
        fileMenu.getItems().addAll(exitMenuItem,closeMenuItem);
        simulationMenu.getItems().addAll(randomMenuItem,menualMenuItem,multiMenuItem,colorMenuItem,startMenuItem,stopMenuItem);



        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(fileMenu,simulationMenu);


        return menuBar;
    }

    /***
     * method to build the grid
     * @param gridPane
     * @return
     */
    public int[][] buildGrid (GridPane gridPane){
        for (int i = 0; i < gridRowNum; i++) {
            for (int j = 0; j < gridColumnNum; j++) {
                Button cellButton = findCellButton(gridPane, i, j);
                if (cellButton != null) {
                    String cellStyle = cellButton.getStyle();

                    if (!cellStyle.contains("-fx-background-color: white;")) {
                        grid[i][j] = 1; // Cell is alive
                    } else {
                        grid[i][j] = 0; // Cell is dead
                    }
                }
            }
        }
        return grid;
    }

    /***
     * Method to find a cell as a node in grid
     * @param gridPane
     * @param row
     * @param col
     * @return
     */
    private Button findCellButton(GridPane gridPane, int row, int col) {
        for (Node node : gridPane.getChildren()) {
            if (node instanceof Button) {
                Integer rowIndex = GridPane.getRowIndex(node);
                Integer colIndex = GridPane.getColumnIndex(node);
                if (rowIndex != null && colIndex != null && rowIndex == row && colIndex == col) {
                    return (Button) node;
                }
            }
        }
        return null;
    }

    /***
     * Getter for the grid
     * @return
     */
    public int[][] getGrid(){
        ;
        return buildGrid(this.gridPane);

    }
    public GridPane getGridPane(){
        return this.gridPane;
    }

    /***
     * Method to update the grid
     * @param grid
     */
    public void updateGridPane(int[][] grid) {
        Platform.runLater(() -> {
            executeLabel.setText("Executed: " + this.stepCount);
            for (int i = 0; i < gridRowNum; i++) {
                for (int j = 0; j < gridColumnNum; j++) {
                    Button cellButton = findCellButton(gridPane, i, j);
                    if (cellButton != null) {
                        if (grid[i][j] == 1 && isMultiColored && stepCount ==0) {
                            // Set the cell to alive (black background)

                            cellButton.setStyle("-fx-background-color: #" + getRandomColor().toString().substring(2) + "; -fx-border-color: black; -fx-min-width: 5px; -fx-min-height: 5px;");
                        } else if (grid[i][j] == 1 && isMultiColored && stepCount > 0) {
                            cellButton.setStyle("-fx-background-color: #" + colorEval(i,j).toString().substring(2) + "; -fx-border-color: black; -fx-min-width: 5px; -fx-min-height: 5px;");
                            
                        } else if (grid[i][j] == 1 && !isMultiColored) {
                            cellButton.setStyle("-fx-background-color: Black; -fx-border-color: black; -fx-min-width: 5px; -fx-min-height: 5px;");

                        } else {

                            // Set the cell to dead (white background)
                            cellButton.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-min-width: 5px; -fx-min-height: 5px;");
                        }
                    }
                }
            }
        });
    }

    /***
     * method to fill grid with buttons
     */
    public void fillGridWithButtons(){
        for(int i = 0; i < gridRowNum; i++){
            for(int j = 0; j < gridColumnNum; j++)
            {
                gridPane.add(createButton(i, j),i,j);
            }
        }
    }

    /***
     * getter for module text field
     * @return
     */
    public String getModuleText(){
        return this.moduleTextField.getText();
    }

    /***
     * Setter for step counter
     * @param stepCount
     */
    public void setStepCount(int stepCount){
        this.stepCount = stepCount;
    }

    /***
     * getter for step text field
     * @return
     */
    public String getStepsText() {
        return stepsTextField.getText();
    }

    /**
     * generates a random color object
     * @return
     */
    public Color getRandomColor() {
        Random random = new Random();
        double red = random.nextDouble();
        double green = random.nextDouble();
        double blue = random.nextDouble();
        return new Color(red, green, blue, 1.0);
    }

    /***
     * method to evaluate the color of a cell
     * @param row
     * @param col
     * @return
     */
    public Color colorEval(int row, int col) {
        int maxRed = 0;
        int maxGreen = 0;
        int maxBlue = 0;
        int validNeighbors = 0;

        // Define offsets for the surrounding cells
        int[][] offsets = {
                { -1, -1 }, { -1, 0 }, { -1, 1 },
                { 0, -1 }, /* Center cell */ { 0, 1 },
                { 1, -1 }, { 1, 0 }, { 1, 1 }
        };

        for (int[] offset : offsets) {
            int neighborRow = row + offset[0];
            int neighborCol = col + offset[1];

            // Check if the neighbor cell is within bounds
            if (neighborRow >= 0 && neighborRow < gridRowNum && neighborCol >= 0 && neighborCol < gridColumnNum) {
                // Check if the neighbor cell is alive and not white
                Button cellButton = findCellButton(gridPane, neighborRow, neighborCol);
                if (cellButton != null) {
                    String cellStyle = cellButton.getStyle();
                    if (grid[neighborRow][neighborCol] == 1 && !cellStyle.contains("-fx-background-color: white;")) {
                        // Extract the color and accumulate its components
                        String hexColor = cellStyle.substring(22, 29); // Extract color value from the style
                        if (hexColor.length() == 7) {  // Check if it's a valid color value
                            int red = Integer.parseInt(hexColor.substring(1, 3), 16);
                            int green = Integer.parseInt(hexColor.substring(3, 5), 16);
                            int blue = Integer.parseInt(hexColor.substring(5, 7), 16);

                            maxRed = Math.max(maxRed, red);
                            maxGreen = Math.min(maxGreen, green);
                            maxBlue = Math.max(maxBlue,blue);
                            validNeighbors++;
                        }
                    }
                }
            }
        }

        // Calculate the average color
        if (validNeighbors > 0) {
            if(maxRed > 200){
                maxRed-=50;
            }
            if(maxGreen > 200){
                maxGreen-=50;
            }
            if(maxBlue > 200){
                maxBlue-=50;
            }
            String hexColor = String.format("#%02X%02X%02X", maxRed, maxGreen, maxBlue);
            return Color.web(hexColor);
        } else {
            // If there are no valid neighbors, return a default color (e.g., white)
            return Color.WHITE;
        }
    }



}
