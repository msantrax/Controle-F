/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.virna5.graph;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;
import java.util.logging.Logger;
import javax.swing.JPanel;

/**
 *
 * @author opus
 */
public class ChartPane extends JPanel{

    private static final Logger LOG = Logger.getLogger(ChartPane.class.getName());

    public ChartPane() {
        
        mxGraph graph = new mxGraph();
        Object parent = graph.getDefaultParent();

        graph.getModel().beginUpdate();
        try {
            Object v1 = graph.insertVertex(parent, null, "Hello", 20, 20, 80, 30);
            Object v2 = graph.insertVertex(parent, null, "World!", 240, 150, 80, 30);
            graph.insertEdge(parent, null, "Edge", v1, v2);
        }
        finally{
                graph.getModel().endUpdate();
        }

        mxGraphComponent graphComponent = new mxGraphComponent(graph);
        
        this.add(graphComponent);
       
        
    }
    
    
    
    
}
