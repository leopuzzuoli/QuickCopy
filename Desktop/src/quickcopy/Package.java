/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quickcopy;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Chipleo
 */
public class Package {
    
    private String name, date;
    List<Connection> recipients = new ArrayList<>();
    List<String> files = new ArrayList<>();
    private int ID;
    
    public Package(String _name, List<String> _files, String _date, int id){
        name = _name;
        ID = id;
        //recipients = _recipient;
        date = _date;
        files = _files;
    }
    //for empty list construction
    public Package(String _name, String _date, int id){
        name = _name;
        date = _date;
        ID = id;
    }
    
    public String getName(){
        return name;
    }
    
    public void setName(String _name){
        name = _name;
    }
    
    public List getRecipients(){
        return recipients;
    }
    
    public void addRecipient(Connection conn){
        recipients.add(conn);
    }
    
    public void removeRecipient(Connection conn){
        recipients.remove(conn);
    }
    
    public List getFiles(){
        return files;
    }
    
    public void addFile(String file){
        files.add(file);
    }
    
    public void removeFile(String file){
        files.remove(file);
    }
    
    public String getDate(){
        //date in format DD.MM.YY / HH:MM
        return date;
    }
    
    public int getId(){
        return ID;
    }
    public void setId(int id){
        ID = id;
    }
}
