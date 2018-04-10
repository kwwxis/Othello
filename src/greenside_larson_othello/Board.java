/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package greenside_larson_othello;

import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author Trevor Greenside
 */
public class Board extends GridPane {
    
    // private Space[][] spaces;
    private static int DIMENSION;
    private final Rectangle[][] spaces;
    
    public Board(int numSpaces) {
//        DIMENSION = numSpaces;
//        spaces = new Space[DIMENSION][DIMENSION];
//        this.setHeight(600);
//        this.setWidth(600);
//        this.setStyle("-fx-background-color: green;");
//        addSpaces();

        DIMENSION = numSpaces;
        spaces = new Rectangle[DIMENSION][DIMENSION];
        this.setHeight(800);
        this.setWidth(800);
        addSpaces();
    }
    
    @SuppressWarnings("static-access")
    private void addSpaces() {
        for (int i = 0; i < DIMENSION; i++) {
            for (int j = 0; j < DIMENSION; j++) {
                spaces[i][j] = new Space();
                this.setRowIndex(spaces[i][j], i);
                this.setColumnIndex(spaces[i][j], j);
                this.getChildren().add(spaces[i][j]);
            }
        }
    }
    
}
