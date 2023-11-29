package Projects;

import javafx.application.Application;
import javafx.stage.Stage;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import javafx.scene.control.Alert;


class A22_GL_Controller extends Application {
    ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    int gridRowNum = 30;
    int gridColumnNum = 30;
    int[][] grid = new int[gridRowNum][gridColumnNum];
    Stage stage = new Stage();
    A22_GL_View view = new A22_GL_View(this);
    A22_GL_Model model = new A22_GL_Model(this, view.getModuleText());
    private String color = "black";
    public String lan_random;
    public String lan_manual;
    public String lan_model;
    public String lan_mulicolor;
    public String lan_color;
    public String lan_start;
    public String lan_steps;
    public String lan_executed;
    public String lan_stop;
    public String lan_close;

    /***
     * Constructor for controller
     * @param lan
     */
    public A22_GL_Controller(String lan){
        if(lan.equals("Russian")) {
            lan_random = "случ";
            lan_manual = "руч";
            lan_model = "модель";
            lan_mulicolor = "многоцвет";
            lan_color = "цвет";
            lan_start = "пуск";
            lan_steps = "шаги";
            lan_executed = "выпол";
            lan_stop = "стоп";
            lan_close = "зак";
        }
        else {
            lan_random = "Random";
            lan_manual = "Manual";
            lan_model = "Model";
            lan_mulicolor = "Mulicolor";
            lan_color = "Color";
            lan_start = "Start";
            lan_steps = "Steps";
            lan_executed = "Executed";
            lan_stop = "Stop";
            lan_close = "Close";

        }

    }

    /***
     * Star method for controller
     * @param stage
     * @throws Exception
     */
    @Override
    public void start(Stage stage) throws Exception {

    }

    /***
     * method to start the view
     */
    public void startView(){
        view = new A22_GL_View(this);
        view.start(stage);

    }
    public void sendGridToModel() {
        int[][] grid = view.getGrid();
        model.setGrid(grid);
    }
    public void setA22_GL_View(A22_GL_View view){
        this.view = view;
    }

    public void printGrid(int[][] grid){
        for(int i = 0; i < gridRowNum; i++){
            for(int j = 0; j < gridColumnNum; j++){
                System.out.print(grid[i][j]);
            }
            System.out.println("");
        }
    }
    private boolean isRunning = false;

    /***
     * method to run after a start button is pressed
     */
    public void clickStart() {
        AtomicInteger stepCounter = new AtomicInteger();
        this.grid = view.getGrid();
        view.stopButton.setDisable(false);
        view.startButton.setDisable(true);
        model.setSeedString(view.getModuleText());
        executor = Executors.newScheduledThreadPool(1);

        executor.scheduleAtFixedRate(() -> {
            System.out.println("In controller");

            this.grid = model.evalGrid(this.grid);
            System.out.println("Updated");
            printGrid(grid);
            view.updateGridPane(grid);
            view.setStepCount(stepCounter.incrementAndGet());
            if (!view.getStepsText().equals("") && stepCounter.get() >= Integer.parseInt(view.getStepsText())) {
                executor.shutdown();
                System.out.println("IN SD");
            }


        }, 50, 300, TimeUnit.MILLISECONDS);
    }

    /***
     * method to run when stop button is pressed
     */
    public void clickStop(){
        view.startButton.setDisable(false);
        if (executor != null && !executor.isTerminated()) {
            executor.shutdown();
            executor = null;
        }

    }
    public void setGrid(int[][] grid){
        this.grid = grid;

    }
    public int[][] getGrid(){
        return this.grid;
    }

    /***
     * getter for rows
     * @return
     */
    public int getGridRowNum(){
        return this.gridRowNum;
    }

    /***
     * Getter for colls
     * @return
     */
    public int getGridColumnNum(){
        return this.gridColumnNum;
    }

    /***
     * Method to generate a random grid
     * @return
     */
    public int[][] genRandomGrid(){
        int[][] randomGrid = new int[gridRowNum][gridColumnNum];
        Random random = new Random();

        for (int i = 0; i < gridRowNum; i++) {
            for (int j = 0; j < gridColumnNum; j++) {
                randomGrid[i][j] = random.nextInt(2);
            }
        }
        return randomGrid;
    }

    /***
     * method to check if a string is an int
     * @param steps
     * @return
     */
    public boolean isStepsInt(String steps){
        try {
            Integer.parseInt(steps);
            return true;
        }
        catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.WARNING, "Steps must be an integer or null");
            alert.showAndWait();
            return false;
        }
    }

    /***
     * setter for color
     * @param color
     */
    public void setColor(String color) {
        this.color = color;
    }

    /***
     * getter for color
     * @return
     */
    public String getColor() {
        return color;
    }




}
