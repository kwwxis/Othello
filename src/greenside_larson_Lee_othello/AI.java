/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package greenside_larson_Lee_othello;

import greenside_larson_Lee_othello.Board.FutureBoard;

import java.util.Map;

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
        maxVal(tree, null, null);
        
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

    Space maxVal(Space space, Map.Entry<Space, FutureBoard> alpha, Map.Entry<Space, FutureBoard> beta){
        Map.Entry<Space, FutureBoard> curSpace = null;
        Board board1 = space.board;
        Space key;
        FutureBoard val;
        int v = -1;
        for(Map.Entry<Space, FutureBoard> set : board1.children.entrySet()){
            key = set.getKey();
            val = set.getValue();
            //get minval
            Map.Entry<Space, FutureBoard> v1 = minVal(val, alpha, beta);
            if(v1.getKey().score > v) {
                curSpace = v1;
            }
            if(beta != null){
                if(key.score >= beta.getKey().score){
                    return curSpace;
                }
            }
            if(alpha.getKey().score < v1.getKey().score){
                alpha = v1;
            }


        }
        return curSpace;
    }

    Space minVal(FutureBoard board, Space space, Map.Entry<Space, FutureBoard> alpha, Map.Entry<Space,FutureBoard> beta){
        Map.Entry<Space, FutureBoard> curSpace = null;
        Space key;
        FutureBoard val;
        if(board.depth == 3){
            return space;
        }
        int v = -1;
        for(Map.Entry<Space, FutureBoard> set: board.children.entrySet()){
            key = set.getKey();
            val = set.getValue();
            Map.Entry<Space, FutureBoard> v1 = minVal(val, key, alpha, beta);
            if(v1.getKey().score < v){
                curSpace = v1;
            }
            if(alpha != null){
                if(key.score <= beta.getKey().score){
                    return curSpace;
                }
            }
            if(beta.getKey().score > v1.getKey().score){
                beta = v1;
            }
        }
        return curSpace;
    }

}
