/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package message;

/**
 *
 * @author janat
 */
public class AlarmSongRequest extends SongRequest{
   
    private int idAlarm;
    
    public AlarmSongRequest(String s,int uid,int idA){
         super(s,uid);
         idAlarm=idA;
    }
    public int getIdAlarm() {
        return idAlarm;
    }

    public void setIdAlarm(int idAlarm) {
        this.idAlarm = idAlarm;
    }
    
    
}
