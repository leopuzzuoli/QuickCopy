/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickcopy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

//TODO: proper error handling, : not allowed in name
//whem server cannot exit, force close, when server cannot start, see what can be done or exit app, better etc.
/**
 *
 * @author Chipleo
 */
public class TServer extends Thread {
    
    private int port;
    private boolean running = true;
    
    public TServer(int l_port) {
        port = l_port;
    }
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    
    @Override
    public void run() {
        if (tryport(port)) {
            MainController.setMyPort(port);
            System.out.println("TCP Server Running on localhost: " + port);
            waitandcommunicate();
        } else if (port > 5000) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Critical Error Starting TCP Server");
            alert.setContentText("The TCP server could not be started, see the log for more informnation or contact the developer at chipleo.codes@gmail.com");
            
            alert.showAndWait();
            System.exit(1);
        } else {
            System.out.println("eRR");
            port += 1;
            run();
        }
    }
    
    private void waitandcommunicate() {
        while (running) {
            try {
                clientSocket = serverSocket.accept();
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String received = in.readLine();
                //if recieved is reply to scan
                System.out.println("rec: " +received);
                if (received.startsWith("QC responding from ")) {
                    //add connection
                    String[] i_i = received.split("QC responding from ");
                    String[] all = i_i[1].split(":");
                    
                    Connection newConn = new Connection(all[0], Integer.parseInt(all[1]));
                    //checks should be made for name
                    newConn.setName(all[2]);
                    
                    MainController.addConnection(newConn);
                } else {
                    //handle recieved message
                }
            } catch (Exception e) {
                System.out.println("communication error: " + e.toString());
            }
        }
    }
    
    private boolean tryport(int port) {
        try {
            serverSocket = new ServerSocket(port);
            return true;
        } catch (IOException e) {
            System.out.println("ERROR: port probably already in use: " + e.toString());
            return false;
        }
    }
    
    public void halt() {
        try {
            running = false;
            in.close();
            out.close();
            clientSocket.close();
            serverSocket.close();
        } catch (Exception e) {
            System.out.println("Could not stop server: " + e.toString());
        }
    }
}
