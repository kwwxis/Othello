package greenside_larson_lee_othello;

import javafx.scene.control.Label;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.util.Duration;

/**
 *
 * @author Trevor Greenside
 * @author Kayla Larson
 * @author Matthew Lee
 */
public class GameTimer extends Label {

    private final String LABEL_PREFIX;
    private final Game game;
    private final int START;
    private final Timeline decrementer;

    private int remaining;

    public GameTimer(Game game, int startTime) {
        this.game = game;
        this.START = startTime;
        this.remaining = START;
        this.LABEL_PREFIX = "AI Time: ";

        this.setText(LABEL_PREFIX + Integer.toString(START));
        this.setPadding(new Insets(5, 5, 5, 10));

        decrementer = new Timeline(new KeyFrame(Duration.seconds(1), (ActionEvent event) -> {
            timeDecrement();
        }));
        decrementer.setCycleCount(Timeline.INDEFINITE);
    }

    public void setActive(boolean newState) {
        if (newState) {
            this.timeReset();
        } else {
            this.timeReset();
            this.timePause();
            this.setText(LABEL_PREFIX + "n/a");
        }
    }

    public void setPaused(boolean newState) {
        if (newState) {
            this.timePause();
        } else {
            this.timeResume();
        }
    }

    private void timeReset() {
        remaining = START;
        this.setText(LABEL_PREFIX + Integer.toString(START));
        this.decrementer.play();
    }

    private void timeResume() {
        this.decrementer.play();
    }

    private void timeDecrement() {
        remaining--;
        Platform.runLater(() -> {
            GameTimer.this.setText(LABEL_PREFIX + Integer.toString(remaining));
        });
        if (remaining == 0) {
            decrementer.stop();
            game.endGame(game.getCurrentPlayer().getName() + " ran out of time.");
        }
    }

    private void timePause() {
        this.decrementer.stop();
    }

}
