/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.virna5.interceptor;

import com.virna5.filewriter.*;
import com.virna5.contexto.DescriptorNode;
import com.virna5.sapfilter.SAPFieldPropertyEditor;
import com.virna5.sapfilter.SAPFieldsWrapper;
import java.beans.IntrospectionException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.ChangeListener;
import org.openide.ErrorManager;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;


public class InterceptorNode  extends DescriptorNode implements ChangeListener {

    private static final Logger log = Logger.getLogger(InterceptorNode.class.getName());
    private InterceptorDescriptor lfod;
    
    public InterceptorNode(InterceptorDescriptor fod) throws IntrospectionException{
        super (fod);
        lfod = fod;
        log.setLevel(Level.FINE);
    }
    
    
    public ArrayList<ItemDescriptor> getHeader_items(){
        return lfod.getHeader_items();
    }
    
    public AlloyFilters getAlloyfilters(){
        return lfod.getAlloyfilters();
    }
    
    
    @Override
    public InterceptorDescriptor getDescriptor(){
        return getLookup().lookup(InterceptorDescriptor.class);
    }
 
    @Override
    @SuppressWarnings("unchecked")
    protected Sheet createSheet() {
        
        Sheet sheet = super.createSheet();
        Sheet.Set set = Sheet.createPropertiesSet();
        set.setName("header");
        set.setDisplayName("Geral");

        PropertySupport.Reflection psr;
   
        InterceptorDescriptor obj = lfod;
        
        try {
        
            psr = new PropertySupport.Reflection<>(obj, ArrayList.class, "getHeader_items", null);
            psr.setName("Campos do Cabeçalho");
            psr.setShortDescription("Gabarito de campos do cabeçalho da analise");
            psr.setPropertyEditorClass(HeaderFieldsPropertyEditor.class);
            set.put(psr);
                
            psr = new PropertySupport.Reflection<>(obj, AlloyFilters.class, "getAlloyfilters", null);
            psr.setName("Ligas a controlar");
            psr.setShortDescription("Gabarito de ligas a controlar");
            psr.setPropertyEditorClass(AlloyFieldsPropertyEditor.class);
            set.put(psr);
            
            
        } catch (NoSuchMethodException noSuchMethodException) {
            ErrorManager.getDefault().notify(noSuchMethodException);
        } 

        sheet.put(set);
        return sheet;
    }
        
    
}
