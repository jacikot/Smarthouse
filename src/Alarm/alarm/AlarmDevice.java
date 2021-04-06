/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alarm;


import java.util.Calendar;
import java.util.Date;
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
import javax.jms.Topic;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import message.AlarmRequest;
import message.SongRequest;
import message.AlarmSongRequest;

import entities.Alarm;
import entities.Pesma;
import entities.User;
import entities.Vreme;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.security.GeneralSecurityException;
import javax.jms.TextMessage;
import javax.persistence.TemporalType;



/**
 *
 * @author janat
 */
public class AlarmDevice extends Thread{
    @Resource(lookup="projectConnFactory")
    private static ConnectionFactory myCf;
    
    @Resource(lookup="alarmQueue")
    private static Queue alarmQueue;
    
    @Resource(lookup="projectQueue")
    private static Queue myQ;
    
    @Resource(lookup = "projectTopic")
    private static Topic myT;
    
    private static JMSContext context;
    private static JMSConsumer consumer;
    private static JMSProducer producer;
    
//    String url;
//    String songName;
    
    private static EntityManagerFactory emf;
    private static EntityManager em1;
    private static EntityManager em2;
    
    private static final String defSong="defaultRing";
    private static final String defUri="https://www.youtube.com/watch?v=zud9LiIS7IQ";
    /**
     * @param args the command line arguments
     */
    public void run(){
        try{
            while(true){
                Date current=new Date(System.currentTimeMillis());
                //System.out.println(current);
                TypedQuery<Alarm> tq=em2.createQuery("SELECT a FROM Alarm a WHERE a.aktivacija<=:akt AND a.aktivan=1",Alarm.class).setParameter("akt",current,TemporalType.TIMESTAMP);
                List<Alarm>list=tq.getResultList();
               
                for(int i=0;i<list.size();i++){
                    Alarm a=list.get(i);
                    if(a.getAktivacija().before(current)&&a.getAktivan()==1){
                        Pesma p=a.getIdP();
                        activateAlarm(p.getNaziv(),a.getIdU().getIdU());
                        synchronized(em2){
                            em2.getTransaction().begin();
                            if(a.getPerioda()==null){
                                a.setAktivan(0);
                            }
                            else {
                                Date akt=new Date(System.currentTimeMillis()+a.getPerioda().getTime());
                                a.setAktivacija(akt);
                                System.out.println(a.getAktivacija().getTime());

                            }
                            em2.getTransaction().commit();
                        }
                        
                    }
                }
                Thread.sleep(1000);
                
            }
        }catch (InterruptedException ex) {
                Logger.getLogger(Alarm.class.getName()).log(Level.SEVERE, null, ex);
            } catch (JMSException ex) {
            Logger.getLogger(AlarmDevice.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    public static void setTimeAlarm(AlarmRequest ar,Alarm a){
        Date akt=new Date();
        System.out.println(akt);
        akt.setTime(ar.getTime());
        System.out.println(ar.getTime());
        System.out.println(akt);
        a.setAktivacija(akt);
        
    }
    
    public static void setPeriodAlarm(AlarmRequest ar,Alarm a){
        Date akt=new Date();
        System.out.println(akt);
        System.out.println(ar.getTime()/1000/60);
        akt.setTime(akt.getTime()+ar.getTime());
        System.out.println(akt);
        a.setAktivacija(akt);
        a.setPerioda(new Date(ar.getTime()));
    }
    
    public static void setRandomAlarm(Alarm a){
        
        Date akt=new Date();
        Date newD=new Date();
        System.out.println(newD);
        TypedQuery<Long> tq2=em2.createQuery("SELECT COUNT(v) FROM Vreme v",Long.class);
        long count=tq2.getSingleResult();
        int randNum=(int)(Math.random()*count);
        if(count==0)return;
        TypedQuery<Vreme> tq3=em2.createNamedQuery("Vreme.findAll",Vreme.class);
        akt.setTime(akt.getTime()+tq3.getResultList().get(randNum).getVreme().getTime()+1000*60*60);
        System.out.println(tq3.getResultList().get(randNum).getVreme());
        System.out.println(akt);
        a.setAktivacija(akt);
    }
    public static void acceptAlarmRequests() throws JMSException{
        while(true){
            System.out.println("salje se");
            Message m=consumer.receive();
            System.out.println("primljeno");
            if(m instanceof ObjectMessage){
                if(m.getStringProperty("type").equals("navijanje")){
                     AlarmRequest ar=(AlarmRequest)(((ObjectMessage)m).getObject());
                     synchronized(em2){
                            em2.getTransaction().begin();
                            Alarm a=new Alarm();
                            TypedQuery<Pesma> tq=em2.createNamedQuery("Pesma.findByNaziv",Pesma.class).setParameter("naziv",defSong);
                            Pesma p=tq.getSingleResult();
                            a.setIdP(p);
                            a.setAktivan(1);
                            User u=em2.find(User.class,ar.getIdUser());
                            a.setIdU(u);
                            //Date akt=new Date();
                            switch(ar.getType()){
                                case 0: //zadat trenutak
                                    setTimeAlarm(ar, a);
                                    System.out.println("poslato");
                                    break;
                                case 1: //zadata perioda
                                    setPeriodAlarm(ar, a);
                                    break;
                                case 2: //jedno od ponudjenih
                                    setRandomAlarm(a);
                                    break;

                            }
                            em2.persist(a);
                            em2.getTransaction().commit();
                            TextMessage tm=context.createTextMessage("id unesenog alarma: "+a.getIdA().toString());
                            tm.setIntProperty("id",2);
                            producer.send(myT,tm);
                            
                     }
                     
                     
                }
                else{
                    try {
                        AlarmSongRequest asr=(AlarmSongRequest)((ObjectMessage)m).getObject();
                        configureRing(asr);
                    } catch (GeneralSecurityException ex) {
                        Logger.getLogger(AlarmDevice.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(AlarmDevice.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                
            }
        }
    }
    public void activateAlarm(String songName,int idu) throws JMSException{
        SongRequest sq=new SongRequest(songName, idu);
        ObjectMessage om=context.createObjectMessage(sq);
        om.setIntProperty("request",0);
        producer.send(myQ,om);
        System.out.println("pokrenut");
    }
    
    
    public static void configureRing(AlarmSongRequest asr) throws GeneralSecurityException, IOException{
        User curUser=em2.find(User.class,asr.getUserID());
        Alarm a=em2.find(Alarm.class,asr.getIdAlarm());
        if(a==null||!a.getIdU().equals(curUser)) return;
        TypedQuery<Pesma>tq=em2.createNamedQuery("Pesma.findByNaziv",Pesma.class).setParameter("naziv",asr.getSong());
        List<Pesma>list=tq.getResultList();
        synchronized(em2){
            em2.getTransaction().begin();
            Pesma pesma;
            if(list.isEmpty()){
                pesma=new Pesma();
                MyYoutube yt=new MyYoutube();
                
                pesma.setLink(yt.findURL(asr.getSong()));
                pesma.setNaziv(asr.getSong());
                em2.persist(pesma);
            }
            else{
                pesma=list.get(0);
            }
            a.setIdP(pesma);
            em2.getTransaction().commit();
        }
        
    }
    public static void main(String[] args) {
        // TODO code application logic here
        context=myCf.createContext();
        producer=context.createProducer();
        consumer=context.createConsumer(alarmQueue);
        emf=Persistence.createEntityManagerFactory("AlarmPU");
        em1=emf.createEntityManager();
        em2=emf.createEntityManager();
        AlarmDevice a=new AlarmDevice();
        a.start();
        try {
            acceptAlarmRequests();
        } catch (JMSException ex) {
            Logger.getLogger(AlarmDevice.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
