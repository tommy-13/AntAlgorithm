package streetmap.task;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.Point;

import language.Messages;
import streetmap.view.general.StreetPanel;
import streetmap.view.graphicalElements.JStreetField;

public class ZoomTask implements Task {
	
	private StreetPanel 		streetPanel;
	private Point				dimension;
	private JStreetField[][]	fields;
	private GridBagConstraints[][] gbcs;
	private boolean				bigFields;
	private String				title;
	private String				text;
	
	public ZoomTask(StreetPanel streetPanel, Point dimension, JStreetField[][] fields,
			GridBagConstraints[][] gbcs, boolean bigFields) {
		this.streetPanel	= streetPanel;
		this.dimension		= dimension;
		this.fields			= fields;
		this.gbcs			= gbcs;
		this.bigFields		= bigFields;
		title = Messages.getString("Task.ZoomTitle");
		text  = Messages.getString("Task.ZoomText");
	}
	
	@Override
	public boolean isDone() {
		return true;
	}
	@Override
	public void execute() {
		streetPanel.removeAll();
		streetPanel.validate();
		for(int i=0; i<dimension.x; i++) {
			for(int j=0; j<dimension.y; j++) {
				fields[i][j].setBigPictures(bigFields);
				streetPanel.add(fields[i][j], gbcs[i][j]);
			}
		}
		streetPanel.validate();
	}
	@Override
	public Component getParent() {
		return streetPanel;
	}
	@Override
	public String getTaskTitle() {
		return title;
	}
	@Override
	public String getTaskText() {
		return text;
	}

}
