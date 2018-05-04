/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package greenside_larson_Lee_othello;

import greenside_larson_Lee_othello.Board.FutureBoard;

/**
 *
 * @author Trevor Greenside
 */
public class AI {

    private final Game game;

    public AI(Game game) {
        this.game = game;
    }

    // AI turn, make a move
    // Note: Timer is started in Game function nextTurn()
    public void moveAI() {
        final Board board = this.game.getBoard();
        final int max_depth = 3;
        final FutureBoard tree = board.buildFutureTree(max_depth);
        
        System.out.println("### Future Board Tree:");
        tree.print();
        
        Space maxSpace = null;
        int maxScore = 0;
        
        for (int i = 0; i < board.DIMENSION; i++) {
            for (int j = 0; j < board.DIMENSION; j++) {
            	Space theSpace = board.spaces[i][j];
            	
                if (theSpace.isClaimable() && theSpace.score > maxScore) {
                    maxSpace = theSpace;
                    maxScore = theSpace.score;
                }
            }
        }
        
        if (maxSpace != null) {
            new ConfirmMove(game.getBoard(), maxSpace);
        } else {
        	System.out.println("This should never happen.");
        }
        
        // get board state
        // see update state
        // build tree
    }

}
