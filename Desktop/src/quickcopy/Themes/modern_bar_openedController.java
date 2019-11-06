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
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;

/**
 *
 * @author Leonardo Puzzuoli
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

    @FXML
    Rectangle top_main;
    @FXML
    Rectangle top_left;
    @FXML
    Rectangle top_right;
    @FXML
    Rectangle bottom;
    @FXML
    Rectangle bott_up_l;
    @FXML
    Rectangle bott_up_r;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public void send(modern_bar_opened m, String userjn, String color) {
        controller = m;
        username.setText(userjn);

        switch (color) {
            case "green":
                top_main.setFill(Color.web("#51ab75"));
                top_left.setFill(Color.web("#51ab75"));
                top_right.setFill(Color.web("#51ab75"));
                bottom.setFill(Color.web("#479566"));
                bott_up_l.setFill(Color.web("#479566"));
                bott_up_r.setFill(Color.web("#479566"));
                break;
            case "yellow":
                top_main.setFill(Color.web("#FFC42086"));
                top_left.setFill(Color.web("#FFC42086"));
                top_right.setFill(Color.web("#FFC42086"));
                bottom.setFill(Color.web("#FFC420"));
                bott_up_l.setFill(Color.web("#FFC420"));
                bott_up_r.setFill(Color.web("#FFC420"));
                break;
            case "aqua_blue":
                top_main.setFill(Color.web("#00C3C9"));
                top_left.setFill(Color.web("#00C3C9"));
                top_right.setFill(Color.web("#00C3C9"));
                bottom.setFill(Color.web("#00B5BB"));
                bott_up_l.setFill(Color.web("#00B5BB"));
                bott_up_r.setFill(Color.web("#00B5BB"));
                break;
            default:
                top_main.setFill(Color.web("#51ab75"));
                top_left.setFill(Color.web("#51ab75"));
                top_right.setFill(Color.web("#51ab75"));
                bottom.setFill(Color.web("#479566"));
                bott_up_l.setFill(Color.web("#479566"));
                bott_up_r.setFill(Color.web("#479566"));
        }
    }

    @FXML
    private void close() {
        controller.close();
    }

    @FXML
    private void sendQuickMessage() {
        //send order to send message with attachments to modern_bar_opened
        controller.sendQuickMessage(files, filenames, message.getText());
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
