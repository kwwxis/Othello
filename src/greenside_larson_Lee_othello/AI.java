/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package greenside_larson_Lee_othello;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.util.Duration;

/**
 *
 * @author Trevor Greenside
 */
public class AI {
    
    private final Game game;
    
    public AI(Game game) {
        this.game = game;
        game.initBoard();
    }
    
    // AI turn, make a move
    public void moveAI() {
        // create timer in a new thread
        AITimer time = new AITimer(this);
        
        // get board state // see update state
        
        
        // build tree
    }
    
    // stop AI if timer reaches end
    public void endGame() {
        game.endGame(game.getCurrentPlayer().getName() + " ran out of time.");
    }
    
    private class AITimer {
        
        private final Timeline decrementer;
        private int remaining;
        private final AI ai;
        
        public AITimer(AI ai) {
            this.ai = ai;
            this.remaining = 10;
            decrementer = new Timeline(new KeyFrame(Duration.seconds(1), (ActionEvent event) -> {
                timeDecrement();
            }));
            decrementer.setCycleCount(Timeline.INDEFINITE);
            decrementer.play();
        }
        
        public void timeDecrement() {
            // TODO
            remaining--;
            if (remaining == 0) {
                decrementer.stop();
                ai.endGame();
            }
        }
    }
}
