/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickcopy.Themes;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author Chipleo
 */
public class modern_bar_closedController implements Initializable{
    @FXML
    Label username;
    @FXML
    Rectangle bar_background;
    
    modern_bar_closed controller;
    @Override
    public void initialize(URL url, ResourceBundle rb){
        
    }
    @FXML
    private void open(){
        controller.open();
    }
    public void send(modern_bar_closed m){
        controller = m;
    }
    public void setAll(String name, String color)
    {
        username.setText(name);
        switch(color){
            case "green":
                bar_background.setFill(Color.web("#51ab75"));
                break;
            case "yellow":
                bar_background.setFill(Color.web("#FFC420"));
                break;
            case "aqua_blue":
                bar_background.setFill(Color.web("#00C3C9"));
                break;
            default:
                bar_background.setFill(Color.web("#FFC420"));
        }
        
    }
}
