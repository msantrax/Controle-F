
package com.virna5.graph;

import java.awt.Color;
import java.awt.Point;
import java.net.URL;
import java.text.NumberFormat;
import javax.swing.ImageIcon;

import org.w3c.dom.Document;

import com.virna5.grapheditor.BasicGraphEditor;
import com.virna5.grapheditor.EditorPalette;
import com.mxgraph.io.mxCodec;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxICell;
import com.mxgraph.model.mxIGraphModel;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.util.mxGraphTransferable;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxEventSource.mxIEventListener;
import com.mxgraph.util.mxResources;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxGraph;
import com.virna5.csvfilter.CSVFilterConnector;
import com.virna5.edge.EdgeConnector;
import com.virna5.fileobserver.FileObserverConnector;
import com.virna5.filewriter.FileWriterConnector;
import com.virna5.interceptor.InterceptorConnector;
import com.virna5.qs4generator.QS4GeneratorConnector;
import com.virna5.sapfilter.SAPFilterConnector;
import java.util.logging.Logger;

public class GraphEditor extends BasicGraphEditor{

    private static final Logger log = Logger.getLogger(GraphEditor.class.getName());
    
    private static final long serialVersionUID = -4601740824088314699L;
    
    private static GraphEditor instance;
    public static GraphEditor getInstance(){
        if (instance == null) {instance = new GraphEditor();}
        return instance;
    }
    
    public static final NumberFormat numberFormat = NumberFormat.getInstance();

    public static URL url = null;
    

    public GraphEditor(){
            this("mxGraph Editor", new CustomGraphComponent(new CustomGraph()));
    }

    public GraphEditor(String appTitle, mxGraphComponent component){

        super(appTitle, component);
        final mxGraph graph = graphComponent.getGraph();

        // Creates the shapes palette
        EditorPalette shapesPalette = insertPalette(mxResources.get("shapes"));
        //EditorPalette imagesPalette = insertPalette(mxResources.get("images"));
        //EditorPalette symbolsPalette = insertPalette(mxResources.get("symbols"));

        // Sets the edge template to be used for creating new edges if an edge
        // is clicked in the shape palette
        shapesPalette.addListener(mxEvent.SELECT, new mxIEventListener() {
            public void invoke(Object sender, mxEventObject evt){    
                Object tmp = evt.getProperty("transferable");
                if (tmp instanceof mxGraphTransferable){
                    mxGraphTransferable t = (mxGraphTransferable) tmp;
                    Object cell = t.getCells()[0];
                    if (graph.getModel().isEdge(cell)){
                        ((CustomGraph) graph).setEdgeTemplate(cell);
                    }
                }
            }

        });

        // Adds some template cells for dropping into the graph
        
        shapesPalette.addTemplate("Observador de Arquivos",new ImageIcon(GraphEditor.class.getResource("/com/virna5/graphimages/hd-48x48.png")),
                                        "icon;image=/com/virna5/graphimages/triangle.png",
                                        120, 70, new FileObserverConnector() ); 
        
        shapesPalette.addTemplate("Filtro CSV",new ImageIcon(GraphEditor.class.getResource("/com/virna5/graphimages/filter.png")),
                                        "icon;image=/com/virna5/graphimages/filter.png",
                                        120, 70, new CSVFilterConnector() );
        
        shapesPalette.addTemplate("Filtro SAP",new ImageIcon(GraphEditor.class.getResource("/com/virna5/graphimages/filter.png")),
                                        "icon;image=/com/virna5/graphimages/filter.png",
                                        120, 70, new SAPFilterConnector() );
        
        
        shapesPalette.addTemplate("Gravador de Arquivos",new ImageIcon(GraphEditor.class.getResource("/com/virna5/graphimages/hd-48x48.png")),
                                        "icon;image=/com/virna5/graphimages/save.gif",
                                        120, 70, new FileWriterConnector() );
        
        shapesPalette.addTemplate("Gerador QS4",new ImageIcon(GraphEditor.class.getResource("/com/virna5/graphimages/hd-48x48.png")),
                                        "icon;image=/com/virna5/graphimages/gear.png",
                                        120, 70, new QS4GeneratorConnector() );
        
        shapesPalette.addTemplate("Interceptador",new ImageIcon(GraphEditor.class.getResource("/com/virna5/graphimages/hd-48x48.png")),
                                        "icon;image=/com/virna5/graphimages/gear.png",
                                        120, 70, new InterceptorConnector() );
        
        
        shapesPalette.addTemplate("Container",new ImageIcon(GraphEditor.class.getResource("/com/virna5/graphimages/swimlane.png")),
                                        "swimlane", 280, 280, "Container");
        
        
        
        
//        shapesPalette.addTemplate("Icon",new ImageIcon(GraphEditor.class.getResource("/com/virna5/graphimages/rounded.png")),
//                                        "icon;image=/com/virna5/graphimages/wrench.png",
//                                        120, 70, new FileObserverConnector() );
//        shapesPalette.addTemplate("Label",new ImageIcon(GraphEditor.class.getResource("/com/virna5/graphimages/rounded.png")),
//                                        "label;image=/com/virna5/graphimages/gear.png",130, 50, "Etiqueta");
//        shapesPalette.addTemplate("Rounded Rectangle",new ImageIcon(GraphEditor.class.getResource("/com/virna5/graphimages/rounded.png")),
//                                        "rounded=1", 160, 120, "");
//        shapesPalette.addTemplate("Horizontal Line",new ImageIcon(GraphEditor.class.getResource("/com/virna5/graphimages/hline.png")),
//                                        "line", 160, 10, "");	
        
        
        
        shapesPalette.addEdgeTemplate("Straight",new ImageIcon(GraphEditor.class.getResource("/com/virna5/graphimages/straight.png")),
                                        "straight", 120, 120, new EdgeConnector());
        shapesPalette.addEdgeTemplate("Horizontal Connector",new ImageIcon(GraphEditor.class.getResource("/com/virna5/graphimages/connect.png")),
                                        null, 100, 100, new EdgeConnector());
        shapesPalette.addEdgeTemplate("Vertical Connector",new ImageIcon(GraphEditor.class.getResource("/com/virna5/graphimages/vertical.png")),
                                        "vertical", 100, 100, new EdgeConnector());
        shapesPalette.addEdgeTemplate("Entity Relation",new ImageIcon(GraphEditor.class.getResource("/com/virna5/graphimages/entity.png")),
                                        "entity", 100, 100, new EdgeConnector());
        
        
        instance = this;
        
    }

    
    public static class CustomGraphComponent extends mxGraphComponent {

