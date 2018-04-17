/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.virna5.contexto;

import java.io.Serializable;

/**
 *
 * @author opus
 */
public class DescriptorConnector implements Serializable{
    
    protected transient BaseDescriptor descriptor;
    protected transient DescriptorNode node;
    
    private long id;

    public DescriptorConnector() {
        this.id = 0l;
    }

    /**
     * @return the descriptor
     */
    public BaseDescriptor getDescriptor() {
        return descriptor;
    }

    /**
     * @param descriptor the descriptor to set
     */
    public void setDescriptor(BaseDescriptor descriptor) {
        this.descriptor = descriptor;
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
        this.descriptor = new BaseDescriptor();
        this.node = new DescriptorNode( this.descriptor);
        
    }
    
}
