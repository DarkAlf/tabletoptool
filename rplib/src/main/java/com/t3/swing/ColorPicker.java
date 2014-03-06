/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 */
package com.t3.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Paint;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

import com.jeta.forms.components.panel.FormPanel;
import com.jeta.forms.gui.form.FormAccessor;

public class ColorPicker extends JPanel {

    private Component owner;

    private PaintedPanel foregroundColor;
    private PaintedPanel backgroundColor;
    private List<PaintedPanel> recentColors = new ArrayList<PaintedPanel>(16);
    private JToggleButton snapToggle;
    private JToggleButton eraseToggle;
    private PaintChooser paintChooser;
    private JSpinner penWidthSpinner;
    private JSpinner transparencySpinner;

    private static final int RECENT_COLOR_LIST_SIZE = 16;
    
    private static final int maxPenWidth = 300;

    private static final Color[] DEFAULT_COLORS = new Color[] { null, Color.black, Color.darkGray, Color.lightGray, Color.white, Color.pink,
            new Color(127, 0, 0), Color.red, Color.orange, Color.yellow, new Color(0, 127, 0), Color.green, Color.blue, Color.cyan,
            new Color(127, 0, 127), Color.magenta, new Color(127 + 32, 127, 61), };

    @SuppressWarnings("unchecked")
    public ColorPicker(Component owner) {
        this.owner = owner;
        
        paintChooser = new PaintChooser();
		paintChooser.setPreferredSize(new Dimension(450, 400));

        FormPanel panel = new FormPanel("com/t3/swing/forms/colorPanel.xml");

        ColorWellListener listener = new ColorWellListener(1);

        foregroundColor = new PaintedPanel();
        backgroundColor = new PaintedPanel();

        JPanel wrappedForeground = new JPanel(new GridLayout());
        wrappedForeground.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        wrappedForeground.add(foregroundColor);
        foregroundColor.addMouseListener(listener);
        
        JPanel wrappedBackground = new JPanel(new GridLayout());
        wrappedBackground.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        wrappedBackground.add(backgroundColor);
        backgroundColor.addMouseListener(listener);
        
        FormAccessor accessor = panel.getFormAccessor( "colorPanel" );
        
        accessor.replaceBean("foregroundColor", wrappedForeground);
        accessor.replaceBean("backgroundColor", wrappedBackground);
        
        listener = new ColorWellListener(2);
        accessor = panel.getFormAccessor("recentColors");
        for (int i = 0; i < RECENT_COLOR_LIST_SIZE; i++) {
        	PaintedPanel paintedPanel = new PaintedPanel();
        	paintedPanel.setPreferredSize(new Dimension(15, 15));
        	
        	JPanel wrappedPanel = new JPanel(new GridLayout());
        	wrappedPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        	wrappedPanel.add(paintedPanel);
        	
            accessor.replaceBean("recentColor" + i, wrappedPanel);
            paintedPanel.addMouseListener(listener);
            recentColors.add(paintedPanel);
        }

        snapToggle = (JToggleButton) panel.getButton("toggleSnapToGrid");
        eraseToggle = (JToggleButton) panel.getButton("toggleErase");

        penWidthSpinner = panel.getSpinner("penWidth");
        penWidthSpinner.setModel(new SpinnerNumberModel(3, 1, maxPenWidth, 1));
        penWidthSpinner.addChangeListener(new ChangeListener() {
        	public void stateChanged(ChangeEvent e) {
        		try {
        			penWidthSpinner.commitEdit();
        		} catch (ParseException pe) {
        			pe.printStackTrace();
        		}
        	}
        });
        
        
        transparencySpinner = panel.getSpinner("opacity");
        transparencySpinner.setModel(new SpinnerNumberModel(100, 1, 100, 1));
        transparencySpinner.addChangeListener(new ChangeListener() {
        	public void stateChanged(ChangeEvent e) {
        		try {
        			transparencySpinner.commitEdit();
        		} catch (ParseException pe) {
        			pe.printStackTrace();
        		}
        	}
        });
        
        initialize();

        add(panel);
    }
    
    public PaintChooser getPaintChooser() {
    	return paintChooser;
    }

    public void initialize() {
        
        foregroundColor.setPaint(Color.BLACK);
        backgroundColor.setPaint(Color.WHITE);

        for (int i = 0; i < DEFAULT_COLORS.length && i < RECENT_COLOR_LIST_SIZE; i++) {
            recentColors.get(i).setPaint(DEFAULT_COLORS[i]);
        }
        
    }

    public void setForegroundPaint(Paint paint) {
        foregroundColor.setPaint(paint);
    }

    public void setBackgroundPaint(Paint paint) {
        backgroundColor.setPaint(paint);
    }

    public boolean isFillForegroundSelected() {
        return foregroundColor.getPaint() != null;
    }
    
    public boolean isFillBackgroundSelected() {
        return backgroundColor.getPaint() != null;
    }
    
    public void setEraseSelected(boolean selected) {
    	eraseToggle.setSelected(selected);
    }
    
    public boolean isEraseSelected() {
        return eraseToggle.isSelected();
    }
    
    public void setSnapSelected(boolean selected) {
    	snapToggle.setSelected(selected);
    }

    public boolean isSnapSelected() {
        return snapToggle.isSelected();
    }

    public void setTranslucency(int percent) {
    	percent = Math.max(0, percent);
    	percent = Math.min(100, percent);
    	
        transparencySpinner.setValue(percent);
    }

    public void setPenWidth(int width) {
    	width = Math.max(0, width);
    	width = Math.min(maxPenWidth, width);
    	
        penWidthSpinner.setValue(width);
    }

    public Paint getForegroundPaint() {
        return foregroundColor.getPaint();
    }

    public Paint getBackgroundPaint() {
        return backgroundColor.getPaint();
    }
    
    public int getStrokeWidth() {
    	return (Integer)penWidthSpinner.getValue();
    }
    
    public float getOpacity() {
    	return ((Integer)transparencySpinner.getValue()) / 100.0f;
    }
    
    public class ColorWellListener extends MouseAdapter {
    	
    	private int clickCount;
    	public ColorWellListener(int clickCount) {
    		this.clickCount = clickCount;
    	}
    	
        public void mouseClicked(MouseEvent evt) {

            PaintedPanel comp = (PaintedPanel) evt.getSource();

            if (evt.getClickCount() == clickCount) {
                Paint result = paintChooser.choosePaint((JFrame)owner, comp.getPaint()); // TODO: This is a bad cast, be more smart about it
                comp.setPaint(result);
                return;
            }

            switch (evt.getButton()) {
            case MouseEvent.BUTTON1:
                foregroundColor.setPaint(comp.getPaint());
                break;
            case MouseEvent.BUTTON3:
                backgroundColor.setPaint(comp.getPaint());
                break;
            }
        }
    }
    
}
