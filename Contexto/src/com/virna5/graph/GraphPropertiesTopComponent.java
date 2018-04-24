/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.virna5.graph;

import com.virna5.contexto.BaseDescriptor;
import com.virna5.contexto.DescriptorNode;
import com.virna5.fileobserver.FileObserverDescriptor;
import com.virna5.fileobserver.FileObserverNode;
import java.beans.IntrospectionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.GroupLayout;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.explorer.propertysheet.PropertySheet;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.Mode;
import org.openide.windows.WindowManager;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(
        dtd = "-//com.virna5.graph//GraphProperties//EN",
        autostore = false
)
@TopComponent.Description(
        preferredID = "GraphPropertiesTopComponent",
        iconBase = "com/virna5/graph/blender.png",
        persistenceType = TopComponent.PERSISTENCE_NEVER
)
@TopComponent.Registration(mode = "properties", openAtStartup = false)
@ActionID(category = "Window", id = "com.virna5.graph.GraphPropertiesTopComponent")
@ActionReference(path = "Menu/Window" /*, position = 333 */)
@TopComponent.OpenActionRegistration(
        displayName = "#CTL_GraphPropertiesAction",
        preferredID = "GraphPropertiesTopComponent"
)
@Messages({
    "CTL_GraphPropertiesAction=GraphProperties",
    "CTL_GraphPropertiesTopComponent=Dados do Artefato",
    "HINT_GraphPropertiesTopComponent=Essa é a janela de dados do artefato"
})
public final class GraphPropertiesTopComponent extends TopComponent {

    private static final Logger log = Logger.getLogger(GraphPropertiesTopComponent.class.getName());

    private static final String PREFERRED_ID = "GraphPropertiesTopComponent";
    private static GraphPropertiesTopComponent instance;
    
    public static synchronized GraphPropertiesTopComponent getDefault() {
        if (instance == null) {
            instance = new GraphPropertiesTopComponent();
        }
        return instance;
    }
    
    /**
     * Obtain the TopComponent instance. Never call {@link #getDefault} directly!
     */
    public static synchronized GraphPropertiesTopComponent findInstance() {
        
        Mode propmode = WindowManager.getDefault().findMode("properties");
        //TopComponent win = GraphPropertiesTopComponent.getRegistry().getActivated();
        TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
        if (win == null) {
            log.log(Level.INFO, "Não foi possivel encontrar o componente " + PREFERRED_ID);
            getDefault();
            propmode.dockInto(instance);
            instance.open();
            instance.requestActive();
            return instance;
        }
        if (win instanceof GraphPropertiesTopComponent) {
            log.log(Level.INFO, "Component " + PREFERRED_ID+ " is already active");
            propmode.dockInto(win);
            win.open();
            win.requestActive();
            return (GraphPropertiesTopComponent) win;
        }
        log.log(Level.WARNING, 
                "Parece que há várias instancias do ID '" + PREFERRED_ID
                + "' Isso é uma fonte potencial de problemas e comportamento errático");
        return getDefault();
    }

    private PropertySheet ps;
    private GroupLayout layout;
    boolean flag = false;
    
    public GraphPropertiesTopComponent() {
        initComponents();
        setName(Bundle.CTL_GraphPropertiesTopComponent());
        setToolTipText(Bundle.HINT_GraphPropertiesTopComponent());
        
        
//        try {
//            setNode(new DescriptorNode(new BaseDescriptor()));
//        } catch (IntrospectionException ex) {
//            Exceptions.printStackTrace(ex);
//        }
        
        
        ps = new PropertySheet();
        try {
            ps.setNodes(new Node[] {new DescriptorNode(new BaseDescriptor())});
            //ps.setNodes(new Node[] {new FileObserverNode(new FileObserverDescriptor())});
        } catch (IntrospectionException ex) {
            Exceptions.printStackTrace(ex);
        }
        //ps.setDescriptionAreaVisible(true);
        ps.setName("PSV");
        //ps.addNotify();
        
        
        layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ps, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ps, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        
    }

    public void setNode(DescriptorNode dn){
    
        ps.setNodes(new Node[] {dn});
      
        //ps.setDescriptionAreaVisible(flag);      
    }
    
   
    
    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of
     * this method is always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    @Override
    public void componentOpened() {
        
    }

    @Override
    public void componentClosed() {
        // TODO add custom code on component closing
    }

    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
        // TODO store your settings
    }

    void readProperties(java.util.Properties p) {
        String version = p.getProperty("version");
        // TODO read your settings according to their version
    }
}
