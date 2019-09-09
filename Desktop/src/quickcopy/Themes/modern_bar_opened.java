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
public class modern_bar_opened {
    Pane bar;
    modern controller;
    
    public modern_bar_opened(Connection conn, modern c){
        controller = c;
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("modern_scan_open.fxml"));
        try{    
        bar = (Pane) loader.load();
        modern_bar_openedController bc = loader.getController();
        bc.send(this);
        }catch(IOException e){
            System.out.println("Could not load bar out of FXML,: " + e.toString());
        }
    }
    
    public Pane getBar(){
        return bar;
    }
    public void close(){
        controller.close(this);
    }
}
