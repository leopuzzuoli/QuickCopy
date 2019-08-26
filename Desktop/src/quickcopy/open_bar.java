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
public class open_bar {
    Pane bar;
        
    public open_bar(Package pack, PackageManagerController contr){
        
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("bar_opened.fxml"));
        try{ 
        bar = (Pane) loader.load();
        openbarController b = loader.getController();
        b.send(contr,this);
        }catch(IOException e){
            System.out.println("Could not load open_bar out of FXML,: " + e.toString());
        }
    }
    
    public Pane getBar(){
        return bar;
    }
}
