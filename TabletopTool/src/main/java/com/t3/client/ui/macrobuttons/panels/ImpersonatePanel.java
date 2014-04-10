/*
 * Copyright (c) 2014 tabletoptool.com team.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     rptools.com team - initial implementation
 *     tabletoptool.com team - further development
 */
package com.t3.client.ui.macrobuttons.panels;

import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import com.jidesoft.docking.DockableFrame;
import com.t3.client.AppStyle;
import com.t3.client.AppUtil;
import com.t3.client.TabletopTool;
import com.t3.client.ui.T3Frame;
import com.t3.client.ui.T3Frame.MTFrame;
import com.t3.language.I18N;
import com.t3.GUID;
import com.t3.model.Token;

public class ImpersonatePanel extends AbstractMacroPanel {
	private boolean currentlyImpersonating = false;

	public ImpersonatePanel() {
		setPanelClass("ImpersonatePanel");
		TabletopTool.getEventDispatcher().addListener(this,
				TabletopTool.ZoneEvent.Activated);
	}

	public void init() {
		boolean panelVisible = true;
		final T3Frame mtf = TabletopTool.getFrame();

		// Get the current visibility / autohide state of the Impersonate panel
		if (mtf != null) {
			DockableFrame impersonatePanel = mtf.getDockingManager().getFrame("IMPERSONATED");
			if (impersonatePanel != null)
				panelVisible = (impersonatePanel.isVisible() && !impersonatePanel.isAutohide()) || impersonatePanel.isAutohideShowing() ? true : false;
		}
		// Only repaint the panel if its visible
		if (panelVisible && mtf != null && mtf.getCurrentZoneRenderer() != null) {
			List<Token> selectedTokenList = mtf.getCurrentZoneRenderer().getSelectedTokensList();

			if (currentlyImpersonating && getToken() != null) {
				Token token = getToken();
				mtf.getFrame(MTFrame.IMPERSONATED).setFrameIcon(token.getIcon(16, 16));
				mtf.getFrame(MTFrame.IMPERSONATED).setTitle(getTitle(token));
				addArea(getTokenId());
			} else if (selectedTokenList.size() != 1) {
				return;
			} else {
				// add the "Impersonate Selected" button
				final Token t = selectedTokenList.get(0);

				if (AppUtil.playerOwns(t)) {
					JButton button = new JButton(I18N.getText("panel.Impersonate.button.impersonateSelected"), t.getIcon(16, 16)) {
						private static final long serialVersionUID = 1L;

						@Override
						public Insets getInsets() {
							return new Insets(2, 2, 2, 2);
						}
					};
					button.addMouseListener(new MouseAdapter() {
						@Override
						public void mouseClicked(MouseEvent event) {
							TabletopTool.getFrame().getCommandPanel().quickCommit("/im " + t.getId(), false);
						}
					});
					button.setBackground(null);
					add(button);
				}
			}
		}
	}

	public void startImpersonating(Token token) {
		stopImpersonating();
		setTokenId(token);
		currentlyImpersonating = true;
		token.setBeingImpersonated(true);
		reset();
	}

	public void stopImpersonating() {
		Token token = getToken();
		if (token != null) {
			token.setBeingImpersonated(false);
		}
		setTokenId((GUID) null);
		currentlyImpersonating = false;
		clear();
	}

	public String getTitle(Token token) {
		if (token.getGMName() != null && token.getGMName().trim().length() > 0) {
			return token.getName() + " (" + token.getGMName() + ")";
		} else {
			return token.getName();
		}
	}

	@Override
	public void clear() {
		removeAll();
		TabletopTool.getFrame().getFrame(MTFrame.IMPERSONATED).setFrameIcon(new ImageIcon(AppStyle.impersonatePanelImage));
		TabletopTool.getFrame().getFrame(MTFrame.IMPERSONATED).setTitle(Tab.IMPERSONATED.title);
		if (getTokenId() == null) {
			currentlyImpersonating = false;
		}
		doLayout();
		revalidate();
		repaint();
	}

	@Override
	public void reset() {
		clear();
		init();
	}

	/**
	 * This method is currently not used and (likely) hasn't been kept up to date with the rest of the code. I've marked
	 * it as deprecated to reflect this and to warn anyone who calls it.
	 */
	@Deprecated
	public void addCancelButton() {
		ImageIcon i = new ImageIcon(AppStyle.cancelButton);
		JButton button = new
				JButton("Cancel Impersonation", i) {
					@Override
					public Insets getInsets() {
						return new Insets(3, 3, 3, 3);
					}
				};
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent event) {
				TabletopTool.getFrame().getCommandPanel().quickCommit("/im");
			}
		});
		button.setBackground(null);
		add(button);
	}
}
