/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package greenside_larson_lee_othello;

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

        if (board.game.getCurrentPlayer() == board.game.getHumanPlayer()) {
            // Create YES/NO dialog for human player's move

            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirm your move");
            alert.setHeaderText("Your Selected Move: " + space.toSimpleString());
            alert.setContentText("Do you want to play this move?");

            ButtonType buttonYes = new ButtonType("Yes");
            ButtonType buttonNo = new ButtonType("No");

            alert.getButtonTypes().setAll(buttonYes, buttonNo);

            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == buttonYes) {
                alert.hide();
                space.claim();
                board.game.nextTurn();
            } else if (result.get() == buttonNo) {
                alert.hide();
                space.unclaim();
                board.game.getTimer().setPaused(true);
            }
        } else {
            // Create OK dialog for computer's move

            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Computer's move");
            alert.setHeaderText(null);
            alert.setContentText("Computer's Selected Move: " + space.toSimpleString());
            alert.showAndWait();

            space.claim();
            board.game.nextTurn();
        }

    }

}
