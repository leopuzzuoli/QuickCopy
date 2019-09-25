/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickcopy;

import java.io.IOException;
import java.util.List;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

/**
 *
 * @author Chipleo
 */
public class bar {
    
    Pane bar;
        
    public bar(Package pack, PackageManagerController contr){
        
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("bar.fxml"));
        try{
            
        bar = (Pane) loader.load();
        barController bar = loader.getController();
        bar.send(contr, this);
        bar.setAll(pack.getDate(), pack.getName());
        }catch(IOException e){
            System.out.println("Could not load bar out of FXML,: " + e.toString());
        }
    }
    
    public Pane getBar(){
        return bar;
    }
    
}
