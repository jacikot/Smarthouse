/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package message;

import java.io.Serializable;

/**
 *
 * @author janat
 */

public class SongRequest implements Serializable {
    private String song;
    private int userID;
    private String url;
    
    public SongRequest(String s,int uid){
        song=s;
        userID=uid;
    }

    public String getSong() {
        return song;
    }

    public int getUserID() {
        return userID;
    }
    public String getURL(){
        return url;
    }
    public void setURL(String u){
        url=u;
    }
    
}
