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
package com.t3.client.ui;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;

import com.t3.client.AppActions;
import com.t3.client.AppActions.OpenUrlAction;
import com.t3.client.AppConstants;
import com.t3.client.AppSetup;
import com.t3.client.AppState;
import com.t3.client.AppUtil;
import com.t3.client.MRUCampaignManager;
import com.t3.client.TabletopTool;
import com.t3.client.ui.T3Frame.MTFrame;
import com.t3.language.I18N;
import com.t3.model.Zone;
import com.t3.persistence.FileUtil;

public class AppMenuBar extends JMenuBar {
	private static MRUCampaignManager mruManager;

	public AppMenuBar() {
		add(createFileMenu());
		add(createEditMenu());
		add(createMapMenu());
		add(createViewMenu());
		add(createToolsMenu());
		add(createWindowMenu());
		add(createHelpMenu());
	}

	// This is a hack to allow the menubar shortcut keys to still work even
	// when it isn't showing (fullscreen mode)
	@Override
	public boolean isShowing() {
		return TabletopTool.getFrame() != null && TabletopTool.getFrame().isFullScreen() ? true : super.isShowing();
	}

	protected JMenu createFileMenu() {
		JMenu fileMenu = I18N.createMenu("menu.file");

		// MAP CREATION
		fileMenu.add(new JMenuItem(AppActions.NEW_CAMPAIGN));
		fileMenu.add(new JMenuItem(AppActions.LOAD_CAMPAIGN));
		fileMenu.add(new JMenuItem(AppActions.SAVE_CAMPAIGN));
		fileMenu.add(new JMenuItem(AppActions.SAVE_CAMPAIGN_AS));

		fileMenu.add(new JMenuItem(AppActions.SAVE_MESSAGE_HISTORY));
		fileMenu.addSeparator();
		fileMenu.add(createExportMenu());
		fileMenu.addSeparator();
		fileMenu.add(new JMenuItem(AppActions.ADD_RESOURCE_TO_LIBRARY));
		fileMenu.addSeparator();
		fileMenu.add(new JMenuItem(AppActions.START_SERVER));
		fileMenu.add(new JMenuItem(AppActions.CONNECT_TO_SERVER));
		fileMenu.add(new JMenuItem(AppActions.DISCONNECT_FROM_SERVER));
		fileMenu.add(new JMenuItem(AppActions.SHOW_CONNECTION_INFO));
		fileMenu.addSeparator();
		fileMenu.add(createRecentCampaignMenu());
		if (!TabletopTool.MAC_OS_X) {
			fileMenu.addSeparator();
			fileMenu.add(new JMenuItem(AppActions.EXIT));
		}
		return fileMenu;
	}

	protected JMenu createExportMenu() {
		JMenu menu = new JMenu(I18N.getText("menu.export"));

		menu.add(new JMenuItem(AppActions.EXPORT_SCREENSHOT_LAST_LOCATION));
		menu.add(new JMenuItem(AppActions.EXPORT_SCREENSHOT));

		menu.addSeparator();

		menu.add(new JMenuItem(AppActions.EXPORT_CAMPAIGN_REPO));
//		menu.add(new JMenuItem(AppActions.UPDATE_CAMPAIGN_REPO));

		return menu;
	}

	protected JMenu createMapMenu() {
		JMenu menu = I18N.createMenu("menu.map");

		menu.add(new JMenuItem(AppActions.NEW_MAP));
		menu.add(createQuickMapMenu());
		menu.add(new JMenuItem(AppActions.LOAD_MAP));
		menu.add(new JMenuItem(AppActions.SAVE_MAP_AS));
		menu.addSeparator();

		// MAP TOGGLES
		menu.add(new RPCheckBoxMenuItem(AppActions.TOGGLE_CURRENT_ZONE_VISIBILITY, menu));
		menu.add(new RPCheckBoxMenuItem(AppActions.TOGGLE_FOG, menu));
		menu.add(createVisionTypeMenu());

		menu.addSeparator();

		menu.add(new JMenuItem(AppActions.EDIT_MAP));
		menu.add(new JMenuItem(AppActions.ADJUST_GRID));
		menu.add(new JMenuItem(AppActions.ADJUST_BOARD));
		menu.add(new JMenuItem(AppActions.RENAME_ZONE));
		menu.add(new JMenuItem(AppActions.COPY_ZONE));
		menu.add(new JMenuItem(AppActions.REMOVE_ZONE));

		return menu;
	}

	protected JMenu createVisionTypeMenu() {
		JMenu menu = I18N.createMenu("menu.vision");

		menu.add(new RPCheckBoxMenuItem(new AppActions.SetVisionType(Zone.VisionType.OFF), menu));
		menu.add(new RPCheckBoxMenuItem(new AppActions.SetVisionType(Zone.VisionType.DAY), menu));
		menu.add(new RPCheckBoxMenuItem(new AppActions.SetVisionType(Zone.VisionType.NIGHT), menu));

		return menu;
	}

