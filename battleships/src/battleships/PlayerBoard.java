/********************************************************************************
 * Author: 1816477                                                              *
 * Date: Dec 2020                                                               *
 * Assignment: Element 011 Battleships Game                                     *
 * Description: Class PlayerBoard is subclass of the Board superclass.          *
 * It is used to create a battleships board made up of a grid of squares.       *
 * This subclass contains specific methods used to create the player's grid,    *
 * such as creating the ships and managing the ship placement visuals.          *
 ********************************************************************************/

package battleships;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import java.util.Arrays;

public class PlayerBoard extends Board {

    //****************************Class constructors*****************************//

    /** constructor
     * Creates a new PlayerBoard instance and builds a grid of Squares using the
     * Board superclass constructor. All the players ships are then created ready to
     * be placed on the grid.
     * All squares are then made clickable using the mouse click handler event provided.
     * @param boardSize: {int} number of rows and columns
     * @param squareSize: {int} height and width of each grid square
     * @param squareClickHandler: {MouseEvent} action when a square is clicked
     * @param labelClickHandler: {MouseEvent} action when a label is clicked
     */
    public PlayerBoard(int boardSize, int squareSize, EventHandler<? super MouseEvent> squareClickHandler,
                       EventHandler<? super MouseEvent> labelClickHandler) {

        // call superclass Board constructor
        super(boardSize, squareSize);
        // create all ships for player to place
        createPlayerShips(labelClickHandler);
        // set each squares mouse click event handler
        enableClickableGrid(squareClickHandler);
    }

    //***************************************************************************//

    //********************************Class methods******************************//

    /** method createPlayerShips()
     * Creates Ship objects and adds them to the ships array in the Board superclass
     * @param labelClickHandler: {MouseEvent} action when a label is clicked
     */
    private void createPlayerShips(EventHandler<? super MouseEvent> labelClickHandler) {

        // create and add all ships to ArrayList
        getShips().addAll(Arrays.asList(
                new Ship("Battleship", 4, Color.GREEN, labelClickHandler),
                new Ship("Cruiser1", 3, Color.DARKBLUE, labelClickHandler),
                new Ship("Cruiser2", 3, Color.WHITE, labelClickHandler),
                new Ship("Destroyer1", 2, Color.PURPLE, labelClickHandler),
                new Ship("Destroyer2", 2, Color.YELLOW, labelClickHandler),
                new Ship("Destroyer3", 2, Color.CHARTREUSE, labelClickHandler)));
    } // close method

    /** method exitSquare()
     * This method overrides the original superclasses method version.
     * It removes the highlighting effects during the Ship placement
     * area of the game. It uses the provided Square as a starting point
     * and removes the effect of the squares.
     * @param square: {Square} Square hovered over by the player
     */
    @Override
    public void exitSquare(Square square) {

        int shipSize = getSelectedShip().getLength(); // get currently selected ship
        int row = square.getRow(); // get squares row
        int column = square.getColumn(); // get squares column

        if (isHorizontal()) { // if currently set at horizontal placement

            if ((column + shipSize) <= getBoardSize()) { // if ship will fit in grid

                for (int i = 0; i < shipSize; i++) { // highlight each square

                    super.exitSquare(getGrid()[row][column + i]);
                }
            }
        } else { // if currently set at vertical placement

            if ((row + shipSize) <= getBoardSize()) { // if ship will fit in grid

                for (int i = 0; i < shipSize; i++) { // highlight each square

                   super.exitSquare(getGrid()[row + i][column]);
                }
            }
        } // close if statement
    } // close method

    /** method enterSquare()
     * This method overrides the original superclasses method version.
     * It adds additional highlighting effects during the Ship placement
     * area of the game. It uses the provided Square as a starting point
     * and highlights the squares next to it to indicate where the Ship
     * will be placed.
     * @param square: {Square} Square hovered over by the player
     */
    @Override
    public void enterSquare(Square square) {

        int shipSize = getSelectedShip().getLength(); // get currently selected ship
        int row = square.getRow(); // get squares row
        int column = square.getColumn(); // get squares column

        if (isHorizontal()) { // if currently set at horizontal placement

            if ((column + shipSize) <= getBoardSize()) { // if ship will fit in grid

                for (int i = 0; i < shipSize; i++) { // highlight each square

                    super.enterSquare(getGrid()[row][column + i]);
                }
            }

        } else { // if currently set at vertical placement

            if ((row + shipSize) <= getBoardSize()) { // if ship will fit in grid

                for (int i = 0; i < shipSize; i++) { // highlight each square

                    super.enterSquare(getGrid()[row + i][column]);
                }
            }
        } // close if statement
    } // close method

} // close class PlayerBoard
