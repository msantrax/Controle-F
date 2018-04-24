/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.virna5.qs4generator;

import com.virna5.contexto.ContextUtils;
import com.virna5.contexto.DescriptorConnector;
import com.virna5.contexto.DescriptorNode;
import java.beans.IntrospectionException;
import java.io.Serializable;
import org.openide.util.Exceptions;


public class QS4GeneratorConnector extends DescriptorConnector implements Serializable {

    
    public QS4GeneratorConnector() {    
        super();
    }

    @Override
    public String toString() {
        if (node == null){
            return "QS4:"+ this.hashCode();
        }
        else{
            return node.getDescriptor().getName();        
        }
    }

   
    @Override
    public QS4GeneratorNode getNode() {
        return (QS4GeneratorNode)node;
    }

    
    @Override
    public void setNode(DescriptorNode node) {
        this.node = (QS4GeneratorNode)node;
    }
    
    @Override
    public void initNode(){
    
        try {
            node = new QS4GeneratorNode(new QS4GeneratorDescriptor());
            QS4GeneratorDescriptor fod = (QS4GeneratorDescriptor)node.getDescriptor();
            fod.setUID(ContextUtils.getUID());
        } catch (IntrospectionException ex) {
            Exceptions.printStackTrace(ex);
        }
       
        
    }
    
    
}
