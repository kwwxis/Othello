/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package greenside_larson_Lee_othello;

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
    
    @Override
    public void start(Stage primaryStage) {
        
        Game root = new Game();
        
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
