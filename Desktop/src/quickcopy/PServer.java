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
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Chipleo
 */
public class PServer extends Thread {

    private DatagramSocket socket;
    private boolean running;
    private byte[] buf = new byte[256];

    private String myname = "QuickCopy";
    private List<String> myIPs = new ArrayList<>();
    private int myPort;

    public PServer() {
        try {
            //Try to open server at 4445
            socket = new DatagramSocket(4445);
        } catch (SocketException e) {
            System.out.println("Could not open socket: " + e.toString());
        }
    }

    @Override
    public void run() {
        running = true;
        System.out.println("UDP Listener is running at localhost:4445");

        while (running) {
            DatagramPacket packet
                    = new DatagramPacket(buf, buf.length);
            try {
                socket.receive(packet);
            } catch (IOException e) {
                System.out.println("IOException at socket.recieve: " + e.toString());
            }

            InetAddress address = packet.getAddress();
            int port = packet.getPort();
            packet = new DatagramPacket(buf, buf.length, address, port);
            //Save incoming
            String received
                    = new String(packet.getData(), 0, packet.getLength());

            System.out.println("<- : " + received);
            //Check if recieved conenction is from a QC Client
            if (received.startsWith("QC at ")) {
                //if yes extract information, respond, add to conenctions (for passive discovery)

                //string identifier_id
                String[] i_i = received.split("QC at ");
                //string ip_port
                String[] i_p = i_i[1].split(":");
                //create new Connection
                //remove newline character on port value only works with 4 char ports
                Connection connfound = new Connection(i_p[0], Integer.parseInt(i_p[1].substring(0, 4)));

                //respond
                TClient tc = new TClient();
                tc.startConnection(connfound.getAddr(), connfound.getPort());
                //respond with all IPs
                for(String addr : myIPs){
                System.out.println("-> QC responding from " + addr + ":" + myPort+":" + myname);
                System.out.println(tc.sendMessage("QC responding from " + addr + ":" + myPort+":" + myname));
                }
                tc.stopConnection();

                //add to conns
                MainController.addConnection(connfound);

            }

            /*try {
                socket.send(packet);
            } catch (IOException e) {
                System.out.println("IOException at socket.send: " + e.toString());
            }*/
        }
        System.out.println("UDP Listener stopped");
        socket.close();
    }

    public void halt() {
        running = false;
    }
    
    public void setIP(List<String> ip){
        myIPs = ip;
    }
    
    public void setPort(int port){
        myPort = port;
    }
    public void setHostname(String Hostname){
        myname = Hostname;
    }
}
