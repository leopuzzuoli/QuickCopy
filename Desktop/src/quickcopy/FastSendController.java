/*
 * Copyright 2019 Chipleo.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package quickcopy;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

/**
 *
 * @author Chipleo
 */
public class FastSendController implements Initializable {

    //Connection to which the user is referring#
    Connection connection;
    @FXML
    TextField text;
    Stage stage;
    @FXML
    javafx.scene.shape.Rectangle rectangle;
    @FXML
    ImageView image;
    @FXML
    WebView wv;
    WebEngine we;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //get screenshow for blur
        try {
            Robot robot = new Robot();

            Toolkit myToolkit = Toolkit.getDefaultToolkit();
            Dimension screenSize = myToolkit.getScreenSize();

            Rectangle screen = new Rectangle(screenSize);

            BufferedImage screenBlurImage = robot.createScreenCapture(screen);
            //get relevant section
            screenBlurImage = screenBlurImage.getSubimage(457, 415, 1006, 107);
            //Apply blur
            BoxBlur bb = new BoxBlur();
            bb.setWidth(15);
            bb.setHeight(15);
            bb.setIterations(3);

            //ColorAdjust colorAdjust = new ColorAdjust();
            //colorAdjust.setBrightness(0.1);
            //set image
            image.setImage(SwingFXUtils.toFXImage(screenBlurImage, null));

            this.we = this.wv.getEngine();
            File f = new File("src/quickcopy/FastSend.html");
            if (f.exists()) {
                System.out.println("I Exist");
            } else {
                System.out.println("PATH: " + f.getAbsolutePath());
            }
            this.we.load(f.toURI().toString());
            final com.sun.webkit.WebPage webPage = com.sun.javafx.webkit.Accessor.getPageFor(we);
            webPage.setBackgroundColor(0);

            //image.setEffect(colorAdjust);
            //image.setEffect(bb);            //g.getChildren().add(rectangle);
            //g.setTranslateX(451);
            //g.setTranslateY(415);
            System.out.println(rectangle.getX());
            System.out.println(rectangle.getY());

            //rectangle.setX(457);
            //rectangle.setY(415);
            System.out.println(rectangle.getX());
            System.out.println(rectangle.getY());

            System.out.println(rectangle.getWidth());
            System.out.println(rectangle.getHeight());

            //rectangle.setFill(Color.RED);
        } catch (AWTException e) {
            System.out.println("Error capturing screen, blur unavailable");
        }
    }

    @FXML
    void onEnter(KeyEvent event) {
        //if enter is pressed
        if (event.getCode() == KeyCode.ENTER) {
            //get text
            String msg = text.getText();

            TClient client = new TClient();
            try {
                //connect and send message
                client.startConnection(connection.getAddr(), connection.getPort());
                client.sendMessage("msg " + msg);
                client.stopConnection();
                //exit window
                stage.close();
            } catch (SocketTimeoutException e) {

            }
        }
    }

    public void send(Connection conn, Stage sta) {
        connection = conn;
        stage = sta;
    }
}
