/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.virna5.edge;

import com.virna5.contexto.ContextUtils;
import com.virna5.contexto.DescriptorConnector;
import com.virna5.contexto.DescriptorNode;
import java.beans.IntrospectionException;
import java.io.Serializable;
import org.openide.util.Exceptions;


public class EdgeConnector extends DescriptorConnector implements Serializable {

    
    public EdgeConnector() {    
        super();
    }

    @Override
    public String toString() {
        if (node == null){
            return "CON:"+ this.hashCode();
        }
        else{
            return node.getDescriptor().getName();        
        }
    }

   
    @Override
    public EdgeNode getNode() {
        return (EdgeNode)node;
    }

    
    @Override
    public void setNode(DescriptorNode node) {
        this.node = (EdgeNode)node;
    }
    
    @Override
    public void initNode(){
    
        try {
            node = new EdgeNode(new EdgeDescriptor());
            EdgeDescriptor fod = (EdgeDescriptor)node.getDescriptor();
            fod.setUID(ContextUtils.getUID());
        } catch (IntrospectionException ex) {
            Exceptions.printStackTrace(ex);
        }
       
        
    }
    
    
}
