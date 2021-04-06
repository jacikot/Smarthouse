/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package endpoints;

import static endpoints.Utility.getUser;
import entities.User;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.ConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import message.SongRequest;

/**
 *
 * @author janat
 */
@Path("sound")
public class Sound {
    
    @Resource(lookup="projectConnFactory")
    private ConnectionFactory myCf;
    
    
    @Resource(lookup="projectQueue")
    private Queue myQ;
    
    @PersistenceContext
    EntityManager em;
    
    
    @GET
    public Response xx(){
        return Response.ok().build();
    }
    
    
    @POST()
    @Path("{songName}")
    public Response playSong(@PathParam("songName")String song,@Context HttpHeaders httpHeaders){
        JMSContext context=myCf.createContext();
        JMSProducer producer=context.createProducer();
        User u=Utility.getUser(httpHeaders,em);
        SongRequest sr=new SongRequest(song, u.getIdU());
        ObjectMessage om=context.createObjectMessage(sr);
        try {
            om.setIntProperty("request",0);
            producer.send(myQ,om);
        } catch (JMSException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Problem u playSong slanje zahteva").build();
        }
        return Response.ok().entity("izvrseno").build();
    }
    
    
}
