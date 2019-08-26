/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickcopy;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author Chipleo
 */
public class openbarController implements Initializable {
    
    PackageManagerController contr;
    open_bar na;
    
    @Override
    public void initialize(URL url, ResourceBundle rb){
        
    }
    
    @FXML
    private void open(MouseEvent event) {
        contr.close(na);
    }
    
    public void send(PackageManagerController _contr, open_bar close){
        contr = _contr;
        na = close;
    }
}
