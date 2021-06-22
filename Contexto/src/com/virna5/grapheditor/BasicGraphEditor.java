package com.virna5.grapheditor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.File;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.layout.mxCompactTreeLayout;
import com.mxgraph.layout.mxEdgeLabelLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.layout.mxOrganicLayout;
import com.mxgraph.layout.mxParallelEdgeLayout;
import com.mxgraph.layout.mxPartitionLayout;
import com.mxgraph.layout.mxStackLayout;
import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.model.mxGraphModel;
import com.mxgraph.model.mxGraphModel.mxValueChange;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.mxGraphOutline;
import com.mxgraph.swing.handler.mxKeyboardHandler;
import com.mxgraph.swing.handler.mxRubberband;
import com.mxgraph.swing.util.mxMorphing;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.util.mxResources;
import com.mxgraph.util.mxUndoManager;
import com.mxgraph.util.mxUndoableEdit;
import com.mxgraph.util.mxEventSource.mxIEventListener;
import com.mxgraph.util.mxUndoableEdit.mxUndoableChange;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxGraphSelectionModel;
import com.virna5.contexto.BaseDescriptor;
import com.virna5.contexto.ContextNodes;
import com.virna5.contexto.ContextNodesDeserializer;
import com.virna5.contexto.ContextUtils;
import com.virna5.contexto.Controler;
import com.virna5.contexto.DescriptorConnector;
import com.virna5.contexto.DescriptorNode;
import com.virna5.edge.EdgeDescriptor;
import com.virna5.graph.ArtifactsTopComponent;
import com.virna5.graph.GraphOutlineTopComponent;
import com.virna5.graph.GraphPropertiesTopComponent;
import com.virna5.graph.GraphTopComponent;
import com.virna5.contexto.RootConnector;
import com.virna5.contexto.RootDescriptor;
import com.virna5.contexto.RootNode;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

public class BasicGraphEditor extends JPanel {

    private static final long serialVersionUID = -6561623072112577140L;
    
    private static final Logger log = Logger.getLogger(BasicGraphEditor.class.getName());

    static {
        try {
            mxResources.add("com/virna5/graphresources/editor");
        } catch (Exception e) {
            // ignore
        }
    }

    protected mxGraphComponent graphComponent;

    protected mxGraphOutline graphOutline;

    protected JTabbedPane libraryPane;

    protected mxUndoManager undoManager;

    protected String appTitle;

    protected JLabel statusBar;

    protected File currentFile;

    protected boolean modified = false;

    protected mxRubberband rubberband;

    protected mxKeyboardHandler keyboardHandler;

    
    private Controler ctrl;
    private GraphTopComponent graph_panel;
    private GraphPropertiesTopComponent properties_panel;
    
    private mxGraph graph;
    private mxGraphModel model;
    private mxCell rootcell;
        
    protected mxIEventListener undoHandler = new mxIEventListener() {
        public void invoke(Object source, mxEventObject evt) {
            undoManager.undoableEditHappened((mxUndoableEdit) evt.getProperty("edit"));
        }
    };
     
    protected mxIEventListener changeTracker = new mxIEventListener() {
        public void invoke(Object source, mxEventObject evt) {
            
            StringBuilder sb = new StringBuilder();
            Map<String,Object> prop = evt.getProperties();
            mxCell cell;
            
            ArrayList<Object>changes =(ArrayList<Object>)prop.get("changes");
            
            if (changes.size() == 3){
                sb.append("Node was created");
            }
            else{
                Object obj = changes.get(0);
                if (obj instanceof mxGraphModel.mxGeometryChange){
                    sb.append("Geometry changed ");
                }
                else if (obj instanceof mxGraphModel.mxChildChange){
                    //mxCell mxc = (mxCell)gch.getCell();
                    sb.append("Node was deleted ");
                }
                else if (obj instanceof mxGraphModel.mxValueChange){
                    Object prev = (( mxGraphModel.mxValueChange) obj).getPrevious();
                    cell = (mxCell)((mxValueChange) obj).getCell();
                    cell.setValue(prev);
                    sb.append("Value was changed");
                }
            }
            
            log.info(sb.toString());
  
            setModified(true);
        }
    };

    private void updatePointers(){
        ctrl = Controler.getInstance();
        graph_panel = GraphTopComponent.findInstance();
        properties_panel = graph_panel.getPropertiesPanel();
    }
    
