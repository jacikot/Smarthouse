/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alarm;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author janat
 */
public class MyYoutube {
    
    private static final String APIKEY="AIzaSyBAlUQcB6uOW4sOTPobY-3nB-IA00kFrsA";
    
    public YouTube getService() throws GeneralSecurityException, IOException {
        final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        return new YouTube.Builder(httpTransport, JacksonFactory.getDefaultInstance(), null)
            .setApplicationName("ReprodukcijaZvuka")
            .build();
    }
    
    public String findURL(String songName) throws GeneralSecurityException, IOException{
         YouTube youtubeService = getService();
       
        List<String>list=new ArrayList<String>();
        list.add("snippet");
        YouTube.Search.List request = youtubeService.search()
            .list(list);
        List<String>type=new ArrayList<String>();
        type.add("video");
        
        SearchListResponse response = request.setKey(APIKEY).setType(type).setQ(songName).execute();
        List<SearchResult> result=response.getItems();
        String videoId=result.get(0).getId().getVideoId();
        return "https://www.youtube.com/watch?v="+videoId;
    }
   public String ytAPI(String songName) throws GeneralSecurityException, IOException{
        
        String path=findURL(songName);
         if(Desktop.isDesktopSupported()){
            try {
                final URI uri=new URI(path);
                Desktop.getDesktop().browse(uri);
                
            } catch (IOException ex) {
                Logger.getLogger(AlarmDevice.class.getName()).log(Level.SEVERE, null, ex);
            } catch (URISyntaxException ex) {
                Logger.getLogger(MyYoutube.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
         return path;
    }
}