	protected JMenu createToolsMenu() {
		JMenu menu = I18N.createMenu("menu.tools");
		menu.add(new JMenuItem(AppActions.CHAT_COMMAND));
		menu.add(new JMenuItem(AppActions.ENTER_COMMAND));
		menu.add(new JMenuItem(AppActions.ENFORCE_ZONE_VIEW));
		menu.add(new JMenuItem(AppActions.ENFORCE_ZONE));
		menu.add(new RPCheckBoxMenuItem(AppActions.TOGGLE_LINK_PLAYER_VIEW, menu));
		menu.add(new RPCheckBoxMenuItem(AppActions.TOGGLE_MOVEMENT_LOCK, menu));
		menu.add(new RPCheckBoxMenuItem(AppActions.TOGGLE_ZOOM_LOCK, menu));
		menu.add(new RPCheckBoxMenuItem(AppActions.TOGGLE_ENFORCE_NOTIFICATION, menu));

		menu.add(new JSeparator());

		menu.add(new RPCheckBoxMenuItem(AppActions.TOGGLE_COLLECT_PROFILING_DATA, menu));

		return menu;
	}

	protected JMenu createEditMenu() {
		JMenu menu = I18N.createMenu("menu.edit");
		menu.add(new JMenuItem(AppActions.UNDO_PER_MAP));
		menu.add(new JMenuItem(AppActions.REDO_PER_MAP));
//		menu.add(new JMenuItem(AppActions.UNDO_DRAWING));
//		menu.add(new JMenuItem(AppActions.REDO_DRAWING));
		menu.add(new JMenuItem(AppActions.CLEAR_DRAWING));

		menu.addSeparator();

		menu.add(new JMenuItem(AppActions.COPY_TOKENS));
		menu.add(new JMenuItem(AppActions.CUT_TOKENS));
		menu.add(new JMenuItem(AppActions.PASTE_TOKENS));

		menu.addSeparator();

		menu.add(new JMenuItem(AppActions.CAMPAIGN_PROPERTIES));
		if (!TabletopTool.MAC_OS_X)
			menu.add(new JMenuItem(AppActions.SHOW_PREFERENCES));

		return menu;
	}

	protected JMenu createViewMenu() {
		JMenu menu = I18N.createMenu("menu.view");
		menu.add(new RPCheckBoxMenuItem(AppActions.TOGGLE_SHOW_PLAYER_VIEW, menu));

		menu.addSeparator();

		menu.add(createZoomMenu());
		menu.add(new JMenuItem(AppActions.TOGGLE_SHOW_TOKEN_NAMES));

		JCheckBoxMenuItem item = new RPCheckBoxMenuItem(AppActions.TOGGLE_SHOW_MOVEMENT_MEASUREMENTS, menu);
		item.setSelected(AppState.getShowMovementMeasurements());
		menu.add(item);

		item = new RPCheckBoxMenuItem(AppActions.TOGGLE_SHOW_LIGHT_SOURCES, menu);
		item.setSelected(AppState.isShowLightSources());
		menu.add(item);

		// menu.add(new RPCheckBoxMenuItem(AppActions.TOGGLE_ZONE_SELECTOR));
		menu.add(new RPCheckBoxMenuItem(AppActions.TOGGLE_GRID, menu));
		menu.add(new RPCheckBoxMenuItem(AppActions.TOGGLE_COORDINATES, menu));
		// LATER: This needs to be genericized, but it seems to constant, and so
		// short, that I
		// didn't feel compelled to do that in this impl
		JMenu gridSizeMenu = I18N.createMenu("action.gridSize");
		JCheckBoxMenuItem gridSize1 = new RPCheckBoxMenuItem(new AppActions.GridSizeAction(1), menu);
		JCheckBoxMenuItem gridSize2 = new RPCheckBoxMenuItem(new AppActions.GridSizeAction(2), menu);
		JCheckBoxMenuItem gridSize3 = new RPCheckBoxMenuItem(new AppActions.GridSizeAction(3), menu);
		JCheckBoxMenuItem gridSize5 = new RPCheckBoxMenuItem(new AppActions.GridSizeAction(5), menu);

		ButtonGroup sizeGroup = new ButtonGroup();
		sizeGroup.add(gridSize1);
		sizeGroup.add(gridSize2);
		sizeGroup.add(gridSize3);
		sizeGroup.add(gridSize5);

		gridSizeMenu.add(gridSize1);
		gridSizeMenu.add(gridSize2);
		gridSizeMenu.add(gridSize3);
		gridSizeMenu.add(gridSize5);
		menu.add(gridSizeMenu);

		menu.addSeparator();
		menu.add(new RPCheckBoxMenuItem(AppActions.TOGGLE_DRAW_MEASUREMENTS, menu));
		menu.add(new RPCheckBoxMenuItem(AppActions.TOGGLE_DOUBLE_WIDE, menu));

		menu.addSeparator();
		menu.add(new JMenuItem(AppActions.SHOW_FULLSCREEN));

		return menu;
	}

