/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package greenside_larson_othello;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author Trevor Greenside
 */
public class BoardEventHandler implements EventHandler<MouseEvent> {
    private Space space;
    
    public BoardEventHandler(Space space) {
        this.space = space;
    }
    
    @Override
    public void handle(MouseEvent event) {
        System.out.println("This is doing shit");
        space.transition();
    }
    
}