    protected mxIEventListener selectionTracker = new mxIEventListener() {
        
        public void invoke(Object sender, mxEventObject evt) {

            long uid;
            mxGraphSelectionModel sm = (mxGraphSelectionModel) sender;
            mxCell cell = (mxCell) sm.getCell();
            
            if (cell != null) {
                // Cell is selected
                String id = cell.getId();
                String ctype =  cell.isVertex() ? "vertex" : "edge"; 
                Object value = cell.getValue();
                if (value instanceof DescriptorConnector){
                    DescriptorConnector dc = (DescriptorConnector)value;
                    if (dc.getID() == 0){
                        dc.setID(ContextUtils.getUID());
                        dc.initNode();                    
                    }
                    uid = dc.getID();
                    DescriptorNode dn = dc.getNode();
                    if (ctrl == null){
                        updatePointers();
                    }
                    properties_panel.setNode(dn);
                }
                else{
                    uid = -1l;
                }
                log.info(String.format("Cell %s was selected, is a %s with class hash %s, uid %s", id, ctype, value.toString(), uid));               
            }
            else{
                // No Cell selected - show global graph properties
                DescriptorConnector rdc = (DescriptorConnector)rootcell.getValue();
                DescriptorNode dn = rdc.getNode();
                properties_panel.setNode(dn);
            }
        }
    };
    
 
    // ===============================================================================================================
    public BasicGraphEditor(String appTitle, mxGraphComponent component) {
        
        // Stores and updates the frame title
        this.appTitle = "Sistema Controle - Designer de Tarefas";

        // Stores a reference to the graph and creates the command history
        graphComponent = component;
        
        graph = graphComponent.getGraph();
        model = (mxGraphModel)graphComponent.getGraph().getModel();
        rootcell = (mxCell)model.getCell("1");
        RootConnector rc = new RootConnector();
        rc.initNode();
        rootcell.setValue(rc);
        
        undoManager = createUndoManager();

        // Do not change the scale and translation after files have been loaded
        graph.setResetViewOnRootChange(false);
        
        TopComponent win;
        
        String PREFERRED_ID = "ArtifactsTopComponent";
        ArtifactsTopComponent artifacts;
        win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
        if (win == null) {
            log.log(Level.WARNING, "Não foi possivel encontrar o componente " + PREFERRED_ID);         
        }
        if (win instanceof ArtifactsTopComponent) {
            artifacts =  (ArtifactsTopComponent)win;
            libraryPane = artifacts.getPane();
        }
        
        graphOutline = new mxGraphOutline(graphComponent);
        GraphOutlineTopComponent outline;
        PREFERRED_ID = "GraphOutlineTopComponent";
        win = WindowManager.getDefault().findTopComponent(PREFERRED_ID);
        if (win == null) {
            log.log(Level.WARNING, "Não foi possivel encontrar o componente " + PREFERRED_ID);         
        }
        if (win instanceof GraphOutlineTopComponent) {
            outline =  (GraphOutlineTopComponent)win;
            outline.addOutlinePane(graphOutline);
        }
        
        // Updates the modified flag if the graph model changes
        graph.getModel().addListener(mxEvent.CHANGE, changeTracker);

        // Adds the command history to the model and view
        graph.getModel().addListener(mxEvent.UNDO, undoHandler);
        graph.getView().addListener(mxEvent.UNDO, undoHandler);
        
        graph.getSelectionModel().addListener(mxEvent.CHANGE, selectionTracker);

//        graph.getSelectionModel().addListener(mxEvent.CHANGE, new mxIEventListener() {
//            public void invoke(Object source, mxEventObject evt) {
//                log.info("Cell togled");
//            }
//        });
        
        
        // Keeps the selection in sync with the command history
        mxIEventListener undoHandler = new mxIEventListener() {
            public void invoke(Object source, mxEventObject evt) {
                List<mxUndoableChange> changes = ((mxUndoableEdit) evt.getProperty("edit")).getChanges();
                graph.setSelectionCells(graph.getSelectionCellsForChanges(changes));
            }
        };

        undoManager.addListener(mxEvent.UNDO, undoHandler);
        undoManager.addListener(mxEvent.REDO, undoHandler);

        // Creates the status bar
        statusBar = createStatusBar();

        // Display some useful information about repaint events
        installRepaintListener();

        // Puts everything together
        setLayout(new BorderLayout());
        add(graphComponent, BorderLayout.CENTER);
        add(statusBar, BorderLayout.SOUTH);
        installToolBar();

        // Installs rubberband selection and handling for some special
        // keystrokes such as F2, Control-C, -V, X, A etc.
        installHandlers();
        installListeners();
        updateTitle();
    
        
    }
   
