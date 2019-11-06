/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickcopy;

import javafx.fxml.FXMLLoader;
import java.io.IOException;
import javafx.fxml.FXML;

/**
 *
 * @author Leonardo Puzzuoli
 */

public class ScanControl {

    public ScanControl() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
                "scancontrol.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
}
