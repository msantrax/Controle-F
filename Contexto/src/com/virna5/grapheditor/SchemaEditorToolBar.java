package com.virna5.grapheditor;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;
import javax.swing.TransferHandler;

import com.virna5.grapheditor.EditorActions.HistoryAction;
import com.virna5.grapheditor.EditorActions.NewAction;
import com.virna5.grapheditor.EditorActions.OpenAction;
import com.virna5.grapheditor.EditorActions.PrintAction;
import com.virna5.grapheditor.EditorActions.SaveAction;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.util.mxGraphActions;
import com.mxgraph.util.mxEvent;
import com.mxgraph.util.mxEventObject;
import com.mxgraph.util.mxResources;
import com.mxgraph.util.mxEventSource.mxIEventListener;
import com.mxgraph.view.mxGraphView;

public class SchemaEditorToolBar extends JToolBar
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3979320704834605323L;

	/**
	 * 
	 * @param frame
	 * @param orientation
	 */
	private boolean ignoreZoomChange = false;

	/**
	 * 
	 */
	public SchemaEditorToolBar(final BasicGraphEditor editor, int orientation)
	{
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

		final mxGraphView view = editor.getGraphComponent().getGraph()
				.getView();
		final JComboBox zoomCombo = new JComboBox(new Object[] { "400%",
				"200%", "150%", "100%", "75%", "50%", mxResources.get("page"),
				mxResources.get("width"), mxResources.get("actualSize") });
		zoomCombo.setEditable(true);
		zoomCombo.setMinimumSize(new Dimension(75, 0));
		zoomCombo.setPreferredSize(new Dimension(75, 0));
		zoomCombo.setMaximumSize(new Dimension(75, 100));
		zoomCombo.setMaximumRowCount(9);
		add(zoomCombo);

		// Sets the zoom in the zoom combo the current value
		mxIEventListener scaleTracker = new mxIEventListener()
		{
			/**
			 * 
			 */
			public void invoke(Object sender, mxEventObject evt)
			{
				ignoreZoomChange = true;

				try
				{
					zoomCombo.setSelectedItem((int) Math.round(100 * view
							.getScale())
							+ "%");
				}
				finally
				{
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

		zoomCombo.addActionListener(new ActionListener()
		{
			/**
			 * 
			 */
			public void actionPerformed(ActionEvent e)
			{
				mxGraphComponent graphComponent = editor.getGraphComponent();

				// Zoomcombo is changed when the scale is changed in the diagram
				// but the change is ignored here
				if (!ignoreZoomChange)
				{
					String zoom = zoomCombo.getSelectedItem().toString();

					if (zoom.equals(mxResources.get("page")))
					{
						graphComponent.setPageVisible(true);
						graphComponent
								.setZoomPolicy(mxGraphComponent.ZOOM_POLICY_PAGE);
					}
					else if (zoom.equals(mxResources.get("width")))
					{
						graphComponent.setPageVisible(true);
						graphComponent
								.setZoomPolicy(mxGraphComponent.ZOOM_POLICY_WIDTH);
					}
					else if (zoom.equals(mxResources.get("actualSize")))
					{
						graphComponent.zoomActual();
					}
					else
					{
						try
						{
							zoom = zoom.replace("%", "");
							double scale = Math.min(16, Math.max(0.01,
									Double.parseDouble(zoom) / 100));
							graphComponent.zoomTo(scale, graphComponent
									.isCenterZoom());
						}
						catch (Exception ex)
						{
							JOptionPane.showMessageDialog(editor, ex
									.getMessage());
						}
					}
				}
			}
		});
	}
}
