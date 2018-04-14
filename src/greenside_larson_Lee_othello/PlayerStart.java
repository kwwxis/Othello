/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package greenside_larson_Lee_othello;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 *
 * @author Trevor Greenside
 */
public class PlayerStart extends VBox {
	// name fields
    private final TextField getName;
    private final Label getNameLabel;
    
    // player color select fields
    private final Label playerSelectLabel;
    private final ToggleGroup playerSelect;
    private Color chosenColor;
    
    // board config fields
    private final Label configLabel;
    private final ToggleGroup configSelect;
    private boolean isConfigWBWB;
    
    // start button
    public Button startButton;
    
    public PlayerStart() {
        this.setPadding(new Insets(10, 10, 10, 10));
        this.setSpacing(10);
        
        getNameLabel = new Label("Enter Your Name: ");
        getName = new TextField();
        
        // initialize player color selector components
        // -------------------------------------------
        
        playerSelectLabel = new Label("Choose Your Color (black goes first):");
        playerSelect = new ToggleGroup();
        chosenColor = Color.BLACK;
        
        RadioButton playerSelectBlack = new RadioButton("Black");
        playerSelectBlack.setToggleGroup(playerSelect);
        playerSelectBlack.setSelected(true);
        playerSelectBlack.setOnAction((ActionEvent e) -> {
            chosenColor = Color.BLACK;
        });
        
        RadioButton playerSelectWhite = new RadioButton("White");
        playerSelectWhite.setToggleGroup(playerSelect);
        playerSelectWhite.setOnAction((ActionEvent e) -> {
            chosenColor = Color.WHITE;
        });
        
        // initialize board configuration components
        // -----------------------------------------
        
        configLabel = new Label("Select initial board configuration:");
        isConfigWBWB = true;
        configSelect = new ToggleGroup();
        
        RadioButton configWBWB = new RadioButton("[W B]\n[B W]");
        configWBWB.setToggleGroup(configSelect);
        configWBWB.setSelected(true);
        configWBWB.setOnAction((ActionEvent e) -> {
            isConfigWBWB = true;
        });
        
        RadioButton configBWBW = new RadioButton("[B W]\n[W B]");
        configBWBW.setToggleGroup(configSelect);
        configBWBW.setOnAction((ActionEvent e) -> {
            isConfigWBWB = false;
        });
        
        // initialize start button component
        // ---------------------------------
        startButton = new Button("Start");
        
        // add components
        // --------------
        this.getChildren().addAll(
        		// name field
        		getNameLabel, getName,
        		// player color fields
        		playerSelectLabel, playerSelectBlack, playerSelectWhite,
        		// config fields
        		configLabel, configWBWB, configBWBW,
        		// start button
        		startButton);
    }
    
    public void focusPlayerNameField() {
    	getName.requestFocus();
    }
    
    public String getPlayerName() {
        return getName.getText();
    }
    
    public Color getPlayerColor() {
        return chosenColor;
    }
    
    public Color getComputerColor() {
        if (this.getPlayerColor() == Color.BLACK)
            return Color.WHITE;
        else
            return Color.BLACK;
    }
    
    public boolean getIsConfigWBWB() {
        return isConfigWBWB;
    }
}
