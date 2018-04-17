package com.virna5.grapheditor;

import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;
import javax.swing.TransferHandler;

import com.virna5.grapheditor.EditorActions.ColorAction;
import com.virna5.grapheditor.EditorActions.FontStyleAction;
import com.virna5.grapheditor.EditorActions.HistoryAction;
import com.virna5.grapheditor.EditorActions.KeyValueAction;
import com.virna5.grapheditor.EditorActions.NewAction;
import com.virna5.grapheditor.EditorActions.OpenAction;
import com.virna5.grapheditor.EditorActions.PrintAction;
import com.virna5.grapheditor.EditorActions.SaveAction;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.util.mxGraphActions;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxResources;
import com.mxgraph.util.mxEventSource.mxIEventListener;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxGraphView;

public class EditorToolBar extends JToolBar {

    /**
     *
     */
    private static final long serialVersionUID = -8015443128436394471L;

    /**
     *
     * @param frame
     * @param orientation
     */
    private boolean ignoreZoomChange = false;

    /**
     *
     */
    public EditorToolBar(final BasicGraphEditor editor, int orientation) {
        super(orientation);
        setBorder(BorderFactory.createCompoundBorder(BorderFactory
                .createEmptyBorder(3, 3, 3, 3), getBorder()));
        setFloatable(false);

        add(editor.bind("New", new NewAction(),
                "/com/virna5/graphimages/new.gif"));
        add(editor.bind("Open", new OpenAction(),
                "/com/virna5/graphimages/open.gif"));
        add(editor.bind("Save", new SaveAction(false),
                "/com/virna5/graphimages/save.gif"));

        addSeparator();

        add(editor.bind("Print", new PrintAction(),
                "/com/virna5/graphimages/print.gif"));

        addSeparator();

        add(editor.bind("Cut", TransferHandler.getCutAction(),
                "/com/virna5/graphimages/cut.gif"));
        add(editor.bind("Copy", TransferHandler.getCopyAction(),
                "/com/virna5/graphimages/copy.gif"));
        add(editor.bind("Paste", TransferHandler.getPasteAction(),
                "/com/virna5/graphimages/paste.gif"));

        addSeparator();

        add(editor.bind("Delete", mxGraphActions.getDeleteAction(),
                "/com/virna5/graphimages/delete.gif"));

        addSeparator();

        add(editor.bind("Undo", new HistoryAction(true),
                "/com/virna5/graphimages/undo.gif"));
        add(editor.bind("Redo", new HistoryAction(false),
                "/com/virna5/graphimages/redo.gif"));

        addSeparator();

        // Gets the list of available fonts from the local graphics environment
        // and adds some frequently used fonts at the beginning of the list
        GraphicsEnvironment env = GraphicsEnvironment
                .getLocalGraphicsEnvironment();
        List<String> fonts = new ArrayList<String>();
        fonts.addAll(Arrays.asList(new String[]{"Helvetica", "Verdana",
            "Times New Roman", "Garamond", "Courier New", "-"}));
        fonts.addAll(Arrays.asList(env.getAvailableFontFamilyNames()));

        final JComboBox fontCombo = new JComboBox(fonts.toArray());
        fontCombo.setEditable(true);
        fontCombo.setMinimumSize(new Dimension(120, 0));
        fontCombo.setPreferredSize(new Dimension(120, 0));
        fontCombo.setMaximumSize(new Dimension(120, 100));
        add(fontCombo);

        fontCombo.addActionListener(new ActionListener() {
            /**
             *
             */
            public void actionPerformed(ActionEvent e) {
                String font = fontCombo.getSelectedItem().toString();

                if (font != null && !font.equals("-")) {
                    mxGraph graph = editor.getGraphComponent().getGraph();
                    graph.setCellStyles(mxConstants.STYLE_FONTFAMILY, font);
                }
            }
        });

        final JComboBox sizeCombo = new JComboBox(new Object[]{"6pt", "8pt",
            "9pt", "10pt", "12pt", "14pt", "18pt", "24pt", "30pt", "36pt",
            "48pt", "60pt"});
        sizeCombo.setEditable(true);
        sizeCombo.setMinimumSize(new Dimension(65, 0));
        sizeCombo.setPreferredSize(new Dimension(65, 0));
        sizeCombo.setMaximumSize(new Dimension(65, 100));
        add(sizeCombo);

        sizeCombo.addActionListener(new ActionListener() {
            /**
             *
             */
            public void actionPerformed(ActionEvent e) {
                mxGraph graph = editor.getGraphComponent().getGraph();
                graph.setCellStyles(mxConstants.STYLE_FONTSIZE, sizeCombo
                        .getSelectedItem().toString().replace("pt", ""));
            }
        });

        addSeparator();

        add(editor.bind("Bold", new FontStyleAction(true),
                "/com/virna5/graphimages/bold.gif"));
        add(editor.bind("Italic", new FontStyleAction(false),
                "/com/virna5/graphimages/italic.gif"));

        addSeparator();

        add(editor.bind("Left", new KeyValueAction(mxConstants.STYLE_ALIGN,
                mxConstants.ALIGN_LEFT),
                "/com/virna5/graphimages/left.gif"));
        add(editor.bind("Center", new KeyValueAction(mxConstants.STYLE_ALIGN,
                mxConstants.ALIGN_CENTER),
                "/com/virna5/graphimages/center.gif"));
        add(editor.bind("Right", new KeyValueAction(mxConstants.STYLE_ALIGN,
                mxConstants.ALIGN_RIGHT),
                "/com/virna5/graphimages/right.gif"));

        addSeparator();

        add(editor.bind("Font", new ColorAction("Font",
                mxConstants.STYLE_FONTCOLOR),
                "/com/virna5/graphimages/fontcolor.gif"));
        add(editor.bind("Stroke", new ColorAction("Stroke",
                mxConstants.STYLE_STROKECOLOR),
                "/com/virna5/graphimages/linecolor.gif"));
        add(editor.bind("Fill", new ColorAction("Fill",
                mxConstants.STYLE_FILLCOLOR),
                "/com/virna5/graphimages/fillcolor.gif"));

        addSeparator();

        final mxGraphView view = editor.getGraphComponent().getGraph()
                .getView();
        final JComboBox zoomCombo = new JComboBox(new Object[]{"400%",
            "200%", "150%", "100%", "75%", "50%", mxResources.get("page"),
            mxResources.get("width"), mxResources.get("actualSize")});
        zoomCombo.setEditable(true);
        zoomCombo.setMinimumSize(new Dimension(75, 0));
        zoomCombo.setPreferredSize(new Dimension(75, 0));
        zoomCombo.setMaximumSize(new Dimension(75, 100));
        zoomCombo.setMaximumRowCount(9);
        add(zoomCombo);

        // Sets the zoom in the zoom combo the current value
        mxIEventListener scaleTracker = new mxIEventListener() {
            /**
             *
             */
            public void invoke(Object sender, mxEventObject evt) {
                ignoreZoomChange = true;

                try {
                    zoomCombo.setSelectedItem((int) Math.round(100 * view
                            .getScale())
                            + "%");
                } finally {
                    ignoreZoomChange = false;
                }
            }
        };

        // Installs the scale tracker to update the value in the combo box
        // if the zoom is changed from outside the combo box
        view.getGraph().getView().addListener(mxEvent.SCALE, scaleTracker);
        view.getGraph().getView().addListener(mxEvent.SCALE_AND_TRANSLATE,
                scaleTracker);

        // Invokes once to sync with the actual zoom value
        scaleTracker.invoke(null, null);

        zoomCombo.addActionListener(new ActionListener() {
            /**
             *
             */
            public void actionPerformed(ActionEvent e) {
                mxGraphComponent graphComponent = editor.getGraphComponent();

                // Zoomcombo is changed when the scale is changed in the diagram
                // but the change is ignored here
                if (!ignoreZoomChange) {
                    String zoom = zoomCombo.getSelectedItem().toString();

                    if (zoom.equals(mxResources.get("page"))) {
                        graphComponent.setPageVisible(true);
                        graphComponent
                                .setZoomPolicy(mxGraphComponent.ZOOM_POLICY_PAGE);
                    } else if (zoom.equals(mxResources.get("width"))) {
                        graphComponent.setPageVisible(true);
                        graphComponent
                                .setZoomPolicy(mxGraphComponent.ZOOM_POLICY_WIDTH);
                    } else if (zoom.equals(mxResources.get("actualSize"))) {
                        graphComponent.zoomActual();
                    } else {
                        try {
                            zoom = zoom.replace("%", "");
                            double scale = Math.min(16, Math.max(0.01,
                                    Double.parseDouble(zoom) / 100));
                            graphComponent.zoomTo(scale, graphComponent
                                    .isCenterZoom());
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(editor, ex
                                    .getMessage());
                        }
                    }
                }
            }
        });
    }
}
