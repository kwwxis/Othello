/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package greenside_larson_Lee_othello;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 *
 * @author Trevor Greenside
 */
public class Othello extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        
        PlayerStart theStart = new PlayerStart();
        theStart.startButton.setOnAction((ActionEvent e) -> {
            String playername = theStart.getPlayerName();
            Color color = theStart.getColor();
            Boolean isConfigWBWB = theStart.getIsConfigWBWB();
            startGame(primaryStage, playername, color, isConfigWBWB); 
        });
        
        Scene startMenu = new Scene(theStart, 200, 300);
        primaryStage.setTitle("Othello - Greenside & Larson");
        primaryStage.setScene(startMenu);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    private void startGame(Stage primaryStage, String playername, Color color, Boolean isConfigWBWB) {
        Game root = new Game(playername, color, isConfigWBWB);
        
        Scene scene = new Scene(root, 750, 675);
        
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
