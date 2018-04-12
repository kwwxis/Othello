/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package greenside_larson_Lee_othello;

import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author Trevor Greenside
 */
public class ConfirmMove extends Stage {
    private final VBox layout;
    private final Scene scene;
    private final Button yes;
    private final Button no;
    
    public ConfirmMove(Board board, Space space) {
        
        layout = new VBox();
        yes = new Button("Yes");
        yes.setOnAction((ActionEvent e) -> {
            space.setBlack();
            board.nextTurn();
            this.hide();
        });
        no = new Button("No");
        no.setOnAction((ActionEvent e) -> {
            space.reset();
            board.resume();
            this.hide();
        });
        Label prompt = new Label("Do you want to make this move?");
        layout.getChildren().addAll(prompt, yes, no);
        scene = new Scene(layout, 400, 200);
        this.setScene(scene);
        this.show();
    }
    
    public void showThis() {
        this.show();
    }
}
