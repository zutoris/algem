/*
 * @(#)FrameDetach.java	2.6.a 25/09/12
 * 
 * Copyright (c) 1999-2012 Musiques Tangentes. All Rights Reserved.
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

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import net.algem.util.module.GemDesktopCtrl;

/**
 * comment
 *
 * @author <a href="mailto:eric@musiques-tangentes.asso.fr">Eric</a>
 * @author <a href="mailto:jmg@musiques-tangentes.asso.fr">Jean-Marc Gobat</a>
 * @version 2.6.a
 */
public class FrameDetach
        extends JFrame
        implements ActionListener
{

  private GemDesktopCtrl desktop;
  private String label;
  private Container panel;
  private GemButton btQuit;

  public FrameDetach(GemDesktopCtrl _desktop, String _label, Container p) {
    super("Algem " + _label);

    desktop = _desktop;
    label = _label;
    panel = p;

    btQuit = new GemButton("Menu.quit.label");
    btQuit.addActionListener(this);

    Container pan = getContentPane();
    pan.setLayout(new BorderLayout());
    pan.add(panel, BorderLayout.CENTER);
    pan.add(btQuit, BorderLayout.SOUTH);

    setFont(new Font("Helvetica", Font.PLAIN, 14));

    enableEvents(AWTEvent.WINDOW_EVENT_MASK);
  }

  public void dumpComp() {
    Component c[] = panel.getComponents();
    for (int i = 0; i < c.length; i++) {
      System.out.println("COMP:" + i + " = " + c[i]);
    }
  }

  public boolean canClose() {
    return true;
  }

  public void close() {
    setVisible(false);
    dispose();
    if (desktop != null) {
      desktop.addPanel(label, panel);
    }
  }

  @Override
  public void processWindowEvent(WindowEvent e) {
    if (e.getID() == WindowEvent.WINDOW_CLOSING) {
      if (canClose()) {
        super.processWindowEvent(e);
        close();
      }
    } else {
      super.processWindowEvent(e);
    }
  }

  @Override
  public void actionPerformed(ActionEvent evt) {
    if (evt.getActionCommand().equals("Quitter")) {
      if (canClose()) {
        close();
      }
    }
  }
}
