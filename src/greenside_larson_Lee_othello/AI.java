/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package greenside_larson_Lee_othello;


/**
 *
 * @author Trevor Greenside
 */
public class AI {

    private final Game game;

    public AI(Game game) {
        this.game = game;
        game.initBoard();
    }

    // AI turn, make a move
    public void moveAI() {
        // Note: Timer started in Game function nextTurn()
        
        System.out.println("Timer should have been created.");
        int highScore = 0;
        Board board = this.game.getBoard();
        Space maxSpace = null;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board.spaces[i][j].score > highScore) {
                    maxSpace = board.spaces[i][j];
                    highScore = maxSpace.score;
                }
            }
        }
        if (maxSpace != null) {
            maxSpace.claim();
        }
        // get board state // see update state
        // build tree
        game.stopAITimer();
    }

    // stop AI if timer reaches end
    public void endGame() {
        game.endGame(game.getCurrentPlayer().getName() + " ran out of time.");
    }

}
