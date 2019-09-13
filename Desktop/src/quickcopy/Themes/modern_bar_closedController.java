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

/**
 *
 * @author Chipleo
 */
public class modern_bar_closedController implements Initializable{
    @FXML
    Label username;
    
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
    public void setAll(String name)
    {
        username.setText(name);
    }
}
