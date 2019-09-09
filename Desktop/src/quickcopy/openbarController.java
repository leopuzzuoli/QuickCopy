/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickcopy;

import com.sun.javafx.tk.FontLoader;
import com.sun.javafx.tk.Toolkit;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

/**
 *
 * @author Chipleo
 */
public class openbarController implements Initializable {
    
    PackageManagerController contr;
    open_bar na;
    @FXML
    Label bar_to, bar_date;
    @FXML
    TextField bar_title;
    @FXML
    Pane add_button;
    
    @Override
    public void initialize(URL url, ResourceBundle rb){
        
    }
    
    public void setAll(String date, String title, String recipients){
        bar_to.setText(recipients);
        
        bar_title.setText(title);
        //calculate label width
        FontLoader fontLoader = Toolkit.getToolkit().getFontLoader();
        double length = fontLoader.computeStringWidth(bar_to.getText(), bar_to.getFont());
        //move add button respectively
        add_button.setLayoutX(add_button.getLayoutX() + length - 80);
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
