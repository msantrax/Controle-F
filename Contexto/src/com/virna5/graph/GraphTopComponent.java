/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.virna5.graph;

import com.virna5.contexto.BaseDescriptor;
import com.virna5.contexto.DescriptorNode;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.text.DefaultEditorKit;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.ExplorerUtils;
import org.openide.nodes.Node;
import org.openide.util.Lookup;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.WindowManager;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(
        dtd = "-//com.virna5.graph//Graph//EN",
        autostore = false
)
@TopComponent.Description(
        preferredID = "GraphTopComponent",
        iconBase = "com/virna5/graph/frame_edit.png",
        persistenceType = TopComponent.PERSISTENCE_ALWAYS
)
@TopComponent.Registration(mode = "editor", openAtStartup = false)
@ActionID(category = "Window", id = "com.virna5.graph.GraphTopComponent")
@ActionReference(path = "Menu/Window" /*, position = 333 */)
@TopComponent.OpenActionRegistration(
        displayName = "#CTL_GraphAction",
        preferredID = "GraphTopComponent"
)
@Messages({
    "CTL_GraphAction=Graph",
    "CTL_GraphTopComponent=Graph Window",
    "HINT_GraphTopComponent=This is a Graph window"
})
public final class GraphTopComponent extends TopComponent implements ExplorerManager.Provider, Lookup.Provider  {

    private static final Logger log = Logger.getLogger(GraphTopComponent.class.getName());

    private static GraphTopComponent instance;
    private Lookup lookup;
    private static final ExplorerManager em = new ExplorerManager();
    private static transient Node rootnode;
    private static final String PREFERRED_ID = "GraphTopComponent";
    
    public GraphTopComponent() {
        initComponents();
        setName(Bundle.CTL_GraphTopComponent());
        setToolTipText(Bundle.HINT_GraphTopComponent());

        ActionMap map = this.getActionMap();
        map.put(DefaultEditorKit.copyAction, ExplorerUtils.actionCopy(em));
        map.put(DefaultEditorKit.cutAction, ExplorerUtils.actionCut(em));
       // Action paste =  ExplorerUtils.actionPaste(em);
        //map.put(DefaultEditorKit.pasteAction, paste);

        //map.put(DefaultEditorKit.pasteAction, new CMPasteAction());
        //map.put("paste", new CMPasteAction());
        map.put("delete", ExplorerUtils.actionDelete(em, false)); //NOI18N

        InputMap keys = getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        keys.put(KeyStroke.getKeyStroke("control C"), DefaultEditorKit.copyAction);
        keys.put(KeyStroke.getKeyStroke("control X"), DefaultEditorKit.cutAction);
        keys.put(KeyStroke.getKeyStroke("control V"), DefaultEditorKit.pasteAction);
        keys.put(KeyStroke.getKeyStroke("DELETE"), "delete");
        
        lookup = ExplorerUtils.createLookup(em, map);
        associateLookup(lookup);
        
        
        
        GraphEditor chart_panel = GraphEditor.getInstance();
        //chart_panel.createFrame(new EditorMenuBar(chart_panel)).setVisible(true);
        
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(chart_panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(chart_panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        
         initClassmaps();
        
        instance = this;
    }

    
    private void initClassmaps() {   
        
        //noderegister.initClassMap();
        // Inicie a arvore (isso deverá criar o node 0 (Banco),  Os primários e a lata de lixo
        
        //DescriptorNode fon = new DescriptorNode(new IndexChildren(1));
        
        rootnode = new DescriptorNode(new BaseDescriptor());
        
 
        em.setRootContext(rootnode);
        //fon.updateProp();
 
    }
    
    public static synchronized GraphTopComponent getDefault() {
        if (instance == null) {
            instance = new GraphTopComponent();
        }
        return instance;
    }
    
    /**
     * Obtain the VegaTopComponent instance. Never call {@link #getDefault} directly!
     */
    public static synchronized GraphTopComponent findInstance() {
        
        TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
        if (win == null) {
            log.log(Level.WARNING, "Não foi possivel encontrar o componente " + PREFERRED_ID);
            return getDefault();
        }
        if (win instanceof GraphTopComponent) {
            return (GraphTopComponent) win;
        }
        log.log(Level.WARNING, 
                "Parece que há várias instancias do ID '" + PREFERRED_ID
                + "' Isso é uma fonte potencial de problemas e comportamento errático");
        return getDefault();
    }

    
    @Override
    public ExplorerManager getExplorerManager() {return em; }

    @Override
    public Lookup getLookup() { return lookup;}
    
    /*É uma boa ideia chavear as actions quando o componente é ativado ou não 
     As rotinas abaixo tratam disso */
    @Override
    protected void componentActivated() { ExplorerUtils.activateActions(em, true); }
    
    @Override
    protected void componentDeactivated() { ExplorerUtils.activateActions(em, false);}

    @Override
    public void addNotify() {
        super.addNotify();
        ExplorerUtils.activateActions(em, true);
    }
    
    @Override
    public void removeNotify() {
//        try {
//            log.log(Level.WARNING, "Fechando conexão com o banco de dados");
//            dbs.getConnection("CTL").commit();
//        } catch (SQLException ex) {
//            log.log(Level.WARNING, "Não foi possivel fechar a conexão com obanco de dados");
//        }
        ExplorerUtils.activateActions(em, false);
        super.removeNotify();
    }
    
    
    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of
     * this method is always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setLayout(new java.awt.BorderLayout());
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    @Override
    public void componentOpened() {
        // TODO add custom code on component opening
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



        
//        mxGraph graph = new mxGraph();
//        Object parent = graph.getDefaultParent();
//
//        graph.getModel().beginUpdate();
//        try {
//            Object v1 = graph.insertVertex(parent, null, "FileObserver", 20, 20, 80, 30);
//            Object v2 = graph.insertVertex(parent, null, "CSV Filter", 240, 150, 80, 30);
//            graph.insertEdge(parent, null, "Result", v1, v2);
//        }
//        finally{
//            graph.getModel().endUpdate();
//        }
//
//        mxGraphComponent chart_panel = new mxGraphComponent(graph);