        private static final long serialVersionUID = -6833603133512882012L;

        public CustomGraphComponent(mxGraph graph){
            super(graph);

            // Sets switches typically used in an editor
            setPageVisible(false);
            setGridVisible(true);
            setToolTips(true);
            getConnectionHandler().setCreateTarget(true);

            // Loads the defalt stylesheet from an external file
            mxCodec codec = new mxCodec();
            Document doc = mxUtils.loadDocument(GraphEditor.class.getResource(
                            "/com/virna5/graphresources/default-style.xml")
                            .toString());
            codec.decode(doc.getDocumentElement(), graph.getStylesheet());

            // Sets the background to white
            getViewport().setOpaque(true);
            getViewport().setBackground(Color.WHITE);
            
        }

        /**
         * Overrides drop behaviour to set the cell style if the target
         * is not a valid drop target and the cells are of the same
         * type (eg. both vertices or both edges). 
         */
        public Object[] importCells(Object[] cells, double dx, double dy, Object target, Point location){

            if (target == null && cells.length == 1 && location != null){
                target = getCellAt(location.x, location.y);

                if (target instanceof mxICell && cells[0] instanceof mxICell){
                    mxICell targetCell = (mxICell) target;
                    mxICell dropCell = (mxICell) cells[0];

                    if (targetCell.isVertex() == dropCell.isVertex()|| targetCell.isEdge() == dropCell.isEdge()){
                        mxIGraphModel model = graph.getModel();
                        model.setStyle(target, model.getStyle(cells[0]));
                        graph.setSelectionCell(target);
                        return null;
                    }
                }
            }
            return super.importCells(cells, dx, dy, target, location);
        }

    }

    /**
     * A graph that creates new edges from a given template edge.
     */
    public static class CustomGraph extends mxGraph{
        
        /**
        * Holds the edge to be used as a template for inserting new edges.
        */
        protected Object edgeTemplate;

        /**
         * Custom graph that defines the alternate edge style to be used when
         * the middle control point of edges is double clicked (flipped).
         */
        public CustomGraph(){
            setAlternateEdgeStyle("edgeStyle=mxEdgeStyle.ElbowConnector;elbow=vertical");
        }

        /**
         * Sets the edge template to be used to inserting edges.
         */
        public void setEdgeTemplate(Object template){
            edgeTemplate = template;
        }

        /**
         * Prints out some useful information about the cell in the tooltip.
         */
        public String getToolTipForCell(Object cell){                
            String tip = "tooltip";
            return tip;
        }

        /**
         * Overrides the method to use the currently selected edge template for
         * new edges.
         * 
         * @param graph
         * @param parent
         * @param id
         * @param value
         * @param source
         * @param target
         * @param style
         * @return
         */
        public Object createEdge(Object parent, String id, Object value,Object source, Object target, String style){

            if (edgeTemplate != null){
                    mxCell edge = (mxCell) cloneCells(new Object[] { edgeTemplate })[0];
                    edge.setId(id);
                    return edge;
            }
            return super.createEdge(parent, id, value, source, target, style);
        }

    }

}


//
//private void runKFactor(){       
//        
//         IDPainelModel idpm = new IDPainelModel("Analises", this);
//            if (idpm != null){
//                SwingUtilities.invokeLater(new Runnable(){
//                @Override
//                    public void run(){          
//                        ABNTCalibTopComponent tc = new ABNTCalibTopComponent();
//                        if (tc!=null){
//                            tc.loadContext(idpm);
//                            Mode m = WindowManager.getDefault().findMode("editor");
//                            if(m != null){
//                                m.dockInto(tc);
//                                tc.open();
//                                tc.requestActive();
//                            } 
//                        }   
//                    }
//                });
//           } 
//            
//    }    