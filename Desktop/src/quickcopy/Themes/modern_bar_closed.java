/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickcopy.Themes;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import quickcopy.Connection;

/**
 *
 * @author Chipleo
 */
public class modern_bar_closed {
    Pane bar;
    modern control;
    public modern_bar_closed(Connection conn, modern c){
        control = c;
        //create FXML Loader
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("modern_scan_closed.fxml"));
        
        //try to load modern closed bar design
        try{    
        bar = (Pane) loader.load();
        modern_bar_closedController bc = loader.getController();
        bc.send(this);
        }catch(IOException e){
            System.out.println("Could not load bar out of FXML,: " + e.toString());
        }
    }
    public Pane getBar(){
        return bar;
    }
    
    public void open(){
        control.open(this);
    }
}