    // ----------------------------------------------------------------------------------------------------------------
    
    public void proccessGraph(String file){
        
        StringBuilder sb = new StringBuilder();
        boolean failed = false;
        
        BaseDescriptor bd;
        RootDescriptor rd = new RootDescriptor();
        ArrayList<BaseDescriptor> nodes = new ArrayList<>();
           
        Map<String,Object> cells_map = model.getCells();
        //Collection<Object>cells_list = cells_map.values();
        
        Long context = ContextUtils.getUID();
        
        
        sb.append(String.format("Graph with %d cells is :\n", cells_map.size()));
        
        for (Object c : cells_map.values()){
            mxCell mxc = (mxCell)c;
            Object cvalue = mxc.getValue();
            if (cvalue != null){
                if (mxc.getId().equals("1")){
                    sb.append("Found root\n");
                    DescriptorConnector rdc = (DescriptorConnector)mxc.getValue();
                    rd = (RootDescriptor)rdc.getDescriptor();
                    rd.setGraph_widget(mxc.getId());
                    rd.setContext(context);
                    rd.setUID(ContextUtils.getUID());
                    rd.getContextNodes().clear();
                }
                else{
                    // Build info fot log servicces
                    String stype = mxc.isEdge() ? "Edge" : "Vertex";
                    sb.append("Cell ").append(mxc.getId()).append(" is a ").append(stype).append(" of class ").append(cvalue.toString()).append("\n");
                    
                    // Now if it is proper rooted process else tell log 
                    if (!(mxc.getParent().getId().equals("1"))){
                        sb.append("\tCell ").append(mxc.getId()).append(" doesnt match parent 1\n");
                    }
                    else{
                        
                        if ((mxc.getValue() instanceof DescriptorConnector)){
                            DescriptorConnector obd = (DescriptorConnector)mxc.getValue();
                            bd = obd.getDescriptor();
                            bd.setContext(context);
                            bd.setUID(ContextUtils.getUID());
                            bd.setGraph_widget(mxc.getId());
                            bd.setStyle(mxc.getStyle());
                            if (mxc.isVertex()){
                                mxGeometry mxg = mxc.getGeometry();
                                bd.setPosition(mxg.getX(), mxg.getY());
                                bd.setDimension(mxg.getWidth(), mxg.getHeight());
                                //bd.setContext(context);
                                nodes.add(bd);
                                sb.append("\tVertex added to pool with geometry : ")
                                        .append(bd.getXpos()+ " - ").append(bd.getYpos()+ " - ")
                                        .append(bd.getWidth()+ " - ").append(bd.getHeight()+ "\n");
                            }
                            else{
                                EdgeDescriptor ebd = (EdgeDescriptor)bd;
                                ebd.setGraph_widget(mxc.getId());
                                ebd.setStyle(mxc.getStyle());
                                mxGeometry mxg = mxc.getGeometry();
                                if (mxc.getSource() != null) {
                                    ebd.setSource_widget(mxc.getSource().getId());
                                    //ebd.setSourcept_x(mxg.getSourcePoint().getX());
                                    //ebd.setSourcept_y(mxg.getSourcePoint().getY());
                                    sb.append("\tEdge conected to source Vertex ").append(ebd.getSource_widget())
                                            .append(" @ : ").append(ebd.getSourcept_x()).append(" / ").append(ebd.getSourcept_y()).append("\n");
                                }
                                else{
                                    sb.append("\tEdge has a dangling source point\n");
                                }
                                if (mxc.getTarget() != null){
                                    ebd.setTarget_widget(mxc.getTarget().getId());
                                    //ebd.setTargetpt_x(mxg.getTargetPoint().getX());
                                    //ebd.setTargetpt_y(mxg.getTargetPoint().getY());
                                    sb.append("\tEdge conected to target Vertex ").append(ebd.getTarget_widget())
                                            .append(" @ : ").append(ebd.getTargetpt_x()).append(" / ").append(ebd.getTargetpt_y()).append("\n");
                                }
                                else{
                                    sb.append("\tEdge has a dangling target point\n");   
                                }
                                ebd.setContext(context);
                                nodes.add(ebd);
                            }
                        }
                        else{
                            sb.append("\tCell ").append(mxc.getId()).append(" is not of type BaseDescriptor\n");
                            failed = true;
                        }
                    } 
                }
            }
            else{
                sb.append("Cell ").append(mxc.getId()).append(" has null value, bypassing... \n");
            }
        }
        
        if (failed) sb.append("Parse has failed !\n");
        log.info(sb.toString());
        
        if (!failed){
            
            for (BaseDescriptor nd : nodes){
                rd.addNode(nd);
            }
            
            GsonBuilder builder = new GsonBuilder(); 
            builder.registerTypeAdapter(ContextNodes.class, new ContextNodesDeserializer()); 
            builder.setPrettyPrinting(); 
            Gson gson = builder.create();
            String sjson = gson.toJson(rd);
     
            log.info("========== JSON : ==================\n\r");
            log.info(sjson);
            log.info(String.format("Json parser loaded %d chars", sjson.length()));
                log.info("File saved");
            try {
                //ContextUtils.saveJson("/Bascon/BSW1/Testbench/graph1.json", sjson);
                ContextUtils.saveJson(file, sjson);
            } catch (IOException ex) {
                log.info(String.format("Failed to save json file due : %s", ex.getMessage()));
            }         
        }
        
    }
    
