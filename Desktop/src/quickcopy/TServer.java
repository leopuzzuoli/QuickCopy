/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickcopy;

import com.sun.javafx.tk.Toolkit;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

//TODO: proper error handling, : not allowed in name
//whem server cannot exit, force close, when server cannot start, see what can be done or exit app, better etc.
/**
 *
 * @author Chipleo
 */
public class TServer extends Thread {

    //TODO: Path
    String PATH = "E:\\Documents\\Programming\\Java\\";
    private int port;
    private boolean running = true;
    private final List<String> acceptedFiles = new ArrayList<>();

    MainController mc;

    public TServer(int l_port, MainController _mc) {
        mc = _mc;
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

    //TODO: Put handling on different thread, the current way we can only accept 1 conn at once plus we need to reconenct for every file we send
    private void waitandcommunicate() {
        while (running) {
            try {
                clientSocket = serverSocket.accept();
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            out = new PrintWriter(clientSocket.getOutputStream(), true);
                            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                            String received = in.readLine();
                            //if recieved is reply to scan
                            System.out.println("<- " + received);
                            if (received.startsWith("QC responding from ")) {
                                //add connection
                                String[] i_i = received.split("QC responding from ");
                                String[] all = i_i[1].split(":");

                                Connection newConn = new Connection(all[0], Integer.parseInt(all[1]));
                                //checks should be made for name
                                newConn.setName(all[2]);

                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        // Update UI here.
                                        MainController.addConnection(newConn, mc);
                                    }
                                });
                            } else if (received.startsWith("msg")) {
                                //handle recieved message
                                System.out.println(received.substring(3));
                                //create notification
                                if (SystemTray.isSupported()) {
                                        //gets the sender of the message
                                        String sender = "A user ";
                                        List<Connection> conns = MainController.getConnections();
                                        for (Connection c : conns) {
                                            if (clientSocket.getInetAddress().toString().substring(1).equals(c.getAddr())) {
                                                sender = c.getName() + " ";
                                            }
                                        }
                                        //get tray from MainController
                                        TrayIcon trayIcon = MainController.trayIcon;
                                        //Display notification
                                        trayIcon.displayMessage(sender + "sent you a message", received.substring(3), MessageType.INFO);
                                        //Add message to received screen
                                        //check if link

                                } else {
                                    System.err.println("System tray not supported!");
                                }

                            } else if (received.startsWith("Accept")) {
                                System.out.println(received.substring(6));

                                //see who is sending the message
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        String sender = "A user ";
                                        List<Connection> conns = MainController.getConnections();
                                        for (Connection c : conns) {
                                            if (clientSocket.getInetAddress().toString().substring(1).equals(c.getAddr())) {
                                                sender = c.getName() + " ";
                                            }
                                        }
                                        //ask user if he wants to accept the files
                                        Alert alert = new Alert(AlertType.CONFIRMATION);
                                        alert.setTitle("Request received");
                                        alert.setHeaderText(sender + "wants to send you these files, Accept?");
                                        alert.setContentText(received.substring(6));
                                        ButtonType buttonTypeAccept = new ButtonType("Accept");
                                        ButtonType buttonTypeCancel = new ButtonType("Refuse", ButtonBar.ButtonData.CANCEL_CLOSE);

                                        alert.getButtonTypes().setAll(buttonTypeAccept, buttonTypeCancel);
                                        Optional<ButtonType> result = alert.showAndWait();
                                        //1 accept 0 refuse
                                        if (result.get() == buttonTypeAccept) {
                                            out.println("green");
                                            System.out.println("green");
                                            //add accepted files to list of accepted files
                                            acceptedFiles.addAll(Arrays.asList(received.substring(7, received.length() - 1).split(", ")));
                                        } else {
                                            out.println("red");
                                        }
                                    }

                                });

                            } else if (received.startsWith("file")) {
                                //get data
                                String filename = received.split(" ")[1];
                                String _filesize = received.split(" ")[2];
                                int filesize = Integer.parseInt(_filesize);

                                DataInputStream dis = new DataInputStream(clientSocket.getInputStream());
                                FileOutputStream fos = new FileOutputStream(PATH + filename);
                                byte[] buffer = new byte[4096];

                                int read = 0;
                                int totalRead = 0;
                                int remaining = filesize;
                                while ((read = dis.read(buffer, 0, Math.min(buffer.length, remaining))) > 0) {
                                    totalRead += read;
                                    remaining -= read;
                                    //TODO: make into loading bar
                                    System.out.println("read " + totalRead + " bytes.");
                                    fos.write(buffer, 0, read);
                                }
                                System.out.println("dojne: " + filename);
                                fos.close();
                                dis.close();
                            }
                        } catch (IOException | NumberFormatException e) {
                            System.out.println("communication error inside Thread: " + e.toString());
                        }
                    }
                }.start();
            } catch (IOException | NumberFormatException e) {
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
            //           in.close();
            //           out.close();
            //          clientSocket.close();
            //          serverSocket.close();
        } catch (Exception e) {
            System.out.println("Could not stop server: " + e.toString());
        }
    }
}
