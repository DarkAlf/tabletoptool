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
import java.awt.GridLayout;
import java.awt.Rectangle;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.Scrollable;

public class ScrollablePanel extends JPanel implements Scrollable {

	private int unitIncrement;
	private int blockIncrement;

	public ScrollablePanel(JComponent component, int unitIncrement, int blockIncrement) {
		setLayout(new GridLayout());
		add(component);
		
		this.unitIncrement = unitIncrement;
		this.blockIncrement = blockIncrement;
	}

	public ScrollablePanel(JComponent component, int unitIncrement) {
		this(component, unitIncrement, unitIncrement * 3);
	}

	public ScrollablePanel(JComponent component) {
		this(component, 20);
	}
	
	public static JScrollPane wrap(JComponent component) {
		
		JScrollPane pane = new JScrollPane(new ScrollablePanel(component), JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		return pane;
	}
	
	////
	// SCROLLABLE
	@Override
	public Dimension getPreferredScrollableViewportSize() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
		return blockIncrement;
	}
	@Override
	public boolean getScrollableTracksViewportHeight() {
		return false;
	}
	@Override
	public boolean getScrollableTracksViewportWidth() {
		return true;
	}
	@Override
	public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
		return unitIncrement;
	}
}
