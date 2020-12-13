/********************************************************************************
 * Author: 1816477                                                              *
 * Date: Dec 2020                                                               *
 * Assignment: Element 011 Battleships Game                                     *
 * Description: Abstract class Board is used to create a grid of squares and    *
 * place them in a 2d array. These squares are where the ships for the game     *
 * will be placed. The placed ships are kept in an ArrayList, which is also     *
 * used by other classes to determine the current progress of the game.         *
 * When subclasses extend this class they gain access to all the methods        *
 * and attributes necessary to create a battleships board game. The board is    *
 * added to an AnchorPane to be displayed on the GUI.                           *
 ********************************************************************************/

package battleships;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import java.util.ArrayList;

public abstract class Board {

    //**************************Class instance variables*************************//

    private int boardSize; // number of rows and columns
    private Square[][] grid; // 2d array containing rows and columns of Squares
    private AnchorPane anchorPane = new AnchorPane(); // gui pane used to display the grid
    private boolean isHorizontal = false; // direction of ship placement
    private ArrayList<Ship> ships = new ArrayList<>(); // all ships placed on the board
    private Ship selectedShip; // ship to be placed on the board

    //***************************************************************************//

    //****************************Class constructors*****************************//

    /** constructor
     * Creates a new Board instance and builds a grid of Squares. This is then added
     * to an anchor pane object, ready to be added to the GUI.
     * @param boardSize: {int} number of rows and columns
     * @param squareSize: {int} height and width of each grid square
     */
    public Board(int boardSize, int squareSize) {

        this.boardSize = boardSize; // set board size

        this.grid = new Square[boardSize][boardSize]; // declare array and set the size

        // create the grid
        for (int row = 0; row < boardSize; row++) { // loop through each row

            for (int column = 0; column < boardSize; column++) { // loop through each column

                int xPosition = squareSize * column; // set the x axis position variable
                int yPosition = squareSize * row; // set the y axis position variable

                // create a new square object
                Square square = new Square(xPosition, yPosition, squareSize, squareSize, row, column);
                square.setStroke(Color.RED); // set square border colour
                anchorPane.getChildren().add(square); // add square to the anchor pane
                grid[row][column] = square; // add square to the 2d grid array

            } // close column loop
        } // close row loop
    } // close constructor

    //***************************************************************************//

    //********************************Class methods******************************//

    /** method enterSquare()
     * This method enables the highlighting effect of the square provided
     * @param square: {Square} Square to change the effects of
     */
    public void enterSquare(Square square) {

        square.enableEffect(); // enable square effects
    }

    /** method exitSquare()
     * This method disables the highlighting effect of the square provided
     * @param square: {Square} Square to change the effects of
     */
    public void exitSquare(Square square) {

        square.disableEffect(); // disable square effects
    }

    /** method placeShip()
     * Places a ship on the grid based on the square and ship provided.
     * Returns true if ship placement is successful, else false if unable to place.
     * @param square: {Square} Starting square of ship
     * @param ship: {Ship} Ship to place
     */
    public boolean placeShip(Square square, Ship ship) {

        // return false if squares are not within the board limits
        if (!checkWithinBoard(square, ship.getLength())) return false;

        // return false if squares collide with another ship
        if (!checkCollision(square, ship.getLength())) return false;

        // set Ship instance variable as placed
        if (isHorizontal) { // if direction is horizontal

            for (int i = 0; i < ship.getLength(); i++) {
                // add the ship to the squares on the grid
                grid[square.getRow()][square.getColumn() + i].addShip(ship);
            }

        } else { // if direction is vertical

            for (int i = 0; i < ship.getLength(); i++) {
                // add the ship to the squares on the grid
                grid[square.getRow() + i][square.getColumn()].addShip(ship);
            }
        }

        return true; // successful placement
    }

