/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickcopy;

/**
 *
 * @author Chipleo
 */
public class Connection {

    final private int port;
    final private String inetAddr;
    private String name = "QuickCopy";

    public Connection(String _inetAddr, int _port) {
        inetAddr = _inetAddr;
        port = _port;
    }

    public String getAddr() {
        return inetAddr;
    }

    public int getPort() {
        return port;
    }
    
    public String getName(){
        return name;
    }
    
    public void setName(String _name){
        //checks should be made on name
        name = _name;
    }
}
