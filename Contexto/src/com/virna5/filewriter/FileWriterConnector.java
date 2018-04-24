/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.virna5.filewriter;

import com.virna5.fileobserver.*;
import com.virna5.contexto.BaseDescriptor;
import com.virna5.contexto.ContextUtils;
import com.virna5.contexto.DescriptorConnector;
import com.virna5.contexto.DescriptorNode;
import java.beans.IntrospectionException;
import java.io.Serializable;
import org.openide.util.Exceptions;


public class FileWriterConnector extends DescriptorConnector implements Serializable {

    
    public FileWriterConnector() {    
        super();
    }

    @Override
    public String toString() {
        if (node == null){
            return "FW:"+ this.hashCode();
        }
        else{
            return node.getDescriptor().getName();        
        }
    }

   
    @Override
    public FileWriterNode getNode() {
        return (FileWriterNode)node;
    }

    
    @Override
    public void setNode(DescriptorNode node) {
        this.node = (FileWriterNode)node;
    }
    
    @Override
    public void initNode(){
    
        try {
            node = new FileWriterNode(new FileWriterDescriptor());
            FileWriterDescriptor fod = (FileWriterDescriptor)node.getDescriptor();
            fod.setUID(ContextUtils.getUID());
        } catch (IntrospectionException ex) {
            Exceptions.printStackTrace(ex);
        }
       
        
    }
    
    
}
