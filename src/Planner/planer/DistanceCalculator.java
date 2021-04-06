/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planer;

import com.google.maps.DistanceMatrixApi;
import com.google.maps.DistanceMatrixApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.TravelMode;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author janat
 */
public class DistanceCalculator {
    private static final String APIKEY="AIzaSyBAlUQcB6uOW4sOTPobY-3nB-IA00kFrsA";
    private GeoApiContext gcontext;
    
    public DistanceCalculator(){
        gcontext= new GeoApiContext.Builder().apiKey(APIKEY).build();
        System.out.println("geokontekst napravljen");
    }
    public long calculateDistance(String placeA, String placeB){
        try {
            DistanceMatrixApiRequest request = DistanceMatrixApi.newRequest(gcontext);
            DistanceMatrix result = request.origins(placeA)
                    .destinations(placeB)
                    .mode(TravelMode.DRIVING)
                    .await();
            //System.out.println("zahtev poslat");
    
            //System.out.println(result);
            System.out.println(result.rows[0].elements[0].distance.inMeters);
            return result.rows[0].elements[0].distance.inMeters;
        } catch (ApiException ex) {
            Logger.getLogger(DistanceCalculator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(DistanceCalculator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DistanceCalculator.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }
    public long calculateTime(String placeA, String placeB ){
        try {
            DistanceMatrixApiRequest request = DistanceMatrixApi.newRequest(gcontext);
            DistanceMatrix result = request.origins(placeA)
                    .destinations(placeB)
                    .mode(TravelMode.DRIVING)
                    .await();
            //System.out.println("zahtev poslat");
    
            //System.out.println(result);
            System.out.println(result.rows[0].elements[0].duration.inSeconds);
            return result.rows[0].elements[0].duration.inSeconds;
        } catch (ApiException ex) {
            Logger.getLogger(DistanceCalculator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(DistanceCalculator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DistanceCalculator.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }
}
