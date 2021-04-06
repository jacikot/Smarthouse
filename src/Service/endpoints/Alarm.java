/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package endpoints;

import entities.User;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import message.AlarmRequest;
import message.AlarmSongRequest;

/**
 *
 * @author janat
 */
@Path("alarm")
public class Alarm {
    @Resource(lookup="projectConnFactory")
    private ConnectionFactory myCf;
    
    
    @Resource(lookup="alarmQueue")
    private Queue myQ;
    
    @Resource(lookup ="projectTopic")
    private Topic myT;
    
    @PersistenceContext
    EntityManager em;
    
    @POST
    @Path("sound/{songName}/{alarmID}")
    public Response setAlarmSound(@Context HttpHeaders httpHeaders, @PathParam("songName")String song,@PathParam("alarmID")int idA){
        try {
            User u=Utility.getUser(httpHeaders, em);
            JMSContext context=myCf.createContext();
            JMSProducer producer=context.createProducer();
            
            AlarmSongRequest asr=new AlarmSongRequest(song, u.getIdU(), idA);
            ObjectMessage om=context.createObjectMessage(asr);
            om.setStringProperty("type","pesma");
            producer.send(myQ,om);
            return Response.ok().build();
        } catch (JMSException ex) {
            Logger.getLogger(Alarm.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response.serverError().entity("Greska set AlarmSound").build();
    }
    
    @POST
    @Path("{type}")
    public Response setAlarm(@Context HttpHeaders httpHeaders, @PathParam("type")int type,@QueryParam("time")Long time){
        try {
            
            User u=Utility.getUser(httpHeaders, em);
            JMSContext context=myCf.createContext();
            JMSProducer producer=context.createProducer();
            JMSConsumer consumer=context.createConsumer(myT,"id=2",false);
         
            if(u==null) return Response.noContent().entity("Nije pronadjen korisnik").build();
            
            AlarmRequest ar=new AlarmRequest();
            ar.setIdUser(u.getIdU());
           // System.out.println(time);
            ar.setTime(time);
            ar.setType(type);
            
            ObjectMessage om=context.createObjectMessage(ar);
            om.setStringProperty("type","navijanje");
            producer.send(myQ,om);
            
            Message m=consumer.receive();
            if(m instanceof TextMessage){
                TextMessage tm=(TextMessage)m;
                return Response.ok().entity(tm.getText()).build();
            }
            
        } catch (JMSException ex) {
            Logger.getLogger(Alarm.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response.noContent().entity("Greska u setAlarm").build();
    }   
    
}
