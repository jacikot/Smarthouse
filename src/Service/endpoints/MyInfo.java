/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package endpoints;

import entities.User;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.StringTokenizer;
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
import javax.persistence.TypedQuery;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import message.PlannerRequest;
import message.SongList;
import message.SongRequest;

/**
 *
 * @author janat
 */
@Path("users/myinfo")
public class MyInfo {
    @Resource(lookup="projectConnFactory")
    private ConnectionFactory myCf;
    
    
    @Resource(lookup="projectQueue")
    private Queue myQ;
    
    @Resource(lookup="plannerQueue")
    private Queue myPlannerQ;
    
    @Resource(lookup ="projectTopic")
    private Topic myT;
    
    @PersistenceContext
    EntityManager em;
    
   
    @GET
    @Path("songs")
    public Response getAllPlayedSongs(@Context HttpHeaders httpHeaders){
        try {
            User u=Utility.getUser(httpHeaders, em);
            if(u==null) return Response.noContent().entity("Ne postoji korisnik").build();
            JMSContext context=myCf.createContext();
            JMSProducer producer=context.createProducer();
            JMSConsumer consumer=context.createConsumer(myT,"id=1",false);
            
            SongRequest sr=new SongRequest(null,u.getIdU());
            ObjectMessage om=context.createObjectMessage(sr);
            om.setIntProperty("request", 1);
            producer.send(myQ, om);
            System.out.println("poslato");
            Message msgR=consumer.receive();
            System.out.println("primljeno");
            if(msgR instanceof ObjectMessage){
                ObjectMessage omsg=(ObjectMessage)msgR;
                SongList sl=(SongList)omsg.getObject();
                List<String>list=sl.getList();
                return Response.ok().entity(new GenericEntity<List<String>>(list){}).build();
            }
        } catch (JMSException ex) {
            Logger.getLogger(MyInfo.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response.noContent().entity("Greska u getAllPlayedSongs").build();
    }
    
    @GET
    @Path("obligations")
    public Response getObligations(@Context HttpHeaders httpHeaders){
        try {
            User u=Utility.getUser(httpHeaders, em);
            if(u==null) return Response.noContent().entity("Ne postoji korisnik").build();
            JMSContext context=myCf.createContext();
            JMSProducer producer=context.createProducer();
            JMSConsumer consumer=context.createConsumer(myT,"id=4",false);
            
            PlannerRequest pr=new PlannerRequest(u.getIdU(),0, null, 0, null);
            ObjectMessage pom=context.createObjectMessage(pr);
            pom.setIntProperty("type",1);
            producer.send(myPlannerQ,pom);
            
            System.out.println("poslato");
            Message msgR=consumer.receive();
            System.out.println("primljeno");
            
            if(msgR instanceof TextMessage){
                TextMessage omsg=(TextMessage)msgR;
                return Response.ok().entity(omsg.getText()).build();
              
            }
        } catch (JMSException ex) {
            Logger.getLogger(MyInfo.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response.noContent().entity("Greska u getObligations").build(); 
            
    }
}
