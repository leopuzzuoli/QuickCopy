/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickcopy;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

/**
 *
 * @author Chipleo
 */
public class fileController implements Initializable {

    @FXML
    Label filename;
    @FXML
    Label filesize;
    Pane myself;
    @FXML
    Pane backgroundShape;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @FXML
    public void setAll(String _filename, String _filepath, String fileSize, openbarController contr, Pane me) {
        myself = me;
        ContextMenu montextmnu = new ContextMenu();
        MenuItem Delete_btn = new MenuItem("Delete");

        montextmnu.getItems().addAll(Delete_btn);

        Delete_btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                System.out.println("removing " + filename.getText());
                contr.removeFile(me, _filepath);
            }
        });
        
        backgroundShape.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                if (e.getButton() == MouseButton.SECONDARY) {
                    montextmnu.show(backgroundShape, e.getScreenX(), e.getScreenY());
                }
            }
        });

        filesize.setText(fileSize);
        filename.setText(_filename);

    }
}
