/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.virna5.csvfilter;

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


public class CSVFilterNode  extends DescriptorNode implements ChangeListener {

    private static final Logger log = Logger.getLogger(CSVFilterNode.class.getName());
 
    protected CSVFieldsWrapper cfw;
    
    public CSVFilterNode(CSVFilterDescriptor fod) throws IntrospectionException{
        super (fod);
        cfw = fod.getCsvfields();
        log.setLevel(Level.FINE);
    }
    
    
    @Override
    public CSVFilterDescriptor getDescriptor(){
        return getLookup().lookup(CSVFilterDescriptor.class);
    }
 
    @Override
    @SuppressWarnings("unchecked")
    protected Sheet createSheet() {
        
        Sheet sheet = super.createSheet();
        Sheet.Set set = Sheet.createPropertiesSet();
        set.setName("header");
        set.setDisplayName("Geral");

        PropertySupport.Reflection psr;
   
        //BaseDescriptor obj = (BaseDescriptor)this.getBean(); //getLookup().lookup(CSVFilterDescriptor.class);
        CSVFilterDescriptor obj = getLookup().lookup(CSVFilterDescriptor.class);
        
        try {
        
            psr = new PropertySupport.Reflection<>(obj, CSVFieldsWrapper.class, "getCsvfields", null);
            psr.setName("Campos de dados");
            psr.setShortDescription("Gabarito de translação da estrutura de dados");
            psr.setPropertyEditorClass(CSVFieldPropertyEditor.class);
            set.put(psr);
            
            psr = new PropertySupport.Reflection<>(obj, String.class, "locale");
            psr.setName("Linguagem");
            psr.setShortDescription("Configuração do país (separador decimal, formato de data, etc) utilizado no arquivo");
            set.put(psr);
            
            
            psr = new PropertySupport.Reflection<>(obj, Boolean.class, "isUseheader", "setUseheader");
            psr.setName("Cabeçalho presente");
            psr.setShortDescription("Assumir que o cabeçalho com a identificação dos campos está presente");
            set.put(psr);
          
            psr = new PropertySupport.Reflection<>(obj, String.class, "separator");
            psr.setName("Separador de Campos");
            psr.setShortDescription("Caracter utilizado para a separação dos campos (usualmente , ou ;");
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
    public CSVFieldsWrapper getCfw() {
        return cfw;
    }
    
    
    
    
}




//    public FileObserverNode(Children children) {
//        //super(children);
//    }
    
    
    
     
    
    
//    public FileObserverNode(BaseDescriptor c, Index.ArrayChildren child){
//        this (c, child, new InstanceContent());
//        log.setLevel(Level.ALL);
//    }
//
//    private FileObserverNode (BaseDescriptor c, Index.ArrayChildren child, InstanceContent ct){
//        super (child, new AbstractLookup(ct));
//        ct.set(Arrays.asList(c,this), null);
//        //c.addPropertyChangeListener(WeakListeners.propertyChange(this, c));
//        this.content = ct;
//        
//    }
     

//
//    @Override
//    public void updateProp(){
//        log.info("updating properties");
//        this.firePropertySetsChange(null, this.getPropertySets()); 
//        
//    }
//    
//    
//    @Override
//    @SuppressWarnings("unchecked")
//    public void stateChanged(ChangeEvent e) {
////        Set newcontent = new HashSet<Classmap>();
////        @SuppressWarnings("unchecked")
////        boolean add = newcontent.add(getClassmap());
////        content.set(newcontent, null);
//    }
//
//    
    
    
    