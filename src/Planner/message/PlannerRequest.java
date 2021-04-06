/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package message;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author janat
 */

public class PlannerRequest implements Serializable{
    private int userID;
    private int idO;
    private Date start;
    private long duration;
    private String place;

    public int getUserID() {
        return userID;
    }

    public int getIdO() {
        return idO;
    }

    public void setIdO(int idO) {
        this.idO = idO;
    }

    public PlannerRequest(int userID,int idO, Date start, long duration, String place) {
        this.userID = userID;
        this.start = start;
        this.duration = duration;
        this.place = place;
        this.idO=idO;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }
    
    
}
