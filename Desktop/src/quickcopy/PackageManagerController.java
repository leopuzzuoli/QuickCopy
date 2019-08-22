/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickcopy;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 *
 * @author Chipleo
 */
public class PackageManagerController implements Initializable{
    @FXML
    private Button bar_add,bar_add_large;
    @FXML
    private VBox list;
    private List<Package> packages;
    private List<bar> bars;
    
    @Override
    public void initialize(URL url, ResourceBundle rb){
        //test for boxes, should get them from memory
        List<String> fl = new ArrayList<>();
        fl.add("file1");
        fl.add("file2");
        List<Connection> co = new ArrayList<>();
        co.add(new Connection("192.168.2.103", 22));
        Package i = new Package("TestBox",fl,co,"23.01.3006 / 12:38"); 
        Package ii = new Package("AnotherTestBox",fl,co,"23.01.3006 / 12:38"); 
        Package iii = new Package("SuperBox",fl,co,"23.01.3006 / 12:38"); 
        
      //  packages.add(i);
      //  packages.add(ii);
      //  packages.add(iii);
        
    }
    
    @FXML
    private void draw(){
        for(Package box : packages){
            bars.add(new bar(box));
            list.getChildren().add(bars.get(bars.size() - 1).getBar());
        }
    }
    
    public void sendScene(Scene scene){
        list = (VBox) scene.lookup("#package_list");
        draw();
    }
}
