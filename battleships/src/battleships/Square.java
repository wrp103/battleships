/********************************************************************************
 * Author: 1816477                                                              *
 * Date: Dec 2020                                                               *
 * Assignment: Element 011 Battleships Game                                     *
 * Description: Class Square uses the superclass Rectangle to create a square   *
 * shape object. These are used to make up the battleships grid.                *
 * Variables are used to verify the current status of the square including,     *
 * if the square is empty, occupied, destroyed or has a ship on it.             *
 ********************************************************************************/

package battleships;

import javafx.scene.effect.ColorAdjust;
import javafx.scene.shape.Rectangle;

public class Square extends Rectangle {

    //**************************Class instance variables*************************//

    // grid variables
    private int row; // squares row
    private int column; // squares column

    // variables for gameplay validation
    private boolean knownEmpty = false; // square known empty (Blue)
    private boolean knownOccupied = false; // square known occupied (Contains ship)
    private boolean knownDestroyed = false; // square know destroyed (Orange)
    private Ship occupyingShip = null; // square is occupied by a ship

    // highlight effects used for mouse events
    private ColorAdjust squareEffect = new ColorAdjust(); // squares colour adjust effects

    //***************************************************************************//

    //****************************Class constructors*****************************//

    /** constructor
     * Creates a new Square instance and uses the arguments provided to
     * call the superclass Rectangle constructor. This creates a graphical
     * shape based on the position and size.
     * @param xAxis: {int} X coordinate of upper left corner of Square
     * @param yAxis: {int} Y coordinate of upper left corner of Square
     * @param width: {int} width of Square
     * @param height: {int} height of Square
     * @param row: {int} grid array row element
     * @param column: {int} grid array column element
     */
    public Square(int xAxis, int yAxis, int width, int height, int row, int column) {

        super(xAxis, yAxis, width, height); // call superclass constructor to build the shape
        this.row = row; // set the squares row position in the grid
        this.column = column; // set the squares column position in the grid
        squareEffect.setBrightness(0.24); // set the squares brightness effect
    }

    //***************************************************************************//

    //********************************Class methods******************************//

    /** method addShip()
     * Sets the squares colour and disables further effects. It also assigns
     * the provided Ship object to the occupyingShip instance variable.
     * The Ship object is then declared placed.
     * @param ship: {Ship} Starting square of Ship
     */
    public void addShip(Ship ship) {

        // set Square colour and disable effects
        this.setFill(ship.getColor());
        disableEffect();
        // assign Ship object and set as placed
        this.occupyingShip = ship;
        ship.setPlaced();
    }

    /** method enableEffect()
     * Enables the squares effect using the instance effect variable
     * with the JavaFX public method setEffect
     */
    public void enableEffect() {

        setEffect(squareEffect); // set effect
    }

    /** method disableEffect()
     * Disables the squares effect using 'null'
     * with the JavaFX public method setEffect
     */
    public void disableEffect() {

        setEffect(null); // disable effect
    }


    //***************************************************************************//

    //**************************Class getters and setters************************//

    /** method getRow()
     * This method returns the squares row number as an integer
     */
    public int getRow() {

        return row;
    }

    /** method getColumn()
     * This method returns the squares column number as an integer
     */
    public int getColumn() {

        return column;
    }

    /** method getOccupyingShip()
     * This method returns the ship occupying the square
     */
    public Ship getOccupyingShip() {

        return occupyingShip;
    }

    /** method setKnownEmpty()
     * This method sets if the ship on this square is known as empty
     * @param knownEmpty {boolean}
     */
    public void setKnownEmpty(boolean knownEmpty) {

        this.knownEmpty = knownEmpty;
    }

    /** method setKnownOccupied()
     * This method sets if the ship on this square is known as occupied
     * @param knownOccupied {boolean}
     */
    public void setKnownOccupied(boolean knownOccupied) {

        this.knownOccupied = knownOccupied;
    }

    /** method setKnownDestroyed()
     * This method sets if the ship on this square is known as destroyed
     * @param knownDestroyed {boolean}
     */
    public void setKnownDestroyed(boolean knownDestroyed) {

        this.knownDestroyed = knownDestroyed;
    }

    /** method isKnownEmpty()
     * This method returns if the square is currently known as empty or not
     */
    public boolean isKnownEmpty() {

        return knownEmpty;
    }

    /** method isKnownOccupied()
     * This method returns if the square is currently known as occupied or not
     */
    public boolean isKnownOccupied() {

        return knownOccupied;
    }

    /** method isKnownDestroyed()
     * This method returns if the ship on this square is currently known as
     * destroyed or not
     */
    public boolean isKnownDestroyed() {

        return knownDestroyed;
    }

    /** method toString()
     * Return a string of information about the square object
     */
    @Override
    public String toString() {

        return "Square{" +
                "row=" + row + // square row
                ", column=" + column + // square column
                ", knownEmpty=" + knownEmpty + // has square been attacked and missed
                ", knownOccupied=" + knownOccupied + // has square been attacked and hit
                ", knownDestroyed=" + knownDestroyed + // has square been hit and destroyed
                ", occupyingShip=" + occupyingShip + // does the square have a ship on it
                '}';
    }

} // close class Square