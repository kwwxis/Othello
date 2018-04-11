/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package greenside_larson_Lee_othello;

import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

/**
 *
 * @author Trevor Greenside
 */
public class Board extends GridPane {
    
    private static int DIMENSION;
    private final Space[][] spaces;
    private final GameTimer gameTimer;
    private final Game game;
    
    private boolean stopped;
    
    public Board(int numSpaces, GameTimer timer, Game game) {
        this.game = game;
        DIMENSION = numSpaces;
        spaces = new Space[DIMENSION][DIMENSION];
        
        addSpaces();
        
        for (int i = 0; i < DIMENSION; i++) {
            for (int j = 0; j < DIMENSION; j++) {
                this.spaces[i][j].setOnMouseClicked(new BoardEventHandler(spaces[i][j], this));
            }
        }
        
        this.gameTimer = timer; // to reset
        this.stopped = false;
    }
    
    /**
     * This method populates the board with GUI components.
     */
    @SuppressWarnings("static-access")
    private void addSpaces() {
        // set board dimensions
        this.setHeight(600);
        this.setWidth(600);
        
        // create row and column labels
        // row labels:
        for (int i = 0; i < DIMENSION; i++) {
            String rowName = Character.toString((char) (i + 49));
            Label rowLabel = new Label(rowName);
            this.setRowIndex(rowLabel, (i+1));
            this.setColumnIndex(rowLabel, 0);
            this.getChildren().add(rowLabel);
        }
        
        // column labels:
        for (int i = 0; i < DIMENSION; i++) {
            String rowName = Character.toString((char) (i + 65));
            Label rowLabel = new Label(rowName);
            this.setRowIndex(rowLabel, 0);
            this.setColumnIndex(rowLabel, (i+1));
            this.getChildren().add(rowLabel);
        }
        
        // create squares
        for (int i = 0; i < DIMENSION; i++) {
            for (int j = 0; j < DIMENSION; j++) {
                spaces[i][j] = new Space();
                this.setRowIndex(spaces[i][j], i+1);
                this.setColumnIndex(spaces[i][j], j+1);
                this.getChildren().add(spaces[i][j]);
            }
        }
    }
    
    public void outOfTime() {
        this.stopped = true;
    }
    
    public boolean isStopped() {
        return this.stopped;
    }
    
    public void pauseTime() {
        // !!!!! Nullptr exception, does not reach gameTimer
        game.pauseTime();
    }
}
