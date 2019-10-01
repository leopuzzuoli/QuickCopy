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
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author Chipleo
 */
public class barController implements Initializable {

    @FXML
    Label bar_date;
    @FXML
    TextField bar_title;
    @FXML
    Rectangle backgroundShape;

    PackageManagerController contr;
    bar na;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @FXML
    private void open(MouseEvent event) {
        contr.open(na);
    }

    public void send(PackageManagerController _contr, bar our) {
        contr = _contr;
        na = our;
    }

    public void setAll(String date, String title) {
        //Add rightcllick
        ContextMenu contextmnu = new ContextMenu();

        //TODO: there should be a divider between send and Delete
        MenuItem send_btn = new MenuItem("Send");
        MenuItem Delete_btn = new MenuItem("Delete");

        contextmnu.getItems().addAll(send_btn, Delete_btn);

        send_btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                System.out.println("Cut...");
            }
        });
        Delete_btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                System.out.println("Delete");
                contr.delete(na);
            }
        });
        backgroundShape.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                if (e.getButton() == MouseButton.SECONDARY) {
                    contextmnu.show(backgroundShape, e.getScreenX(), e.getScreenY());
                }
            }
        });

        //add onTextChangedListener
        bar_title.textProperty().addListener((observable, oldValue, newValue) -> {
            na.updatePackageName(newValue);
        });

        bar_date.setText(date.split(" /")[0]);

        bar_title.setText(title);

    }
    
        public void setTitle(String title){
        //set name/title of bar
        bar_title.setText(title);
    }
}
