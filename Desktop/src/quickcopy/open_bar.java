/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickcopy;

import java.io.File;
import java.io.IOException;
import java.util.List;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

/**
 *
 * @author Leonardo Puzzuoli
 */
public class open_bar {
    Pane bar;
    Package storedPackage;
    PackageManagerController pmc;
    openbarController b;
    
    public open_bar(Package pack, PackageManagerController contr){
        //store pack and contr for use in update
        storedPackage = pack;
        pmc = contr;
        //load open_bar FXML design
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("bar_opened.fxml"));
        try{ 
        bar = (Pane) loader.load();
        b = loader.getController();
        b.send(contr,this);
        b.setAll(pack.getDate(), pack.getName(), pack.getFiles());
        }catch(IOException e){
            System.out.println("Could not load open_bar out of FXML,: " + e.toString());
        }
    }
    
    public Pane getBar(){
        return bar;
    }
    
    public void updatePackageFiles(List<String> files){
        //get new Files in Package and update it to the PackageManagerController
        Package p = new Package(storedPackage.getName(), files, storedPackage.getDate(), storedPackage.getId());
        pmc.update(storedPackage, p);
        storedPackage = p;
    }
    public void updatePackageName(String name){
        //get new name of Package
        Package p = new Package(name, storedPackage.getFiles(), storedPackage.getDate(), storedPackage.getId());
        pmc.update(storedPackage, p);
        storedPackage = p;
    }
    public void updateName(String title){
        //update name in open_bar
        b.setTitle(title);
    }
    
    public void updateConnections(){
        b.updateConnections();
    }
}
