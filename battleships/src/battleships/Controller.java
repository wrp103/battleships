/********************************************************************************
 * Author: 1816477                                                              *
 * Date: Dec 2020                                                               *
 * Assignment: Element 011 Battleships Game                                     *
 * Description: Class Controller is instantiated by the FXML file upon running  *
 * the application. The class is used to modify the visual appearance of the    *
 * game during runtime.                                                         *
 * It's also used to set and call GUI click events, including clicking on       *
 * buttons, the board and ship labels. These events allows the user to          *
 * progress or switch between game phases.                                      *
 ********************************************************************************/

package battleships;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Controller {

    //**************************Class instance variables*************************//

    // instance variables used for controlling the game
    private static final int boardSize = 10; // number of grid rows and columns
    private static final int squareSize = 46; // height and width of the grid squares
    private PlayerBoard playerBoard; // player board object
    private ComputerBoard computerBoard; // computer board object
    private Gameplay gameplay; // gameplay object

    // instance variables to handle changes to the FXML objects
    @FXML
    private Pane leftBoard; // left pane
    @FXML
    private Pane rightBoard; // right pane
    @FXML
    private VBox leftSouthArea; // left ship label area
    @FXML
    private VBox rightSouthArea; // right ship label area
    @FXML
    private Button startGame; // start game button
    @FXML
    private Button rotateShip; // rotate ship button
    @FXML
    private Button resetGrid; // reset grid button
    @FXML
    private VBox mainMenuArea; // main menu area vbox
    @FXML
    private Label mainLabel; // main menu header
    @FXML
    private VBox countDownArea; // countdown area vbox
    @FXML
    private Label countDownLabel; // countdown label

    private Text leftSouthMessage = new Text(); // text used to update left side message (above ship labels)
    private Text rightSouthMessage = new Text(); // text used to update right side message (above ship labels)

    //***************************************************************************//

    //********************************Class methods******************************//

    /** method initialize()
     * This method is called after class construction and gives access to @FXML annotated objects.
     * It also starts the game music.
     */
    public void initialize() {

        mainMenuArea.setVisible(true); // display main menu
        AudioPlayer.MUSIC.loop(); // start music
    }

    /** method newGame()
     * Builds and displays the main area used by the user to place their ships. It creates both grids
     * and places them on their particular side. The labels and buttons are then added to the southern areas.
     */
    @FXML
    private void newGame() {

        // clear previous game
        if (playerBoard != null) {

            clearGame(); // clear game
        }

        createBoards(); // create player and computer grids

        // build and add ship labels and a header to south area
        buildSouthArea(leftSouthArea, leftSouthMessage, playerBoard.getShips(),
                "BLUE", "Place your ships..");
        buildSouthArea(rightSouthArea, rightSouthMessage, computerBoard.getShips(),
                "GREY", "Computer is ready..");

        // display all elements of main game area
        rotateShip.requestFocus();
        resetGrid.setVisible(true);
        startGame.setVisible(true);
        rotateShip.setVisible(true);
        // remove the main menu display
        mainMenuArea.setVisible(false);

        // set selected ship and its label styling
        playerBoard.setSelectedShip(playerBoard.getShips().get(0));
        setLabelSelected(playerBoard.getSelectedShip().getShipsLabel(), true);
    }

    /** method clearGame()
     * Clears both boards, resets the progress of the ship placement labels and
     * enables the rotate and reset buttons and disables the start game button.
     */
    @FXML
    private void clearGame() {

        // clear board objects
        playerBoard = null;
        computerBoard = null;
        // clear labels from GUI
        leftSouthArea.getChildren().clear();
        rightSouthArea.getChildren().clear();
        // enable buttons
        rotateShip.setDisable(false);
        resetGrid.setDisable(false);
        startGame.setDisable(true);

    }

    /** method createBoards()
     * Creates objects of the PlayerBoard and ComputerBoard classes and adds
     * them to their respective sides of the GUI.
     */
    private void createBoards() {

        // create boards
        playerBoard = new PlayerBoard( boardSize, squareSize, this::placeShipClick, this::clickLabel);
        computerBoard = new ComputerBoard(boardSize, squareSize);
        // add boards to GUI
        leftBoard.getChildren().add(playerBoard.getAnchorPane());
        rightBoard.getChildren().add(computerBoard.getAnchorPane());
    }

    /** method buildSouthArea()
     * Modifies a given VBox by adding a line of Text and a list of ship labels
     * @param area: {VBox} The VBox to modify
     * @param southMessage: {Text} The message that's display
     * @param shipList: {ArrayList<Ship>} The list of ships to display
     * @param labelColour: {String} The labels colour
     * @param message: {String} The text used for the message
     */
    private void buildSouthArea(VBox area, Text southMessage, ArrayList<Ship> shipList, String labelColour, String message) {

        // set the message using the text String
        southMessage.setText(message);
        area.getChildren().add(southMessage);

        // add all ship labels to the VBox
        for (Ship ship : shipList) { // iterate through ship array

            area.getChildren().add(ship.getShipsLabel()); // add to VBox
            ship.getShipsLabel().setStyle("-fx-background-color:" + labelColour); // label colour
        }
    }

    /** method placeShipClick()
     * Gets the square clicked on and calls the methods for ship placement and validation.
     * @param event: {MouseEvent} Mouse event (location of click)
     */
    private void placeShipClick(MouseEvent event) {

        // attempt to place a ship at this location using the placeShip method
        if (playerBoard.placeShip((Square) event.getSource(), playerBoard.getSelectedShip())) {

            // loop through all the ships and check if there are any ships unplaced
            boolean shipsRemaining = false;
            for (Ship ship : playerBoard.getShips()) {

                if (!ship.isPlaced()) {  // if ship is not yet placed

                    shipsRemaining = true; // set ships remaining as true
                    playerBoard.setSelectedShip(ship); // set the unplaced ship as next to place
                    setLabelSelected(ship.getShipsLabel(), true); // set label styling
                    break; // break out of loop
                }
            }

            if (!shipsRemaining) { // if all ships are placed

                playerBoard.disableClickableGrid(); // disable clickable grid
                rotateShip.setDisable(true); // disable rotate button
                leftSouthMessage.setText("Press start game to begin"); // set start game message
                startGame.setDisable(false); // show start game button
                startGame.requestFocus(); // highlight start game button
            }
        } // close outer If statement
    }

    /** method clickLabel()
     * Gets the label object clicked on by the user.
     * @param event: {MouseEvent} Mouse event (location of click)
     */
    private void clickLabel(MouseEvent event) {

        // select the ship the label is associated with
        selectShip(playerBoard.getSelectedShip(), (Label) event.getSource());
    }

    /** method selectShip()
     * Changes the current ship selected to the newly selected ship based on
     * the label clicked on by the user.
     * @param currentSelected: {Ship} Ship currently selected
     * @param newSelected: {Label} Label clicked on
     */
    private void selectShip(Ship currentSelected, Label newSelected) {

        // if the label clicked on is NOT already selected
        if (!currentSelected.getShipsLabel().equals(newSelected)) {

            // set the currently selected ship style back to default
            setLabelSelected(currentSelected.getShipsLabel(), false);

            // loop through all ships to be placed
            for (Ship ship : playerBoard.getShips()) {

                // if the label object clicked on matches a ship
                if (ship.getShipsLabel().equals(newSelected)) {

                    // set the ship as the selected
                    playerBoard.setSelectedShip(ship);
                    // set the styling of the ship to indicate a new selection
                    setLabelSelected(ship.getShipsLabel(), true);
                } // close inner if statement
            } // close for loop
        } // close outer if statement
    } // close method

    /** method setLabelSelected()
     * Styles the ship labels when the user makes a selection. It gives the
     * selected label a green border while the unselected ship a black border.
     * @param label: {Label} Label to style
     * @param selected: {boolean} If selected
     */
    private void setLabelSelected(Label label, boolean selected) {

        if (selected) { // if currently selected, set a green border

            label.setStyle(
                    "-fx-border-color: GREEN; " + // border colour
                    "-fx-border-width: 5; "); // border width

        } else { // else, set a black border

            label.setStyle(
                    "-fx-border-color: BLACK; " + // border colour
                    "-fx-border-width: 1");
        }
    }

    /** method rotate()
     * This methods changes the direction of the ship to be placed.
     */
    @FXML
    private void rotate() {

        if (playerBoard.isHorizontal()) { // if currently horizontal

            playerBoard.setHorizontal(false); // set as vertical

        } else { // if currently vertical

            playerBoard.setHorizontal(true); // set as horizontal
        }
    }

    /** method startGame()
     * This methods starts the countdown, modifies the GUI for game play and then displays it
     * It also creates a new Gameplay object and makes the computer board clickable and ready for
     * attacking.
     */
    @FXML
    private void startGame() {

        countDown(); // begin countdown

        // disable buttons visibility
        resetGrid.setVisible(false);
        startGame.setVisible(false);
        rotateShip.setVisible(false);

        // remove boards from anchor pane
        leftBoard.getChildren().remove(playerBoard.getAnchorPane());
        rightBoard.getChildren().remove(computerBoard.getAnchorPane());

        // add boards (switching computerboard to the left and playerboard to right
        leftBoard.getChildren().add(computerBoard.getAnchorPane());
        rightBoard.getChildren().add(playerBoard.getAnchorPane());

        // remove labels
        leftSouthArea.getChildren().clear();
        rightSouthArea.getChildren().clear();

        // add new labels
        buildSouthArea(leftSouthArea, leftSouthMessage, computerBoard.getShips(),
                "BLUE", "Computer ships remaining:");
        buildSouthArea(rightSouthArea, rightSouthMessage, playerBoard.getShips(),
                "BLUE", "Player ships remaining:");

        // create new gameplay object
        gameplay = new Gameplay(playerBoard);

        // enable board to be clickable
        computerBoard.enableClickableGrid(this::attackClick);
    }

    /** method countDown()
     * This methods creates a new thread that firstly, sets the countdown screen as
     * visible and then iterates through a loop to display a countdown to the user.
     */
    private void countDown()  {

        // create thread
        Thread countDown = new Thread(() -> {

            // display the count down screen to the users
            Platform.runLater(() -> countDownArea.setVisible(true));

            // count down starting from the int variable i
            for (int i = 3; i >= 0; i--) {

                // pause the thread
                try {
                    TimeUnit.MILLISECONDS.sleep(1200);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }

                // set and display current counter digit
                int counter = i;
                Platform.runLater(() -> countDownLabel.setText(String.valueOf(counter)));
            }

            // display a message to indicate the game is about to start
            Platform.runLater(() -> countDownLabel.setText(("GO!")));

            // pause the thread
            try {
                TimeUnit.MILLISECONDS.sleep(1200);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }

            // remove the countdown screen from users visibility
            Platform.runLater(() -> countDownArea.setVisible(false));
        });

        countDown.start(); // start thread
    }

    /** method attackClick()
     * This method gets the square that was clicked on and uses the Gameplay object
     * to initiate a round of attacks. If this round of attacks returns that either
     * the player or computer has won, the main menu is displayed with a message
     * indicating who the winner is.
     * @param event: {MouseEvent} Mouse event (location of click)
     */
    private void attackClick(MouseEvent event) {

        // initiate a round of attacks and assign the result to the temporary variable
        int winnerCheck = gameplay.attack((Square) event.getSource(), playerBoard, computerBoard);

        // if the result is over 0, a winner has been found
        if (winnerCheck > 0) {

            // modify the main menu styling
            mainMenuArea.setStyle("-fx-background-color: rgba(0, 100, 100, 0.8);");

            if (winnerCheck == 1) { // if the result equals 1, the player has won

                mainLabel.setText("YOU WIN"); // set winning message

            } else {  // else the computer has won

                mainLabel.setText("YOU LOSE"); // set a losing message
            }

            // display the main menu
            mainMenuArea.setVisible(true);
        }
    } // close attackClick method

    /** method quitButton()
     * Exits the application
     */
    @FXML
    private void quitButton() {

        Platform.exit();
        System.exit(0);
    }

} // close class Controller
