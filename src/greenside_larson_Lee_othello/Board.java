/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package greenside_larson_Lee_othello;

import java.util.ArrayList;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

/**
 *
 * @author Trevor Greenside
 */
public class Board extends GridPane {

    private final int DIMENSION;
    protected final Space[][] spaces;
    private final Game game;

    public Board(Game game, int numSpaces) {
        this.game = game;
        this.DIMENSION = numSpaces;
        this.spaces = new Space[DIMENSION][DIMENSION];

        addSpaces();

        for (int i = 0; i < DIMENSION; i++) {
            for (int j = 0; j < DIMENSION; j++) {
                this.spaces[i][j].setOnMouseClicked(new BoardEventHandler(spaces[i][j], this));
            }
        }
    }

    public boolean isInBounds(int row, int column) {
        return row >= 0 && column >= 0 && row < DIMENSION && column < DIMENSION;
    }

    protected void rewindSpacesToTurn(int turn) {
        for (int i = 0; i < DIMENSION; i++) {
            for (int j = 0; j < DIMENSION; j++) {
                this.spaces[i][j].rewind(turn);
            }
        }
    }

    protected void updateState() {
        int total_possible_score = 0;

        for (int i = 0; i < DIMENSION; i++) {
            for (int j = 0; j < DIMENSION; j++) {
                Space space = this.spaces[i][j];

                if (space.isClicked()) {
                    space.score = 0;
                    continue;
                }

                if (!this.checkIfValidMove(space)) {
                    space.score = 0;
                    space.setAvailable(false);
                    continue;
                }

                // add 1 to account for the space itself
                space.score = this.claimSurrounding(space, true, game.getCurrentPlayer().getColor()) + 1;

                if (space.isClaimable()) {
                    space.setAvailable(true);
                    total_possible_score += space.score;
                } else {
                    space.setAvailable(false);
                }
            }
        }

        if (total_possible_score == 0) {
            game.endGame();
        }
    }

    /**
     * This method populates the board with GUI components.
     */
    private void addSpaces() {
        // set board dimensions
        this.setHeight(600);
        this.setWidth(600);

        // create squares
        for (int row = 0; row < DIMENSION; row++) {
            String rowName = Character.toString((char) (row + 49));

            Label rowLabel = new Label(rowName);
            rowLabel.setPadding(new Insets(0, 5, 0, 5));
            GridPane.setRowIndex(rowLabel, row + 1);
            GridPane.setColumnIndex(rowLabel, 0);
            this.getChildren().add(rowLabel);

            for (int col = 0; col < DIMENSION; col++) {
                String colName = Character.toString((char) (col + 65));

                if (row == 0) {
                    Label colLabel = new Label(colName);
                    colLabel.setPadding(new Insets(5, 0, 5, 0));
                    colLabel.setMaxWidth(Double.MAX_VALUE);
                    colLabel.setAlignment(Pos.CENTER);
                    GridPane.setRowIndex(colLabel, 0);
                    GridPane.setColumnIndex(colLabel, (col + 1));
                    this.getChildren().add(colLabel);
                }

                spaces[row][col] = new Space(this.game, row, col, rowName, colName);
                GridPane.setRowIndex(spaces[row][col], row + 1);
                GridPane.setColumnIndex(spaces[row][col], col + 1);
                this.getChildren().add(spaces[row][col]);
            }

        }
    }

    /**
     * Check if choosing the given space is a valid move.
     *
     * @param space the space that the player wants to choose
     */
    protected boolean checkIfValidMove(Space space) {
        int row = space.row;
        int col = space.column;

        if (space.isClicked()) {
            return false;
        }

        Space s;
        Color c = game.getCurrentPlayer().getColor();

        // check up
        if (this.isInBounds(row - 1, col) && (s = this.spaces[row - 1][col]).isClicked() && s.getColor() != c) {
            return true;
        }
        // check down
        if (this.isInBounds(row + 1, col) && (s = this.spaces[row + 1][col]).isClicked() && s.getColor() != c) {
            return true;
        }

        // check left
        if (this.isInBounds(row, col - 1) && (s = this.spaces[row][col - 1]).isClicked() && s.getColor() != c) {
            return true;
        }
        // check top left
        if (this.isInBounds(row - 1, col - 1) && (s = this.spaces[row - 1][col - 1]).isClicked() && s.getColor() != c) {
            return true;
        }
        // check bottom -left
        if (this.isInBounds(row + 1, col - 1) && (s = this.spaces[row + 1][col - 1]).isClicked() && s.getColor() != c) {
            return true;
        }

        // check right
        if (this.isInBounds(row, col + 1) && (s = this.spaces[row][col + 1]).isClicked() && s.getColor() != c) {
            return true;
        }
        // check top right
        if (this.isInBounds(row - 1, col + 1) && (s = this.spaces[row - 1][col + 1]).isClicked() && s.getColor() != c) {
            return true;
        }
        // check bottom right
        if (this.isInBounds(row + 1, col + 1) && (s = this.spaces[row + 1][col + 1]).isClicked() && s.getColor() != c) {
            return true;
        }

        return false;
    }