    public void loadContext(String payload){
        
        if (ctrl == null) updatePointers();
        RootDescriptor loadedctx = ctrl.loadContextDescriptor(payload);
       
        
        mxCell parent = (mxCell)graph.getDefaultParent();
        RootConnector rc = (RootConnector)parent.getValue();
        
        // TODO1
        rc.initNode();
        
        
        RootNode rn = (RootNode)rc.getNode();
        rn.setDescriptor(loadedctx);
        
        ArrayList<BaseDescriptor> nodes = loadedctx.getContextNodes();
        LinkedHashMap<String, mxCell> nodemap = new LinkedHashMap<>();
        
        graph.getModel().beginUpdate();
        try {
            for (BaseDescriptor bs : nodes){
                if (!(bs instanceof EdgeDescriptor)){
                    mxCell nd = (mxCell)graph.insertVertex(parent, bs.getGraph_widget(), bs.buildConnector(), 
                        bs.getXpos(), bs.getYpos(), bs.getWidth(), bs.getHeight(),
                        bs.getStyle());
                    nodemap.put(bs.getGraph_widget(), nd);
                }
            }
        } finally {
            graph.getModel().endUpdate();
        }
        
        graph.getModel().beginUpdate();
        try {
            for (BaseDescriptor bs : nodes){
                if (bs instanceof EdgeDescriptor){
                    mxCell nd = (mxCell)graph.insertEdge(parent, bs.getGraph_widget(), bs.buildConnector(),
                            nodemap.get(((EdgeDescriptor) bs).getSource_widget()), 
                            nodemap.get(((EdgeDescriptor) bs).getTarget_widget()), 
                            bs.getStyle());                 
                    nodemap.put(bs.getGraph_widget(), nd);
                }
            }
        } finally {
            graph.getModel().endUpdate();
        }
        
        
        
        log.info("Context reloaded");
    }
    
    
    
    protected mxUndoManager createUndoManager() {
        return new mxUndoManager();
    }

    protected void installHandlers() {
        rubberband = new mxRubberband(graphComponent);
        keyboardHandler = new EditorKeyboardHandler(graphComponent);
    }

    protected void installToolBar() {
        add(new EditorToolBar(this, JToolBar.HORIZONTAL), BorderLayout.NORTH);
    }

    protected JLabel createStatusBar() {
        JLabel statusBar = new JLabel(mxResources.get("ready"));
        statusBar.setBorder(BorderFactory.createEmptyBorder(2, 4, 2, 4));

        return statusBar;
    }

    protected void installRepaintListener() {
        graphComponent.getGraph().addListener(mxEvent.REPAINT,new mxIEventListener() {
            public void invoke(Object source, mxEventObject evt) {
                String buffer = (graphComponent.getTripleBuffer() != null) ? "": " (unbuffered)";
                mxRectangle dirty = (mxRectangle) evt.getProperty("region");
//                if (dirty == null) {
//                    status("Repaint all" + buffer);
//                } else {
//                    status("Repaint: x=" + (int) (dirty.getX()) + " y="
//                            + (int) (dirty.getY()) + " w="
//                            + (int) (dirty.getWidth()) + " h="
//                            + (int) (dirty.getHeight()) + buffer);
//                }
            }
        });
    }

