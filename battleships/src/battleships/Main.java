/********************************************************************************
 * Author: 1816477                                                              *
 * Date: Dec 2020                                                               *
 * Assignment: Element 011 Battleships Game                                     *
 * Description: The Main class extends the Application class and is used to     *
 * start the application. It loads the FXML file which then instantiates the    *
 * Controller class.                                                            *
 ********************************************************************************/

package battleships;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    /** method start()
     * Loads the Java FXML file and launches the main GUI window
     * @param primaryStage: {Stage} Window of JavaFX desktop application
     */
    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("Layout.fxml")); // load FXML file
        primaryStage.setTitle("Battleships"); // set window title
        primaryStage.setScene(new Scene(root, 1200, 780)); // set window size
        primaryStage.show(); // show primary window for the JavaFX application
    }

    /** method main()
     * Launches the application
     */
    public static void main(String[] args) {
        launch(args);
    }

} // close class Main
