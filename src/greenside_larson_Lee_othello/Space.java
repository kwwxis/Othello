/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package greenside_larson_Lee_othello;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * @author Trevor Greenside
 */
public class Space extends Rectangle {

    public static final Color TRANSITION_COLOR = Color.YELLOW;
    public static final Color INITIAL_COLOR = Color.GREEN;
    public static final Color AVAILABLE_COLOR = Color.web("4DA84D");

    private Color color;
    protected int score;

    public final int column;
    public final String columnName; // A - H
    public final int row;
    public final String rowName; // 1 - 8

    private final Game game;
    protected final Board board;
    private final SortedMap<Integer, Color> history;

    public Space(Game game, Board board, int row, int column, String rowName, String columnName) {
        this.row = row;
        this.column = column;
        this.rowName = rowName;
        this.columnName = columnName;

        this.game = game;
        this.board = board;
        this.history = new TreeMap<Integer, Color>(java.util.Collections.reverseOrder());

        this.setHeight(75);
        this.setWidth(75);
        this.setStroke(Color.GRAY);
        this.unclaim();
    }

    protected Space() {
        this.row = -1;
        this.column = -1;
        this.rowName = null;
        this.columnName = null;
        this.game = null;
        this.board = null;
        this.history = null;
        this.score = 0;
    }

    protected boolean isDummySpace() {
        return this.game == null && this.board == null;
    }

    private Space(Space space, Board board) {
        this.row = space.row;
        this.column = space.column;
        this.rowName = space.rowName;
        this.columnName = space.columnName;

        this.game = space.game;
        this.board = board;
        this.history = null;

        this.score = space.score;
        this.color = space.color;
    }

    /**
     * Clone this Space but without history.
     */
    public Space cloneSpace(Board board) {
        return new Space(this, board);
    }

    protected void rewind(int turn) {
        Set<Integer> removals = new HashSet<Integer>();

        Color newColor = INITIAL_COLOR;

        for (Map.Entry<Integer, Color> entry : this.history.entrySet()) {
            int historyTurn = entry.getKey();
            newColor = entry.getValue();

            if (historyTurn == 0) {
                break;
            }

            if (turn < historyTurn) {
                // remove turns greater than the turn we want to go to
                removals.add(historyTurn);
            }

            if (historyTurn <= turn) {
                // we found the turn to rewind to
                break;
            }
        }

        if (!removals.isEmpty()) {
            this.history.keySet().removeAll(removals);
        }

        this.color = newColor;
        this.setFill(this.color);

        // don't use updateHistory() here because we're not adding to the history but
        // rather reverting the history
    }

    private void updateHistory() {
        if (this.history == null) {
            return;
        }

        // update history to current color
        // usage of this method must come after setFill()
        this.history.put(game.getCurrentTurn(), color);
    }

    public void setClaimInProgress() {
        this.setFill(color = TRANSITION_COLOR);
    }

    public void setAvailable(boolean state) {
        if (state) {
            this.setFill(color = AVAILABLE_COLOR);
        } else {
            this.setFill(color = INITIAL_COLOR);
        }
    }

    public boolean isClicked() {
        return !this.color.equals(INITIAL_COLOR) && !this.color.equals(AVAILABLE_COLOR);
    }

    public boolean isClaimable() {
        return this.score > 0;
    }

    /**
     * Returns the amount that would be added to the player's score if this
     * space was chosen.
     */
    public int getClaimScore() {
        return this.score;
    }

    protected void claim(Player player) {
        claimBasic(false, player);
    }

    private void claimBasic(boolean single, Player player) {
        this.setFill(color = player.getColor());

        this.updateHistory();

        if (!single) {
            this.board.claimSurrounding(this, false, this.color);
        }
    }

    protected void claimSingle() {
        claimBasic(true, this.game.getCurrentPlayer());
    }

    public void claim() {
        claimBasic(false, this.game.getCurrentPlayer());
    }

    /**
     * Set the initial state of this space to BLACK
     */
    public void setBlackInitial() {
        this.setFill(color = Color.BLACK);
        if (this.history != null) {
            this.history.put(0, color);
        }
    }

    /**
     * Set the initial state of this space to BLACK
     */
    public void setWhiteInitial() {
        this.setFill(color = Color.WHITE);
        if (this.history != null) {
            this.history.put(0, color);
        }
    }

    public void unclaim() {
        this.setFill(color = AVAILABLE_COLOR);
        this.updateHistory();
    }

    public Color getColor() {
        return this.color;
    }

    public String toSimpleString() {
        return this.columnName + "-" + this.rowName;
    }

    @Override
    public String toString() {
        String s = "<" + this.columnName + "-" + this.rowName + ", Score:" + this.score;
        if (this.board != null && this.board instanceof Board.FutureBoard) {
            s += ", Depth:" + ((Board.FutureBoard) this.board).depth;
        }
        if (this.isDummySpace()) {
            s += ", (dummy space)";
        }
        return s + ">";
    }
}
