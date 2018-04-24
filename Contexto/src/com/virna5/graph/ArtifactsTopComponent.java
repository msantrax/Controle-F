/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.virna5.graph;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTabbedPane;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.Mode;
import org.openide.windows.WindowManager;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(
        dtd = "-//com.virna5.graph//Artifacts//EN",
        autostore = false
)
@TopComponent.Description(
        preferredID = "ArtifactsTopComponent",
        //iconBase="SET/PATH/TO/ICON/HERE", 
        persistenceType = TopComponent.PERSISTENCE_NEVER
)
@TopComponent.Registration(mode = "explorer", openAtStartup = false)
@ActionID(category = "Window", id = "com.virna5.graph.ArtifactsTopComponent")
@ActionReference(path = "Menu/Window" /*, position = 333 */)
@TopComponent.OpenActionRegistration(
        displayName = "#CTL_ArtifactsAction",
        preferredID = "ArtifactsTopComponent"
)
@Messages({
    "CTL_ArtifactsAction=Artifacts",
    "CTL_ArtifactsTopComponent=Artefatos",
    "HINT_ArtifactsTopComponent=Repositorio de artefatos para o projeto de tarefas"
})
public final class ArtifactsTopComponent extends TopComponent {

    private static final Logger log = Logger.getLogger(ArtifactsTopComponent.class.getName());

    
    private static final String PREFERRED_ID = "ArtifactsTopComponent";
    private static ArtifactsTopComponent instance;
    
    public static synchronized ArtifactsTopComponent getDefault() {
        if (instance == null) {
            instance = new ArtifactsTopComponent();
        }
        return instance;
    }
    
    /**
     * Obtain the TopComponent instance. Never call {@link #getDefault} directly!
     */
    public static synchronized ArtifactsTopComponent findInstance() {
        
        Mode propmode = WindowManager.getDefault().findMode("explorer");
        //TopComponent win = ArtifactsTopComponent.getRegistry().getActivated();
        TopComponent win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
        if (win == null) {
            log.log(Level.INFO, "Não foi possivel encontrar o componente " + PREFERRED_ID);
            getDefault();
            propmode.dockInto(instance);
            instance.open();
            instance.requestActive();
            return instance;
        }
        if (win instanceof ArtifactsTopComponent) {
            log.log(Level.INFO, "Component " + PREFERRED_ID+ " is already active");
            propmode.dockInto(win);
            win.open();
            win.requestActive();
            return (ArtifactsTopComponent) win;
        }
        log.log(Level.WARNING, 
                "Parece que há várias instancias do ID '" + PREFERRED_ID
                + "' Isso é uma fonte potencial de problemas e comportamento errático");
        return getDefault();
    }
    
    

    public ArtifactsTopComponent() {
        initComponents();
        setName(Bundle.CTL_ArtifactsTopComponent());
        setToolTipText(Bundle.HINT_ArtifactsTopComponent());

    }

    public JTabbedPane getPane(){
        return jtp_artifacts;
    }
    
    
    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of
     * this method is always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jtp_artifacts = new javax.swing.JTabbedPane();

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jtp_artifacts, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jtp_artifacts, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane jtp_artifacts;
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
