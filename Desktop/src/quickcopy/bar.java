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
    PackageManagerController pmc;
    Package storedPackage;
    barController b;
    
    public bar(Package pack, PackageManagerController contr){
        //store pack and contr for unse in update
        storedPackage = pack;
        pmc = contr;
        
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("bar.fxml"));
        try{
            
        bar = (Pane) loader.load();
        b = loader.getController();
        b.send(contr, this);
        b.setAll(pack.getDate(), pack.getName());
        }catch(IOException e){
            System.out.println("Could not load bar out of FXML,: " + e.toString());
        }
    }
    
    public Pane getBar(){
        return bar;
    }
    public void updatePackageName(String name){
        //get new name of Package
        Package p = new Package(name, storedPackage.getFiles(), storedPackage.getDate(), storedPackage.getId());
        pmc.update(storedPackage, p);
        storedPackage = p;
    }
        public void updateName(String title){
        //update name in bar
        b.setTitle(title);
    }
}
