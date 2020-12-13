/********************************************************************************
 * Author: 1816477                                                              *
 * Date: Dec 2020                                                               *
 * Assignment: Element 011 Battleships Game                                     *
 * Description: The Ship class is used to create the ships used in the          *
 * battleships game. These are placed on the board and are attacked by the      *
 * players. It contains vital ship information such as its length and health.   *
 * It is also used to handle visual changes to the ships label. This is located *
 * in the main GUI and is used to indicate the current status of the ship.      *
 ********************************************************************************/

package battleships;

import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class Ship {

    //**************************Class instance variables*************************//

    private String type; // name of the type of ship, used for the label
    private int length; // length of the ship based on number of squares
    private int health; // health of the ship based on number of squares
    private Label shipsLabel; // label of the ship
    private boolean isPlaced = false; // check if the ship has been placed or not
    private Color color = Color.BLACK; // default colour of the ship

    //***************************************************************************//

    //****************************Class constructors*****************************//

    /** constructor 1
     * Creates a basic Ship by setting its type and length. The length is
     * how many squares the ship will cover on the grid and also the amount of health
     * it has.
     * Each ship has a label that is used and displayed in the south area of the GUI.
     * @param type: {String} Ship type
     * @param length: {int} Ship length
     */
    public Ship (String type, int length) {

        this.type = type; // set ship type
        this.length = length; // set ship length
        this.health = length; // set ship health
        this.shipsLabel = new Label(type + ": " + length + " squares"); // create ship label
    }

    /** constructor 2
     * Overloaded constructor that also sets the ships colour and label click handler.
     * @param type: {String} Ship type
     * @param length: {int} Ship length
     * @param color: {Color} Ship colour
     * @param labelClickHandler: {MouseEvent} action when label is clicked
     */
    public Ship(String type, int length, Color color, EventHandler<? super MouseEvent> labelClickHandler) {

        this(type, length); // call default constructor

        // additional Ship settings
        this.color = color; // set ship color
        this.shipsLabel.setOnMouseClicked(labelClickHandler); // label mouse click event
    }

    //***************************************************************************//

    //********************************Class methods******************************//

    /** method lowerHealth()
     * This method lowers the health of the Ship by 1 and greys the
     * label if the ship reaches 0 health.
     */
    public void lowerHealth() {

        health--; // lower health by 1

        if (health < 1) { // if health drops below 1

            shipsLabel.setStyle("-fx-background-color: GREY;"); // set label as grey
        }
    }


    //***************************************************************************//

    //**************************Class getters and setters************************//

    /** method getLength()
     * This method returns the length of the ship as an integer
     */
    public int getLength() {

        return length;
    }

    /** method isPlaced()
     * This method returns a boolean stating if the ship has been placed or not
     */
    public boolean isPlaced() {

        return isPlaced;
    }

    /** method setPlaced()
     * This method sets ship as placed and changes the label to grey
     */
    public void setPlaced() {

        shipsLabel.setOnMouseClicked(null);
        isPlaced = true; // set as true
        // set label colour
        shipsLabel.setStyle("-fx-border-color: BLACK; " +
                "-fx-border-width: 1; -fx-background-color: GREY");
    }

    /** method getShipsLabel()
     * This method returns the ships Label
     */
    public Label getShipsLabel() {

        return shipsLabel;
    }

    /** method getHealth()
     * This method returns the ships current health as an integer
     */
    public int getHealth() {

        return health;
    }

    /** method getColor()
     * This method returns the ships color
     */
    public Color getColor() {

        return color;
    }

    /** method toString()
     * Return a string of information about the ship object
     */
    @Override
    public String toString() {

        return "Ship{" +
                "type='" + type + '\'' + // ship type
                ", length=" + length + // ship length
                ", health=" + health + // ship health
                '}';
    }

} // close class Ship
