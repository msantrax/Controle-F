/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.virna5.fileobserver;

import com.virna5.contexto.BaseDescriptor;
import com.virna5.contexto.ContextUtils;
import com.virna5.contexto.DescriptorConnector;
import com.virna5.contexto.DescriptorNode;
import java.beans.IntrospectionException;
import java.io.Serializable;
import org.openide.util.Exceptions;


public class FileObserverConnector extends DescriptorConnector implements Serializable {

    
    public FileObserverConnector() {    
        super();
    }

    @Override
    public String toString() {
        if (node == null){
            return "FOD:"+ this.hashCode();
        }
        else{
            return node.getDescriptor().getName();        
        }
    }

   
    @Override
    public FileObserverNode getNode() {
        return (FileObserverNode)node;
    }

    
    @Override
    public void setNode(DescriptorNode node) {
        this.node = (FileObserverNode)node;
    }
    
    @Override
    public void initNode(){
    
        try {
            node = new FileObserverNode(new FileObserverDescriptor());
            FileObserverDescriptor fod = (FileObserverDescriptor)node.getDescriptor();
            fod.setUID(ContextUtils.getUID());
        } catch (IntrospectionException ex) {
            Exceptions.printStackTrace(ex);
        }
       
        
    }
    
    
}
