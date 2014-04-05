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

import javax.swing.JLabel;

/**
 */
public class SpacerStatusBar extends JLabel {

    private Dimension minSize = new Dimension(0, 10);
    
    public SpacerStatusBar(int size) {
    	minSize = new Dimension(size, 10);
    }
    
    @Override
	public Dimension getMinimumSize() {
        return minSize;
    }
    
    @Override
	public Dimension getPreferredSize() {
        return getMinimumSize();
    }
}
