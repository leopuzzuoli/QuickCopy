/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickcopy.Themes;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;

/**
 *
 * @author Chipleo
 */
public class modern_bar_openedController implements Initializable {

    modern_bar_opened controller;
    boolean selected = false;
    List<String> files = new ArrayList<>();
    List<String> filenames = new ArrayList<>();
    @FXML
    TextField message;
    @FXML
    Circle attached_icon;
    @FXML
    Label username;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public void send(modern_bar_opened m, String userjn) {
        controller = m;
        username.setText(userjn);
    }

    @FXML
    private void close() {
        controller.close();
    }

    @FXML
    private void sendQuickMessage() {
        //send order to send message with attachments to modern_bar_opened
        controller.sendQuickMessage(files,filenames, message.getText());
    }

    @FXML
    private void fileSelector() {
        if (selected) {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText("You already attached Files, proceed?");
            alert.setContentText("If you choose to proceed all already attached files will be cleared");

            ButtonType buttonTypeOK = new ButtonType("Continue");
            ButtonType buttonTypeClear = new ButtonType("Clear Files");
            ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

            alert.getButtonTypes().setAll(buttonTypeOK, buttonTypeClear, buttonTypeCancel);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == buttonTypeOK) {
                // ... user chose "Continue"
                files.clear();
                filenames.clear();
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Files");
                List<File> _files = fileChooser.showOpenMultipleDialog(message.getScene().getWindow());
                for (File f : _files) {
                    files.add(f.getAbsolutePath());
                    filenames.add(f.getName());
                }
                //set selected icon and flag
                attached_icon.setVisible(true);
                selected = true;
            } else if (result.get() == buttonTypeClear) {
                // ... user chose "Clear"
                files.clear();
                filenames.clear();
                attached_icon.setVisible(false);
                selected = false;
            } else {
                // ... user chose CANCEL or closed the dialogs
            }
        } else {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Files");
            List<File> _files = fileChooser.showOpenMultipleDialog(message.getScene().getWindow());
            for (File f : _files) {
                files.add(f.getAbsolutePath());
                filenames.add(f.getName());
            }
            //set selected icon and flag
            attached_icon.setVisible(true);
            selected = true;
        }
    }
    
}
