/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.virna5.interceptor;

import java.util.ArrayList;
import java.util.logging.Logger;

/**
 *
 * @author opus
 */
public class AlloyFilters {

    private static final Logger LOG = Logger.getLogger(AlloyFilters.class.getName());

    
    private ArrayList<AlloyFilter> alloys;
     
    
    public AlloyFilters() {
        alloys = new ArrayList(); 
    }
    
    public AlloyFilters clone (String requested_alloy){
        
        AlloyFilters afs = new AlloyFilters();
        if (requested_alloy == null){
            afs.alloys.add(alloys.get(0).clone());
        }
        else if (requested_alloy.equals("ALL")){
            for (AlloyFilter afl : alloys){
            afs.alloys.add(afl.clone());
        }
        }
        else{
            AlloyFilter af = locateFilter(requested_alloy);
            if (af != null){
                afs.alloys.add(af.clone());
            }
        }
        return afs;
    }
    
    public void addFilter (ArrayList<ItemDescriptor> items, String tag){
        AlloyFilter af = new AlloyFilter(tag);
        af.loadItems(items);
        alloys.add(af);
    }
   
    
    public AlloyFilter locateFilter(String tag){
        
        for (AlloyFilter af : alloys) {
            if (af.getAccept_tag().equals(tag)) return af;
        }
        return null;
    }
    
     public ArrayList<String> getFilterTags(){
        
        ArrayList<String> tags = new ArrayList<>(); 
         
        for (AlloyFilter af : alloys){
            tags.add(af.getAccept_tag());
        }
        return tags;
    }
    
    public ArrayList<AlloyFilter> getFilters() { return alloys;}
    
    public int size() {return alloys.size();}
     
}
