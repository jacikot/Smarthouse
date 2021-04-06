/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package korisnickiuredjaj;

import static com.sun.xml.ws.spi.db.BindingContextFactory.LOGGER;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.logging.Level;
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
public class MyHttpRequest {
     public static void setAuthorization(HttpRequestBase httpReq, String username, String password) throws UnsupportedEncodingException{
        String credential = Base64.getEncoder().encodeToString((username+":"+password).getBytes("UTF-8"));
//        httpReq.setHeader("Authorization", "Basic " + credential.substring(0, credential.length()-1));
        httpReq.setHeader("Authorization", "Basic " + credential);
        
        httpReq.setHeader("Accept", "application/json");
        httpReq.setHeader("Connection", "close");
    }
    public static void GET(String url,String username, String password) throws UnsupportedEncodingException{
        final HttpClient httpClient = new DefaultHttpClient();
            final HttpGet httpGet = new HttpGet(url);
            setAuthorization(httpGet,username,password);
            HttpResponse response = null;
            try {
                response = httpClient.execute(httpGet);
                StatusLine statusLine = response.getStatusLine();
                if(statusLine.getStatusCode()!=HttpStatus.SC_OK){
                    System.out.println("Statusni kod greske:"+statusLine.getStatusCode());
                    return;
                }
            } catch (IOException ex) {
                if (LOGGER.isLoggable(Level.INFO)) {
                    LOGGER.info("The method is down." + ex.getMessage());
                }
            }
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            } catch (IOException ex) {
                if (LOGGER.isLoggable(Level.INFO)) {
                    LOGGER.info("The method is down." + ex.getMessage());
                }
            }
            String line = "";
            while (true) {
                try {
                    if (!((line = reader.readLine()) != null)) break;
                } catch (IOException ex) {
                    if (LOGGER.isLoggable(Level.INFO)) {
                        LOGGER.info("The method is down." + ex.getMessage());
                    }
                }
                System.out.println(line);
            }
      }
    public static void POST(String url,String username, String password) throws UnsupportedEncodingException{
            final HttpClient httpClient = new DefaultHttpClient();
            final HttpPost httpPost = new HttpPost(url);
            setAuthorization(httpPost,username,password);
            StringEntity input = null;
            try {
                input = new StringEntity("id");
            } catch (UnsupportedEncodingException ex) {
                if (LOGGER.isLoggable(Level.INFO)) {
                    LOGGER.info("The method is down." + ex.getMessage());
                }
            }
            httpPost.setEntity(input);
            HttpResponse response = null;
            
            try {
                response = httpClient.execute(httpPost);
                StatusLine statusLine = response.getStatusLine();
                if(statusLine.getStatusCode()!=HttpStatus.SC_OK){
                    System.out.println("Statusni kod greske:"+statusLine.getStatusCode());
                    return;
                }
            } catch (IOException ex) {
                if (LOGGER.isLoggable(Level.INFO)) {
                    LOGGER.info("The method is down." + ex.getMessage());
                }
            }
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            } catch (IOException ex) {
                if (LOGGER.isLoggable(Level.INFO)) {
                    LOGGER.info("The method is down." + ex.getMessage());
                }
            }
            String line = "";
            
            while (true) {
                try {
                    if (!((line = reader.readLine()) != null)) break;
                } catch (IOException ex) {
                    if (LOGGER.isLoggable(Level.INFO)) {
                        LOGGER.info("The method is down." + ex.getMessage());
                    }
                }
                System.out.println(line);
            }
    }
    
    public static void DELETE(String url,String username, String password) throws UnsupportedEncodingException{
        final HttpClient httpClient = new DefaultHttpClient();
            final HttpDelete httpDelete = new HttpDelete(url);
            setAuthorization(httpDelete,username,password);
            StringEntity input = null;
            try {
                input = new StringEntity("id");
            } catch (UnsupportedEncodingException ex) {
                if (LOGGER.isLoggable(Level.INFO)) {
                    LOGGER.info("The method is down." + ex.getMessage());
                }
            }
            HttpResponse response = null;
            
            try {
                response = httpClient.execute(httpDelete);
                StatusLine statusLine = response.getStatusLine();
                if(statusLine.getStatusCode()!=HttpStatus.SC_OK){
                    System.out.println("Statusni kod greske:"+statusLine.getStatusCode());
                    return;
                }
            } catch (IOException ex) {
                if (LOGGER.isLoggable(Level.INFO)) {
                    LOGGER.info("The method is down." + ex.getMessage());
                }
            }
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            } catch (IOException ex) {
                if (LOGGER.isLoggable(Level.INFO)) {
                    LOGGER.info("The method is down." + ex.getMessage());
                }
            }
            String line = "";
            
            while (true) {
                try {
                    if (!((line = reader.readLine()) != null)) break;
                } catch (IOException ex) {
                    if (LOGGER.isLoggable(Level.INFO)) {
                        LOGGER.info("The method is down." + ex.getMessage());
                    }
                }
                System.out.println(line);
            }
    }
}
