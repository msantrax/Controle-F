/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.virna5.graph;

import com.mxgraph.model.mxCell;
import com.mxgraph.view.mxGraph;
import com.virna5.grapheditor.BasicGraphEditor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import org.openide.loaders.DataObject;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

@ActionID(
        category = "Designer",
        id = "com.virna5.graph.SaveTask"
)
@ActionRegistration(
        iconBase = "com/virna5/graph/save_all.png",
        displayName = "#CTL_SaveTask"
)
@ActionReferences({
    @ActionReference(path = "Menu/Designer", position = 200)
    ,
  @ActionReference(path = "Toolbars/File", position = 500)
})
@Messages("CTL_SaveTask=Salvar Tarefa")
public final class SaveTask implements ActionListener {

    private final DataObject context;

    public SaveTask(DataObject context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        BasicGraphEditor editor = GraphEditor.getInstance();
            
        if (editor != null) {
            if (!editor.isModified() || 
                JOptionPane.showConfirmDialog(editor,"Perder as Modificações ?") == JOptionPane.YES_OPTION) {

                mxGraph graph = editor.getGraphComponent().getGraph();

                // Check modified flag and display save dialog
                mxCell root = new mxCell();
                root.insert(new mxCell());
                graph.getModel().setRoot(root);

                editor.setModified(false);
                editor.setCurrentFile(null);
                editor.getGraphComponent().zoomAndCenter();
            }
        }
    }
}
