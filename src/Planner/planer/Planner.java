/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planer;

import entities.Alarm;
import entities.Obaveza;
import entities.Pesma;
import entities.User;
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
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import message.PlannerRequest;

/**
 *
 * @author janat
 */
public class Planner {
    
    @Resource(lookup="projectConnFactory")
    private static ConnectionFactory myCf;
    
    @Resource(lookup="plannerQueue")
    private static Queue plannerQueue;
    
    @Resource(lookup="projectQueue")
    private static Queue myQ;
    
    @Resource(lookup = "projectTopic")
    private static Topic myT;
    
    private static JMSContext context;
    private static JMSConsumer consumer;
    private static JMSProducer producer;
    
    private static EntityManagerFactory emf;
     private static EntityManager em;
    
     private static DistanceCalculator dc;
     
    private void sendResponseText(String response,int id) throws JMSException{
        TextMessage tm=context.createTextMessage(response);
        tm.setIntProperty("id",id);
        producer.send(myT,tm);
    }
    private void addObligation(PlannerRequest pr){
        try {
            System.out.println(pr.getStart());
            Obaveza o=new Obaveza();
            User u=em.find(User.class,pr.getUserID());
            if(u==null) return;
            em.getTransaction().begin();
            o.setIdU(u);
            o.setPocetak(pr.getStart());
            o.setKraj(new Date(pr.getStart().getTime()+pr.getDuration()));
            if(pr.getPlace()==null){
                o.setMesto(u.getAdresa());
            }
            else {
                o.setMesto(pr.getPlace());
            }   
            TypedQuery<Obaveza> tq=em.createQuery("SELECT o FROM Obaveza o WHERE o.idU.idU=:user ORDER BY o.pocetak",Obaveza.class)
                    .setParameter("user",pr.getUserID());
            List<Obaveza>list=tq.getResultList();
            Obaveza before=null,after=null;
            for(Obaveza obl:list){
                System.out.println(obl);
                if(obl.getPocetak().before(o.getPocetak())) before=obl;
                if(obl.getPocetak().after(o.getPocetak())) {
                    after=obl;
                    break;
                }
            }
            
//            if(before!=null)System.out.println(dc.calculateTime(before.getMesto(), o.getMesto()));
//            if(after!=null)System.out.println(dc.calculateTime(o.getMesto(), after.getMesto()));
//            if(before!=null){
//                System.out.println(o.getPocetak().getTime()-before.getKraj().getTime());
//                System.out.println(o.getPocetak());
//                System.out.println(before.getKraj());
//            }
            
            
            if(before==null||((dc.calculateTime(before.getMesto(), o.getMesto())*1000)<=(o.getPocetak().getTime()-before.getKraj().getTime()))){
            if(after==null||((dc.calculateTime(o.getMesto(), after.getMesto())*1000)<=(after.getPocetak().getTime()-o.getKraj().getTime()))){
                    em.persist(o);
                    em.getTransaction().commit();
                    sendResponseText("Uspesno sacuvana obaveza id= "+o.getIdO(),3);
                    return;
                }
                else sendResponseText("Neuspesno cuvanje obaveze",3);
            }
            else sendResponseText("Neuspesno cuvanje obaveze",3);
            em.getTransaction().commit();
        } catch (JMSException ex) {
            Logger.getLogger(Planner.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void removeObligation(int idO){
        Obaveza o=em.find(Obaveza.class,idO);
        if(o==null) return;
        em.getTransaction().begin();        
        em.remove(o);
        em.getTransaction().commit();
    }
    
    private void listObligations(int userID) throws JMSException{
        TypedQuery<Obaveza>tq=em.createQuery("SELECT o FROM Obaveza o WHERE o.idU.idU=:id",Obaveza.class).setParameter("id",userID);
        List<Obaveza>list=tq.getResultList();
        String resp="";
        for(Obaveza o:list){
            resp+=o.toString()+"\n";
        }
        sendResponseText(resp,4);
        
    }
    
    private void changeObligation(PlannerRequest pr) throws JMSException{
        Obaveza o=em.find(Obaveza.class,pr.getIdO());
        User u=em.find(User.class,pr.getUserID());
        
        em.getTransaction().begin();
        
        Date start=(pr.getStart()!=null)?pr.getStart():o.getPocetak();
        String place=(pr.getPlace()!=null)?pr.getPlace():o.getMesto();
        Date end;
        System.out.println(pr.getDuration());
        if(pr.getDuration()>0){
            end=new Date(start.getTime()+pr.getDuration());
            System.out.println("kraj "+end);
        }
        else end=new Date(start.getTime()+o.getKraj().getTime()-o.getPocetak().getTime());
        
        TypedQuery<Obaveza> tq=em.createQuery("SELECT o FROM Obaveza o WHERE o.idU.idU=:user AND o.idO!=:id ORDER BY o.pocetak",Obaveza.class)
                    .setParameter("user",pr.getUserID()).setParameter("id",pr.getIdO());
        List<Obaveza>list=tq.getResultList();
        
        Obaveza before=null,after=null;
            for(Obaveza obl:list){
//                System.out.println(obl);
                if(obl.getPocetak().before(start)) before=obl;
                if(obl.getPocetak().after(start)) {
                    after=obl;
                    break;
                }
            }
        if(before==null||((dc.calculateTime(before.getMesto(), place)*1000)<=(start.getTime()-before.getKraj().getTime()))){
            if(after==null||((dc.calculateTime(place, after.getMesto())*1000)<=(after.getPocetak().getTime()-end.getTime()))){
                    o.setKraj(end);
                    System.out.println(o.getKraj());
                    o.setMesto(place);
                    o.setPocetak(start);
                    em.getTransaction().commit();
                    sendResponseText("Uspesno sacuvana obaveza id= "+o.getIdO(),6);
                    return;
                }
                else sendResponseText("Neuspesna izmena obaveze",6);
            }
            else sendResponseText("Neuspesna izmena obaveze",6);
         
        
        em.getTransaction().commit();
    }
    
    private void activateReminder(int idO,int idU) throws JMSException{
        Obaveza o=em.find(Obaveza.class,idO);
        User u=em.find(User.class,idU);
        if(o==null) return;
        em.getTransaction().begin();   
        Alarm a=new Alarm();
        a.setAktivan(1);
        a.setIdU(u);
        Pesma p=em.createNamedQuery("Pesma.findByNaziv",Pesma.class).setParameter("naziv","defaultRing").getSingleResult();
        a.setIdP(p);
        Date startO=o.getPocetak();
        TypedQuery<Obaveza>tq=em.createQuery("SELECT oo FROM Obaveza oo WHERE oo.pocetak<:dat ORDER BY oo.pocetak DESC",Obaveza.class).setParameter("dat",startO);
        List<Obaveza> list=tq.getResultList();
        String from=null;
        if(list.isEmpty()) from=u.getAdresa();
        else from=list.get(0).getMesto();
        
        long time=dc.calculateTime(from, o.getMesto());
        System.out.println("vreme "+time);
        a.setAktivacija(new Date(o.getPocetak().getTime()-time*1000));
        em.persist(a);
        o.setIdA(a);
        em.getTransaction().commit();
        sendResponseText("Postavljen alarm id="+a.getIdA(), 5);
    }
    public void execute(){
        try {
            while(true){
                Message m=consumer.receive();
                if(m instanceof ObjectMessage){
                    ObjectMessage om=(ObjectMessage)m;
                    PlannerRequest pr=(PlannerRequest)(om.getObject());
                    switch(om.getIntProperty("type")){
                        case 0:
                            addObligation(pr); 
                            break;
                        case 1:
                            listObligations(pr.getUserID());
                            break;
                        case 2:
                            removeObligation(pr.getIdO());
                            break;
                        case 3:
                            activateReminder(pr.getIdO(),pr.getUserID());
                            break;
                            
                        case 4:
                            changeObligation(pr);
                            break;
                    }

                }
               
            }
        } catch (JMSException ex) {
                Logger.getLogger(Planner.class.getName()).log(Level.SEVERE, null, ex);
         }
        
    }
     
    public static void main(String[] args) {
        // TODO code application logic here
        dc=new DistanceCalculator();
        context=myCf.createContext();
        producer=context.createProducer();
        consumer=context.createConsumer(plannerQueue);
        emf=Persistence.createEntityManagerFactory("PlanerPU");
        em=emf.createEntityManager();
        
        new Planner().execute();
        
//        System.out.println("izvrsava main");
//        new DistanceCalculator().calculateDistance("Zmajevacka 68B", "Bulevar Kralja Aleksandra 73");
//        new DistanceCalculator().calculateTime("Zmajevacka 68B", "Bulevar Kralja Aleksandra 73");

          
    }
}
