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
    
    private final GameTimer aiTimer;

    public Game(Othello othelloMain, PlayerStartMenu startConfig) {
        this.othelloMain = othelloMain;
        this.gameScene = new Scene(this, 900, 770);

        ai = new AI(this);
        board = new Board(this, 8, startConfig.getIsConfigWBWB());
        aiTimer = new GameTimer(this, 10);

        human = new Player(startConfig.getPlayerName(), startConfig.getPlayerColor());
        skynet = new Player("Computer", startConfig.getComputerColor());

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
        	this.aiTimer.setPaused(true);
        	
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Restart game");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to restart the game?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                restart();
            } else {
            	this.aiTimer.setPaused(false);
            }
        });

        this.isHumanTurn = (human.getColor() == Color.BLACK); // black goes first
        this.aiTimer.setActive(!this.isHumanTurn);

        this.addComponents();
        this.calcScore();
        this.showCurrentTurn();

        this.board.updateState();
        
        Platform.runLater(() -> {
        	// make sure initial call to nextTurn() runs inside runLater()
        	// so that the GUI can update and the board will be visible when
        	// nextTurn() is called
        	this.nextTurn(true);
        });
    }

    private void addComponents() {
        this.setMaxWidth(600);
        this.setHeight(800);

        HBox buttons = new HBox(5);
        buttons.setPadding(new Insets(5, 0, 5, 15));
        buttons.getChildren().addAll(undo, restartButton, aiTimer);

        this.getChildren().addAll(playerStatusPane, board, buttons);
    }
    
    public GameTimer getTimer() {
    	return this.aiTimer;
    }

    public Scene getGameScene() {
        return this.gameScene;
    }

    public Player getCurrentPlayer() {
        return this.isHumanTurn ? human : skynet;
    }
    
    public Player getHumanPlayer() {
    	return human;
    }
    
    public Player getComputerPlayer() {
    	return skynet;
    }

    public int getCurrentTurn() {
        return this.currentTurn;
    }

    public Board getBoard() {
        return this.board;
    }

    /**
     * Restarts the game
     */
    public void restart() {
    	this.aiTimer.setPaused(true);
        othelloMain.restartGame();
    }
    
    /**
     * End the game with the default message.
     */
    public void endGame() {
        endGame(null);
    }
    
    /**
     * End the game with a custom message.
     * 
     * @param extraMessage the custom message
     */
    public void endGame(String extraMessage) {
    	this.aiTimer.setPaused(true);
    	
        int humanScore = board.calcScore(human.getColor());
        int skynetScore = board.calcScore(skynet.getColor());

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText("Winner: "
                + ((humanScore == skynetScore) ? "nobody (game tied)"
                        : (humanScore > skynetScore ? human.getName() : skynet.getName()))
        );
        alert.setContentText(
                (extraMessage != null ? extraMessage : "No moves left.") + "\n\n"
                + human.getName() + ": " + humanScore + " points\n"
                + skynet.getName() + ": " + skynetScore + " points");

        ButtonType restartButtonType = new ButtonType("Restart game");
        ButtonType quitButtonType = new ButtonType("Quit game");

        alert.getButtonTypes().setAll(restartButtonType, quitButtonType);

        alert.show();

        alert.setOnHidden(evt -> {
            ButtonType result = alert.getResult();
            if (result == restartButtonType) {
                restart();
            } else if (result == quitButtonType) {
                Platform.exit();
                System.exit(0);
            }
        });
    }

    public void nextTurn() {
    	nextTurn(false);
    }
    
    private void nextTurn(boolean afterRewind) {
        this.calcScore();
        
        if (!afterRewind) {
            this.isHumanTurn = !this.isHumanTurn;
            this.currentTurn++;
        }
        
        this.showCurrentTurn();

        this.board.updateState();

        if (!this.isHumanTurn) {
            aiTimer.setActive(true);
            this.ai.moveAI();
        } else {
        	aiTimer.setActive(false);
        }
    }

    /**
     * Revert turns.
     *
     * @param turns if positive integer, then revert to this specific turn if
     * negative integer, then reverts back that many turns
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
            
            this.nextTurn(true);
        });
    }

    public void showCurrentTurn() {
        String turnInfo = "Turn #" + Integer.toString(this.currentTurn);

        if (isHumanTurn) {
            currentTurnLabel.setText(turnInfo + ", human's turn");
        } else {
            currentTurnLabel.setText(turnInfo + ", computer's turn");
        }
    }

    public void calcScore() {
        int humanScore = board.calcScore(human.getColor());
        int skynetScore = board.calcScore(skynet.getColor());

        System.out.println("Human score: " + humanScore + ", Computer score: " + skynetScore);

        human.updateScore(humanScore);
        skynet.updateScore(skynetScore);
    }
   
}
