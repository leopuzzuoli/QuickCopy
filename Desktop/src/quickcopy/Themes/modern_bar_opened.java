/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickcopy.Themes;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Base64;
import java.util.List;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import quickcopy.Connection;
import quickcopy.TClient;

/**
 *
 * @author Leonardo Puzzuoli
 */
public class modern_bar_opened {

    Pane bar;
    modern controller;
    //connection this bar represents
    Connection represents;

    public modern_bar_opened(Connection conn, modern c, String theme_color) {
        controller = c;
        represents = conn;
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("modern_scan_open.fxml"));
        try {
            bar = (Pane) loader.load();
            modern_bar_openedController bc = loader.getController();
            bc.send(this, represents.getName(), theme_color);
        } catch (IOException e) {
            System.out.println("Could not load bar out of FXML,: " + e.toString());
        }
    }

    public Pane getBar() {
        return bar;
    }

    public void close() {
        controller.close(this);
    }

    public void sendQuickMessage(List<String> files_to_send, List<String> file_names, String message) {
        if (files_to_send.isEmpty()) {
            TClient client = new TClient();
            try {
                client.startConnection(represents.getAddr(), represents.getPort());

            } catch (SocketTimeoutException e) {
                System.out.println(e.toString());
            }
            //encode message to Base64 to prevent escaping
            client.sendMessage("msg " + message);
            client.stopConnection();
        } else {
            //send file acceptance request
            new Thread() {
                @Override
                public void run() {
                    TClient client = new TClient();
                    try {
                        client.startConnection(represents.getAddr(), represents.getPort());

                    } catch (SocketTimeoutException e) {
                        System.out.println(e.toString());
                    }
                    client.sendAccept(file_names, files_to_send);
                    client.sendMessage("msg " + message);
                    client.stopConnection();
                }
            }.start();
        }
    }
}
