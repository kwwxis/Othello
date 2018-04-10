/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package greenside_larson_othello;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

/**
 *
 * @author Trevor Greenside
 */
public class Game extends VBox {
    
    private Board board;
    private BorderPane players;
    private Player human;
    private Player skynet;
    
    public Game() {
        players = new BorderPane();
        
        human = new Player("De Palma");
        players.setLeft(human);
        
        board = new Board(8);
        
        addComponents();
    }
    
    private void addComponents() {
        this.getChildren().addAll(players, board);
    }
    
}