    /**
     * Try to claim surrounding spaces according to othello rules
     *
     * @param space the space that the player chose
     */
    protected int claimSurrounding(Space space, boolean onlyCheckScore, Color claimColor) {
        int score = 0;

        {
            ArrayList<Space> upList = new ArrayList<Space>();
            ArrayList<Space> downList = new ArrayList<Space>();

            for (int row = space.row - 1; row > 0; row--) {
                Space down = spaces[row][space.column];
                downList.add(down);

                if (!down.isClicked()) {
                    break;
                }

                if (down.getColor().equals(claimColor)) {
                    score += downList.size() - 1;
                    if (!onlyCheckScore) {
                        downList.forEach((s) -> s.claimSingle());
                    }
                    break;
                }
            }

            for (int row = space.row + 1; row < DIMENSION; row++) {
                Space up = spaces[row][space.column];
                upList.add(up);

                if (!up.isClicked()) {
                    break;
                }

                if (up.getColor().equals(claimColor)) {
                    score += upList.size() - 1;
                    if (!onlyCheckScore) {
                        upList.forEach((s) -> s.claimSingle());
                    }
                    break;
                }
            }
        }
        {

            ArrayList<Space> rightMidList = new ArrayList<Space>();
            ArrayList<Space> rightDiagUpList = new ArrayList<Space>();
            ArrayList<Space> rightDiagDownList = new ArrayList<Space>();

            boolean rightMidDone = false;
            boolean rightDiagUpDone = false;
            boolean rightDiagDownDone = false;

            for (int rightCol = space.column; rightCol < DIMENSION; rightCol++) {
                int diff = rightCol - space.column;

                if (diff == 0) {
                    continue;
                }

                if (!rightMidDone) {
                    Space rightMid = spaces[space.row][rightCol];
                    rightMidList.add(rightMid);

                    if (!rightMid.isClicked()) {
                        rightMidDone = true;
                    } else if (rightMid.getColor().equals(claimColor)) {
                        rightMidDone = true;
                        score += rightMidList.size() - 1;
                        if (!onlyCheckScore) {
                            rightMidList.forEach((s) -> s.claimSingle());
                        }
                    }
                }

                if (!rightDiagUpDone && isInBounds(space.row + diff, rightCol)) {
                    Space rightDiagUp = spaces[space.row + diff][rightCol];
                    rightDiagUpList.add(rightDiagUp);

                    if (!rightDiagUp.isClicked()) {
                        rightDiagUpDone = true;
                    } else if (rightDiagUp.getColor().equals(claimColor)) {
                        rightDiagUpDone = true;
                        score += rightDiagUpList.size() - 1;
                        if (!onlyCheckScore) {
                            rightDiagUpList.forEach((s) -> s.claimSingle());
                        }
                    }
                }

                if (!rightDiagDownDone && isInBounds(space.row - diff, rightCol)) {
                    Space rightDiagDown = spaces[space.row - diff][rightCol];
                    rightDiagDownList.add(rightDiagDown);

                    if (!rightDiagDown.isClicked()) {
                        rightDiagDownDone = true;
                    } else if (rightDiagDown.getColor().equals(claimColor)) {
                        rightDiagDownDone = true;
                        score += rightDiagDownList.size() - 1;
                        if (!onlyCheckScore) {
                            rightDiagDownList.forEach((s) -> s.claimSingle());
                        }
                    }
                }
            }
        }
        {
            ArrayList<Space> leftMidList = new ArrayList<Space>();
            ArrayList<Space> leftDiagUpList = new ArrayList<Space>();
            ArrayList<Space> leftDiagDownList = new ArrayList<Space>();

            boolean leftMidDone = false;
            boolean leftDiagUpDone = false;
            boolean leftDiagDownDone = false;

            for (int leftCol = space.column; leftCol > 0; leftCol--) {
                int diff = space.column - leftCol;

                if (diff == 0) {
                    continue;
                }

                if (!leftMidDone) {
                    Space leftMid = spaces[space.row][leftCol];
                    leftMidList.add(leftMid);

                    if (!leftMid.isClicked()) {
                        leftMidDone = true;
                    } else if (leftMid.getColor().equals(claimColor)) {
                        leftMidDone = true;
                        score += leftMidList.size() - 1;
                        if (!onlyCheckScore) {
                            leftMidList.forEach((s) -> s.claimSingle());
                        }
                    }
                }

                if (!leftDiagUpDone && isInBounds(space.row + diff, leftCol)) {
                    Space leftDiagUp = spaces[space.row + diff][leftCol];
                    leftDiagUpList.add(leftDiagUp);

                    if (!leftDiagUp.isClicked()) {
                        leftDiagUpDone = true;
                    } else if (leftDiagUp.getColor().equals(claimColor)) {
                        leftDiagUpDone = true;
                        score += leftDiagUpList.size() - 1;
                        if (!onlyCheckScore) {
                            leftDiagUpList.forEach((s) -> s.claimSingle());
                        }
                    }
                }

                if (!leftDiagDownDone && isInBounds(space.row - diff, leftCol)) {
                    Space leftDiagDown = spaces[space.row - diff][leftCol];
                    leftDiagDownList.add(leftDiagDown);

                    if (!leftDiagDown.isClicked()) {
                        leftDiagDownDone = true;
                    } else if (leftDiagDown.getColor().equals(claimColor)) {
                        leftDiagDownDone = true;
                        score += leftDiagDownList.size() - 1;
                        if (!onlyCheckScore) {
                            leftDiagDownList.forEach((s) -> s.claimSingle());
                        }
                    }
                }
            }
        }

        return score;
    }

    public void setInitialConfig(Boolean isConfigWBWB) {
        if (isConfigWBWB) {
            spaces[3][3].setWhiteInitial();
            spaces[4][4].setWhiteInitial();
            spaces[3][4].setBlackInitial();
            spaces[4][3].setBlackInitial();
        } else {
            spaces[3][3].setBlackInitial();
            spaces[4][4].setBlackInitial();
            spaces[3][4].setWhiteInitial();
            spaces[4][3].setWhiteInitial();
        }
    }

    public Game getGame() {
        return game;
    }

    public int calcScore(Color color) {
        int score = 0;
        for (int i = 0; i < DIMENSION; i++) {
            for (int j = 0; j < DIMENSION; j++) {
                if (spaces[i][j].getColor().equals(color)) {
                    score++;
                }
            }
        }
        return score;
    }

}
