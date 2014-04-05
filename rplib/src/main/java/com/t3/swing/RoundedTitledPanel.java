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

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;

import javax.swing.JPanel;

public class RoundedTitledPanel extends JPanel {

	public RoundedTitledPanel() {
		super.setLayout(new RoundedTitlePanelLayout());
	}
	
	@Override
	public void setLayout(LayoutManager mgr) {
		throw new IllegalAccessError("Can't change the layout");
	}
	
	private class RoundedTitlePanelLayout implements LayoutManager {

		@Override
		public void addLayoutComponent(String name, Component comp) {
			
		}
		@Override
		public void layoutContainer(Container parent) {
		}
		@Override
		public Dimension minimumLayoutSize(Container parent) {
			return null;
		}
		@Override
		public Dimension preferredLayoutSize(Container parent) {
			return null;
		}
		@Override
		public void removeLayoutComponent(Component comp) {
		}
	}
}
