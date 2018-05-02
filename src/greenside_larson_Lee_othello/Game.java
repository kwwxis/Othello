/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package greenside_larson_Lee_othello;

import java.util.Optional;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 *
 * @author Trevor Greenside
 */
public class Game extends VBox {
	// gui parent objects
	private final Othello othelloMain;
	private final Scene gameScene;

	// game objects
	private final AI ai;
    private final Board board;
    private final GameTimer gameTimer;
    
    // player objects
    private final Player human;
    private final Player skynet;
    
    // status
    private final FlowPane playerStatusPane;
    private Label currentTurnLabel;
    private int currentTurn;
    private boolean isHumanTurn;
    
    // buttons
    private final Button undo;
    private final Button restartButton;
    
    public Game(Othello othelloMain, PlayerStart startConfig) {
    	this.othelloMain 	= othelloMain;
        this.gameScene 		= new Scene(this, 900, 770);
        
        ai 			= new AI(this);
        board 		= new Board(this, 8);
        gameTimer 	= new GameTimer(this, 10);
        
        human 		= new Player(startConfig.getPlayerName(), startConfig.getPlayerColor());
        skynet 		= new Player("Computer", startConfig.getComputerColor());
        
        // initialize current turn label
        currentTurnLabel = new Label();
        currentTurn = 1;
        
        // initialize players pane
        playerStatusPane = new FlowPane();
        playerStatusPane.getChildren().addAll(human, skynet, currentTurnLabel);
        playerStatusPane.setHgap(50);
        
        // initialize undo button
        undo = new Button("Undo previous turn");
        undo.setOnAction((ActionEvent e) -> {
        	rewind(-1);
        });
        
        restartButton = new Button("Restart game");
        restartButton.setOnAction((ActionEvent e) -> {
        	pause();
        	
        	Alert alert = new Alert(AlertType.CONFIRMATION);
        	alert.setTitle("Restart game");
        	alert.setHeaderText(null);
        	alert.setContentText("Are you sure you want to restart the game?");

        	Optional<ButtonType> result = alert.showAndWait();
        	if (result.get() == ButtonType.OK){
        	    restart();
        	} else {
        		resume();
        	}
        });
        
        board.setInitialConfig(startConfig.getIsConfigWBWB());
        isHumanTurn = (human.getColor() == Color.BLACK); // black goes first

        this.addComponents();
        this.calcScore();
        this.showCurrentTurn();
        
        this.board.updateState();
    }
    
    private void addComponents() {
        this.setMaxWidth(600);
        this.setHeight(800);
        
        HBox buttons = new HBox(5);
        buttons.setPadding(new Insets(5, 0, 5, 15));
        buttons.getChildren().addAll(undo, restartButton);
        
        this.getChildren().addAll(playerStatusPane, board, gameTimer, buttons);
    }
    
    public Scene getGameScene() {
    	return this.gameScene;
    }
    
    public Player getCurrentPlayer() {
    	return this.isHumanTurn ? human : skynet;
    }
    
    public int getCurrentTurn() {
    	return this.currentTurn;
    }
    
    public boolean getIsHumanTurn() {
    	return this.isHumanTurn;
    }
    
    public Board getBoard() {
    	return this.board;
    }
    
    public GameTimer getGameTimer() {
    	return this.gameTimer;
    }
    
    public void pause() {
        gameTimer.timePause();
    }
    
    public void resume() {
        gameTimer.timeResume();
    }
    
    /**
     * Restarts the game
     */
    public void restart() {
    	this.pause();
    	othelloMain.restartGame();
    }
    
    public void endGame() {
    	endGame(null);
    }
    
    public void endGame(String extraMessage) {
    	this.pause();
    	
        int humanScore = board.calcScore(human.getColor());
        int skynetScore = board.calcScore(skynet.getColor());
        
    	Alert alert = new Alert(AlertType.CONFIRMATION);
    	alert.setTitle("Game Over");
    	alert.setHeaderText("Winner: " + 
    				((humanScore == skynetScore) ? "nobody (game tied)"
    						: (humanScore > skynetScore ? human.getName() : skynet.getName()))
    			);
    	alert.setContentText(
    			(extraMessage != null ? extraMessage : "No moves left.") + "\n\n" +
    			human.getName() + ": " + humanScore + " points\n" +
    			skynet.getName() + ": " + skynetScore + " points");

    	ButtonType restartButtonType = new ButtonType("Restart game");
    	ButtonType quitButtonType = new ButtonType("Quit game");

    	alert.getButtonTypes().setAll(restartButtonType, quitButtonType);

    	alert.show();
        
        alert.setOnHidden(evt -> {
            ButtonType result = alert.getResult();
            if (result == restartButtonType){
                restart();
            } else if (result == quitButtonType) {
                Platform.exit();
                System.exit(0);
            }
        });
    }
    
    public void nextTurn() {
        gameTimer.timeReset();
        this.calcScore();
        
        this.isHumanTurn = !this.isHumanTurn;
        this.currentTurn++;
        this.showCurrentTurn();
        
        this.board.updateState();
        
        if (!this.isHumanTurn) {
        	this.ai.moveAI();
        }
    }
    
    /**
     * Revert turns.
     * 
     * @param turns if positive integer, then revert to this specific turn
     * 				if negative integer, then reverts back that many turns
     */
    public void rewind(int turns) {
    	int revertToTurn;
    	
    	if (turns < 0) {
    		// turns is negative, so add
    		// subtract 1 to account for current (unplayed) turn
    		revertToTurn = (this.currentTurn + turns) - 1;
    	} else {
    		revertToTurn = turns;
    	}
    	
    	if (revertToTurn < 0) {
        	System.out.println("Can't revert to turn: #" + revertToTurn);
    		return;
    	}

    	this.pause();
    	System.out.println("Reverting to turn: #" + revertToTurn);
    	
    	// BLACK goes first, turn number starts at 1, so BLACK has all odd turns
    	// and WHITE has all even turns. So if human is BLACK it's their turn
    	// if the turn number is odd, otherwise even
    	boolean humanIsBLACK = human.getColor().equals(Color.BLACK);
    	
    	Platform.runLater(() -> {
        	this.board.rewindSpacesToTurn(revertToTurn);
        	this.currentTurn = revertToTurn + 1; // add 1 to go to next turn after the turn we reverted to
        	
        	if (this.currentTurn % 2 == 0) {
        		// if even
        		this.isHumanTurn = !humanIsBLACK;
        	} else {
        		// if odd
        		this.isHumanTurn = humanIsBLACK;
        	}
        	
            gameTimer.timeReset();
            this.calcScore();
            this.showCurrentTurn();

            this.board.updateState();

            if (!this.isHumanTurn) {
            	this.ai.moveAI();
            }
    	});
    }
    
    public void initBoard() {
        // display pop-up window to determine which player will be white
        // and which will be black. Black moves first. Human will choose,
        // Black is the other color.
        
        System.out.println("Init board method incomplete");
    }
    
    public void showCurrentTurn() {
    	String turnInfo = "Turn #" + Integer.toString(this.currentTurn);
    	
        if (isHumanTurn)
        	currentTurnLabel.setText(turnInfo + ", human's turn");
        else
        	currentTurnLabel.setText(turnInfo + ", computer's turn");
    }
    
    public void calcScore() {
        int humanScore = board.calcScore(human.getColor());
        int skynetScore = board.calcScore(skynet.getColor());
        
        System.out.println("Human score: " + humanScore + ", Computer score: " + skynetScore);
        
        human.updateScore(humanScore);
        skynet.updateScore(skynetScore);
    }
}
