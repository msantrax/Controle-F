/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.virna5.contexto;

import com.virna5.edge.*;
import com.virna5.contexto.ContextUtils;
import com.virna5.contexto.DescriptorConnector;
import com.virna5.contexto.DescriptorNode;
import java.beans.IntrospectionException;
import java.io.Serializable;
import org.openide.util.Exceptions;


public class RootConnector extends DescriptorConnector implements Serializable {

    
    public RootConnector() {    
        super();
    }

    @Override
    public String toString() {
        if (node == null){
            return "ROOT:"+ this.hashCode();
        }
        else{
            return node.getDescriptor().getName();        
        }
    }

    @Override
    public RootNode getNode() {
        return (RootNode)node;
    }
 
    @Override
    public void setNode(DescriptorNode node) {
        this.node = (RootNode)node;
    }
    
    @Override
    public void initNode(){
        try {
            node = new RootNode(new RootDescriptor());
            RootDescriptor fod = (RootDescriptor)node.getDescriptor();
            fod.setUID(ContextUtils.getUID());
        } catch (IntrospectionException ex) {
            Exceptions.printStackTrace(ex);
        }
    }
    
    
}
