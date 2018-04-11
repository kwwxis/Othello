/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package greenside_larson_othello;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

/**
 *
 * @author Trevor Greenside
 */
public class Game extends VBox {
    
    private final Board board;
    private final FlowPane players;
    private final Player human;
    private final Player skynet;
    private GameTimer gameTimer;
    private final Button undo;
    
    public Game() {
        players = new FlowPane();
        
        human = new Player("De Palma");
        skynet = new Player("A.I.");
        players.getChildren().addAll(human, skynet);
        players.setHgap(50);
        
        board = new Board(8, gameTimer);
        
        gameTimer = new GameTimer(10, board);
        
        undo = new Button("Undo");
        undo.setOnAction((ActionEvent e) -> {
            System.out.println("Undo button clicked");
        });
        
        addComponents();
    }
    
    private void addComponents() {
        this.setMaxWidth(600);
        this.setHeight(800);
        FlowPane buttons = new FlowPane();
        buttons.getChildren().addAll(undo);
        this.getChildren().addAll(players, board, gameTimer, buttons);
    }
    
}