    public EditorPalette insertPalette(String title) {
        final EditorPalette palette = new EditorPalette();
        final JScrollPane scrollPane = new JScrollPane(palette);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        libraryPane.add(title, scrollPane);

        // Updates the widths of the palettes if the container size changes
        libraryPane.addComponentListener(new ComponentAdapter() {
            
            public void componentResized(ComponentEvent e) {
                int w = scrollPane.getWidth()
                        - scrollPane.getVerticalScrollBar().getWidth();
                palette.setPreferredWidth(w);
            }

        });

        return palette;
    }

    protected void mouseWheelMoved(MouseWheelEvent e) {
        if (e.getWheelRotation() < 0) {
            graphComponent.zoomIn();
        } else {
            graphComponent.zoomOut();
        }

//        status(mxResources.get("scale") + ": "
//                + (int) (100 * graphComponent.getGraph().getView().getScale())
//                + "%");
    }

    protected void showOutlinePopupMenu(MouseEvent e) {
        
        Point pt = SwingUtilities.convertPoint(e.getComponent(), e.getPoint(),graphComponent);
        JCheckBoxMenuItem item = new JCheckBoxMenuItem(mxResources.get("magnifyPage"));
        item.setSelected(graphOutline.isFitPage());

        item.addActionListener(new ActionListener() {
           
            public void actionPerformed(ActionEvent e) {
                graphOutline.setFitPage(!graphOutline.isFitPage());
                graphOutline.repaint();
            }
        });

        JCheckBoxMenuItem item2 = new JCheckBoxMenuItem(mxResources.get("showLabels"));
        item2.setSelected(graphOutline.isDrawLabels());

        item2.addActionListener(new ActionListener() {
            
            public void actionPerformed(ActionEvent e) {
                graphOutline.setDrawLabels(!graphOutline.isDrawLabels());
                graphOutline.repaint();
            }
        });

        JCheckBoxMenuItem item3 = new JCheckBoxMenuItem(mxResources.get("buffering"));
        item3.setSelected(graphOutline.isTripleBuffered());

        item3.addActionListener(new ActionListener() {
            
            public void actionPerformed(ActionEvent e) {
                graphOutline.setTripleBuffered(!graphOutline.isTripleBuffered());
                graphOutline.repaint();
            }
        });

        JPopupMenu menu = new JPopupMenu();
        menu.add(item);
        menu.add(item2);
        menu.add(item3);
        menu.show(graphComponent, pt.x, pt.y);

        e.consume();
    }

    protected void showGraphPopupMenu(MouseEvent e) {
        Point pt = SwingUtilities.convertPoint(e.getComponent(), e.getPoint(),graphComponent);
        EditorPopupMenu menu = new EditorPopupMenu(BasicGraphEditor.this);
        menu.show(graphComponent, pt.x, pt.y);

        e.consume();
    }

    
    protected void mouseLocationChanged(MouseEvent e) {
        status(e.getX() + ", " + e.getY());
    }

