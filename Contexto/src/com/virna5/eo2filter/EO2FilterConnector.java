/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.virna5.eo2filter;

import com.virna5.eo2filter.*;
import com.virna5.contexto.ContextUtils;
import com.virna5.contexto.DescriptorConnector;
import com.virna5.contexto.DescriptorNode;
import java.beans.IntrospectionException;
import java.io.Serializable;
import org.openide.util.Exceptions;


public class EO2FilterConnector extends DescriptorConnector implements Serializable {

    
    public EO2FilterConnector() {    
        super();
    }

    @Override
    public String toString() { 
        if (node == null){
            return "EO2F:"+ this.hashCode();
        }
        else{
            return node.getDescriptor().getName();        
        }
    }

   
    @Override
    public EO2FilterNode getNode() {
        return (EO2FilterNode)node;
    }

    
    @Override
    public void setNode(DescriptorNode node) {
        this.node = (EO2FilterNode)node;
    }
    
    @Override
    public void initNode(){
    
        try {
            node = new EO2FilterNode(new EO2FilterDescriptor());
            EO2FilterDescriptor fod = (EO2FilterDescriptor)node.getDescriptor();
            fod.setUID(ContextUtils.getUID());
        } catch (IntrospectionException ex) {
            Exceptions.printStackTrace(ex);
        }
       
        
    }
    
    
}
