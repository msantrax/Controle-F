/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.virna5.qs4generator;

import com.virna5.contexto.DescriptorNode;
import java.beans.IntrospectionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.ChangeListener;
import org.openide.ErrorManager;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;


public class QS4GeneratorNode  extends DescriptorNode implements ChangeListener {

    private static final Logger log = Logger.getLogger(QS4GeneratorNode.class.getName());
 
    protected QS4GeneratorFieldsWrapper cfw;
    
    public QS4GeneratorNode(QS4GeneratorDescriptor fod) throws IntrospectionException{
        super (fod);
        cfw = fod.getGeneratorfields();
        log.setLevel(Level.FINE);
    }
    
    
    @Override
    public QS4GeneratorDescriptor getDescriptor(){
        return getLookup().lookup(QS4GeneratorDescriptor.class);
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
        QS4GeneratorDescriptor obj = getLookup().lookup(QS4GeneratorDescriptor.class);
        
        try {
        
            psr = new PropertySupport.Reflection<>(obj, QS4GeneratorFieldsWrapper.class, "getGeneratorfields", null);
            psr.setName("Campos de dados");
            psr.setShortDescription("Gabarito de translação da estrutura de dados");
            psr.setPropertyEditorClass(QS4GeneratorPropertyEditor.class);
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

    public QS4GeneratorFieldsWrapper getCfw() {
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
    
    
    