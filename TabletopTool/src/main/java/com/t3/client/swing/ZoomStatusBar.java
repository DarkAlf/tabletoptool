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

package com.t3.client.swing;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;

import javax.swing.JTextField;

import com.t3.client.AppState;
import com.t3.client.TabletopTool;
import com.t3.client.ui.zone.ZoneRenderer;
import com.t3.util.StringUtil;

/**
 * Manages the zoom level in the main TabletopTool window's status bar at the bottom of the window. This means displaying the
 * current zoom level as a percentage as well as allowing a value to be entered and changing the zoom level to that
 * amount.
 */
public class ZoomStatusBar extends JTextField implements ActionListener {
	private static final Dimension minSize = new Dimension(50, 10);

	public ZoomStatusBar() {
		super("", RIGHT);
		setToolTipText("Zoom Level");
		addActionListener(this);
	}

	@Override
	public boolean isEnabled() {
		return !AppState.isZoomLocked() && super.isEnabled();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JTextField target = (JTextField) e.getSource();
		if (TabletopTool.getFrame().getCurrentZoneRenderer() != null) {
			double zoom;
			ZoneRenderer renderer;
			try {
				zoom = StringUtil.parseDecimal(target.getText());
				renderer = TabletopTool.getFrame().getCurrentZoneRenderer();
				renderer.setScale(zoom / 100);
				renderer.maybeForcePlayersView();
				update();
			} catch (ParseException ex) {
				// If the number is invalid, ignore it.
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JComponent#getMinimumSize()
	 */
	@Override
	public Dimension getMinimumSize() {
		return minSize;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JComponent#getPreferredSize()
	 */
	@Override
	public Dimension getPreferredSize() {
		return getMinimumSize();
	}

	public void clear() {
		setText("");
	}

	public void update() {
		String zoom = "";
		if (TabletopTool.getFrame().getCurrentZoneRenderer() != null) {
			double scale = TabletopTool.getFrame().getCurrentZoneRenderer().getZoneScale().getScale();
			scale *= 100;
			zoom = String.format("%d%%", (int) scale);
		}
		setText(zoom);
	}
}
