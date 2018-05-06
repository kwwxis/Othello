/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package greenside_larson_Lee_othello;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

/**
 *
 * @author Trevor Greenside
 */
public class ConfirmMove {

    public ConfirmMove(Board board, Space space) {
    	board.game.getTimer().setPaused(true);
        space.setClaimInProgress();
        
        String playerName = board.game.getCurrentPlayer().getName();
        
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirm " + playerName + "'s move");
        alert.setHeaderText(playerName + "'s selected Move: " + space.toSimpleString());
        alert.setContentText("Do you want to play this move?");

        ButtonType buttonYes = new ButtonType("Yes");
        ButtonType buttonNo = new ButtonType("No");

        alert.getButtonTypes().setAll(buttonYes, buttonNo);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == buttonYes) {
            space.claim();
            board.game.nextTurn();
        } else if (result.get() == buttonNo) {
            space.unclaim();
        	board.game.getTimer().setPaused(false);
        }
    }

}
