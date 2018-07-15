/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.virna5.sapfilter;

import com.virna5.csvfilter.*;
import com.virna5.fileobserver.*;
import com.virna5.contexto.BaseDescriptor;
import com.virna5.contexto.DescriptorNode;
import java.beans.IntrospectionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.ChangeListener;
import org.openide.ErrorManager;
import org.openide.nodes.Node.Property;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;


public class SAPFilterNode  extends DescriptorNode implements ChangeListener {

    private static final Logger log = Logger.getLogger(SAPFilterNode.class.getName());
 
    protected SAPFieldsWrapper cfw;
    
    public SAPFilterNode(SAPFilterDescriptor fod) throws IntrospectionException{
        super (fod);
        cfw = fod.getSapfields();
        log.setLevel(Level.FINE);
    }
    
    
    @Override
    public SAPFilterDescriptor getDescriptor(){
        return getLookup().lookup(SAPFilterDescriptor.class);
    }
 
    @Override
    @SuppressWarnings("unchecked")
    protected Sheet createSheet() {
        
        Sheet sheet = super.createSheet();
        Sheet.Set set = Sheet.createPropertiesSet();
        set.setName("header");
        set.setDisplayName("Geral");

        PropertySupport.Reflection psr;
   
        //BaseDescriptor obj = (BaseDescriptor)this.getBean(); //getLookup().lookup(SAPFilterDescriptor.class);
        SAPFilterDescriptor obj = getLookup().lookup(SAPFilterDescriptor.class);
        
        try {
            
            psr = new PropertySupport.Reflection<>(obj, SAPFieldsWrapper.class, "getSapfields", null);
            psr.setName("Campos de dados");
            psr.setShortDescription("Gabarito de translação da estrutura de dados");
            psr.setPropertyEditorClass(SAPFieldPropertyEditor.class);
            set.put(psr);
            
            
        } catch (NoSuchMethodException noSuchMethodException) {
            ErrorManager.getDefault().notify(noSuchMethodException);
        } 

        sheet.put(set);
        return sheet;
    }

    /**
     * @return the cfw
     */
    public SAPFieldsWrapper getCfw() {
        return cfw;
    }
 
    
}

