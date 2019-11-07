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

import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

/**
 *
 * @author Chipleo
 */
public class FastSendController implements Initializable{
    //Connection to which the user is referring#
    Connection connection;
    @FXML
    TextField text;
    Stage stage;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }
    
    @FXML
    void onEnter(KeyEvent event){
        //if enter is pressed
        if(event.getCode() == KeyCode.ENTER){
            //get text
            String msg = text.getText();
            
            TClient client = new TClient();
            try{
            //connect and send message
            client.startConnection(connection.getAddr(), connection.getPort());
            client.sendMessage("msg " + msg);
            client.stopConnection();
            //exit window
            stage.close();
            }
            catch(SocketTimeoutException e){
                
            }
        }
    }
    
    public void send(Connection conn, Stage sta){
        connection = conn;
        stage = sta;
    }
}
