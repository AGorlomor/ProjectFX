package Projects;
class A22_GL_Model {

    int gridRowNum;
    int gridColumnNum;
    int[][] grid;
    private int[] deadSeed = new int[8];
    private int[] aliveSeed = new int[8];

    private int[][] familyGrid = new int[3][3];

    A22_GL_Controller controller;

    /***
     * Constructor for Model
     * @param controller
     * @param seed
     */
    public A22_GL_Model(A22_GL_Controller controller, String seed) {
        this.controller = controller;
        this.gridRowNum = controller.getGridRowNum();
        this.gridColumnNum = controller.getGridColumnNum();
        this.grid = new int[gridRowNum][gridColumnNum]; // Initialize the grid array with the correct dimensions
        initGrid(this.grid);
        initSeedArrays(seed);
    }

    /***
     * method to initiate the grid
     * @param grid
     */
    private void initGrid(int[][] grid) {
        for (int i = 0; i < gridRowNum; i++) {
            for (int j = 0; j < gridColumnNum; j++) {
                this.grid[i][j] = 0; // Set grid element at (i, j) to 0
            }
        }
    }

    /***
     * setter for grid
     * @param grid
     */
    public void setGrid(int[][] grid) {
        this.grid = grid;
        System.out.println("in Model");
    }

    public int[][] getGrid() {
        return evalGrid(this.grid);
    }

    public void printGrid(int[][] grid) {
        for (int i = 0; i < gridRowNum; i++) {
            for (int j = 0; j < gridColumnNum; j++) {
                System.out.print(grid[i][j]);
            }
            System.out.println("");
        }
    }

    /***
     * Evaluating method for the grid
     * @param grid
     * @return
     */
    public int[][] evalGrid(int[][] grid) {
        int[][] tempGrid = new int[gridRowNum][gridColumnNum];
        for (int row = 0; row < gridRowNum; row++) {
            for (int col = 0; col < gridColumnNum; col++) {
                populateFamilyGrid(row, col, grid);
                tempGrid[row][col] = evalCell(familyGrid);
            }
        }
        return tempGrid;
    }

    /***
     * family grid builder
     * @param row
     * @param col
     * @param grid
     */
    private void populateFamilyGrid(int row, int col, int[][] grid) {
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int r = row + i;
                int c = col + j;

                if (r >= 0 && r < gridRowNum && c >= 0 && c < gridColumnNum) {
                    familyGrid[i + 1][j + 1] = grid[r][c];
                } else {
                    familyGrid[i + 1][j + 1] = 0;
                }
            }
        }
    }

    /***
     * seed parts builder
     * @param seed
     */
    private void initSeedArrays(String seed) {
        for (int i = 0; i < 8; i++) {
            deadSeed[i] = Character.getNumericValue(seed.charAt(i));
            aliveSeed[i] = Character.getNumericValue(seed.charAt(i + 9));
        }
    }

    /***
     * method to evaluate a cell based on family
     * @param familyGrid
     * @return
     */
    private int evalCell(int[][] familyGrid) {
        int counter = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (i == 1 && j == 1) {
                    continue;
                }
                if (familyGrid[i][j] == 1) {
                    counter++;
                }
            }
        }

        if (familyGrid[1][1] == 1) {
            return (aliveSeed[counter] == 1) ? 1 : 0 ;
        } else if (familyGrid[1][1] == 0) {
            return (deadSeed[counter] == 1)? 1: 0;

        } else {
            return 0;
        }

    }

    public void setSeedString(String moduleText) {

        initSeedArrays(moduleText);
    }

}