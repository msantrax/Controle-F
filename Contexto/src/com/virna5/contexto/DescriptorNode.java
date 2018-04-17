/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.virna5.contexto;

import com.virna5.fileobserver.FileObserverDescriptor;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.ErrorManager;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Index;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author opus
 */
public class DescriptorNode extends AbstractNode implements ChangeListener {

    private static final Logger log = Logger.getLogger(DescriptorNode.class.getName());

    protected BaseDescriptor bd;
    protected InstanceContent content;
    
    
    public DescriptorNode (BaseDescriptor c, Index.ArrayChildren child, InstanceContent ct){
        super (child, new AbstractLookup(ct));
        ct.set(Arrays.asList(c,this), null);
        //c.addPropertyChangeListener(WeakListeners.propertyChange(this, c));
        this.content = ct;    
    }
    
    
    public DescriptorNode(BaseDescriptor bds){
        super (Children.LEAF,Lookups.singleton(bds));
        this.bd = bds;
    }
    
    public DescriptorNode(Children children) {
        super(children);
    }
    
    
    public void updateProp(){
        
        this.firePropertySetsChange(null, this.getPropertySets()); 
        log.info("updating properties");
    }
    
    
    
    @Override
    @SuppressWarnings("unchecked")
    public void stateChanged(ChangeEvent e) {
        Set newcontent = new HashSet<BaseDescriptor>();
        @SuppressWarnings("unchecked")
        boolean add = newcontent.add(new BaseDescriptor());
        content.set(newcontent, null);
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
        BaseDescriptor obj = getLookup().lookup(BaseDescriptor.class);
        
        try {
            psr = new PropertySupport.Reflection<Long>(obj, Long.class, "UID");
            
        } catch (NoSuchMethodException noSuchMethodException) {
            ErrorManager.getDefault().notify(noSuchMethodException);
        }

        sheet.put(set);
        //Registre-se como ouvinte de mudanças da classmap
        //obj.addEntityChangeListener(this);
        return sheet;
    }
    
    
    
    
}
