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
    
    private final Board board;
    private final BorderPane players;
    private final Player human;
    private final Player skynet;
    private GameTimer gameTimer;
    
    public Game() {
        players = new BorderPane();
        
        human = new Player("De Palma");
        skynet = new Player("A.I.");
        players.setLeft(human);
        players.setRight(skynet);
        
        board = new Board(8, gameTimer);
        
        gameTimer = new GameTimer(10, board);
        
        addComponents();
    }
    
    private void addComponents() {
        this.getChildren().addAll(players, board, gameTimer);
    }
    
}
