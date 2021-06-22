/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.virna5.qeffilter;

import com.virna5.contexto.ContextUtils;
import com.virna5.contexto.DescriptorConnector;
import com.virna5.contexto.DescriptorNode;
import java.beans.IntrospectionException;
import java.io.Serializable;
import org.openide.util.Exceptions;


public class QEFFilterConnector extends DescriptorConnector implements Serializable {

    
    public QEFFilterConnector() {    
        super();
    }

    @Override
    public String toString() { 
        if (node == null){
            return "QEFF:"+ this.hashCode();
        }
        else{
            return node.getDescriptor().getName();        
        }
    }

   
    @Override
    public QEFFilterNode getNode() {
        return (QEFFilterNode)node;
    }

    
    @Override
    public void setNode(DescriptorNode node) {
        this.node = (QEFFilterNode)node;
    }
    
    @Override
    public void initNode(){
    
        try {
            node = new QEFFilterNode(new QEFFilterDescriptor());
            QEFFilterDescriptor fod = (QEFFilterDescriptor)node.getDescriptor();
            fod.setUID(ContextUtils.getUID());
        } catch (IntrospectionException ex) {
            Exceptions.printStackTrace(ex);
        }
       
        
    }
    
    
}
