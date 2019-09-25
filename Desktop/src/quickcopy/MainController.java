/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickcopy;

import java.io.File;
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
import java.util.prefs.Preferences;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Ellipse;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import quickcopy.Themes.Circles;
import quickcopy.Themes.modern;

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
    PServer server = new PServer(this);
    TServer TCPServer = new TServer(4446, this);
    private List<String> myIPs = new ArrayList<>();
    String myname = "QuickCopy";
    static int myport = 4446;
    Ellipse topselector, middleselector, bottomselector, trafficselector;
    AnchorPane settingspane, scannerpane, packpane, welcome_pane, trafficpane;
    long timesince = 0;
    public ThemeInterface theme;
    static private List<Connection> connections = new ArrayList<>();
    Preferences prefs = Preferences.userRoot().node(this.getClass().getName());
    Stage stage;
    public static String os = "Undetected/failure"; 
    @FXML
    VBox scanlist;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //settings
        
        //if first launch
        if(prefs.get("flaunch", "true").equals("true")){
            System.out.println("First Launch");
            System.out.println("Detecting OS");
            String _os_toDetect = System.getProperty("os.name").toLowerCase();
            if(_os_toDetect.contains("win")){
                os = "Windows";
            }
            if(_os_toDetect.contains("nux")){
                os = "Linux";
            }
            if(_os_toDetect.contains("mac")){
                os = "MacOs";
            }
            prefs.put("flaunch", "flase");
            
            //set shell context
            if("Windows".equals(os)){
                //TODO:here
            }
                
        }
        //get theme
        String theme_selector = prefs.get("theme", "modern_green");

        //choose correct one
        switch (theme_selector) {
            case "circles":
                theme = new Circles();
                break;
            case "modern_green":
                theme = new modern("green");
                break;
            case "modern_yellow":
                theme = new modern("yellow");
                break;
            case "modern_aqua":
                theme = new modern("aqua_blue");
                break;
            default:
                theme = new modern("green");
                break;

        }

        //get username
        myname = prefs.get("username", "QuickCopy");

        
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
                    if (!myIPs.contains(temp)) {
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

        //add SystemTray ico
        // instructs the javafx system not to exit implicitly when the last application window is shut.
        Platform.setImplicitExit(false);
        // sets up the tray icon (using awt code run on the swing thread).
        javax.swing.SwingUtilities.invokeLater(this::addAppToTray);

    }

    @FXML
    private void Scan() {
        //When Scan Icon is Clicked

        topselector.setVisible(true);
        middleselector.setVisible(false);
        bottomselector.setVisible(false);
        trafficselector.setVisible(false);

        scannerpane.setVisible(true);
        packpane.setVisible(false);
        settingspane.setVisible(false);
        welcome_pane.setVisible(false);
        trafficpane.setVisible(false);

        //if Scan has been a long time ago or never happened
        if (timesince < (10 * 60) && timesince != 0) {
            //drawConnections();
        } else {
            connections.clear();

            //do this on new Thread in order not to block UI
            new Thread() {
                @Override
                public void run() {

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
                            } catch (IOException e) {
                                System.out.println("ERROR: " + e.toString());
                            }
                        }

                    }
                }
            }.start();
        }

        //We should log Sys.out/
    }

    void saywhat() {
        System.out.println("what");
    }

    void show() {
        stage.sizeToScene();
        stage.show();
    }

    //a restart is required if theme color is changed
    private boolean restart_required = false;

    @FXML
    TextField nameField;

    @FXML
    void selectBlue() {
        restart_required = true;
        prefs.put("modern_aqua", "theme");
    }

    @FXML
    void selectYellow() {
        restart_required = true;
        prefs.put("modern_yellow", "theme");
    }

    @FXML
    void selectGreen() {
        restart_required = true;
        prefs.put("modern_green", "theme");
    }

    @FXML
    void saveSettings() {
        if (restart_required) {
            Alert alert = new Alert(AlertType.CONFIRMATION, "A restart is required");
            alert.showAndWait();
        }
        String new_username = nameField.getText();
        if (!new_username.equals("")) {

            prefs.put(new_username, "username");
        }
        power();
    }

    @FXML
    CheckBox visible;

    @FXML
    void changeVisibility() {
        System.out.println(visible.isSelected());
        if (visible.isSelected()) {
            server.start();
        } else {
            server.halt();
        }
    }

    private static final String imageLoc
            = "./src/quickcopy/images/temp_icob_w.png";
    
    java.awt.SystemTray tray;
    public static java.awt.TrayIcon trayIcon;

    private void addAppToTray() {
        try {
            // ensure awt toolkit is initialized.
            java.awt.Toolkit.getDefaultToolkit();

            // app requires system tray support, just exit if there is no support.
            if (!java.awt.SystemTray.isSupported()) {
                System.out.println("No system tray support, application exiting.");
                Platform.exit();
            }

            // set up a system tray icon.
            tray = java.awt.SystemTray.getSystemTray();
            File file = new File(imageLoc);
            java.awt.Image image = ImageIO.read(file);
            trayIcon = new java.awt.TrayIcon(image);

            // if the user double-clicks on the tray icon, show the main app stage.
            trayIcon.addActionListener(event -> Platform.runLater(this::show));

            // if the user selects the default menu item (which includes the app name),
            // show the main app stage.
            java.awt.MenuItem openItem = new java.awt.MenuItem("hello, world");
            openItem.addActionListener(event -> Platform.runLater(this::saywhat));

            // the convention for tray icons seems to be to set the default icon for opening
            // the application stage in a bold font.
            java.awt.Font defaultFont = java.awt.Font.decode(null);
            java.awt.Font boldFont = defaultFont.deriveFont(java.awt.Font.BOLD);
            openItem.setFont(boldFont);

            // to really exit the application, the user must go to the system tray icon
            // and select the exit option, this will shutdown JavaFX and remove the
            // tray icon (removing the tray icon will also shut down AWT).
            java.awt.MenuItem exitItem = new java.awt.MenuItem("Exit");
            exitItem.addActionListener(event -> {
                power();
            });

            // setup the popup menu for the application.
            final java.awt.PopupMenu popup = new java.awt.PopupMenu();
            popup.add(openItem);
            popup.addSeparator();
            popup.add(exitItem);
            trayIcon.setPopupMenu(popup);

            // add the application tray icon to the system tray.
            tray.add(trayIcon);
        } catch (java.awt.AWTException | IOException e) {
            System.out.println("Unable to init system tray");
            e.printStackTrace();
        }
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
    private void PackMan() {
        topselector.setVisible(false);
        middleselector.setVisible(true);
        bottomselector.setVisible(false);
        trafficselector.setVisible(false);

        scannerpane.setVisible(false);
        packpane.setVisible(true);
        settingspane.setVisible(false);
        welcome_pane.setVisible(false);
        trafficpane.setVisible(false);
    }

    @FXML
    private void Settings() {
        topselector.setVisible(false);
        middleselector.setVisible(false);
        bottomselector.setVisible(true);
        trafficselector.setVisible(false);

        scannerpane.setVisible(false);
        packpane.setVisible(false);
        settingspane.setVisible(true);
        welcome_pane.setVisible(false);
        trafficpane.setVisible(false);
    }

    @FXML
    private void Traffic() {
        topselector.setVisible(false);
        middleselector.setVisible(false);
        bottomselector.setVisible(false);
        trafficselector.setVisible(true);

        scannerpane.setVisible(false);
        packpane.setVisible(false);
        settingspane.setVisible(false);
        welcome_pane.setVisible(false);
        trafficpane.setVisible(true);
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
    private void power() {
        //halt application
        server.halt();
        TCPServer.halt();
        Platform.exit();
        tray.remove(trayIcon);
        System.exit(0);
    }

    public void sendScene(Scene scene_l, Stage s) {
        scene = scene_l;
        stage = s;
        //get all interactables

        topselector = (Ellipse) scene.lookup("#selectorontop");
        middleselector = (Ellipse) scene.lookup("#selectoronmiddle");
        bottomselector = (Ellipse) scene.lookup("#selectoronbottom");
        trafficselector = (Ellipse) scene.lookup("#trafficselector");

        settingspane = (AnchorPane) scene.lookup("#settings");
        scannerpane = (AnchorPane) scene.lookup("#scanner");
        packpane = (AnchorPane) scene.lookup("#packets");
        welcome_pane = (AnchorPane) scene.lookup("#Welcome");
        trafficpane = (AnchorPane) scene.lookup("#traffic");

        setdrawPackages();

        //get home page
        String home = prefs.get("home", "Welcome");
        switch (home) {
            case "Welcome":
                topselector.setVisible(false);
                middleselector.setVisible(false);
                bottomselector.setVisible(false);
                trafficselector.setVisible(false);

                scannerpane.setVisible(false);
                packpane.setVisible(false);
                settingspane.setVisible(false);
                welcome_pane.setVisible(true);
                trafficpane.setVisible(false);
                break;
            case "Scan":
                Scan();
                break;
            case "Package":
                PackMan();
                break;
            case "Traffic":
                Traffic();
                break;
            case "Settings":
                Settings();
                break;
            default:
                break;
        }

    }

    public synchronized static void addConnection(Connection conn, MainController contr) {
        boolean _found = false;
        for (int i = 0; i < connections.size(); i++) {
            if (connections.get(i).getAddr().equals(conn.getAddr())) {
                _found = true;
                break;
            }
        }
        if (!_found) {
            connections.add(conn);
            System.out.println("connection added");
            //only draw Conns if new conn is added
            contr.drawConnections();
        }

    }

    public static void setMyPort(int port) {
        myport = port;
    }

    @FXML
    private void drawConnections() {
        theme.draw(connections, scannerpane, scanlist);
    }

    public static List getConnections() {
        return connections;
    }

    @FXML
    private void setdrawPackages() {
        boolean darkmode = false;
        //draw PackMan
        FXMLLoader loader = new FXMLLoader();
        //if darkmode, then load the darkmode version
        if (darkmode) {
            loader.setLocation(getClass().getResource("package_dark.fxml"));
        } else {

            loader.setLocation(getClass().getResource("package.fxml"));
        }

        try {
            ScrollPane showBoxes = (ScrollPane) loader.load();
            packpane.getChildren().add(showBoxes);
            //send scene so PMC can add to VBox
            PackageManagerController controller = (PackageManagerController) loader.getController();
            controller.sendScene(showBoxes);
        } catch (IOException e) {
            System.out.println("The boxes screen failed to load: " + e.toString());
        }
    }
}
