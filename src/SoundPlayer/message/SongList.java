/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package message;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author janat
 */
public class SongList implements Serializable {
    private List<String> list;
    public SongList(List<String>l){
        list=l;
        
    }
    public List<String> getList(){
        return list;
    }
    @Override
    public String toString() {
        return  list.toString();
    }
}
