package greenside_larson_lee_othello;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

/**
 * The board container and logic.
 * 
 * @author Trevor Greenside
 * @author Kayla Larson
 * @author Matthew Lee
 */
public class Board extends GridPane {

    public final int DIMENSION;

    protected final Space[][] spaces;
    protected final Game game;

    public Board(Game game, int numSpaces, boolean isConfigWBWB) {
        this.game = game;
        this.DIMENSION = numSpaces;
        this.spaces = new Space[DIMENSION][DIMENSION];

        addSpaces();

        for (int i = 0; i < DIMENSION; i++) {
            for (int j = 0; j < DIMENSION; j++) {
                this.spaces[i][j].setOnMouseClicked(new BoardEventHandler(spaces[i][j], this));
            }
        }

        if (isConfigWBWB) {
            this.spaces[3][3].setWhiteInitial();
            this.spaces[4][4].setWhiteInitial();
            this.spaces[3][4].setBlackInitial();
            this.spaces[4][3].setBlackInitial();
        } else {
            this.spaces[3][3].setBlackInitial();
            this.spaces[4][4].setBlackInitial();
            this.spaces[3][4].setWhiteInitial();
            this.spaces[4][3].setWhiteInitial();
        }
    }

    public Board(Board board) {
        this.game = board.game;
        this.DIMENSION = board.DIMENSION;
        this.spaces = new Space[this.DIMENSION][this.DIMENSION];

        for (int i = 0; i < this.DIMENSION; i++) {
            for (int j = 0; j < this.DIMENSION; j++) {
                this.spaces[i][j] = board.spaces[i][j].cloneSpace(this);
            }
        }
    }

    public class FutureBoard extends Board {

        private final Board source;

        /**
         * Total score of the computer AI.
         */
        protected int computer_score = 0;

        /**
         * The parent FutureBoard node, null if current FutureBoard is root
         * node.
         */
        protected final FutureBoard parent;

        /**
         * The move from the parent that led to this FutureBoard
         */
        protected final Space parentSpace;

        /**
         * Children FutureBoards. Dictionary from possible move Space to child
         * FutureBoard that would result from scoring with that Space.
         */
        protected final Map<Space, FutureBoard> children;

        /**
         * Possibles moves. Unless if the depth limit is reached, the "children"
         * field should have its keyset exactly the same as this list.
         */
        protected List<Space> possible_moves;

        /**
         * The incremental depth. The root node is always 0
         */
        protected final int depth;

        /**
         * The player who would have the current turn at the depth of this
         * FutureBoard.
         */
        protected final Player player;

        public FutureBoard(Board source, FutureBoard parent, Space parentSpace, int depth, Player player) {
            super(source);
            this.source = source;
            this.parent = parent;
            this.parentSpace = parentSpace;
            this.depth = depth;
            this.player = player;
            this.children = new HashMap<Space, FutureBoard>();
            this.possible_moves = new ArrayList<Space>();
        }

        public boolean hasParent() {
            return this.parent != null;
        }

        public boolean hasChildren() {
            return !this.children.isEmpty();
        }

        public FutureBoard cloneBoard(int new_depth, FutureBoard parentBoard, Space parentSpace, Player player) {
            return new FutureBoard(this.source, parentBoard, parentSpace, new_depth, player);
        }

        public void print() {
            System.out.println(
                    "Depth: " + depth
                    + ", Num Children: " + children.size()
                    + ", Computer Score: " + this.computer_score
            );

            for (FutureBoard child : children.values()) {
                child.print();
            }
        }

    }

    protected FutureBoard buildFutureTree(int maxDepth) {
        // root node depth -> 0
        // root node parent -> null
        FutureBoard rootNode = new FutureBoard(this, null, null, 0, this.game.getCurrentPlayer());

        rootNode.computer_score = rootNode.calcScore(this.game.getComputerPlayer().getColor());

        buildFutureTree(maxDepth, rootNode);

        return rootNode;
    }

