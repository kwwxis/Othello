/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package greenside_larson_Lee_othello;

import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
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
            board.getGame().pause();
            
            if (!space.isClaimable()) {
            	Alert alert = new Alert(AlertType.WARNING);
            	alert.setTitle("Can't choose this space");
            	alert.setHeaderText(null);
            	alert.setContentText(space.isClicked() ?
            			"This space has already been filled. Please select another space." :
            			"Please choose a space adjacent to the other player's space");

            	alert.showAndWait();
                board.getGame().resume();
            	return;
            }
            
            int possible_score = space.getClaimScore();
            
            if (possible_score <= 1) {
            	Alert alert = new Alert(AlertType.WARNING);
            	alert.setTitle("Can't choose this space");
            	alert.setHeaderText(null);
            	alert.setContentText("Unable to score with this space.");

            	alert.showAndWait();
                board.getGame().resume();
            	return;
            }
            
            space.setClaimInProgress();
            new ConfirmMove(board, space);
        } 
    }
    
}
