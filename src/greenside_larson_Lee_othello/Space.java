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
    private final SortedMap<Integer, Color> history;
    
    public Space(Game game, int row, int column, String rowName, String columnName) {
    	this.row = row;
    	this.column = column;
    	this.rowName = rowName;
    	this.columnName = columnName;
    	
    	this.game = game;
    	this.history = new TreeMap<Integer, Color>(java.util.Collections.reverseOrder());
        
        this.setHeight(75);
        this.setWidth(75);
        this.setStroke(Color.GRAY);
        this.unclaim();
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
    	return this.score > 1;
    }
    
    /**
     * Returns the amount that would be added to the player's score if this space was chosen.
     */
    public int getClaimScore() {
    	return this.score;
    }
    
    private void claim(boolean single) {
	    this.setFill(color = game.getCurrentPlayer().getColor());
		
        this.updateHistory();
        
        if (!single) {
        	game.getBoard().claimSurrounding(this, false, this.color);
        }
    }
    
    protected void claimSingle() {
    	claim(true);
    }
    
    public void claim() {
    	claim(false);
    }
    
    /**
     * Set the initial state of this space to BLACK
     */
    public void setBlackInitial() {
        this.setFill(color = Color.BLACK);
        this.history.put(0, color);
    }
    
    /**
     * Set the initial state of this space to BLACK
     */
    public void setWhiteInitial() {
        this.setFill(color = Color.WHITE);
        this.history.put(0, color);
    }
    
    public void unclaim() {
        this.setFill(color = INITIAL_COLOR);
        this.updateHistory();
    }
    
    public Color getColor() {
        return this.color;
    }
    
    @Override
    public String toString() {
    	return this.columnName + "-" + this.rowName;
    }
}
