/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package greenside_larson_othello;

import javafx.scene.control.Label;
import javafx.scene.paint.Color;

/**
 * @author Trevor Greenside
 */
public class Space extends Label {
    public Space() {
        this.setStyle("-fx-border-color: black; -fx-background-color: white;");
        this.setHeight(50);
        this.setWidth(50);
    }
}
