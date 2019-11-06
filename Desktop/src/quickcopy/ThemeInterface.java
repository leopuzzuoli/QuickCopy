/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickcopy;

import java.util.List;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

/**
 *
 * @author Leonardo Puzzuoli
 */
public interface ThemeInterface {
    public void draw(List<Connection> conns, AnchorPane Scanner, VBox _list);
    
}
