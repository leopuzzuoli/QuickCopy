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

/**
 *
 * @author Chipleo
 */
public class modern_bar_openedController implements Initializable{
    modern_bar_opened controller;
    @Override
    public void initialize(URL url, ResourceBundle rb){
        
    }
    public void send(modern_bar_opened m){
        controller = m;
    }
    @FXML
    private void close(){
        controller.close();
    }
}
