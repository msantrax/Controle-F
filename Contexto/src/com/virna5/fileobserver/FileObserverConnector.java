/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.virna5.fileobserver;

import com.virna5.contexto.BaseDescriptor;
import com.virna5.contexto.DescriptorConnector;
import com.virna5.contexto.DescriptorNode;
import java.io.Serializable;


public class FileObserverConnector extends DescriptorConnector implements Serializable {

    
    public FileObserverConnector() {    
        super();
    }

    @Override
    public String toString() {
        
        return "FileObserver " + this.hashCode();
        
    }

    /**
     * @return the node
     */
    @Override
    public FileObserverNode getNode() {
        return (FileObserverNode)node;
    }

    /**
     * @param node the node to set
     */
    @Override
    public void setNode(DescriptorNode node) {
        this.node = (FileObserverNode)node;
    }
    
    public void initNode(){
        //descriptor = new FileObserverDescriptor();
        node = new FileObserverNode();
        
    }
    
    
}
