/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package greenside_larson_othello;

import javafx.scene.control.Label;
import java.util.Timer;
import java.util.TimerTask;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;

/**
 *
 * @author Trevor Greenside
 */
public class GameTimer extends Label {
    
    private static int START;
    private int remaining;
    private Board board;
    private final Timeline decrementer;
    
    public GameTimer(int startTime, Board inBoard) {
        
        this.board = inBoard;
        GameTimer.START = startTime;
        this.setText("Time: " + Integer.toString(START));
        this.remaining = START;
        
        decrementer = new Timeline(new KeyFrame(Duration.seconds(1), (ActionEvent event) -> {
            timeDecrement();
        }));
        decrementer.setCycleCount(Timeline.INDEFINITE);
        decrementer.play();
        
    }
    
    private void timeReset() {
        remaining = START;
        this.setText("Time: " + Integer.toString(START));
    }
    
    private void timeDecrement() {
        remaining--;
        this.setText("Time: " + Integer.toString(remaining));
        if (remaining == 0) {
            decrementer.stop();
            board.outOfTime();
        }
    }
    
}
