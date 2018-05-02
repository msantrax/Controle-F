/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.virna5.contexto;

import com.virna5.contexto.RootConnector;
import com.virna5.contexto.BaseDescriptor;
import com.virna5.contexto.ContextNodes;
import com.virna5.contexto.DescriptorConnector;
import com.virna5.fileobserver.FileObserverConnector;
import com.virna5.fileobserver.FileObserverNode;
import java.beans.IntrospectionException;
import java.util.ArrayList;
import org.openide.util.Exceptions;



/**
 *
 * @author opus
 */
public class RootDescriptor extends BaseDescriptor{

    private String obs;
    
    private ContextNodes contextnodes;

    public RootDescriptor() {
        
        super();
        dependencies = new String[] {};
        this.contextnodes = new ContextNodes();
        
        name="Root";
        desc = "Root";
        
        nodetype = "contexto.RootDescriptor";
        version = "1.0.0";
       
        obs = "Observações iniciais";
         
    }

    @Override
    public RootConnector buildConnector(){
        
        RootConnector foc = new RootConnector();
        try {
            foc.setNode(new RootNode(this));
        } catch (IntrospectionException ex) {
            Exceptions.printStackTrace(ex);
        }
        return foc;
        
    }
    
    
    public void addNode(BaseDescriptor descriptor){
        contextnodes.add(descriptor);
    }
    
    public ContextNodes getContextNodes(){
        return contextnodes;
    }
   
    /**
     * @return the obs
     */
    public String getObs() {
        return obs;
    }

    /**
     * @param obs the obs to set
     */
    public void setObs(String obs) {
        this.obs = obs;
    }

    
}