    /** method checkWithinBoard()
     * Checks the length of the ship is within the board limits, using the
     * starting point of the Square provided.
     * @param square: {Square} Starting square of Ship
     * @param shipLength: {int} Length of the Ship to place
     */
    private boolean checkWithinBoard(Square square, int shipLength) {

        boolean check; // temp variable for validation result
        if (isHorizontal) {
            // check the squares are within the number of columns of the board
            check = (square.getColumn() + shipLength) <= boardSize; // set true if within board limits, else false
        } else {
            // check the squares are within the number of rows of the board
            check = (square.getRow() + shipLength) <= boardSize; // set true if within board limits, else false
        }
        return check; // return result of check
    }

    /** method checkCollision()
     * Check squares based on the starting square. ship length and direction for a collision.
     * Return true if checked squares are not occupied by a ship, else return false.
     * @param square: {Square} Starting square of Ship
     * @param shipLength: {int} Length of the Ship to place
     */
    private boolean checkCollision(Square square, int shipLength) {

        int row = square.getRow(); // get starting square row no.
        int column = square.getColumn(); // get starting square column no.

        if (isHorizontal) { // if ship direction is horizontal

            for (int i = 0; i < shipLength; i++) { // loop based on ship length

                if (grid[row][column + i].getOccupyingShip() != null) { // check next column for collision

                    return false; // fail, square is occupied
                }
            }

        } else { // if ship direction is vertical

            for (int i = 0; i < shipLength; i++) { // loop based on ship length

                if (grid[row + i][column].getOccupyingShip() != null) { // check next row for collision

                    return false; // fail, square is occupied
                }
            }
        }
        return true; // success, no collision detected
    }

    /** method enableClickableGrid()
     * Add action events to each square in the grid
     * @param mouseClickHandler: {MouseEvent} action when a square is clicked
     */
    public void enableClickableGrid(EventHandler<? super MouseEvent> mouseClickHandler) {

        for (Square[] row : grid) { // loop through each row

            for (Square square : row) { // loop through each square in the row

                square.setOnMouseClicked(mouseClickHandler); // set square clickable event
                square.setOnMouseEntered((event) -> enterSquare(square)); // set cursor enter square event
                square.setOnMouseExited((event) -> exitSquare(square)); // set cursor exit square event
            }
        }
    }

    /** method disableClickableGrid()
     * Remove action events from each square in the grid
     */
    public void disableClickableGrid() {

        for (Square[] row : grid) { // loop through each row

            for (Square square : row) { // loop through each square in the row

                square.setOnMouseClicked(null); // disable square clickable event
                square.setOnMouseEntered(null); // disable cursor enter square event
                square.setOnMouseExited(null); // disable cursor exit square event
            }
        }
    }

    //***************************************************************************//

    //**************************Class getters and setters************************//

    /** method getAnchorPane()
     * Return the anchor pane that contains the grid
     */
    public AnchorPane getAnchorPane() {

        return anchorPane;
    }

    /** method getGrid()
     * Return the 2D array that contains the grid squares
     */
    public Square[][] getGrid() {

        return grid;
    }

    /** method getBoardSize()
     * Return the size of the boards rows and columns
     */
    public int getBoardSize() {

        return boardSize;
    }

    /** method isHorizontal()
     * Return if the current ship orientation setting is horizontal or not
     */
    public boolean isHorizontal() {

        return isHorizontal;
    }

    /** method getShips()
     * Return this list of ships that are associated with this board
     */
    public ArrayList<Ship> getShips() {

        return ships;
    }

    /** method getShips()
     * Return the current selected ship
     */
    public Ship getSelectedShip() {

        return selectedShip;
    }

    /** method setHorizontal()
     * Set the ships direction as horizontal or as vertical
     * @param horizontal {boolean} direction, true=horizontal, false=vertical
     */
    public void setHorizontal(boolean horizontal) {

        isHorizontal = horizontal;
    }

    /** method setSelectedShip()
     * Set the current selected ship to be placed
     * @param selectedShip {Ship} The selected ship
     */
    public void setSelectedShip(Ship selectedShip) {

        this.selectedShip = selectedShip;
    }

} // close class Board