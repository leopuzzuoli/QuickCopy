/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickcopy;

import java.io.File;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
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
    List<String> Files, filenames = new ArrayList<>();
    ContextMenu contextmnu;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public void setAll(String date, String title, List<String> _Files) {
        Files = _Files;
        //Add ContextMenu
        addContext();
        //set text of title
        bar_title.setText(title);

        //add listener to change on title of bar 
        bar_title.textProperty().addListener((observable, oldValue, newValue) -> {
            na.updatePackageName(newValue);
        });

        bar_date.setText(date);
        generateBar(Files);
    }
    
    private void addContext(){
        contextmnu = new ContextMenu();

        MenuItem send_btn = new MenuItem("Send");
        MenuItem Delete_btn = new MenuItem("Delete");

        //Sub-Menu
        List<MenuItem> sublist = new ArrayList<>();
        // create a menu 
        Menu m = new Menu("Send to");
        // create menuitems
        List<Connection> conns = MainController.getConnections();
        for (Connection c : conns) {
            MenuItem item = new MenuItem(c.getName());
            item.setId(c.getAddr() + ":" + c.getPort());
            sublist.add(item);
            m.getItems().add(item);
            item.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent event) {
                    //send files to target
                    TClient tclient = new TClient();
                    try {
                        int porttosend = Integer.parseInt(item.getId().split(":")[1]);
                        tclient.startConnection(item.getId().split(":")[0], porttosend);
                        tclient.sendAccept(filenames, Files);
                        tclient.stopConnection();
                    } catch (NumberFormatException | SocketTimeoutException e) {
                        System.out.println(e.toString());
                    }
                }
            });
        }
        //create separator
        SeparatorMenuItem separator = new SeparatorMenuItem();
        contextmnu.getItems().addAll(send_btn, m, separator, Delete_btn);
        //on send is clicked
        send_btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                //send files to target

            }
        });
        //on delete clicked
        Delete_btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                System.out.println("Delete");
                contr.delete(na);
            }
        });
        //add ContextMenu to backgroundShape
        backgroundShape.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                if (e.getButton() == MouseButton.SECONDARY) {
                    contextmnu.show(backgroundShape, e.getScreenX(), e.getScreenY());
                }
            }
        });
    }

    private void generateBar(List<String> _filepahts) {
        for (String file : _filepahts) {
            backgroundShape.setHeight(backgroundShape.getHeight() + 31);
            background.setPrefHeight(background.getHeight() + 31);
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("bar_opened_filedisplay.fxml"));
            try {
                //TODO:check if file exists and get name
                filedisplays.add((Pane) loader.load());
                fileController line = loader.getController();
                line.setAll(file, file, "50MB", this, filedisplays.get(filedisplays.size() - 1));
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
            filenames.add(f.getName());
            //diplay new
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("bar_opened_filedisplay.fxml"));
                filedisplays.add((Pane) loader.load());
                fileController line = loader.getController();
                line.setAll(f.getName(), f.getAbsolutePath(), (f.length() / (1000f * 1000)) + " MB", this, filedisplays.get(filedisplays.size() - 1));
            } catch (IOException e) {
                System.out.println("Could not load bar out of FXML,: " + e.toString());
            }
            verticality.getChildren().add(0, filedisplays.get(filedisplays.size() - 1));

        }
        //resize backgroundShape of open_bar
        backgroundShape.setHeight(backgroundShape.getHeight() + (35 * _files.size() - 1));
        background.setPrefHeight(background.getHeight() + (35 * _files.size() - 1));
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {

                }
                contr.refresh(na);
            }
        });
        //update Files to PackManController
        na.updatePackageFiles(Files);
    }

    void removeFile(Pane me, String path) {
        //remove Pane and resize backgroundShape of open_bar
        verticality.getChildren().remove(me);
        filedisplays.remove(me);
        backgroundShape.setHeight(backgroundShape.getHeight() - 31);
        background.setPrefHeight(background.getHeight() - 31);
        contr.refresh(na);
        //remove file from Files and filenames
        int index = Files.indexOf(path);
        Files.remove(index);
        filenames.remove(index);
        //update Files to PackManController
        na.updatePackageFiles(Files);
    }

    public void setTitle(String title) {
        //set name/title of bar
        bar_title.setText(title);
    }

    public void updateConnections() {
        
        addContext();
    }
}
