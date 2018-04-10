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
    private final Space space;
    private final Board board;
    
    public BoardEventHandler(Space space, Board board) {
        this.space = space;
        this.board = board;
    }
    
    @Override
    public void handle(MouseEvent event) {
        if (!board.isStopped()) {
            System.out.println("This is doing shit");
            space.transition();
        } 
    }
    
}
