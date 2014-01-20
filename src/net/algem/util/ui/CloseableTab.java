/*
 * @(#)CloseableTab.java	2.8.r 17/01/14
 *
 * Copyright (c) 1999-2014 Musiques Tangentes. All Rights Reserved.
 *
 * This file is part of Algem.
 * Algem is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Algem is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with Algem. If not, see <http://www.gnu.org/licenses/>.
 *
 */

package net.algem.util.ui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import net.algem.util.BundleUtil;
import net.algem.util.ImageUtil;

/**
 * Tab with closing button.
 * @author <a href="mailto:jmg@musiques-tangentes.asso.fr">Jean-Marc Gobat</a>
 * @version 2.8.r
 */
public class CloseableTab extends JPanel implements ActionListener
{
  public final static String CLOSE_CMD = "TabClosingCmd";
	private static final String JGOODIES = "com.jgoodies.looks.plastic";
  private final TabPanel pane;
  private final ActionListener listener;
  private GemButton button;
  private final ImageIcon closeIcon = ImageUtil.createImageIcon(ImageUtil.TAB_CLOSING_ICON);

  public CloseableTab(final TabPanel pane, final ActionListener listener)
  {
    super(new FlowLayout(FlowLayout.LEFT, 0, 0));
    this.listener = listener;
    if (pane == null) {
      throw new NullPointerException("TabbedPane is null");
    }
		setOpaque(false);
    this.pane = pane;
		
		setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
    JLabel label = new JLabel()
    {
      @Override
      public String getText()
      {
        int i = pane.indexOfTabComponent(CloseableTab.this);
        if (i != -1) {
					return pane.getTitleAt(i);
        }
        return null;
      }
    };
		
		//add more space between the label and the button
		if (UIManager.getLookAndFeel().getClass().getName().startsWith(JGOODIES)) {
			label.setOpaque(true);
			label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 7));
		} else {
			label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
		}

    add(label);
    
    //tab button
    button = new GemButton(closeIcon);
    button.setContentAreaFilled(false);
    button.setFocusable(false);
    button.setBorder(BorderFactory.createEtchedBorder());
    button.setBorderPainted(false);
    button.setToolTipText(BundleUtil.getLabel("Tab.closing.tip"));
    button.addActionListener(this);
    add(button);
    //add more space to the top of the component
//    setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 5));

  }

  @Override
  public void actionPerformed(ActionEvent e)
  {   
    int i = pane.indexOfTabComponent(this);
    if (i != -1) {
      String classname = pane.getComponentAt(i).getClass().getSimpleName();
			if (classname == null || classname.isEmpty()) {
				classname = pane.getComponentAt(i).getClass().getName();
			}
      pane.remove(i);
      listener.actionPerformed(new ActionEvent(classname, ActionEvent.ACTION_PERFORMED, CLOSE_CMD));
    }

  }
}
