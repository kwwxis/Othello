package greenside_larson_lee_othello;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

/**
 * The entrypoint class. Initialized the window with the PlayerStartMenu class as the first screen.
 * 
 * @author Trevor Greenside
 * @author Kayla Larson
 * @author Matthew Lee
 */
public class Othello extends Application {

    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        PlayerStartMenu theStart = new PlayerStartMenu();
        Scene startMenu = new Scene(theStart, 400, 430);

        theStart.startButton.setOnAction((ActionEvent e) -> {
            String playername = theStart.getPlayerName();

            if (playername.isEmpty()) {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Can't start game yet");
                alert.setHeaderText(null);
                alert.setContentText("Please enter your player name.");

                alert.showAndWait();

                theStart.focusPlayerNameField();
                return;
            }

            startGame(theStart);
        });

        primaryStage.setTitle("Othello - Greenside, Larson, & Lee");
        primaryStage.setScene(startMenu);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    protected void restartGame() {
        this.start(this.primaryStage);
    }

    private void startGame(PlayerStartMenu theStart) {
        Game game = new Game(this, theStart);
        this.primaryStage.setScene(game.getGameScene());
        primaryStage.show();
    }
}
