/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickcopy;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Ellipse;

/**
 * FXML Controller class
 *
 * @author Chipleo
 */
public class MainController implements Initializable {

    @FXML
    private static DatagramSocket socket = null;
    List<InetAddress> channels;
    Scene scene;
    PServer server = new PServer();
    TServer TCPServer = new TServer(4446);
    private List<String> myIPs = new ArrayList<>();
    String myname = "QuickCopy";
    static int myport = 4446;
    Ellipse topselector, middleselector, bottomselector;
    AnchorPane settingspane, scannerpane, packpane, welcome_pane;
    ListView listview;
    long timesince = 0;
    static private List<Connection> connections = new ArrayList<>();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        //get all IP addresses
        try {
            //list of addresses
            List<String> addresses = new ArrayList<>();
            InetAddress localhost = InetAddress.getLocalHost();
            System.out.println(" IP Addr: " + localhost.getHostAddress());
            addresses.add(localhost.getHostAddress());

            // Just in case this host has multiple IP addresses....
            InetAddress[] allMyIps = InetAddress.getAllByName(localhost.getCanonicalHostName());
            if (allMyIps != null && allMyIps.length > 1) {
                System.out.println(" Full list of IP addresses:");
                for (int i = 0; i < allMyIps.length; i++) {
                    System.out.println("    " + allMyIps[i]);
                    addresses.add(allMyIps[i].toString().split("/")[1]);
                    System.out.println(allMyIps[i].toString().split("/")[1]);

                }
            }

            //get IP and Hostname to use as name
            for (String temp : addresses) {
                //if address is correct one
                System.out.println("possible address: " + temp);
                if (temp.startsWith("192.168")) {
                    //set it as my ip
                    if(!myIPs.contains(temp)){
                    myIPs.add(temp);
                    }
                }
            }
        } catch (UnknownHostException e) {
            System.out.println(" (error retrieving server host name)");
        }
        System.out.println(myIPs);
        server.setIP(myIPs);
        server.setHostname(myname);
        server.start();
        TCPServer.start();
        server.setPort(myport);
    }

    @FXML
    private void Scan(MouseEvent event) {
        //When Scan Icon is Clicked

        topselector.setVisible(true);
        middleselector.setVisible(false);
        bottomselector.setVisible(false);

        scannerpane.setVisible(true);
        packpane.setVisible(false);
        settingspane.setVisible(false);
        welcome_pane.setVisible(false);

        //if Scan has been a long time ago or never happened
        if (timesince < (10 * 60) && timesince != 0) {
            displayNodes();
        } else {
            //Find All Broadcast Addresses
            System.out.println("STARTING QUERY");
            timesince = System.nanoTime() / 1000000000 - timesince;
            try {
                channels = listAllBroadcastAddresses();
            } catch (SocketException e) {
                System.out.println("SocketException: " + e.toString());
            }
            System.out.println("QUERY OVER");
            //Broadcast "QC at IP:port" to all Addresses
            for (int i = 0; i < channels.size(); i++) {
                System.out.println("BroadCasting on " + channels.get(i).toString().substring(1));
                //do it for all addresses
                for (String addr : myIPs) {
                    try {
                        System.out.println("-> QC at " + addr + ":" + myport);
                        broadcast("QC at " + addr + ":" + myport, InetAddress.getByName(channels.get(i).toString().substring(1)));
                    } catch (Exception e) {
                        System.out.println("ERROR: " + e.toString());
                    }
                }

            }
        }

        //We should log Sys.out
    }

    public void broadcast(
            String broadcastMessage, InetAddress address) throws IOException {
        socket = new DatagramSocket();
        socket.setBroadcast(true);

        byte[] buffer = broadcastMessage.getBytes();

        DatagramPacket packet
                = new DatagramPacket(buffer, buffer.length, address, 4445);
        socket.send(packet);
        socket.close();
    }

    //Find All BroadcastAddresses
    static public List<InetAddress> listAllBroadcastAddresses() throws SocketException {
        List<InetAddress> broadcastList = new ArrayList<>();
        Enumeration<NetworkInterface> interfaces
                = NetworkInterface.getNetworkInterfaces();
        while (interfaces.hasMoreElements()) {
            NetworkInterface networkInterface = interfaces.nextElement();

            if (networkInterface.isLoopback() || !networkInterface.isUp()) {
                continue;
            }

            networkInterface.getInterfaceAddresses().stream()
                    .map(a -> a.getBroadcast())
                    .filter(Objects::nonNull)
                    .forEach(broadcastList::add);
        }
        return broadcastList;
    }

    @FXML
    private void PackMan(MouseEvent event) {
        topselector.setVisible(false);
        middleselector.setVisible(true);
        bottomselector.setVisible(false);

        scannerpane.setVisible(false);
        packpane.setVisible(true);
        settingspane.setVisible(false);
        welcome_pane.setVisible(false);
    }

    @FXML
    private void Settings(MouseEvent event) {
        topselector.setVisible(false);
        middleselector.setVisible(false);
        bottomselector.setVisible(true);

        scannerpane.setVisible(false);
        packpane.setVisible(false);
        settingspane.setVisible(true);
        welcome_pane.setVisible(false);
    }

    @FXML
    private void disc(MouseEvent event) {
        //check actual state
        boolean isToggled = false;

        //turn server on/off
        if (isToggled) {
            server.halt();
        } else {
            server.start();
        }
    }

    @FXML
    private void power(MouseEvent event) {
        //halt application
        server.halt();
        System.exit(0);
    }

    private void displayNodes() {

    }

    public void sendScene(Scene scene_l) {
        scene = scene_l;
        //get all interactables

        topselector = (Ellipse) scene.lookup("#selectorontop");
        middleselector = (Ellipse) scene.lookup("#selectoronmiddle");
        bottomselector = (Ellipse) scene.lookup("#selectoronbottom");

        settingspane = (AnchorPane) scene.lookup("#settings");
        scannerpane = (AnchorPane) scene.lookup("#scanner");
        packpane = (AnchorPane) scene.lookup("#packets");
        welcome_pane = (AnchorPane) scene.lookup("#Welcome");

        listview = (ListView) scene.lookup("#listview");

        drawConnections();
    }

    public static void addConnection(Connection conn) {
        connections.add(conn);
        System.out.println("connection added");
    }

    public static void setMyPort(int port) {
        myport = port;
    }

    @FXML
    private void drawConnections() {
        //draw connections book
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("scancontrol.fxml"));

        try {
            AnchorPane connectionpane = (AnchorPane) loader.load();

            listview.getItems().add(connectionpane);
            listview.getItems().add(connectionpane);
            listview.getItems().add(connectionpane);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}
