/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package endpoints;

import entities.Obaveza;
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
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import message.PlannerRequest;

/**
 *
 * @author janat
 */
@Path("planner")
public class Planner {
    @Resource(lookup="projectConnFactory")
    private ConnectionFactory myCf;
    
    
    @Resource(lookup="plannerQueue")
    private Queue myQ;
    
    @Resource(lookup ="projectTopic")
    private Topic myT;
    
    @PersistenceContext
    EntityManager em;
    
    @POST
    @Path("{start}/{dur}")
    public Response addObligation(@Context HttpHeaders httpHeaders,@PathParam("start") long start,@PathParam("dur")long dur,@QueryParam("place")String place){
        try {
            User u=Utility.getUser(httpHeaders, em);
            
            JMSContext context=myCf.createContext();
            JMSProducer producer=context.createProducer();
            JMSConsumer consumer=context.createConsumer(myT,"id=3",false);
            
            Date dstart=new Date(start);
            PlannerRequest pr=new PlannerRequest(u.getIdU(),0, dstart, dur, place);
            if(place==null) System.out.println("mesto je null");
            ObjectMessage om=context.createObjectMessage(pr);
            om.setIntProperty("type",0);
            producer.send(myQ,om);
            
            Message m=consumer.receive();
            if(m instanceof TextMessage){
                String resp=((TextMessage)m).getText();
                return Response.ok().entity(resp).build();
            }
           
        } catch (JMSException ex) {
            Logger.getLogger(Planner.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }
    @DELETE
    @Path("{idO}")
    public Response deleteObligation(@Context HttpHeaders httpHeaders,@PathParam("idO") int idO){
        try {
            User u=Utility.getUser(httpHeaders, em);
            Obaveza o=em.find(Obaveza.class,idO);
            if(o==null||u==null) return Response.noContent().entity("ne postoji obaveza").build();
            if(!o.getIdU().equals(u)) return Response.status(Response.Status.BAD_REQUEST).entity("obaveza ne pripada korisniku").build();
            
            JMSContext context=myCf.createContext();
            JMSProducer producer=context.createProducer();
            
            ObjectMessage om=context.createObjectMessage(new PlannerRequest(0,idO, null, 0, null));
            om.setIntProperty("type",2);
            producer.send(myQ,om);
            return Response.ok().entity("obaveza obrisana").build();
        } catch (JMSException ex) {
            Logger.getLogger(Planner.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return Response.status(Response.Status.BAD_REQUEST).build();
    }
    @POST
    @Path("alarm/{idO}")
    public Response activateAlarm(@Context HttpHeaders httpHeaders,@PathParam("idO") int idO){
        try {
            User u=Utility.getUser(httpHeaders, em);
            Obaveza o=em.find(Obaveza.class,idO);
            if(o==null||u==null) return Response.noContent().entity("ne postoji obaveza").build();
            if(!o.getIdU().equals(u)) return Response.status(Response.Status.BAD_REQUEST).entity("obaveza ne pripada korisniku").build();
            
            JMSContext context=myCf.createContext();
            JMSProducer producer=context.createProducer();
            JMSConsumer consumer=context.createConsumer(myT,"id=5",false);
            
            ObjectMessage om=context.createObjectMessage(new PlannerRequest(u.getIdU(),idO, null, 0, null));
            om.setIntProperty("type",3);
            producer.send(myQ,om);
            
            Message m=consumer.receive();
            if(m instanceof TextMessage){
                String resp=((TextMessage)m).getText();
                return Response.ok().entity(resp).build();
            }
        } catch (JMSException ex) {
            Logger.getLogger(Planner.class.getName()).log(Level.SEVERE, null, ex);
        }
         return Response.status(Response.Status.BAD_REQUEST).build();   
    }
    
    @POST
    @Path("{idO}")
    public Response changeObligation(@Context HttpHeaders httpHeaders,@PathParam("idO") int idO,@QueryParam("start")Long start,@QueryParam("dur")Long dur,@QueryParam("place")String place){
        try {
            User u=Utility.getUser(httpHeaders, em);
            Obaveza o=em.find(Obaveza.class,idO);
            if(o==null||u==null) return Response.noContent().entity("ne postoji obaveza").build();
            if(!o.getIdU().equals(u)) return Response.status(Response.Status.BAD_REQUEST).entity("obaveza ne pripada korisniku").build();
            
            JMSContext context=myCf.createContext();
            JMSProducer producer=context.createProducer();
            JMSConsumer consumer=context.createConsumer(myT,"id=6",false);
            
            Date s=null;
            if(start!=null)s=new Date(start);
            long duration;
            if(dur==null)duration=0;
            else duration=dur;
            
            ObjectMessage om=context.createObjectMessage(new PlannerRequest(u.getIdU(),idO, s, duration, place));
            om.setIntProperty("type",4);
            producer.send(myQ,om);
            
            Message m=consumer.receive();
            if(m instanceof TextMessage){
                String resp=((TextMessage)m).getText();
                return Response.ok().entity(resp).build();
            }
            
        } catch (JMSException ex) {
            Logger.getLogger(Planner.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response.status(Response.Status.BAD_REQUEST).build();     
    }
}
