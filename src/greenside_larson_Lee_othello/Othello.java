/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package greenside_larson_Lee_othello;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

/**
 *
 * @author Trevor Greenside
 */
public class Othello extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        PlayerStart theStart = new PlayerStart();
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
            
            startGame(primaryStage, theStart); 
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
    
    private void startGame(Stage primaryStage, PlayerStart theStart) {
        Game gameRoot = new Game(theStart);
        Scene gameScene = new Scene(gameRoot, 900, 770);
        
        primaryStage.setScene(gameScene);
        primaryStage.show();
    }
}
