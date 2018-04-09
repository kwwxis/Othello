/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package greenside_larson_othello;

import javafx.scene.layout.GridPane;

/**
 *
 * @author Trevor Greenside
 */
public class Board extends GridPane {
    
    private Space[][] spaces;
    private static int DIMENSION;
    
    public Board(int numSpaces) {
        DIMENSION = numSpaces;
        spaces = new Space[DIMENSION][DIMENSION];
        
        addSpaces();
    }
    
    @SuppressWarnings("static-access")
    private void addSpaces() {
        for (int i = 0; i < DIMENSION; i++) {
            for (int j = 0; j < DIMENSION; j++) {
                spaces[i][j] = new Space();
                this.setConstraints(spaces[i][j], j, i);
                this.getChildren().addAll(spaces[i][j]);
            }
        }
    }
    
}