	protected JMenu createQuickMapMenu() {
		JMenu menu = I18N.createMenu("menu.QuickMap");

		File textureDir = AppUtil.getAppHome("resource/Default/Textures");

		// Make sure the images exist
		if (textureDir.listFiles().length == 0) {
			try {
				AppSetup.installDefaultTokens();
			} catch (IOException ioe) {
				ioe.printStackTrace();
				menu.add(new JMenuItem(I18N.getText("msg.error.loadingQuickMaps")));
				return menu;
			}
		}
		File[] listFiles = textureDir.listFiles(AppConstants.IMAGE_FILE_FILTER);
		// This shouldn't happen unless the prepackaged tabletoptool-resources.zip becomes corrupted somehow?!
		if (listFiles != null) {
			for (File file : listFiles) {
				menu.add(new JMenuItem(new AppActions.QuickMapAction(FileUtil.getNameWithoutExtension(file), file)));
			}
		}
		// basicQuickMap.putValue(Action.ACCELERATOR_KEY,
		// KeyStroke.getKeyStroke("ctrl shift N"));

		return menu;
	}

	/**
	 * Builds the help menu. This menu contains a block of special url items.
	 * These items are populated from {@link I18N#getUrlActionKeys()}.
	 * 
	 * @return the help menu
	 */
	protected JMenu createHelpMenu() {
		JMenu menu = I18N.createMenu("menu.help");
		menu.add(new JMenuItem(AppActions.ADD_DEFAULT_TABLES));
		menu.add(new JMenuItem(AppActions.RESTORE_DEFAULT_IMAGES));
		menu.addSeparator();

		// @formatter:off
		/* 
		 * This next line will retrieve all properties that match the regex, such as:
		 *		action.helpurl.01=http://tabletoptool.com
		 *		action.helpurl.02=http://something.other
		 * The items are not returned from the method in any kind of order so they are alphabetized here so that their
		 * display in the menu is predictable.
		 */
		// @formatter:on
		List<String> helpItems = I18N.getMatchingKeys("^action[.]helpurl[.]\\d+$");
		if (!helpItems.isEmpty()) {
			String[] helpArray = helpItems.toArray(new String[0]);
			Arrays.sort(helpArray);
			for (String key : helpArray) {
				OpenUrlAction temp = new AppActions.OpenUrlAction(key);
				/*
				 * TODO This could be more efficient by using ImageManager or
				 * AssetManager, but I'm not sure those facilities have been
				 * initialized by the time this code is executed so this is
				 * safer. :-/
				 */
				menu.add(new JMenuItem(temp));
			}
			menu.addSeparator();
		}
		menu.add(new JMenuItem(AppActions.GATHER_DEBUG_INFO));
		if (!TabletopTool.MAC_OS_X) {
			menu.addSeparator();
			menu.add(new JMenuItem(AppActions.SHOW_ABOUT));
		}
		return menu;
	}

	protected JMenu createZoomMenu() {
		JMenu menu = I18N.createMenu("menu.zoom");
		menu.add(new JMenuItem(AppActions.ZOOM_IN));
		menu.add(new JMenuItem(AppActions.ZOOM_OUT));
		menu.add(new JMenuItem(AppActions.ZOOM_RESET));

		return menu;
	}

	protected JMenu createWindowMenu() {
		JMenu menu = I18N.createMenu("menu.window");

		menu.add(new AbstractAction() {
			{
				putValue(Action.NAME, I18N.getText("msg.info.restoreLayout"));
			}

			@Override
			public void actionPerformed(ActionEvent e) {
				TabletopTool.getFrame().getDockingManager().resetToDefault();
			}
		});
		menu.addSeparator();

		for (MTFrame frame : T3Frame.MTFrame.values()) {
			JCheckBoxMenuItem menuItem = new RPCheckBoxMenuItem(new AppActions.ToggleWindowAction(frame), menu);
			menu.add(menuItem);
		}
		menu.addSeparator();
		menu.add(new JMenuItem(AppActions.SHOW_TRANSFER_WINDOW));

		return menu;
	}

	protected JMenu createRecentCampaignMenu() {
		mruManager = new MRUCampaignManager(new JMenu(AppActions.MRU_LIST));
		return mruManager.getMRUMenu();
	}

	public static MRUCampaignManager getMruManager() {
		return mruManager;
	}
}
