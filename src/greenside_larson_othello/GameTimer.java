/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package greenside_larson_othello;

import javafx.scene.control.Label;

/**
 *
 * @author Trevor Greenside
 */
public class GameTimer extends Label {
    
    private static int START;
    private int remaining;
    private Board board;
    
    public GameTimer(int startTime, Board inBoard) {
        GameTimer.START = startTime;
        this.setText("Time: " + Integer.toString(START));
    }
    
    
}
