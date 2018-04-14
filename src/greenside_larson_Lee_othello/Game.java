/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package greenside_larson_Lee_othello;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
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
    private final FlowPane playerStatusPane;
    
    private final Player human;
    private final Player skynet;
    
    private GameTimer gameTimer;
    private final Button undo;
    
    @SuppressWarnings("unused")
	private final AI ai;
    
    private boolean isHumanTurn;
    private Label currentTurnLabel;
    private int currentTurn;
    
    public Game(PlayerStart startConfig) {
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
        	if (!board.isStopped())
        		rewind(-1);
        });

        
        board.setInitialConfig(startConfig.getIsConfigWBWB());
        isHumanTurn = (human.getColor() == Color.BLACK); // black goes first

        this.addComponents();
        this.calcScore();
        this.showCurrentTurn();
    }
    
    private void addComponents() {
        this.setMaxWidth(600);
        this.setHeight(800);
        
        FlowPane buttons = new FlowPane();
        buttons.setPadding(new Insets(5, 0, 5, 15));
        buttons.getChildren().addAll(undo);
        
        this.getChildren().addAll(playerStatusPane, board, gameTimer, buttons);
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
    
    public void nextTurn() {
        gameTimer.timeReset();
        this.calcScore();
        
        this.isHumanTurn = !this.isHumanTurn;
        this.currentTurn++;
        this.showCurrentTurn();
    }
    
    /**
     * Revert turns.
     * 
     * @param turns if positive integer, then revert to this specific turn
     * 				if negative integer, then reverts back that many turns
     */
    public void rewind(int turns) {
    	int revertToTurn;
    	
    	if (turns < 0)
    		// turns is negative, so add
    		// subtract 1 to account for current (unplayed) turn
    		revertToTurn = (this.currentTurn + turns) - 1;
    	else
    		revertToTurn = turns;
    	
    	if (revertToTurn < 0) {
        	System.out.println("Can't revert to turn: #" + revertToTurn);
    		return;
    	}

    	this.pause();
    	System.out.println("Reverting to turn: #" + revertToTurn);
    	
    	// BLACK goes first, turn number starts at 1, so BLACK has all odd turns
    	// and WHITE has all even turns. So if human is BLACK it's their turn
    	// if the turn number is odd, otherwise even
    	boolean humanIsBLACK = (human.getColor() == Color.BLACK);
    	if (revertToTurn % 2 == 0) {
    		// if even
    		this.isHumanTurn = !humanIsBLACK;
    	} else {
    		// if odd
    		this.isHumanTurn = humanIsBLACK;
    	}
    	
    	Platform.runLater(() -> {
        	this.board.rewindSpacesToTurn(revertToTurn);
        	this.currentTurn = revertToTurn + 1; // add 1 to go to next turn after the turn we reverted to
        	
            gameTimer.timeReset();
            this.calcScore();
            this.showCurrentTurn();
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
