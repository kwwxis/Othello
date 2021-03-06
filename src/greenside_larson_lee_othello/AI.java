package greenside_larson_lee_othello;

import javafx.application.Platform;

import java.util.Map;

import greenside_larson_lee_othello.Board.FutureBoard;

/**
 * The computer player.
 * 
 * @author Trevor Greenside
 * @author Kayla Larson
 * @author Matthew Lee
 */
public class AI {

    private final int MAX_DEPTH = 3;
    private final Game game;

    public AI(Game game) {
        this.game = game;
    }

    // AI turn, make a move
    // Note: Timer is started in Game function nextTurn()
    public void moveAI() {
        final Board board = this.game.getBoard();
        final FutureBoard tree = board.buildFutureTree(MAX_DEPTH);

        if (tree.possible_moves.isEmpty()) {
            return;
        }

        System.out.println("### Tree:");
        tree.print();

        System.out.println("### Possible Moves:");
        System.out.println(tree.possible_moves);

        System.out.println("### Future Board AB Prune:");
        Space s = getTopSpace(maxVal(tree, null, null));

        System.out.println("### Future Board Final:");
        System.out.println(s);

        if (s == null) {
            Space s0 = tree.possible_moves.get(0);

            s = board.spaces[s0.row][s0.column];
        }

        final Space chosen_move = s;

        Platform.runLater(() -> {
            new ConfirmMove(game.getBoard(), chosen_move);
        });
    }

    /**
     * Get the best space to score with in the given board with this heuristic.
     *
     * @param board
     * @return Space
     */
    Space heuristicDepthReached(FutureBoard board) {
        Space maxSpace = createDummySpace();
        int maxScore = 0;

        for (Space s : board.possible_moves) {
            if (s.score > maxScore) {
                maxScore = s.score;
                maxSpace = s;
            }
        }

        return maxSpace;
    }

    int heuristicScore(Space s) {
        if (s.board == null) {
            return s.score;
        }

        FutureBoard board = (FutureBoard) s.board;
        return s.score + board.computer_score;
    }

    Space getTopSpace(Space s) {
        if (s == null || s.isDummySpace() || s.board == null || !(s.board instanceof FutureBoard)) {
            return null;
        }

        System.out.println(s);

        FutureBoard board = (FutureBoard) s.board;

        if (board.depth == 0) {
            return this.game.getBoard().spaces[s.row][s.column];
        }

        return getTopSpace(board.parentSpace);
    }

    Space createDummySpace() {
        return new Space(); // creates a throwaway Space with a score of 0
    }

    Space maxVal(FutureBoard node, Integer alpha, Integer beta) {
        Space v = createDummySpace();

        if (node.children.isEmpty()) {
            // Reached bottom of branch
            return heuristicDepthReached(node);
        }

        for (Map.Entry<Space, FutureBoard> child : node.children.entrySet()) {
            Space v1 = minVal(child.getValue(), alpha, beta);

            if (v.isDummySpace() || heuristicScore(v1) > heuristicScore(v)) {
                v = v1;
            }

            if (beta != null) {
                if (heuristicScore(v1) >= beta) {
                    return v;
                }
            }

            if (alpha == null || heuristicScore(v1) > alpha) {
                alpha = heuristicScore(v1);
            }
        }

        return v;
    }

    Space minVal(FutureBoard node, Integer alpha, Integer beta) {
        Space v = createDummySpace();

        if (node.children.isEmpty()) {
            // Reached bottom of branch
            return heuristicDepthReached(node);
        }

        for (Map.Entry<Space, FutureBoard> child : node.children.entrySet()) {
            Space v1 = maxVal(child.getValue(), alpha, beta);

            if (v.isDummySpace() || heuristicScore(v1) < heuristicScore(v)) {
                v = v1;
            }

            if (alpha != null) {
                if (heuristicScore(v1) <= alpha) {
                    return v;
                }
            }

            if (beta == null || heuristicScore(v1) < beta) {
                beta = heuristicScore(v1);
            }
        }

        return v;
    }
    
}
