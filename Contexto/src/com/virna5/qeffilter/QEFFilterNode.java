/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.virna5.qeffilter;

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


public class QEFFilterNode  extends DescriptorNode implements ChangeListener {

    private static final Logger log = Logger.getLogger(QEFFilterNode.class.getName());
 
    protected QEFFieldsWrapper cfw;
    
    public QEFFilterNode(QEFFilterDescriptor fod) throws IntrospectionException{
        super (fod);
        cfw = fod.getQEFfields();
        log.setLevel(Level.FINE);
    }
    
    
    @Override
    public QEFFilterDescriptor getDescriptor(){
        return getLookup().lookup(QEFFilterDescriptor.class);
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
        QEFFilterDescriptor obj = getLookup().lookup(QEFFilterDescriptor.class);
        
        try {
            
            psr = new PropertySupport.Reflection<>(obj, QEFFieldsWrapper.class, "getQEFfields", null);
            psr.setName("Campos de dados");
            psr.setShortDescription("Gabarito de translação da estrutura de dados");
            psr.setPropertyEditorClass(QEFFieldPropertyEditor.class);
            set.put(psr);
            
            psr = new PropertySupport.Reflection<>(obj, String.class, "cardname");
            psr.setName("Nome do Filtro");
            psr.setShortDescription("Identificação do filtro no aplicativo do instrumento (QEF Limit Card)");
            set.put(psr);
            
            psr = new PropertySupport.Reflection<>(obj, String.class, "groupname");
            psr.setName("Nome do Grupo");
            psr.setShortDescription("Grupo da Liga no aplicativo do instrumento (QEF Limit Group)");
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
    public QEFFieldsWrapper getCfw() {
        return cfw;
    }
 
    
}

