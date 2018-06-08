/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.virna5.sapfilter;

import com.virna5.csvfilter.*;
import com.virna5.fileobserver.*;
import com.virna5.contexto.BaseDescriptor;
import com.virna5.contexto.ContextUtils;
import com.virna5.contexto.DescriptorConnector;
import com.virna5.contexto.DescriptorNode;
import java.beans.IntrospectionException;
import java.io.Serializable;
import org.openide.util.Exceptions;


public class SAPFilterConnector extends DescriptorConnector implements Serializable {

    
    public SAPFilterConnector() {    
        super();
    }

    @Override
    public String toString() { 
        if (node == null){
            return "CSVF:"+ this.hashCode();
        }
        else{
            return node.getDescriptor().getName();        
        }
    }

   
    @Override
    public CSVFilterNode getNode() {
        return (CSVFilterNode)node;
    }

    
    @Override
    public void setNode(DescriptorNode node) {
        this.node = (CSVFilterNode)node;
    }
    
    @Override
    public void initNode(){
    
        try {
            node = new CSVFilterNode(new CSVFilterDescriptor());
            CSVFilterDescriptor fod = (CSVFilterDescriptor)node.getDescriptor();
            fod.setUID(ContextUtils.getUID());
        } catch (IntrospectionException ex) {
            Exceptions.printStackTrace(ex);
        }
       
        
    }
    
    
}
