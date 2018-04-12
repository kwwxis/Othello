/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package greenside_larson_Lee_othello;

import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

/**
 *
 * @author Trevor Greenside
 */
public class Player extends BorderPane {
    
    private final String playerName;
    private final Label playerLabel;
    private int score;
    private final Label scoreLabel;
    private final Color color;
    
    public Player(String name, Color colorChoice) {
        this.playerName = name;
        this.score = 0;
        this.scoreLabel = new Label(Integer.toString(this.score));
        
        if (colorChoice == Color.BLACK) {
            this.color = Color.BLACK;
            this.playerLabel = new Label(this.playerName + " (black)\t score: ");
        } else {
            this.color = Color.WHITE;
            this.playerLabel = new Label(this.playerName + " (white)\t score: ");
        }
        addComponents();
    }
    
    private void addComponents() {
        this.setHeight(50);
        this.setWidth(150);
        
        this.setLeft(playerLabel);
        this.setRight(scoreLabel);
    }
    
    public void updateScore(int newPts) {
        this.score = newPts;
        this.scoreLabel.setText(Integer.toString(this.score));
    }
    
    public Color getColor() {
        return color;
    }
}
