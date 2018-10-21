package greenside_larson_lee_othello;

import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;

/**
 * Handles board click events.
 * 
 * @author Trevor Greenside
 * @author Kayla Larson
 * @author Matthew Lee
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
        if (!space.isClaimable()) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Can't choose this space");
            alert.setHeaderText(null);
            alert.setContentText(space.isClicked()
                    ? "This space has already been filled. Please select another space."
                    : "Unable to score with this space.");

            alert.showAndWait();
            return;
        }

        new ConfirmMove(board, space);
    }

}
