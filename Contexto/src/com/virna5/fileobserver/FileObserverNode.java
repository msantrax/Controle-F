/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.virna5.fileobserver;

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


public class FileObserverNode  extends DescriptorNode implements ChangeListener {

    private static final Logger log = Logger.getLogger(FileObserverNode.class.getName());
    private FileObserverDescriptor lfod;
    
    public FileObserverNode(FileObserverDescriptor fod) throws IntrospectionException{
        super (fod);
        lfod = fod;
        log.setLevel(Level.FINE);
    }
    
    
    @Override
    public FileObserverDescriptor getDescriptor(){
        return getLookup().lookup(FileObserverDescriptor.class);
    }
 
    @Override
    @SuppressWarnings("unchecked")
    protected Sheet createSheet() {
        
        Sheet sheet = super.createSheet();
        Sheet.Set set = Sheet.createPropertiesSet();
        set.setName("header");
        set.setDisplayName("Geral");

        PropertySupport.Reflection psr;
   
        //BaseDescriptor obj = (BaseDescriptor)this.getBean(); //getLookup().lookup(FileObserverDescriptor.class);
        //FileObserverDescriptor obj = getLookup().lookup(FileObserverDescriptor.class);
        FileObserverDescriptor obj = lfod;
        
        try {
        
            psr = new PropertySupport.Reflection<>(obj, String.class, "inputfile_path");
            psr.setName("Arquivo observado");
            psr.setShortDescription("Caminho para o arquivo a observar");
            set.put(psr);
          
            psr = new PropertySupport.Reflection<>(obj, String.class, "outputfile");
            psr.setName("Diretorio de backup");
            psr.setShortDescription("Caminho para o diretorio da cópia de segurança/auditoria - Deixe em branco para cancelar a ação");
            set.put(psr);
            
            psr = new PropertySupport.Reflection<>(obj, Long.class, "interval");
            psr.setName("Intervalo");
            psr.setShortDescription("Período (em milisegundos) entre as verificações peródicas da presença "
                    + "do arquivo a observar - Um valor abaixo de <b>400 ms</b> desabilitará o serviço");
            set.put(psr);
            
            psr = new PropertySupport.Reflection<>(obj, Long.class, "timeout");
            psr.setName("Tempo Limite");
            psr.setShortDescription("Tempo máximo (em milisegundos) para a emissão de um evento de alarme e registro "
                    + "no log de erros caso não seja detectada a presença do arquivo observado - "
                    + "Um valor abaixo de <b>2000 ms</b> desabilitará o serviço");
            set.put(psr);
            
            psr = new PropertySupport.Reflection<>(obj, Long.class, "lag");
            psr.setName("Janela de leitura");
            psr.setShortDescription("Período (em milisegundos) entre a primeira e segunda leitura do processo de confirmação "
                    + "de consistência do arquivo e prevenção da colisão de acessos - Um valor <b>igual a 0</b> desabilitará o serviço");
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
    
    
    