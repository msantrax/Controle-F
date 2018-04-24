/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.virna5.edge;

import com.virna5.contexto.DescriptorNode;
import java.beans.IntrospectionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.ChangeListener;
import org.openide.ErrorManager;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;


public class EdgeNode  extends DescriptorNode implements ChangeListener {

    private static final Logger log = Logger.getLogger(EdgeNode.class.getName());
 
    
    public EdgeNode(EdgeDescriptor fod) throws IntrospectionException{
        super (fod);     
        log.setLevel(Level.FINE);
    }
    
    
    @Override
    public EdgeDescriptor getDescriptor(){
        return getLookup().lookup(EdgeDescriptor.class);
    }
 
    @Override
    @SuppressWarnings("unchecked")
    protected Sheet createSheet() {
        
        Sheet sheet = super.createSheet();
        Sheet.Set set = Sheet.createPropertiesSet();
        set.setName("header");
        set.setDisplayName("Geral");

        PropertySupport.Reflection psr;
   
        //BaseDescriptor obj = (BaseDescriptor)this.getBean(); //getLookup().lookup(EdgeDescriptor.class);
        EdgeDescriptor obj = getLookup().lookup(EdgeDescriptor.class);
        
//        try {
//        
//            psr = new PropertySupport.Reflection<>(obj, String.class, "inputfile_path");
//            psr.setName("Arquivo observado");
//            set.put(psr);
//          
//            
//            
//        } catch (NoSuchMethodException noSuchMethodException) {
//            ErrorManager.getDefault().notify(noSuchMethodException);
//        } 

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
    
    
    