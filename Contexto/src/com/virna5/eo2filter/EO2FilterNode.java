/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.virna5.eo2filter;

import com.virna5.eo2filter.*;
import com.virna5.sapfilter.*;
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


public class EO2FilterNode  extends DescriptorNode implements ChangeListener {

    private static final Logger log = Logger.getLogger(EO2FilterNode.class.getName());
 
    protected EO2FieldsWrapper cfw;
    
    public EO2FilterNode(EO2FilterDescriptor fod) throws IntrospectionException{
        super (fod);
        cfw = fod.getEO2fields();
        log.setLevel(Level.FINE);
    }
    
    
    @Override
    public EO2FilterDescriptor getDescriptor(){
        return getLookup().lookup(EO2FilterDescriptor.class);
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
        EO2FilterDescriptor obj = getLookup().lookup(EO2FilterDescriptor.class);
        
        try {
            
            psr = new PropertySupport.Reflection<>(obj, EO2FieldsWrapper.class, "getEO2fields", null);
            psr.setName("Campos de dados");
            psr.setShortDescription("Gabarito de translação da estrutura de dados");
            psr.setPropertyEditorClass(EO2FieldPropertyEditor.class);
            set.put(psr);
            
            psr = new PropertySupport.Reflection<>(obj, String.class, "cardname");
            psr.setName("Nome do Filtro");
            psr.setShortDescription("Identificação do filtro no aplicativo do instrumento (EO2 Limit Card)");
            set.put(psr);
            
            psr = new PropertySupport.Reflection<>(obj, String.class, "groupname");
            psr.setName("Nome do Grupo");
            psr.setShortDescription("Grupo da Liga no aplicativo do instrumento (EO2 Limit Group)");
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
    public EO2FieldsWrapper getCfw() {
        return cfw;
    }
 
    
}