    protected void installListeners() {
             
        // Installs mouse wheel listener for zooming
        MouseWheelListener wheelTracker = new MouseWheelListener() {
            
            public void mouseWheelMoved(MouseWheelEvent e) {
                if (e.getSource() instanceof mxGraphOutline
                        || e.isControlDown()) {
                    BasicGraphEditor.this.mouseWheelMoved(e);
                }
            }

        };

        // Handles mouse wheel events in the outline and graph component
        graphOutline.addMouseWheelListener(wheelTracker);
        graphComponent.addMouseWheelListener(wheelTracker);

        // Installs the popup menu in the outline
        graphOutline.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent e) {
                // Handles context menu on the Mac where the trigger is on mousepressed
                mouseReleased(e);
            }

            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    showOutlinePopupMenu(e);
                }
            }

        });

        // Installs the popup menu in the graph component
        graphComponent.getGraphControl().addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent e) {
                // Handles context menu on the Mac where the trigger is on mousepressed
                mouseReleased(e);
            }

            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    showGraphPopupMenu(e);
                }
            }

        });

        // Installs a mouse motion listener to display the mouse location
        graphComponent.getGraphControl().addMouseMotionListener( new MouseMotionListener() {
    
            public void mouseDragged(MouseEvent e) {
                mouseLocationChanged(e);
            }
  
            public void mouseMoved(MouseEvent e) {
                mouseDragged(e);
            }

        });
    }

    
    public void setCurrentFile(File file) {
        File oldValue = currentFile;
        currentFile = file;

        firePropertyChange("currentFile", oldValue, file);

        if (oldValue != file) {
            updateTitle();
        }
    }

    public File getCurrentFile() {
        return currentFile;
    }

    public void setModified(boolean modified) {
        boolean oldValue = this.modified;
        this.modified = modified;

        firePropertyChange("modified", oldValue, modified);

        if (oldValue != modified) {
            updateTitle();
        }
    }

    public boolean isModified() {
        return modified;
    }

    
    public mxGraphComponent getGraphComponent() {
        return graphComponent;
    }

    public mxGraphOutline getGraphOutline() {
        return graphOutline;
    }

    
    public JTabbedPane getLibraryPane() {
        return libraryPane;
    }

    
    public mxUndoManager getUndoManager() {
        return undoManager;
    }

   
    public Action bind(String name, final Action action) {
        return bind(name, action, null);
    }

    
    @SuppressWarnings("serial")
    public Action bind(String name, final Action action, String iconUrl) {
        AbstractAction newAction = new AbstractAction(name, (iconUrl != null) ? new ImageIcon(
                                                                BasicGraphEditor.class.getResource(iconUrl)) : null) {
            public void actionPerformed(ActionEvent e) {
                action.actionPerformed(new ActionEvent(getGraphComponent(), e
                        .getID(), e.getActionCommand()));
            }
        };

        newAction.putValue(Action.SHORT_DESCRIPTION, action.getValue(Action.SHORT_DESCRIPTION));

        return newAction;
    }

    public void status(String msg) {
        statusBar.setText(msg);
    }

    public void updateTitle() {
        JFrame frame = (JFrame) SwingUtilities.windowForComponent(this);

        if (frame != null) {
            
            String title = (currentFile != null) ? currentFile.getAbsolutePath() : mxResources.get("newDiagram");

            if (modified) {
                title += "*";
            }

            frame.setTitle(title + " - " + appTitle);
        }
    }

    public void about() {
//        JFrame frame = (JFrame) SwingUtilities.windowForComponent(this);
//
//        if (frame != null) {
//            EditorAboutFrame about = new EditorAboutFrame(frame);
//            about.setModal(true);
//
//            // Centers inside the application frame
//            int x = frame.getX() + (frame.getWidth() - about.getWidth()) / 2;
//            int y = frame.getY() + (frame.getHeight() - about.getHeight()) / 2;
//            about.setLocation(x, y);
//
//            // Shows the modal dialog and waits
//            about.setVisible(true);
//        }
    }

    public void exit() {
//        JFrame frame = (JFrame) SwingUtilities.windowForComponent(this);
//
//        if (frame != null) {
//            frame.dispose();
//        }
    }


    /**
     * Creates an action that executes the specified layout.
     *
     * @param key Key to be used for getting the label from mxResources and also to create the layout instance for the commercial
     * graph editor example.
     * @return an action that executes the specified layout
     */
    @SuppressWarnings("serial")
    public Action graphLayout(final String key, boolean animate) {
        final mxIGraphLayout layout = createLayout(key, animate);

        if (layout != null) {
            return new AbstractAction(mxResources.get(key)) {
                public void actionPerformed(ActionEvent e) {
                    final mxGraph graph = graphComponent.getGraph();
                    Object cell = graph.getSelectionCell();

                    if (cell == null || graph.getModel().getChildCount(cell) == 0) {
                        cell = graph.getDefaultParent();
                    }

                    graph.getModel().beginUpdate();
                    try {
                        long t0 = System.currentTimeMillis();
                        layout.execute(cell);
                        status("Layout: " + (System.currentTimeMillis() - t0) + " ms");
                    } finally {
                        mxMorphing morph = new mxMorphing(graphComponent, 20,1.2, 20);

                        morph.addListener(mxEvent.DONE, new mxIEventListener() {

                            public void invoke(Object sender, mxEventObject evt) {
                                graph.getModel().endUpdate();
                            }

                        });

                        morph.startAnimation();
                    }

                }

            };
        } else {
            return new AbstractAction(mxResources.get(key)) {

                public void actionPerformed(ActionEvent e) {
                    JOptionPane.showMessageDialog(graphComponent,mxResources.get("noLayout"));
                }

            };
        }
    }

    /**
     * Creates a layout instance for the given identifier.
     */
    protected mxIGraphLayout createLayout(String ident, boolean animate) {
        mxIGraphLayout layout = null;

        if (ident != null) {
            mxGraph graph = graphComponent.getGraph();

            if (ident.equals("verticalHierarchical")) {
                layout = new mxHierarchicalLayout(graph);
            } else if (ident.equals("horizontalHierarchical")) {
                layout = new mxHierarchicalLayout(graph, JLabel.WEST);
            } else if (ident.equals("verticalTree")) {
                layout = new mxCompactTreeLayout(graph, false);
            } else if (ident.equals("horizontalTree")) {
                layout = new mxCompactTreeLayout(graph, true);
            } else if (ident.equals("parallelEdges")) {
                layout = new mxParallelEdgeLayout(graph);
            } else if (ident.equals("placeEdgeLabels")) {
                layout = new mxEdgeLabelLayout(graph);
            } else if (ident.equals("organicLayout")) {
                layout = new mxOrganicLayout(graph);
            }
            if (ident.equals("verticalPartition")) {
                layout = new mxPartitionLayout(graph, false) {
                    /**
                     * Overrides the empty implementation to return the size of the graph control.
                     */
                    public mxRectangle getContainerSize() {
                        return graphComponent.getLayoutAreaSize();
                    }
                };
            } else if (ident.equals("horizontalPartition")) {
                layout = new mxPartitionLayout(graph, true) {
                    /**
                     * Overrides the empty implementation to return the size of the graph control.
                     */
                    public mxRectangle getContainerSize() {
                        return graphComponent.getLayoutAreaSize();
                    }
                };
            } else if (ident.equals("verticalStack")) {
                layout = new mxStackLayout(graph, false) {
                    /**
                     * Overrides the empty implementation to return the size of the graph control.
                     */
                    public mxRectangle getContainerSize() {
                        return graphComponent.getLayoutAreaSize();
                    }
                };
            } else if (ident.equals("horizontalStack")) {
                layout = new mxStackLayout(graph, true) {
                    /**
                     * Overrides the empty implementation to return the size of the graph control.
                     */
                    public mxRectangle getContainerSize() {
                        return graphComponent.getLayoutAreaSize();
                    }
                };
            } else if (ident.equals("circleLayout")) {
                layout = new mxCircleLayout(graph);
            }
        }

        return layout;
    }

}


        // Creates the graph outline component
        //graphOutline = new mxGraphOutline(graphComponent);

        // Creates the library pane that contains the tabs with the palettes
        //libraryPane = new JTabbedPane();
                
        // Creates the inner split pane that contains the library with the
        // palettes and the graph outline on the left side of the window
