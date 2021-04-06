/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filters;

import entities.Role;
import entities.User;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.StringTokenizer;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import javax.persistence.TypedQuery;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.PathSegment;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author janat
 */
@Provider
//dajemo pristup kontejneru u kome se izvrsava aplikacija???
public class Autentification implements ContainerRequestFilter{
    @PersistenceContext
    EntityManager em;
    
    @Override
    public void filter(ContainerRequestContext crc) throws IOException {
        List<String> headerValuesAut=crc.getHeaders().get("Authorization");
        //dohvatimo zaglavlje - to je lista ali trebalo bi da ima 1 element
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
            if(list.size()!=1) {
                Response r=Response.status(Response.Status.UNAUTHORIZED).entity("Pogresno korisnicko ime").build();
                crc.abortWith(r);
                return;
            }
            User user=list.get(0);
            if(!user.getPassword().equals(pass)){
                Response r=Response.status(Response.Status.UNAUTHORIZED).entity("Pogresna sifra").build();
                crc.abortWith(r);
                return;
            }
            
            //AUTORIZACIJA - da li korisnik ima pravo da pozove metdu
            
             Role role=user.getIdRole(); //dohvatamo ulogu korisnika
             String metoda=crc.getMethod(); //dohvatamo tip metode
             UriInfo uriinfo=crc.getUriInfo(); //dohvatamo url
             String uripath=uriinfo.getPath(); //string posle podrazumevanog dela za program (posle api)
             List<PathSegment>segments=uriinfo.getPathSegments();
             //lako dohvatanje putanje urla
             
             //naziv krajnje tacke je 0ti element liste
             String endPoint=segments.get(0).getPath();
             String subpoint=null;
             if(segments.size()>1)subpoint=segments.get(1).getPath();
             
             if(role.getIme().equals("admin")) return;
             if(role.getIme().equals("privileged")&&!endPoint.equals("roles")&&(!endPoint.equals("users")||(subpoint!=null&&subpoint.equals("myinfo")))) return;
             if(role.getIme().equals("unprivileged")&&!endPoint.equals("roles")&&metoda.equals("GET")&&(!endPoint.equals("users")||(subpoint!=null&&subpoint.equals("myinfo")))) return;
             Response r=Response.status(Response.Status.UNAUTHORIZED).entity("Nemate privilegiju za tu operaciju").build();
                crc.abortWith(r);
                return;
            
            
        }
        Response r=Response.status(Response.Status.UNAUTHORIZED).entity("Niste uneli kor ime i lozinku").build();
                crc.abortWith(r);
                return;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
