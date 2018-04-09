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
    
    public Player(String name) {
        this.playerName = name;
        this.playerLabel = new Label(this.playerName);
        addComponents();
    }
    
    private void addComponents() {
        this.setLeft(playerLabel);
    }
    
}
