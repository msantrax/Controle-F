/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.virna5.contexto;

import java.util.ArrayList;


public class BaseDescriptor {
    
    
    private long context;
    private Long uid;
    
    public String[] dependencies;

    private ArrayList<Long>vertex;
    
    public BaseDescriptor() {
        vertex = new ArrayList<>();
    }
 
    /**
     * @return the context
     */
    public long getContext() {
        return context;
    }

    /**
     * @param context the context to set
     */
    public void setContext(long context) {
        this.context = context;
    }

    /**
     * @return the uid
     */
    public Long getUID() {
        return uid;
    }

    /**
     * @param uid the uid to set
     */
    public void setUID(Long uid) {
        this.uid = uid;
    }

    /**
     * @return the vertex
     */
    public ArrayList<Long> getVertex() {
        return vertex;
    }

    
    
    
    
    
}
