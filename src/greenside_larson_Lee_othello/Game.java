/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package greenside_larson_Lee_othello;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

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
    private final AI ai;
    private boolean playerTurn;
    private Label currentTurn;
    
    public Game(String player1name, Color player1Color, Boolean isConfigWBWB) {
        ai = new AI(this);
        players = new FlowPane();
        
        // override these values with PlayerStart
        human = new Player(player1name, player1Color);
        if (player1Color == Color.BLACK)
            skynet = new Player("Computer", Color.WHITE);
        else
            skynet = new Player("Computer", Color.BLACK);
        
        players.getChildren().addAll(human, skynet);
        players.setHgap(50);
        
        board = new Board(8, gameTimer, this);
        
        gameTimer = new GameTimer(10, board);
        
        undo = new Button("Undo");
        undo.setOnAction((ActionEvent e) -> {
            ai.rewind();
        });
        
        addComponents();
        
        board.setInitialConfig(isConfigWBWB);
        if (human.getColor() == Color.BLACK)
            playerTurn = true;
        else
            playerTurn = false;
        
        currentTurn = new Label("");
        this.calcScore();
        this.showCurrentTurn();
    }
    
    private void addComponents() {
        this.setMaxWidth(600);
        this.setHeight(800);
        FlowPane buttons = new FlowPane();
        buttons.getChildren().addAll(undo);
        this.getChildren().addAll(players, board, gameTimer, buttons);
    }
    
    public void pauseTime() {
        gameTimer.timePause();
    }
    
    public void nextTurn() {
        gameTimer.timeReset();
        this.calcScore();
        this.playerTurn = !this.playerTurn;
        this.showCurrentTurn();
    }
    
    public void resume() {
        gameTimer.timeResume();
    }
    
    public void initBoard() {
        // display pop-up window to determine which player will be white
        // and which will be black. Black moves first. Human will choose,
        // Black is the other color.
        
        System.out.println("Init board method incomplete");
    }
    
    public void showCurrentTurn() {
        if (playerTurn)
            currentTurn.setText("human's turn");
    }
    
    public void calcScore() {
        int whiteScore = board.calcWhiteScore();
        int blackScore = board.calcBlackScore();
        System.out.println("White score: " + whiteScore);
        if (human.getColor().equals(Color.BLACK)) {
            human.updateScore(blackScore);
            skynet.updateScore(whiteScore);
        } else {
            human.updateScore(whiteScore);
            skynet.updateScore(blackScore);
        }
    }
}
