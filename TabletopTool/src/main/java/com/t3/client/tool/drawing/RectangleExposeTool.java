/*
 *  This software copyright by various authors including the RPTools.net
 *  development team, and licensed under the LGPL Version 3 or, at your
 *  option, any later version.
 *
 *  Portions of this software were originally covered under the Apache
 *  Software License, Version 1.1 or Version 2.0.
 *
 *  See the file LICENSE elsewhere in this distribution for license details.
 */

package com.t3.client.tool.drawing;

import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.geom.Area;
import java.io.IOException;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import com.t3.client.TabletopTool;
import com.t3.client.ui.zone.ZoneRenderer;
import com.t3.model.GUID;
import com.t3.model.Zone;
import com.t3.model.drawing.Drawable;
import com.t3.model.drawing.Pen;

public class RectangleExposeTool extends RectangleTool {
	private static final long serialVersionUID = 2072551559910263728L;

	public RectangleExposeTool() {
		try {
			setIcon(new ImageIcon(ImageIO.read(getClass().getClassLoader().getResourceAsStream("com/t3/client/image/tool/fog-blue-rect.png"))));
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	@Override
	public boolean isAvailable() {
		return TabletopTool.getPlayer().isGM();
	}

	@Override
	public String getInstructions() {
		return "tool.rectexpose.instructions";
	}

	@Override
	// Override abstracttool to prevent color palette from
	// showing up
	protected void attachTo(ZoneRenderer renderer) {
		super.attachTo(renderer);
		// Hide the drawable color palette
		TabletopTool.getFrame().hideControlPanel();
	}

	@Override
	protected boolean isBackgroundFill(MouseEvent e) {
		// Expose tools are implied to be filled
		return false;
	}

	@Override
	protected Pen getPen() {
		Pen pen = super.getPen();
		pen.setBackgroundMode(Pen.MODE_TRANSPARENT);
		pen.setThickness(1);
		return pen;
	}

	@Override
	protected void completeDrawable(GUID zoneId, Pen pen, Drawable drawable) {
		if (!TabletopTool.getPlayer().isGM()) {
			TabletopTool.showError("msg.error.fogexpose");
			TabletopTool.getFrame().refresh();
			return;
		}
		Zone zone = TabletopTool.getCampaign().getZone(zoneId);

		Rectangle bounds = drawable.getBounds();
		Area area = new Area(bounds);
		Set<GUID> selectedToks = TabletopTool.getFrame().getCurrentZoneRenderer().getSelectedTokenSet();
		if (pen.isEraser()) {
			zone.hideArea(area, selectedToks);
			TabletopTool.serverCommand().hideFoW(zone.getId(), area, selectedToks);
		} else {
			zone.exposeArea(area, selectedToks);
			TabletopTool.serverCommand().exposeFoW(zone.getId(), area, selectedToks);
		}
		TabletopTool.getFrame().refresh();
	}

	@Override
	public String getTooltip() {
		return "tool.rectexpose.tooltip";
	}
}