/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package greenside_larson_Lee_othello;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * @author Trevor Greenside
 */
public class Space extends Rectangle {
    
    private boolean clicked;
    
    public Space() {
        this.clicked = false;
        
        this.setHeight(75);
        this.setWidth(75);
        this.setStroke(Color.GRAY);
        this.setFill(Color.GREEN);
        
        // not called with current setup, may relocate to Board class
        this.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                transition();
            }
        });
    }
    
    public void transition() {
        this.setFill(Color.BLACK);
        this.clicked = true;
    }
    
    public boolean isClicked() {
        return this.clicked;
    }
}
