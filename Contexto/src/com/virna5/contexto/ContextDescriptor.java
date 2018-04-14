/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.virna5.contexto;

import java.util.logging.Logger;

/**
 *
 * @author opus
 */
public class ContextDescriptor {

    private static final Logger log = Logger.getLogger(ContextDescriptor.class.getName());

    private String nodetype = "ContextDescriptor";
    private long context;
    private long uid;
    
    private ContextNodes contextnodes;
    
    
    public ContextDescriptor() {
        this.contextnodes = new ContextNodes();
    }
    
    public void addNode(BaseDescriptor descriptor){
        contextnodes.add(descriptor);
    }
    
    public ContextNodes getContextNodes(){
        return contextnodes;
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
    public long getUID() {
        return uid;
    }

    /**
     * @param uid the uid to set
     */
    public void setUID(long uid) {
        this.uid = uid;
    }
    
    
}
