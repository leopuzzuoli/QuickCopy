/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickcopy;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;

/**
 *
 * @author Chipleo
 */
public class openbarController implements Initializable {

    PackageManagerController contr;
    open_bar na;
    @FXML
    Rectangle backgroundShape;
    @FXML
    Pane background;
    @FXML
    Label bar_date;
    @FXML
    TextField bar_title;
    @FXML
    Pane add_button;
    List<Pane> filedisplays = new ArrayList<>();
    @FXML
    VBox verticality;
    List<String> Files = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public void setAll(String date, String title, List<String> _Files) {
        Files = _Files;
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

        bar_title.setText(title);
        bar_date.setText(date);
        generateBar(Files);
    }

    private void generateBar(List<String> Files) {
        for (String file : Files) {
            backgroundShape.setHeight(backgroundShape.getHeight() + 31);
            background.setPrefHeight(background.getHeight() + 31);
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("bar_opened_filedisplay.fxml"));
            try {

                filedisplays.add((Pane) loader.load());
                fileController line = loader.getController();
                line.setAll(file, "50MB", this, filedisplays.get(filedisplays.size() - 1));
            } catch (IOException e) {
                System.out.println("Could not load bar out of FXML,: " + e.toString());
            }

            verticality.getChildren().add(0, filedisplays.get(filedisplays.size() - 1));
        }
    }

    @FXML
    private void open(MouseEvent event) {
        contr.close(na);
    }

    public void send(PackageManagerController _contr, open_bar close) {
        contr = _contr;
        na = close;
    }

    @FXML
    void addFiles() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Files");
        List<File> _files = fileChooser.showOpenMultipleDialog(add_button.getScene().getWindow());
        for (File f : _files) {
            Files.add(f.getAbsolutePath());

            //diplay new
            backgroundShape.setHeight(backgroundShape.getHeight() + 35);
            background.setPrefHeight(background.getHeight() + 35);
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("bar_opened_filedisplay.fxml"));
                filedisplays.add((Pane) loader.load());
                fileController line = loader.getController();
                line.setAll(f.getPath(), "50MB", this, filedisplays.get(filedisplays.size() - 1));
            } catch (IOException e) {
                System.out.println("Could not load bar out of FXML,: " + e.toString());
            }

            verticality.getChildren().add(0, filedisplays.get(filedisplays.size() - 1));

        }
        contr.refresh(na);
    }

    void removeFile(Pane me) {
        verticality.getChildren().remove(me);
        filedisplays.remove(me);
        backgroundShape.setHeight(backgroundShape.getHeight() - 31);
        background.setPrefHeight(background.getHeight() - 31);
        contr.refresh(na);
    }
    
}
