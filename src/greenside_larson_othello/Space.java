/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package greenside_larson_othello;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * @author Trevor Greenside
 */
public class Space extends Rectangle {
    public Space() {
        this.setHeight(75);
        this.setWidth(75);
        this.setStroke(Color.BLACK);
        this.setFill(Color.WHITE);
        this.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                transition();
            }
        });
    }
    
    public void transition() {
        this.setFill(Color.BLACK);
    }
}