    private void buildFutureTree(int maxDepth, FutureBoard parentBoard) {
        // figures out a list of all possible moves from the parentBoard
        UpdateStateResult res = parentBoard.updateState(true, parentBoard.player);

        parentBoard.possible_moves = res.possible_moves;

        if (parentBoard.depth >= maxDepth) {
            return;
        }

        // next depth for child boards
        int next_depth = parentBoard.depth + 1;

        for (Space move : res.possible_moves) {
            // ----- FIGURE OUT PLAYER FOR NEXT CHILD DEPTH

            Player nextDepthPlayer;
            // alternate between players for depths
            if (parentBoard.player == this.game.getHumanPlayer()) {
                nextDepthPlayer = this.game.getComputerPlayer();
            } else {
                nextDepthPlayer = this.game.getHumanPlayer();
            }

            // ----- CLONE THE PARENT BOARD AND CREATE/ADD THE CHILD
            FutureBoard childBoard = parentBoard.cloneBoard(next_depth, parentBoard, move, nextDepthPlayer);

            // claim the move
            childBoard.spaces[move.row][move.column].claim(parentBoard.player);

            childBoard.computer_score = childBoard.calcScore(this.game.getComputerPlayer().getColor());

            // add the child to the parent board's children
            parentBoard.children.put(move, childBoard);

            // recursive call
            buildFutureTree(maxDepth, childBoard);
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

    protected UpdateStateResult updateState() {
        return updateState(false, game.getCurrentPlayer());
    }

    /**
     * Update the state of the Board.
     *
     * @param soft if true, no changes to the Game state will be made based on
     * the result of this function
     * @return UpdateStateResult
     */
    protected UpdateStateResult updateState(boolean soft, Player currentPlayer) {
        List<Space> possible_moves = new ArrayList<Space>();
        int total_possible_score = 0;

        for (int i = 0; i < DIMENSION; i++) {
            for (int j = 0; j < DIMENSION; j++) {
                Space space = this.spaces[i][j];

                if (space.isClicked()) {
                    space.score = 0;
                    continue;
                }

                if (!this.checkLocation(space, currentPlayer)) {
                    space.score = 0;
                    space.setAvailable(false);
                    continue;
                }

                space.score = this.claimSurrounding(space, true, currentPlayer.getColor());

                if (space.isClaimable()) {
                    space.setAvailable(true);
                    total_possible_score += space.score;
                    possible_moves.add(space);
                } else {
                    space.setAvailable(false);
                }
            }
        }

        boolean game_ended = false;

        if (total_possible_score == 0) {
            if (!soft) {
                game.endGame();
            }
            game_ended = true;
        }

        return new UpdateStateResult(total_possible_score, possible_moves, game_ended);
    }

    class UpdateStateResult {

        public final int total_possible_score;
        public final List<Space> possible_moves;
        public final boolean game_ended;

        public UpdateStateResult(int total_possible_score, List<Space> possible_moves, boolean game_ended) {
            this.total_possible_score = total_possible_score;
            this.possible_moves = possible_moves;
            this.game_ended = game_ended;
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

                spaces[row][col] = new Space(this.game, this, row, col, rowName, colName);
                GridPane.setRowIndex(spaces[row][col], row + 1);
                GridPane.setColumnIndex(spaces[row][col], col + 1);
                this.getChildren().add(spaces[row][col]);
            }

        }
    }

    /**
     * Check if choosing the given space is a valid location.
     *
     * @param space the space that the player wants to choose
     */
    private boolean checkLocation(Space space, Player player) {
        int row = space.row;
        int col = space.column;

        if (space.isClicked()) {
            return false;
        }

        Space s;
        Color c = player.getColor();

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
                    score += downList.size() - 1; // subtract 1 to not include the stop Space
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
                    score += upList.size() - 1; // subtract 1 to not include the stop Space
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
                        score += rightMidList.size() - 1; // subtract 1 to not include the stop Space
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
                        score += rightDiagUpList.size() - 1; // subtract 1 to not include the stop Space
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
                        score += rightDiagDownList.size() - 1; // subtract 1 to not include the stop Space
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
                        score += leftMidList.size() - 1; // subtract 1 to not include the stop Space
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
                        score += leftDiagUpList.size() - 1; // subtract 1 to not include the stop Space
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
                        score += leftDiagDownList.size() - 1; // subtract 1 to not include the stop Space
                        if (!onlyCheckScore) {
                            leftDiagDownList.forEach((s) -> s.claimSingle());
                        }
                    }
                }
            }
        }

        if (score != 0) {
            // the score so far only accounts for the spaces surrounding the space that
            // was clicked, so add 1 to account for self (the space that was clicked)
            // if there is a score
            score += 1;
        }

        return score;
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
