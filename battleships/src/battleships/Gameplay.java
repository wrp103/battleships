/********************************************************************************
 * Author: 1816477                                                              *
 * Date: Dec 2020                                                               *
 * Assignment: Element 011 Battleships Game                                     *
 * Description: Class Gameplay is used to handle attempts at destroying an      *
 * opponents ship. It includes a public attack method which calls a series      *
 * of other private methods. These provide the result of the players attack     *
 * and initiate an attack by the computer. Most of this class is made up of     *
 * computer attack logic methods, which are used to improve the odds of the     *
 * computer winning the game.                                                   *
 * The attack method will return an integer based on if the player or           *
 * computer has won or if the game is not yet finished.                         *
 ********************************************************************************/

package battleships;

import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Gameplay {

    //**************************Class instance variables*************************//

    private Square targetModeSquare = null; // square used for computers targeting mode
    private boolean targetModeActive = false; // declare when computer should use target mode
    private ArrayList<Square[]> targetBlocks = new ArrayList<>(); // blocks of squares for computer to target
    private ArrayList<Square> playerHits = new ArrayList<>(); // track the squares a player hit a ship
    private ArrayList<Square> computerHits = new ArrayList<>(); // track the squares a computer hit a ship
    private int targetDirection = 0; // computer attack direction 0 = vertical 1 = horizontal

    //***************************************************************************//

    //****************************Class constructors*****************************//

    /** Constructor
     * This constructor creates an object of the Gameplay class and runs the method to
     * create the target blocks used by the computers attacking logic.
     * @param playerBoard: {PlayerBoard} Board used to initiate creation of target blocks
     */
    public Gameplay(PlayerBoard playerBoard) {

        createTargetBlocks(playerBoard); // create first set of target blocks
    }

    //***************************************************************************//

    //********************************Class methods******************************//

    /** method attack()
     * This method uses the square provided to initiate an attacks on each board.
     * One attack being the square clicked and another being randomly chosen by.
     * It returns an integer based on the outcome of these attacks.
     * Returns 0 if no winner || Returns 1 if player has won || Returns 2 if computer has won
     * @param square: {Square} Square attacked by the player
     * @param playerBoard: {PlayerBoard} Board the computer attacks
     * @param computerBoard: {ComputerBoard} Board the player attacks
     */
    public int attack(Square square, PlayerBoard playerBoard, ComputerBoard computerBoard) {

        square.setOnMouseClicked(null); // disable square (cannot be attacked again)

        playersTurn(square, computerBoard); // player attacks square

        if (computerBoard.getShips().isEmpty()) return 1; // if all computer ships destroyed, player wins

        computersTurn(playerBoard); // computer attacks

        if (playerBoard.getShips().isEmpty()) return 2; // if all player ships destroyed, computer wins

        return 0; // game continues
    }

    /** method playersTurn()
     * This method attacks the square provided and checks if the square contains
     * one of the ships from the given list of Ships. If a ship is destroyed,
     * it is removed from the list.
     * @param square: {Square} Square attacked by the player
     * @param computerBoard: {ComputerBoard} Opponents board
     */
    private void playersTurn(Square square, ComputerBoard computerBoard) {

        if (square.getOccupyingShip() == null) { // no ship located in this square

            miss(square); // call miss method

        } else { // ship has been hit

            playerHits.add(square); // add to the list of hits by the player
            hit(square); // call hit method

            if (square.getOccupyingShip().getHealth() < 1) { // if ship has no remaining health

                destroy(playerHits); // call destroy method
                computerBoard.getShips().remove(square.getOccupyingShip()); // remove ship
            }
        }
    }

    /** method computersTurn()
     * This method is the base method for initiating an attack on the players board.
     * It invokes a series of events to attack the most likely square to contain a ship.
     * @param playerBoard: {PlayerBoard} Players board
     */
    private void computersTurn(PlayerBoard playerBoard) {

        // check if target mode is active
        if (targetModeActive) {

            targetMode(playerBoard); // initiate target mode

        } else { // if no target is found

            Square square = getCommonSquare(); // get the most likely square to contain a ship

            // attack the square
            if (square.getOccupyingShip() == null) { // if it does not contain a ship

                miss(square); // call the miss method
                reduceBlocks(square); // remove this square from target blocks

            } else { // if it's a hit

                hit(square, playerBoard); // call hit method
                targetModeSquare = square; // set as the starting point for target mode
            }
        }
    }

    /** method hit()
     * This method sets the colour of the Square provided as red to indicate it was a hit.
     * The square variable is also set to known occupied for validation purposes. The ships
     * health that was struck is reduced. The method also plays a sound effect clip of a
     * small explosion.
     * @param square: {Square} Square attacked
     */
    private void hit(Square square) {

        AudioPlayer.HIT.play(); // play hit sound file
        square.setFill(Color.RED); // set square colour to red
        square.getOccupyingShip().lowerHealth(); // lower the ships health
        square.setKnownOccupied(true); // set square as known occupied
    }

    /** method hit()
     * This method is an overloaded version of the basic hit method.
     * It adds an additional set of actions specific to the computer making a hit.
     * This includes, checking for currently surviving hit ships and activating or
     * deactivating target mode.
     * @param square: {Square} Square attacked
     * @param playerBoard: {PlayerBoard} Board the hit was made on
     */
    private void hit(Square square, PlayerBoard playerBoard) {

        targetModeActive = true; // activate target mode
        computerHits.add(square); // add square to list of hit squares
        hit(square); // call standard hit method

        // if square hit destroys the ship
        if (square.getOccupyingShip().getHealth() < 1) {

            destroy(computerHits); // call destroy method
            playerBoard.getShips().remove(square.getOccupyingShip()); // remove destroyed ship

            // set  currently surviving ships found as the target mode square
            targetModeSquare = getDamagedShips();

            // if no surviving ships were found
            if (targetModeSquare == null) {

                targetModeActive = false; // deactivate target mode
                createTargetBlocks(playerBoard); // create new target blocks
            }
        }
    }

    /** method miss()
     * This method sets the colour of the Square provided as blue to indicate it was a miss.
     * The square variable is also set to known empty for validation purposes.
     * @param square: {Square} Square attacked
     */
    private void miss(Square square) {

        square.setFill(Color.AQUAMARINE); // set square colour to light blue
        square.setKnownEmpty(true); // set square as known empty
    }

    /** method destroy()
     * This method plays a large explosion sound effect and checks the list of
     * hit squares provided, setting their colour orange if they contain a destroyed ship.
     * The squares variable is also set to known destroyed for validation purposes.
     * @param hitSquares: {ArrayList<Square>} List of current hit squares
     */
    private void destroy(ArrayList<Square> hitSquares) {

        AudioPlayer.DESTROY.play(); // play large explosion sound effect

        for (Square square : hitSquares) { // iterate through the list of hit squares

            // if the ship on this square has no remaining health
            if (square.getOccupyingShip().getHealth() < 1) {

                square.setKnownDestroyed(true); // set as destroyed
                square.setFill(Color.ORANGE); // set square colour as orange
            }
        }
    }
    /** method createTargetBlocks()
     * This method creates a list of Squares separated into individual arrays. These are the blocks.
     * The Squares in these blocks are either untouched or contain a hit. These are used by the
     * computer to locate potential areas of the longest ship alive.
     * @param playerBoard: {PlayerBoard} Board used to initiate creation of target blocks
     */
    private void createTargetBlocks(PlayerBoard playerBoard) {

        targetBlocks.clear(); // clear current list of blocks
        int boardSize = playerBoard.getBoardSize(); // get board limit
        int range = calculateRange(playerBoard.getShips()); // set block array size to longest ship alive

        // create blocks of columns (vertical blocks)
        for (int row = 0; row <= (boardSize - range); row++) { // cycle through rows within range of ship size

            for (int column = 0; column < boardSize; column++) { // cycle through columns within the board

                Square[] block = new Square[range]; // create new block

                int squareCounter = 0; // create square counter (used to check the block size matches the range)

                for (int i = 0; i < range; i++) { // cycle through potential block squares

                    Square square = playerBoard.getGrid()[row + i][column]; // get current square

                    if (!square.isKnownEmpty() && !square.isKnownOccupied()) { // if square is untouched

                        block[i] = square; // add square to block
                        squareCounter++; // increase counter
                    }
                } // exit block building loop

                // if squares found match that of target ship, add block to list of target blocks
                if (squareCounter == range) {

                    targetBlocks.add(block); // add block
                }

            } // exit column loop
        } // exit row loop

        // create blocks of rows (horizontal blocks)
        for (int row = 0; row < boardSize; row++) { // cycle through rows within range of ship size

            for (int column = 0;  column <= (boardSize - range); column++) { // cycle through columns within the board

                Square[] block = new Square[range]; // create new block

                int squareCounter = 0; // create square counter (used to check the block size matches the range)

                for (int i = 0; i < range; i++) { // cycle through potential block squares

                    Square square = playerBoard.getGrid()[row][column + i]; // get current square

                    if (!square.isKnownEmpty() && !square.isKnownOccupied()) { // if square is untouched

                        block[i] = square; // add square to block
                        squareCounter++; // increase counter
                    }
                } // exit block building loop

                // if squares found match that of target ship, add block to list of target blocks
                if (squareCounter == range) {

                    targetBlocks.add(block); // add block
                }

            }  // exit column loop
        } // exit row loop
    } // close createTargetBlocks method

    /** method reduceBlocks()
     * This method takes a given Square, scans the current list of blocks and removes
     * any block that contains the square.
     * @param square: {Square} Square to remove from block list
     */
    private void reduceBlocks(Square square) {

        List<Square[]> blocksToRemove = new ArrayList<>(); // temp list of blocks to remove from list

        for (Square[] block : targetBlocks) { // cycle through the target blocks list

            for (Square check : block) { // check each block

                if (check.equals(square)) { // if check matches the square to remove

                    blocksToRemove.add(block); // add this block to the removal list
                }
            }
        }

        targetBlocks.removeAll(blocksToRemove); // remove the blocks found to contain the square
    }

    /** method targetMode()
     * This method uses the instance variable targetModeSquare to locate the next possible
     * square containing the found ship.
     * @param playerBoard: {PlayerBoard} Board containing target to destroy
     */
    private void targetMode(PlayerBoard playerBoard) {

        int boardSize = playerBoard.getBoardSize(); // get current limits of board
        Square[][] grid = playerBoard.getGrid(); // get the current grid array
        int range = calculateRange(playerBoard.getShips()); // get largest possible ship length
        int targetRow = targetModeSquare.getRow(); // get current target row
        int targetColumn = targetModeSquare.getColumn(); // get current target column
        Square nextTarget = null; // create next target object

        while (nextTarget == null) { // loop until next target is found

            if (targetDirection == 0) { // vertical attack

                // check north
                nextTarget = attackNorth(range, targetRow, targetColumn, 0, grid);

                // check south
                if (nextTarget == null) nextTarget = attackSouth(range, targetRow, targetColumn, boardSize, grid);

                // change direction of attack
                if (nextTarget == null) targetDirection = 1;

            }

            if (targetDirection == 1) { // horizontal attack

                // check east
                nextTarget = attackEast(range, targetRow, targetColumn, boardSize, grid);

                // check west
                if (nextTarget == null) nextTarget = attackWest(range, targetRow, targetColumn, 0, grid);

                if (nextTarget == null) targetDirection = 0;
            }

        } // end while loop


        if (nextTarget.getOccupyingShip() == null) { // if next target does not contain a ship

            miss(nextTarget); // call miss method

        } else { // else, hit the ship

            hit(nextTarget, playerBoard); // call hit method
        }
    }

    /** method calculateRange()
     * This method uses a given list of ships and returns the length of the longest ship
     * @param ships: {ArrayList<Ship>} List of ships to check
     */
    private int calculateRange(ArrayList<Ship> ships) {

        int range = 2; // set to minimum range a ship can be

        // loop through the ship list
        for (Ship ship : ships) {

            // if ship found is longer than current range
            if (ship.getLength() > range) {

                range = ship.getLength(); // set as new range
            }
        }

        return range; // return range
    }

    /** method attackSouth()
     * This method uses a given grid of squares, a starting point and an attack range to
     * find a possible ship in the southern direction.
     * @param range: {int} longest possible ship size
     * @param targetRow: {int} row that was hit
     * @param targetColumn: {int} column that was hit
     * @param limit: {int} Limit of the board
     * @param grid: {Square[][]} Grid containing squares to attack
     */
    private Square attackSouth(int range, int targetRow, int targetColumn, int limit, Square[][] grid) {

        for (int i = 1; i < range; i++) { // loop through possible target squares

            if ((targetRow + i) >= limit) { // if square is out of bounds

                return null; // stop loop and return null

            } else { // square is within bounds

                Square square = grid[targetRow + i][targetColumn]; // get potential square

                // if potential square is already a miss or contains a destroyed ship, return null
                if (square.isKnownEmpty() || square.isKnownDestroyed()) {

                    return null; // stop loop and return null

                // else if square is unknown, attack
                } else if (!square.isKnownOccupied()) {

                    return square; // return next target
                }

            } // square must be a hit (red), continue loop and attack the next square along

        } // close loop

        return null; // no viable square found, return null
    }

    /** method attackNorth()
     * This method uses a given grid of squares, a starting point and an attack range to
     * find a possible ship in the northern direction.
     * @param range: {int} longest possible ship size
     * @param targetRow: {int} row that was hit
     * @param targetColumn: {int} column that was hit
     * @param limit: {int} Limit of the board
     * @param grid: {Square[][]} Grid containing squares to attack
     */
    private Square attackNorth(int range, int targetRow, int targetColumn, int limit, Square[][] grid) {

        for (int i = 1; i < range; i++) { // loop through possible target squares

            if ((targetRow - i) < limit) { // if square is out of bounds

                return null; // stop loop and return null

            } else { // square is within bounds

                Square square = grid[targetRow - i][targetColumn]; // get potential square

                // if potential square is already a miss or contains a destroyed ship, return null
                if (square.isKnownEmpty() || square.isKnownDestroyed()) {

                    return null; // stop loop and return null

                // else if square is unknown, attack
                } else if (!square.isKnownOccupied()) {

                    return square; // return next target

                } // square must be a hit (red), continue loop and attack the next square along
            }
        } // close loop

        return null; // no viable square found, return null
    }

    /** method attackEast()
     * This method uses a given grid of squares, a starting point and an attack range to
     * find a possible ship in the eastern direction.
     * @param range: {int} longest possible ship size
     * @param targetRow: {int} row that was hit
     * @param targetColumn: {int} column that was hit
     * @param limit: {int} Limit of the board
     * @param grid: {Square[][]} Grid containing squares to attack
     */
    private Square attackEast(int range, int targetRow, int targetColumn, int limit, Square[][] grid) {

        for (int i = 1; i < range; i++) { // loop through possible target squares

            if ((targetColumn + i) >= limit) { // if square is out of bounds

                return null; // stop loop and return null

            } else { // square is within bounds

                Square square = grid[targetRow][targetColumn + i]; // get potential square

                // if potential square is already a miss or contains a destroyed ship, return null
                if (square.isKnownEmpty() || square.isKnownDestroyed()) {

                    return null; // stop loop and return null

                    // else if square is unknown, attack
                } else if (!square.isKnownOccupied()) {

                    return square; // return next target
                }

            } // square must be a hit (red), continue loop and attack the next square along

        } // close loop

        return null; // no viable square found, return null
    }

    /** method attackWest()
     * This method uses a given grid of squares, a starting point and an attack range to
     * find a possible ship in the western direction.
     * @param range: {int} longest possible ship size
     * @param targetRow: {int} row that was hit
     * @param targetColumn: {int} column that was hit
     * @param limit: {int} Limit of the board
     * @param grid: {Square[][]} Grid containing squares to attack
     */
    private Square attackWest(int range, int targetRow, int targetColumn, int limit, Square[][] grid) {

        for (int i = 1; i < range; i++) { // loop through possible target squares

            if ((targetColumn - i) < limit) { // if square is out of bounds

                return null; // stop loop and return null

            } else { // square is within bounds

                Square square = grid[targetRow][targetColumn - i]; // get potential square

                // if potential square is already a miss or contains a destroyed ship, return null
                if (square.isKnownEmpty() || square.isKnownDestroyed()) {

                    return null; // stop loop and return null

                // else if square is unknown, attack
                } else if (!square.isKnownOccupied()) {

                   return square; // return next target
                }

            } // square must be a hit (red), continue loop and attack the next square along

        } // close loop

        return null; // no viable square found, return null
    }

    /** method getCommonSquare()
     * Finds and returns the most common square found in the targetBlocks ArrayList
     */
    private Square getCommonSquare() {

        // create a map of squares to count the number of times they appear
        Map<Square, Integer> squareCount = new HashMap<>();

        for (Square[] block : targetBlocks) { // loop through all target blocks

            for (Square square : block) { // check the squares in the block

                // if the square has not yet been added to the map
                if (!squareCount.containsKey(square)) {

                    squareCount.put(square, 1); // add and set count as 1

                } else { // if square is already in the map

                    squareCount.put(square, squareCount.get(square) + 1); // add 1 to the count
                }
            } // close individual block loop
        } // close targetBlocks list loop

        // get the current length of the blocks
        int blockSize = targetBlocks.get(0).length;

        int highestCount = 0; // temp variable used to hold the current highest square count
        Square mostCommonSquare = null; // temp variable to hold the current most common square found

        for (Square square : squareCount.keySet()) { // loop through the squares in the map

            // highest possible square count is ship length x 2
            // if square count matches the max possible, this must be the most common
            if (squareCount.get(square) == (blockSize * 2) && !square.isKnownOccupied()) {

                mostCommonSquare = square; // set as most common square
                break; // end loop

            // else check if the square being checked has a higher count than the current highest count
            } else if (squareCount.get(square) > highestCount && !square.isKnownOccupied()) {

                mostCommonSquare = square; // set as current most common square
                highestCount = squareCount.get(square); // set new highest count
            }
        }

        return mostCommonSquare; // return most common square
    }

    /** method getDamagedShips()
     * Checks current hits and returns a Square that contains a surviving ship
     */
    private Square getDamagedShips() {

        // loop through the list of hits
        for (Square square : computerHits) {

            // if square contains a ship with more than 0 health
            if (square.getOccupyingShip().getHealth() > 0) {

                return square; // return the square as next starting location for target mode
            }

        } // close for loop

        return null; // no Squares found with surviving ship, return null
    }

} // close class Gameplay


