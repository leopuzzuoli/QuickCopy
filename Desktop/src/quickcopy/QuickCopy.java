/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickcopy;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author Leonardo Puzzuoli
 */
public class QuickCopy extends Application {

    static String[] arg;
    
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"));
        AnchorPane root = (AnchorPane)loader.load();
        
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.sizeToScene();
        stage.setResizable(false);
        stage.show();
        
        MainController controller = (MainController)loader.getController();
        controller.sendScene(scene, stage, arg);
  
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        arg = args;
        launch(args);
    }
}
