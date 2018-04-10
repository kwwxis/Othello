/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package greenside_larson_othello;

import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author Trevor Greenside
 */
public class Board extends GridPane {
    
    private static int DIMENSION;
    private final Rectangle[][] spaces;
    
    public Board(int numSpaces) {
        DIMENSION = numSpaces;
        spaces = new Rectangle[DIMENSION][DIMENSION];
        this.setHeight(800);
        this.setWidth(800);
        addSpaces();
    }
    
    @SuppressWarnings("static-access")
    private void addSpaces() {
        
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
    
}
