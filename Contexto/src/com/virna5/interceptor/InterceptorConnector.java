/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.virna5.interceptor;

import com.virna5.filewriter.*;
import com.virna5.fileobserver.*;
import com.virna5.contexto.BaseDescriptor;
import com.virna5.contexto.ContextUtils;
import com.virna5.contexto.DescriptorConnector;
import com.virna5.contexto.DescriptorNode;
import java.beans.IntrospectionException;
import java.io.Serializable;
import org.openide.util.Exceptions;


public class InterceptorConnector extends DescriptorConnector implements Serializable {

    
    public InterceptorConnector() {    
        super();
    }

    @Override
    public String toString() {
        if (node == null){
            return "IT:"+ this.hashCode();
        }
        else{
            return node.getDescriptor().getName();        
        }
    }

   
    @Override
    public InterceptorNode getNode() {
        return (InterceptorNode)node;
    }

    
    @Override
    public void setNode(DescriptorNode node) {
        this.node = (InterceptorNode)node;
    }
    
    @Override
    public void initNode(){
    
        try {
            node = new InterceptorNode(new InterceptorDescriptor());
            InterceptorDescriptor fod = (InterceptorDescriptor)node.getDescriptor();
            fod.buildDefaults();
            fod.setUID(ContextUtils.getUID());
        } catch (IntrospectionException ex) {
            Exceptions.printStackTrace(ex);
        }
       
        
    }
    
    
}
