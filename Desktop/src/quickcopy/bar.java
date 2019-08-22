/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickcopy;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

/**
 *
 * @author Chipleo
 */
public class bar {
    
    Pane bar;
        
    public bar(Package pack){
        
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("bar.fxml"));
        try{
        bar = (Pane) loader.load();
        }catch(IOException e){
            System.out.println("Could not load bar out of FXML,: " + e.toString());
        }
    }
    
    public Pane getBar(){
        return bar;
    }
}
