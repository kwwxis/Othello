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
    
}
