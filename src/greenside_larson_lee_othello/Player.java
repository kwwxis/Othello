package greenside_larson_lee_othello;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

/**
 * Human player object, including the GUI.
 * 
 * @author Trevor Greenside
 * @author Kayla Larson
 * @author Matthew Lee
 */
public class Player extends BorderPane {

    private final String playerName;
    private final Label playerLabel;
    private int score;
    private final Label scoreLabel;
    private final Color color;

    public Player(String name, Color colorChoice) {
        this.playerName = name;
        this.score = 0;
        this.scoreLabel = new Label(Integer.toString(this.score));

        if (colorChoice == Color.BLACK) {
            this.color = Color.BLACK;
            this.playerLabel = new Label(this.playerName + " (black)\t score: ");
        } else {
            this.color = Color.WHITE;
            this.playerLabel = new Label(this.playerName + " (white)\t score: ");
        }

        addComponents();
    }

    public String getName() {
        return this.playerName;
    }

    private void addComponents() {
        this.setHeight(50);
        this.setWidth(150);

        this.setPadding(new Insets(5, 5, 0, 15));

        this.setLeft(playerLabel);
        this.setRight(scoreLabel);
    }

    public void updateScore(int newPts) {
        this.score = newPts;
        this.scoreLabel.setText(Integer.toString(this.score));
    }

    public Color getColor() {
        return color;
    }
}
