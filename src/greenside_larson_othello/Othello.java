/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package greenside_larson_othello;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author Trevor Greenside
 */
public class Othello extends Application {
    
    private Board board;
    
    @Override
    public void start(Stage primaryStage) {
        
        VBox root = new VBox();
        BorderPane players = new BorderPane();
        
        Player human = new Player("De Palma");
        players.setLeft(human);
        
        board = new Board(8);
        
        root.getChildren().addAll(players, board);
        Scene scene = new Scene(root, 750, 675);
        
        primaryStage.setTitle("Othello - Greenside & Larson");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
