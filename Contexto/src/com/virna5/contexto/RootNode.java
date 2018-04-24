/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.virna5.contexto;

import com.virna5.contexto.RootDescriptor;
import com.virna5.contexto.DescriptorNode;
import java.beans.IntrospectionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.ChangeListener;
import org.openide.ErrorManager;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;


public class RootNode  extends DescriptorNode implements ChangeListener {

    private static final Logger log = Logger.getLogger(RootNode.class.getName());
 
    
    public RootNode(RootDescriptor fod) throws IntrospectionException{
        super (fod);     
        log.setLevel(Level.FINE);
    }
    
    
    @Override
    public RootDescriptor getDescriptor(){
        return getLookup().lookup(RootDescriptor.class);
    }
 
    @Override
    @SuppressWarnings("unchecked")
    protected Sheet createSheet() {
        
        Sheet sheet = super.createSheet();
        Sheet.Set set = Sheet.createPropertiesSet();
        set.setName("header");
        set.setDisplayName("Geral");

        PropertySupport.Reflection psr;
   
        //BaseDescriptor obj = (BaseDescriptor)this.getBean(); //getLookup().lookup(RootDescriptor.class);
        RootDescriptor obj = getLookup().lookup(RootDescriptor.class);
        
        try {        
            psr = new PropertySupport.Reflection<>(obj, String.class, "obs");
            psr.setName("Observações Gerais");
            set.put(psr);
  
        } catch (NoSuchMethodException noSuchMethodException) {
            ErrorManager.getDefault().notify(noSuchMethodException);
        } 

        sheet.put(set);
        return sheet;
    }
   
}