//        JSplitPane inner = new JSplitPane(JSplitPane.VERTICAL_SPLIT, libraryPane, graphOutline);
//        inner.setDividerLocation(320);
//        inner.setResizeWeight(1);
//        inner.setDividerSize(6);
//        inner.setBorder(null);

        // Creates the outer split pane that contains the inner split pane and
        // the graph component on the right side of the window
        //JPane inner = new JPane();        
//        JSplitPane outer = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JPanel(), graphComponent);
//        outer.setOneTouchExpandable(true);
//        outer.setDividerLocation(200);
//        outer.setDividerSize(6);
//        outer.setBorder(null);




//    /**
//     *
//     */
//    public void setLookAndFeel(String clazz) {
////        JFrame frame = (JFrame) SwingUtilities.windowForComponent(this);
////
////        if (frame != null) {
////            try {
////                UIManager.setLookAndFeel(clazz);
////                SwingUtilities.updateComponentTreeUI(frame);
////
////                // Needs to assign the key bindings again
////                keyboardHandler = new EditorKeyboardHandler(graphComponent);
////            } catch (Exception e1) {
////                e1.printStackTrace();
////            }
////        }
//    }
//
//    /**
//     *
//     */
//    public JFrame createFrame(JMenuBar menuBar) {
//        JFrame frame = new JFrame();
//        frame.getContentPane().add(this);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setJMenuBar(menuBar);
//        frame.setSize(870, 640);
//
//        // Updates the frame title
//        updateTitle();
//
//        return frame;
//    }