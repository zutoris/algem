/*
 * @(#)GemPanel.java	2.6.a 31/07/12
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

import java.awt.Insets;
import java.awt.LayoutManager;

/**
 * JPanel extension.
 * 
 * @author <a href="mailto:eric@musiques-tangentes.asso.fr">Eric</a>
 * @author <a href="mailto:jmg@musiques-tangentes.asso.fr">Jean-Marc Gobat</a>
 * @version 2.6.a
 * @since 1.0a 07/07/1999
 */
public class GemPanel
        extends javax.swing.JPanel
{

  private Insets insets = null;

  public GemPanel() {
  }

  public GemPanel (LayoutManager layout) {
    super(layout);
  }

  public GemPanel(Insets i) {
		this.insets = i;
  }

  @Override
  public Insets getInsets() {
    return insets == null ? super.getInsets() : insets;
  }
}
