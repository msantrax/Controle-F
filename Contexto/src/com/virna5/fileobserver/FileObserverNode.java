/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.virna5.fileobserver;

import com.virna5.contexto.BaseDescriptor;
import com.virna5.contexto.DescriptorNode;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.ErrorManager;
import org.openide.nodes.Children;
import org.openide.nodes.Index;
import org.openide.nodes.Node.Property;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

/**
 *
 * @author opus
 */
public class FileObserverNode  extends DescriptorNode implements ChangeListener {

    private static final Logger log = Logger.getLogger(FileObserverNode.class.getName());
   
    
    
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
    
    
    public FileObserverNode(){
        super (new FileObserverDescriptor());     
        log.setLevel(Level.FINE);
    }
    
    public FileObserverNode(Children children) {
        super(children);
    }
    
    
    @Override
    public void updateProp(){
        log.info("updating properties");
        this.firePropertySetsChange(null, this.getPropertySets()); 
        
    }
    
    
    @Override
    @SuppressWarnings("unchecked")
    public void stateChanged(ChangeEvent e) {
//        Set newcontent = new HashSet<Classmap>();
//        @SuppressWarnings("unchecked")
//        boolean add = newcontent.add(getClassmap());
//        content.set(newcontent, null);
    }

    
    
    
    
    @Override
    @SuppressWarnings("unchecked")
    protected Sheet createSheet() {

        //Sheet sheet = Sheet.createDefault();
        Sheet sheet = super.createSheet();
        Sheet.Set set = Sheet.createPropertiesSet();
        set.setName("Classe");

        Property p ;
        PropertySupport.Reflection psr;
      
        //FileObserverDescriptor obj = (FileObserverDescriptor)bd; //getLookup().lookup(FileObserverDescriptor.class);
        FileObserverDescriptor obj = getLookup().lookup(FileObserverDescriptor.class);
        
        try {
            psr = new PropertySupport.Reflection<String>(obj, String.class, "inputfile_path");
            
        } catch (NoSuchMethodException noSuchMethodException) {
            ErrorManager.getDefault().notify(noSuchMethodException);
        }

        sheet.put(set);
        //Registre-se como ouvinte de mudanças da classmap
        //obj.addEntityChangeListener(this);
        return sheet;
    }
    
    
    
    
}
