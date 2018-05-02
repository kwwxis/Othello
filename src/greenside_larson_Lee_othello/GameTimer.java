/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package greenside_larson_Lee_othello;

import javafx.scene.control.Label;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.util.Duration;

/**
 *
 * @author Trevor Greenside
 */
public class GameTimer extends Label {

    private final Game game;
    private final int START;
    private final Timeline decrementer;
    private int remaining;

    public GameTimer(Game game, int startTime) {
        this.game = game;
        this.START = startTime;
        this.remaining = START;

        this.setText("Time: " + Integer.toString(START));
        this.setPadding(new Insets(5, 0, 5, 15));

        decrementer = new Timeline(new KeyFrame(Duration.seconds(1), (ActionEvent event) -> {
            timeDecrement();
        }));
        decrementer.setCycleCount(Timeline.INDEFINITE);
    }

    public void timeReset() {
        remaining = START;
        this.setText("Time: " + Integer.toString(START));
        this.decrementer.play();
    }

    public void timeResume() {
        this.decrementer.play();
    }

    private void timeDecrement() {
        remaining--;
        this.setText("Time: " + Integer.toString(remaining));
        if (remaining == 0) {
            decrementer.stop();
            game.endGame(game.getCurrentPlayer().getName() + " ran out of time.");
        }
    }

    public void timePause() {
        this.decrementer.stop();
    }

}
