/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.virna5.interceptor;

import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author opus
 */
public class AlloyFilter {
    
    private String accept_tag;
    
    private String sendto;
    private String sendtag;
    
    private ArrayList<ItemDescriptor> items;
    
    public AlloyFilter() {
        this.sendto = "BROADCAST";
        this.sendtag = "INTERCEPTED";
        this.accept_tag = "default";
        items = new ArrayList<>();
    }

    public AlloyFilter(String tag) {
        this.sendto = "BROADCAST";
        this.sendtag = "INTERCEPTED";
        this.accept_tag = tag;
        items = new ArrayList<>();
    }

    @Override
    public AlloyFilter clone(){
        
        AlloyFilter af = new AlloyFilter();
       af.sendto = this.sendto;
       af.sendtag = this.sendtag;
       af.accept_tag = this.accept_tag;
 
        for (ItemDescriptor id : items){
            af.items.add(id.clone());
        }
        
        return af;
    }
    
    
    /**
     * @return the accept_tag
     */
    public String getAccept_tag() {
        return accept_tag;
    }

    /**
     * @param accept_tag the accept_tag to set
     */
    public void setAccept_tag(String accept_tag) {
        this.accept_tag = accept_tag;
    }

    /**
     * @return the items
     */
    public ArrayList<ItemDescriptor> getItems() {
        return items;
    }
    
    public void loadItems (ArrayList<ItemDescriptor> _items){
        items = _items;
    }
    
}
