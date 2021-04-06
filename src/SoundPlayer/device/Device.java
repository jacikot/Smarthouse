/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package device;



import com.google.api.services.youtube.YouTube;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
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
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

//import message.SongList;
import entities.Pesma;
import entities.Pustena;
import entities.User;
import java.security.GeneralSecurityException;

import java.util.ArrayList;

import message.SongList;
import message.SongRequest;




/**
 *
 * @author janat
 */
public class Device{
    
    @Resource(lookup="projectConnFactory")
    private static ConnectionFactory myCf;
    
    
    @Resource(lookup="projectQueue")
    private static Queue myQ;
    
    @Resource(lookup="projectTopic")
    private static Topic myT;
    
    private static JMSContext context;
    private static JMSConsumer consumer;
    private static JMSProducer producer;
    
    private static EntityManagerFactory emf;

    private EntityManager em;
    private MyYoutube youtube;
    
    
    public Device(){
        em=emf.createEntityManager();
        System.out.println("uradjen 3");
        youtube=new MyYoutube();
        
    }
    
    public void start(){
        while(true){
            try{
                Message msg=consumer.receive();
                
                if(msg instanceof ObjectMessage){
                    System.out.println("primljeno");
                    ObjectMessage om=(ObjectMessage)msg;
                    SongRequest sr=(SongRequest)om.getObject();
                    if(om.getIntProperty("request")==0){
                 
                        String url=play(sr.getSong());
                        sr.setURL(url);
                        insertToDatabase(sr);
                       
                    }
                    else{
                        List<String>listened=getListenedSongs(sr.getUserID(),em);
                        ObjectMessage om2=context.createObjectMessage(new SongList(listened));
                        om2.setIntProperty("id",1);
                        producer.send(myT,om2);
                        System.out.println("poslato");
                    } 
                }
            } catch (JMSException ex) {
                Logger.getLogger(Device.class.getName()).log(Level.SEVERE, null, ex);
            } 
            
        }
    }
    public static void main(String[]args){
        context=myCf.createContext();
        consumer=context.createConsumer(myQ);
        producer=context.createProducer();
        emf=Persistence.createEntityManagerFactory("ReprodukcijaZvukaPU");
        Device u=new Device();
        u.start();
        
    }
    public List<String> getListenedSongs(int idu,EntityManager em){
        User u=em.find(User.class,idu);
        if(u==null) return null;
        TypedQuery<String> q=em.createQuery("SELECT DISTINCT p.idP.naziv FROM Pustena p WHERE p.idU=:user", String.class).setParameter("user", u);
        return q.getResultList();
        
    } 
    public void insertToDatabase(SongRequest sr) throws JMSException{
        int u=sr.getUserID();
        User user=em.find(User.class, u);
        if(user==null) return;
        String name=sr.getSong();
        TypedQuery<Pesma> q=em.createNamedQuery("Pesma.findByNaziv", Pesma.class).setParameter("naziv",name);
        List<Pesma>list=q.getResultList();
        Pesma p;
        
        em.getTransaction().begin();
        
        if(list.isEmpty()){
            System.out.println("dodaje se");
            p=new Pesma();
            p.setNaziv(name);
            p.setLink(sr.getURL());
            //p.setIdP(100);
            em.persist(p);
        }
        else {
           System.out.println("ne dodaje se");
            p=list.get(0);
        }
        Pustena pustena=new Pustena();
        pustena.setIdPust(100);
        pustena.setIdP(p);
        pustena.setIdU(user);
        
        em.persist(pustena);
        em.getTransaction().commit();
        
         System.out.println("Transakcija zavrsena");
        
    }
    
    public String play(String songName){
        try {
            return youtube.ytAPI(songName);
        } catch (GeneralSecurityException ex) {
            Logger.getLogger(Device.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Device.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
  
}
