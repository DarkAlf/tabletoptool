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

package com.t3.client.ui;

import org.apache.log4j.Logger;

import com.jidesoft.docking.event.DockableFrameEvent;
import com.jidesoft.docking.event.DockableFrameListener;
import com.t3.client.TabletopTool;

/**
 * This class acts as a listener to the various dockable frames that TabletopTool uses.
 * 
 * Because rendering of the Selection and Impersonate panels is now suppressed when they are not visible (to improve
 * performance) this class resets those panels when they become visible (so that the user sees a seamless transition and
 * does not have to select the token again to get the selection / impersonate panels to populate).
 * 
 * @author Rumble
 * 
 */
public class T3DockListener implements DockableFrameListener {
	private static final Logger log = Logger.getLogger(T3DockListener.class);

	@Override
	public void dockableFrameActivated(DockableFrameEvent dfe) {
		showEvent(dfe.toString());
	}

	@Override
	public void dockableFrameAdded(DockableFrameEvent dfe) {
		showEvent(dfe.toString());
	}

	@Override
	public void dockableFrameAutohidden(DockableFrameEvent dfe) {
		showEvent(dfe.toString());
		updatePanels(dfe.getDockableFrame().getName());
	}

	@Override
	public void dockableFrameAutohideShowing(DockableFrameEvent dfe) {
		updatePanels(dfe.getDockableFrame().getName());
		showEvent(dfe.toString());
	}

	@Override
	public void dockableFrameDeactivated(DockableFrameEvent dfe) {
		showEvent(dfe.toString());
	}

	@Override
	public void dockableFrameDocked(DockableFrameEvent dfe) {
		showEvent(dfe.toString());
		updatePanels(dfe.getDockableFrame().getName());
	}

	@Override
	public void dockableFrameFloating(DockableFrameEvent dfe) {
		showEvent(dfe.toString());
	}

	@Override
	public void dockableFrameHidden(DockableFrameEvent dfe) {
	}

	@Override
	public void dockableFrameMaximized(DockableFrameEvent dfe) {
		showEvent(dfe.toString());
	}

	@Override
	public void dockableFrameRemoved(DockableFrameEvent dfe) {
	}

	@Override
	public void dockableFrameRestored(DockableFrameEvent dfe) {
		showEvent(dfe.toString());
	}

	@Override
	public void dockableFrameShown(DockableFrameEvent dfe) {
		updatePanels(dfe.getDockableFrame().getName());
		showEvent(dfe.toString());
	}

	@Override
	public void dockableFrameTabHidden(DockableFrameEvent dfe) {
		showEvent(dfe.toString());
	}

	@Override
	public void dockableFrameTabShown(DockableFrameEvent dfe) {
		updatePanels(dfe.getDockableFrame().getName());
		showEvent(dfe.toString());
	}

	@Override
	public void dockableFrameMoved(DockableFrameEvent dfe) {
		showEvent(dfe.toString());
	}

	@Override
	public void dockableFrameTransferred(DockableFrameEvent dfe) {
		showEvent(dfe.toString());
	}

	/**
	 * Updates the Selected or Impersonated panel when it becomes visible to improve performance for moving and
	 * selecting tokens.
	 * 
	 * @param panel
	 *            the panel to be updated
	 */
	private void updatePanels(String panel) {
		if (TabletopTool.getFrame() != null) {
			if (panel == "SELECTION") {
				TabletopTool.getFrame().getSelectionPanel().reset();
			}
			if (panel == "IMPERSONATED") {
				TabletopTool.getFrame().getImpersonatePanel().reset();
			}
		}
	}

	/**
	 * Logging convenience function to show which events are fired
	 * 
	 * @param dfeId
	 *            the DockableFrameEvent to record
	 */
	private void showEvent(String dfeId) {
		if (log.isTraceEnabled())
			log.trace(dfeId);
	}
}
