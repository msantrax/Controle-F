/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.virna5.filewriter;

import com.virna5.contexto.DescriptorNode;
import java.beans.IntrospectionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.ChangeListener;
import org.openide.ErrorManager;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;


public class FileWriterNode  extends DescriptorNode implements ChangeListener {

    private static final Logger log = Logger.getLogger(FileWriterNode.class.getName());
    private FileWriterDescriptor lfod;
    
    public FileWriterNode(FileWriterDescriptor fod) throws IntrospectionException{
        super (fod);
        lfod = fod;
        log.setLevel(Level.FINE);
    }
    
    
    @Override
    public FileWriterDescriptor getDescriptor(){
        return getLookup().lookup(FileWriterDescriptor.class);
    }
 
    @Override
    @SuppressWarnings("unchecked")
    protected Sheet createSheet() {
        
        Sheet sheet = super.createSheet();
        Sheet.Set set = Sheet.createPropertiesSet();
        set.setName("header");
        set.setDisplayName("Geral");

        PropertySupport.Reflection psr;
   
        //BaseDescriptor obj = (BaseDescriptor)this.getBean(); //getLookup().lookup(FileWriterDescriptor.class);
        //FileWriterDescriptor obj = getLookup().lookup(FileWriterDescriptor.class);
        FileWriterDescriptor obj = lfod;
        
        try {
        
            psr = new PropertySupport.Reflection<>(obj, String.class, "outputfile");
            psr.setName("Arquivo a gravar");
            psr.setShortDescription("Caminho para o arquivo a gravar");
            set.put(psr);
          
            psr = new PropertySupport.Reflection<>(obj, Boolean.class, "append");
            psr.setName("Adicionar ao arquivo");
            psr.setShortDescription("Caso a opção esteja marcada, os novos dados deverão ser adicionados ao arquivo"
                    + " caso não, o comportamante dependerá da opção sobrescrever abaixo");
            set.put(psr);
            
            psr = new PropertySupport.Reflection<>(obj, Boolean.class, "overwrite");
            psr.setName("Sobrescrever Arquivo");
            psr.setShortDescription("Indica que, caso o arquivo já exista, os dados deverão ser escritos sobre"
                    + " os dados antigos. Caso a opção não esteja marcada, os novos dados irão gerar um novo arquivo"
                    + " com o nome adicionado do sufixo _XXX, onde XXX é uma sequencia.");
            set.put(psr);
            
        } catch (NoSuchMethodException noSuchMethodException) {
            ErrorManager.getDefault().notify(noSuchMethodException);
        } 

        sheet.put(set);
        return sheet;
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
    
    
    