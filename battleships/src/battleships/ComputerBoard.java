/********************************************************************************
 * Author: 1816477                                                              *
 * Date: Dec 2020                                                               *
 * Assignment: Element 011 Battleships Game                                     *
 * Description: Class ComputerBoard is subclass of the Board superclass.        *
 * It is used to create a battleships board made up of a grid of squares.       *
 * This subclass contains specific methods used to create the computer's grid,  *
 * such as creating the ships and automatically placing them.                   *
 ********************************************************************************/

package battleships;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class ComputerBoard extends Board {

    //***************************************************************************//

    //****************************Class constructors*****************************//

    /** constructor
     * Creates a new ComputerBoard instance and builds a grid of Squares using the
     * Board superclass constructor. All the computers ships are then created ready to
     * be placed on the grid. These are then placed using the placeAllShips method.
     * @param boardSize: {int} number of rows and columns
     * @param squareSize: {int} height and width of each grid square
     */
    public ComputerBoard(int boardSize, int squareSize) {

        // call superclass Board constructor
        super(boardSize, squareSize);
        // create all ships for computer to place
        createComputerShips();
        // place all the ships randomly on the grid
        placeAllShips();
    }

    //***************************************************************************//

    //********************************Class methods******************************//

    /** method createComputerShips()
     * Creates Ship objects and adds them to the ships array in the Board superclass
     */
    private void createComputerShips() {

        getShips().addAll(Arrays.asList(
                new Ship("Battleship", 4),
                new Ship("Cruiser1", 3),
                new Ship("Cruiser2", 3),
                new Ship("Destroyer1", 2),
                new Ship("Destroyer2", 2),
                new Ship("Destroyer3", 2)));
    }

    /** method createComputerShips()
     * NOTE: THIS IS USED FOR TESTING PURPOSES,
     * ENABLING THIS MAKES THE COMPUTERS SHIPS VISIBLE TO THE PLAYER
     */
//    private void createComputerShips() {
//        getShips().addAll(Arrays.asList(
//                new Ship("Battleship", 4, Color.LIGHTSLATEGRAY),
//                new Ship("Cruiser1", 3, Color.CORAL),
//                new Ship("Cruiser2", 3, Color.WHITE),
//                new Ship("Destroyer1", 2, Color.DARKTURQUOISE),
//                new Ship("Destroyer2", 2, Color.YELLOW),
//                new Ship("Destroyer3", 2, Color.PALEGREEN)));
//    }

    /** method placeAllShips()
     * Places all the ships in a random location on the grid
     */
    private void placeAllShips() {

        // loop through the number of ships in the array
        for (int i = getShips().size() - 1; i >= 0; i--) {

            // assign random row
            int randomRow = ThreadLocalRandom.current().nextInt(0, getBoardSize());
            // assign random column
            int randomColumn = ThreadLocalRandom.current().nextInt(0, getBoardSize());
            // assign random boolean
            setHorizontal(new Random().nextBoolean());

            // attempt to place the ship
            if (!placeShip(getGrid()[randomRow][randomColumn], getShips().get(i))) {

                i++; // if ship placement failed, increase number of iterations
            }
        }
    }

} // close class ComputerBoard
