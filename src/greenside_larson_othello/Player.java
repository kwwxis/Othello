/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package greenside_larson_othello;

import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

/**
 *
 * @author Trevor Greenside
 */
public class Player extends BorderPane {
    
    private final String playerName;
    private final Label playerLabel;
    private int score;
    private final Label scoreLabel;
    
    public Player(String name) {
        this.playerName = name;
        this.playerLabel = new Label(this.playerName + " \t score: ");
        this.score = 0;
        this.scoreLabel = new Label(Integer.toString(this.score));
        
        addComponents();
    }
    
    private void addComponents() {
        this.setHeight(50);
        this.setWidth(150);
        
        this.setLeft(playerLabel);
        this.setRight(scoreLabel);
    }
    
    public void updateScore(int newPts) {
        this.score += newPts;
        this.scoreLabel.setText(Integer.toString(this.score));
    }
    
}
