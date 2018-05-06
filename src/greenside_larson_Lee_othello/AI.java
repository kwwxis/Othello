/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package greenside_larson_Lee_othello;

import greenside_larson_Lee_othello.Board.FutureBoard;
import javafx.application.Platform;

import java.util.Map;

/**
 *
 * @author Trevor Greenside
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

        System.out.println("### Tree:");
        tree.print();
        
        System.out.println("### Possible Moves:");
        System.out.println(tree.possible_moves);
        
        System.out.println("### Future Board AB Prune:");
        Space s = getTopSpace(maxVal(tree, null, null));
        
        System.out.println("### Future Board Final:");
        System.out.println(s);
        
        if (s != null) {
        	Platform.runLater(() -> {
                new ConfirmMove(game.getBoard(), s);
        	});
        } else {
        	System.out.println("This should never happen.");
        }
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
    
/*
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
    }*/

}
