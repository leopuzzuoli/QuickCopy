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
import static jdk.nashorn.internal.objects.NativeRegExpExecResult.length;


/**
 *
 * @author Chipleo
 */
public class barController implements Initializable {

    @FXML
    Label bar_date,bar_to;
    @FXML
    TextField bar_title;
    
    PackageManagerController contr;
    bar na;
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @FXML
    private void open(MouseEvent event) {
        contr.open(na);
    }
    
    public void send(PackageManagerController _contr, bar our){
        contr = _contr;
        na = our;
    }
    
    public void setAll(String date, String title, String recipient){
        bar_date.setText(date.split(" /")[0]);
        bar_to.setText(recipient);
        
        bar_title.setText(title);
        //calculate label width
        FontLoader fontLoader = Toolkit.getToolkit().getFontLoader();
        double length = fontLoader.computeStringWidth(bar_to.getText(), bar_to.getFont());
        //move label respectively
        bar_title.setLayoutX(length + 20);
        
    }
}
