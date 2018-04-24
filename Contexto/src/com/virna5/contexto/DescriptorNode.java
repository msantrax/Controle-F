/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.virna5.contexto;

import com.virna5.fileobserver.FileObserverDescriptor;
import java.beans.IntrospectionException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.ErrorManager;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.PropertySupport;
import org.openide.nodes.Sheet;
import org.openide.util.lookup.InstanceContent;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author opus
 */
public class DescriptorNode extends AbstractNode implements ChangeListener {

    private static final Logger log = Logger.getLogger(DescriptorNode.class.getName());

    protected InstanceContent content;

    public DescriptorNode(Object bean) throws IntrospectionException {
        super(Children.LEAF, Lookups.singleton(bean));
    }

    public BaseDescriptor getDescriptor(){
        return getLookup().lookup(BaseDescriptor.class);
    }    
    
    public void setDescriptor(BaseDescriptor _bd){
        BaseDescriptor bd =  getLookup().lookup(BaseDescriptor.class);
        bd = _bd; 
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
        set.setName("Teste do property panel");

        //Property p ;
        PropertySupport.Reflection psr;
   
        //BaseDescriptor obj = (BaseDescriptor)this.getBean(); //getLookup().lookup(FileObserverDescriptor.class);
        BaseDescriptor obj = getLookup().lookup(BaseDescriptor.class);
        
        try {
            psr = new PropertySupport.Reflection<>(obj, Long.class, "getUID", null);
            psr.setName("ID no Sistema");
            psr.setShortDescription("Numero unico de identificação no sistema");
            set.put(psr);
            
            psr = new PropertySupport.Reflection<>(obj, String.class, "name");
            psr.setName("Nome do Artefato");
            psr.setShortDescription(obj.desc);
            set.put(psr);
            
            psr = new PropertySupport.Reflection<>(obj, String.class, "getCreatedTime", null);
            psr.setName("Data do Projeto");
            psr.setShortDescription("Data e hora da criação do artefato no projeto");
            set.put(psr);
            
            psr = new PropertySupport.Reflection<>(obj, String.class, "getDesigner", null);
            psr.setName("Projetista");
            psr.setShortDescription("Identificação do autor do projeto da tarefa");
            set.put(psr);
            
            psr = new PropertySupport.Reflection<>(obj, String.class, "getLoadedTime", null);
            psr.setName("Usado em m");
            psr.setShortDescription("Data e hora da ultima utilização do artefato");
            set.put(psr);
            
            psr = new PropertySupport.Reflection<>(obj, String.class, "getOwner", null);
            psr.setName("Usado por");
            psr.setShortDescription("Que utilizou o artefato por ultimo");
            set.put(psr);
            
            psr = new PropertySupport.Reflection<>(obj, String.class, "observ");
            psr.setName("Observações");
            psr.setShortDescription("Informaçoes adicionais sobre o estado e configuração do artefato");
            set.put(psr);
            
        } catch (NoSuchMethodException noSuchMethodException) {
            ErrorManager.getDefault().notify(noSuchMethodException);
        } 

        
        sheet.put(set);
        //Registre-se como ouvinte de mudanças da classmap
        //obj.addEntityChangeListener(this);
        return sheet;
    }
 
}



       //this.bd = (BaseDescriptor)bean;
//    protected BaseDescriptor bd;

    
    
//    public DescriptorNode (BaseDescriptor c, Index.ArrayChildren child, InstanceContent ct){
//        super (child, new AbstractLookup(ct));
//        ct.set(Arrays.asList(c,this), null);
//        //c.addPropertyChangeListener(WeakListeners.propertyChange(this, c));
//        this.content = ct;    
//    }
//    
//    
//    public DescriptorNode(BaseDescriptor bds){
//        super (Children.LEAF,Lookups.singleton(bds));
//        this.bd = bds;
//    }
//    
//    public DescriptorNode(Children children) {
//        super(children);
//    }
//    