/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickcopy;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.List;

/**
 *
 * @author Chipleo
 */
//actually handle exceptions
public class TClient {

    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    
    String Address;
    int po;

    public void startConnection(String ip, int port)  throws SocketTimeoutException{
        try {
            
            Address = ip;
            po = port;
            
            clientSocket = new Socket();
            clientSocket.connect(new InetSocketAddress(ip, port), 1000);
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } 
        catch(SocketTimeoutException e){
            throw new SocketTimeoutException();
        }
        catch (IOException e) {
            System.out.println("Error in TClient: " + e.toString());
        }
    }

    public void sendMessage(String msg) {
        try{
        out.println(msg);
        }
        catch(NullPointerException e){
            e.toString();
        }
    }

    public void sendAccept(List<String> files, List<String> filepaths) {
        //TODO: files with spaces in them cannot be sent yet
        //if files and filepaths are not the same length, someone is trying to smuggle extra files
        sendMessage("Accept " + files);
        try {
            String res = in.readLine();
            if ("green".equals(res)) {
                //send files
                int z = 0;
                for (String file : filepaths) {
                    File acfile = new File(file);
                    if (!acfile.exists() || !acfile.isFile()) {
                        return;
                    }
                    System.out.println("file " + file + " " + acfile.length());
                    stopConnection();
                    startConnection(Address, po);
                    sendMessage("file " + files.get(z) + " " + acfile.length());
                    z++;
                    DataOutputStream dos = new DataOutputStream(clientSocket.getOutputStream());
                    FileInputStream fis = new FileInputStream(file);
                    byte[] buffer = new byte[4096];

                    while (fis.read(buffer) > 0) {
                        dos.write(buffer);
                    }
                    if (file.equals(filepaths.get(filepaths.size() - 1))) {
                        fis.close();
                        dos.close();
                    }

                }
            }
        } catch (IOException e) {
            System.out.println("ERROR: " + e.toString());
        }
    }

    public void stopConnection() {
        try {
            in.close();
            out.close();
            clientSocket.close();
        } catch (IOException e) {
            System.out.println("Connection could not be closed: " + e.toString());
        }
    }
}
