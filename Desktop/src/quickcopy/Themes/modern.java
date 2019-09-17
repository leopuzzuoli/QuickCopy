/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickcopy.Themes;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import quickcopy.Connection;
import quickcopy.ThemeInterface;

/**
 *
 * @author Chipleo
 */
public class modern implements ThemeInterface {

    String color = "a";
    
    public modern(String _color) {
        color = _color;
    }
    //List containing all connections
    List<Connection> connections = new ArrayList<>();
    //List containing all bars
    List<modern_bar_opened> open_bars = new ArrayList<>();
    List<modern_bar_closed> closed_bars = new ArrayList<>();
    //VBox
    VBox list;
    //variables

    @Override
    public void draw(List<Connection> conns, AnchorPane Scanner, VBox _list) {
        list = _list;
        //remove all
        open_bars.clear();
        closed_bars.clear();
        list.getChildren().clear();
        //draw all
        boolean darkmode = false;
        if (darkmode) {
            Scanner.setStyle("-fx-background-color: #000000;");

        }
        for (Connection c : conns) {
            //create closed bar
            closed_bars.add(new modern_bar_closed(c, this, color));

            //create open bar
            open_bars.add(new modern_bar_opened(c, this, color));

            //add closed bar to list
            list.getChildren().add(closed_bars.get(closed_bars.size() - 1).getBar());
            System.out.println(list.getChildren().toString());
        }
    }

    public void open(modern_bar_closed me) {
        //replace closed bar with opened bar

        //get bar to replace
        int index = closed_bars.indexOf(me);
        //set it
        list.getChildren().set(index, open_bars.get(index).getBar());
    }

    public void close(modern_bar_opened me) {
        //replace open bar with closed bar

        //get bar to replace
        int index = open_bars.indexOf(me);
        //set
        list.getChildren().set(index, closed_bars.get(index).getBar());
    }
}
