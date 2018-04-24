/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.virna5.contexto;

import java.beans.IntrospectionException;
import java.io.Serializable;
import org.openide.util.Exceptions;

/**
 *
 * @author opus
 */
public class DescriptorConnector implements Serializable{
    
    protected transient DescriptorNode node;   
    private long id;
    

    public DescriptorConnector() {
        this.id = 0l;
    }

    /**
     * @return the descriptor
     */
    public BaseDescriptor getDescriptor() {
        return node.getDescriptor();
    }

    /**
     * @param descriptor the descriptor to set
     */
    public void setDescriptor(BaseDescriptor descriptor) {
        node.setDescriptor(descriptor);
    }

    /**
     * @return the id
     */
    public long getID() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setID(long id) {
        this.id = id;
    }

    /**
     * @return the node
     */
    public DescriptorNode getNode() {
        return node;
    }

    /**
     * @param node the node to set
     */
    public void setNode(DescriptorNode node) {
        this.node = node;
    }
    
    
    public void initNode(){
        
//        try {
//            this.node = new DescriptorNode(bd);
//        } catch (IntrospectionException ex) {
//            Exceptions.printStackTrace(ex);
//        }
        
    }
    
}
