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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;

/**
 *
 * @author Chipleo
 */
public class trafficController implements Initializable {

    List<Interaction> interactions = new ArrayList<>();
    @FXML
    VBox sent;
    @FXML
    VBox received;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //read previous interactions
        File f = new File("./data/inter.ser");
        if (f.exists()) {
            try {

                FileInputStream fis = new FileInputStream("./data/inter.ser");

                ObjectInputStream ois = new ObjectInputStream(fis);

                interactions = (List) ois.readObject();

                System.out.println("Serialized data is restored from \"./data/inter.ser\" file");

                ois.close();

                fis.close();

            } catch (IOException | ClassNotFoundException e) {

                e.printStackTrace();

            }
        }

        //generate received screen
        
        //generate sent screen
    }

    void saveInteractions() {
        try {

            FileOutputStream fos = new FileOutputStream("./data/inter.ser");

            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(interactions);

            System.out.println("Serialized data is saved in inter.ser file");

            oos.close();

            fos.close();

        } catch (IOException e) {

            e.printStackTrace();

        }
    }
    
    public void addInteraction(Interaction i){
        interactions.add(i);
        
        //refresh VBox
    }

}
