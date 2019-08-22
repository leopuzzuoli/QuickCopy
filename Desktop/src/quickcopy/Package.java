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
    List<Connection> recipients;
    List<String> files = new ArrayList<>();
    public Package(String _name, List<String> _files, List<Connection> _recipient, String _date){
        name = _name;
        recipients = _recipient;
        date = _date;
        files = _files;
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
}
