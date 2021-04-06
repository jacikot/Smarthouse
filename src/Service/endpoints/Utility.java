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
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.ws.rs.core.HttpHeaders;

/**
 *
 * @author janat
 */
public class Utility {
     public static User getUser(HttpHeaders httpHeaders,EntityManager em){
         List<String>headerValuesAut=httpHeaders.getRequestHeader("Authorization");
        if(headerValuesAut!=null&&headerValuesAut.size()>0){
            String aut0=headerValuesAut.get(0);
            String decoded = new String(Base64.getDecoder().decode(aut0.replaceFirst("Basic ", "")),StandardCharsets.UTF_8);
            //nakn sto dekodujemo pojavice nam se admin:sifra
            StringTokenizer st=new StringTokenizer(decoded,":");
            //podeli string, kao separator se koristi :
            String username=st.nextToken();
            String pass=st.nextToken();
            TypedQuery<User> tq=em.createNamedQuery("User.findByUsername", User.class);
            //dohvatamo korisnika i proveravamo da li postoji i da li je sifra odgovarajuca
            tq.setParameter("username", username);
            List<User>list=tq.getResultList();
            return list.get(0);
        }
        return null;
    }

    
    
}
