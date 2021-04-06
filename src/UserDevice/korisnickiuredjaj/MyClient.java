/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package korisnickiuredjaj;


import static com.sun.xml.ws.spi.db.BindingContextFactory.LOGGER;
import java.awt.Frame;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.resource.spi.AdministeredObject;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;


/**
 *
 * @author janat
 */
public class MyClient extends Frame{

    /**
     * @param args the command line arguments
     */
    
    
   
    
    
   
    
    private static void playSong(String song,String username,String password){
        try {
            String songURL="http://localhost:8080/KorisnickiServis/service/sound/"+song.replace(" ", "%20");
            MyHttpRequest.POST(songURL,username,password);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(MyClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static void listAllPlayedSongs(String username,String password){
        try {
            String url="http://localhost:8080/KorisnickiServis/service/users/myinfo/songs";
            MyHttpRequest.GET(url, username, password);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(MyClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static void setAlarm(String username,String password,int type,Long timeVar){
        try {
            System.out.println(timeVar);
            String url="http://localhost:8080/KorisnickiServis/service/alarm/"+type;
            if(timeVar!=null)url+="?time="+timeVar;
            //System.out.println(url);
            MyHttpRequest.POST(url, username, password);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(MyClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static void setAlarmSound(String username,String password,String song,int idA){
        try {
            String url="http://localhost:8080/KorisnickiServis/service/alarm/sound/"+song.replace(" ", "%20")+"/"+idA;
            MyHttpRequest.POST(url, username, password);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(MyClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private static void setObligation(String username,String password,long start,long duration,String place){
        try {
            String url="http://localhost:8080/KorisnickiServis/service/planner/"+start+"/"+duration;
            if(place!=null)url=url+"?place="+place.replace(" ", "%20");
            MyHttpRequest.POST(url, username, password);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(MyClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static void listObligations(String username,String password){
        try {
            String url="http://localhost:8080/KorisnickiServis/service/users/myinfo/obligations";
            MyHttpRequest.GET(url, username, password);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(MyClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    private static void deleteObligation(String username,String password,int idO){
        try {
            String url="http://localhost:8080/KorisnickiServis/service/planner/"+idO;
            MyHttpRequest.DELETE(url, username, password);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(MyClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static void activatePlannerAlarm(String username,String password,int idO){
        try {
            String url="http://localhost:8080/KorisnickiServis/service/planner/alarm/"+idO;
            MyHttpRequest.POST(url, username, password);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(MyClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static void changeObligation(String username,String password,int idO,Long start,Long durationMillis,String place){
        try {
            String url="http://localhost:8080/KorisnickiServis/service/planner/"+idO;
            int paramC=0;
            if(start!=null) {
                url+="?start="+start;
                paramC++;
            }
            if(durationMillis!=null){
                if(paramC==0)url+="?";
                else url+="&";
                url+="dur="+durationMillis;
                paramC++;
            }
            if(place!=null){
                if(paramC==0)url+="?";
                else url+="&";
                url+="place="+place.replace(" ", "%20");
                paramC++;
            }
            MyHttpRequest.POST(url, username, password);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(MyClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static String[] menuSoundDevice={
            "   11. Reprodukcija pesme",
            "   12. Izlistavanje slusanih pesama"
    };
    
    private static String[] menuAlarm={
            "   21. Navijanje alarma u zadatom trenutku",
            "   22. Navijanje periodicnog alarma",
            "   23. Navijanje alarma u jednom od ponudjenih trenutaka",
            "   24. Konfigurisanje zvona alarma"
    };
    private static String[] menuPlanner={
            "   31. Dodavanje obaveze",
            "   32. Brisanje obaveze",
            "   33. Menjanje obaveze",
            "   34. Izlistavanje svih obaveza",
            "   35. Postavljanje alarma za obavezu",
    };

    public static void printMenu(){
        System.out.println("1. Reprodukcije zvuka");
        for(String s:menuSoundDevice){
            System.out.println(s);
        }
        System.out.println("2. Podesavanje alarma");
        for(String s:menuAlarm){
            System.out.println(s);
        }
        System.out.println("3. Podesavanje planera");
        for(String s:menuPlanner){
            System.out.println(s);
        }
        System.out.println("4. Prekid izvrsavanja");
        System.out.println("Unesite stavku koju zelite da izvrsite:");
    }
    public static void main(String[] args) {
        if(args.length<2)return;
        String username=args[0];
        String password=args[1];
        
        while(true){
            try {
                printMenu();
                BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
                int option=Integer.parseInt(br.readLine());
                switch(option){
                    case 11:
                        System.out.println("Unesite ime pesme");
                        String song=br.readLine();
                        playSong(song, username, password);
                        break;
                    case 12:
                        listAllPlayedSongs(username, password);
                        break;
                    case 21:
                        System.out.println("Unesite datum i vreme pustanja alarma (format HH:mm:ss dd-MM-yyyy)");
                        String dateInput=br.readLine();
                        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");
                        try {
                            Date d=format.parse(dateInput);
                            setAlarm(username, password, 0, d.getTime());
                        } catch (ParseException ex) {
                            System.out.println("Neispravan unos");
                        }
                        break;
                        
                    case 22:
                        System.out.println("Unesite periodu alarma u milisekundama");
                        long period=Long.parseLong(br.readLine());
                        setAlarm(username, password, 1, period);
                        break;
                    case 23:
                        setAlarm(username, password, 2, null);
                        break;
                    case 24:
                        System.out.println("Unesite id alarma");
                        int idA=Integer.parseInt(br.readLine());
                        System.out.println("Unesite ime pesme");
                        song=br.readLine();
                        setAlarmSound(username, password, song, idA);
                        break;
                    case 31:
                        System.out.println("Unesite datum i vreme obaveze (format HH:mm:ss dd-MM-yyyy)");
                        dateInput=br.readLine();
                        format = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");
                        try {
                            Date d=format.parse(dateInput);
                            System.out.println("Unesite trajanje obaveze u minutima");
                            long dur=Long.parseLong(br.readLine());
                            System.out.println("Unesite mesto odrzavanja obaveze, ako zelite da se odrzava na kucnoj adresi unesite kuca");
                            String place=br.readLine();
                            if(place.equals("kuca"))place=null;
                            setObligation(username,password,d.getTime(), dur*60*1000, place);
                        } catch (ParseException ex) {
                            System.out.println("Neispravan unos");
                        }
                        break;
                    case 32:
                        System.out.println("Unesite id obaveze");
                        int id=Integer.parseInt(br.readLine());
                        deleteObligation(username, password, id);
                        break;
                    case 33:
                        System.out.println("Unesite id obaveze");
                        id=Integer.parseInt(br.readLine());
                        System.out.println("Unesite datum i vreme obaveze (format HH:mm:ss dd-MM-yyyy), ako zelite da zadrzite staro unesite zadrzi");
                        dateInput=br.readLine();
                        Date d=null;
                        if(!dateInput.equals("zadrzi")){
                            format = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");
                            try {
                                d=format.parse(dateInput);
                            }
                            catch (ParseException ex) {
                                System.out.println("Neispravan unos");
                                break;
                            }
                        }
                        System.out.println("Unesite trajanje obaveze u minutima, ako zelite da zadrzite staro unesite zadrzi");
                        dateInput=br.readLine();
                        Long duration=null;
                        if(!dateInput.equals("zadrzi")){
                            duration=Long.parseLong(dateInput);
                        }
                        System.out.println("Unesite mesto odrzavanja obaveze,ko zelite da zadrzite staro unesite zadrzi");
                        String place=br.readLine();
                        if(place.equals("zadrzi"))
                            place=null;
                        changeObligation(username, password, id, ((d!=null)?d.getTime():null), duration*60*1000, place);
                        break;
                    case 34:
                        listObligations(username, password);
                        break;
                    case 35:
                        System.out.println("Unesite id obaveze");
                        id=Integer.parseInt(br.readLine());
                        activatePlannerAlarm(username, password, id);
                        break;
                    case 4: return;
                    default: System.out.println("Nepostojeca stavka");
                }
            } catch (IOException ex) {
                Logger.getLogger(MyClient.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        //new GUI();
        // TODO code application logic here
        // GET("http://localhost:8080/KorisnickiServis/service/roles","marina","123");
        //playSong("Tako lako Dzenan", username, password);
        //listAllPlayedSongs(username, password);
        //setAlarm(username, password, 0,System.currentTimeMillis()+10*60*1000);
        //setObligation(username, password, System.currentTimeMillis()+15*60*1000, 5*60*1000, "Knez Mihailova 10");
        //activatePlannerAlarm(username, password, 23);
       // changeObligation(username,password,1,System.currentTimeMillis()+5*60*1000,(long)5*60*1000,"Bezanijska kosa");
        //deleteObligation(username, password, 23);
        //listObligations(username, password);
       // Date d=new Date(System.currentTimeMillis());
        //System.out.print(d);
        //setAlarmSound(username,password,"Tako lako Dzenan",11);
        // POST("http://localhost:8080/KorisnickiServis/service/sound/Insomnija","admin", "123");
       // System.out.println("izvrseno");
            
            
    }
    
}
