/*
 * @(#)RoomRateTableModel.java	2.9.4.13 07/10/15
 *
 * Copyright (c) 1999-2015 Musiques Tangentes. All Rights Reserved.
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
package net.algem.room;

import net.algem.accounting.GemAmount;
import net.algem.util.BundleUtil;
import net.algem.util.ui.JTableModel;

/**
 * @author <a href="mailto:jmg@musiques-tangentes.asso.fr">Jean-Marc Gobat</a>
 * @version 2.9.4.13
 * @since 2.1a
 */
public class RoomRateTableModel
        extends JTableModel<RoomRate>
{

  public RoomRateTableModel() {
    header = new String[]{
      BundleUtil.getLabel("Id.label"),
      BundleUtil.getLabel("Label.label"),
      BundleUtil.getLabel("Type.label"),
      "HC",
      "HP",
      "Plafond",
      "Forfait HC",
      "Forfait HP"
    };
  }

  @Override
  public int getIdFromIndex(int i) {
    RoomRate t = tuples.elementAt(i);
    return t.getId();
  }

  @Override
  public Class getColumnClass(int column) {
    switch (column) {
      case 0:
        return Integer.class;
      case 1:
        return String.class;
      case 2:
        return RoomRateEnum.class;
      case 3:
      case 4:
      case 5:
      case 6:
      case 7:
//        return Double.class;
        return GemAmount.class;
      default:
        return Object.class;
    }
  }

  @Override
  public boolean isCellEditable(int row, int column) {
    return false;
  }

  @Override
  public Object getValueAt(int line, int col) {
    RoomRate t = tuples.elementAt(line);
    switch (col) {
      case 0:
        return t.getId();
      case 1:
        return t.getLabel();
      case 2:
        return t.getType();
      case 3:
        return new GemAmount(t.getOffpeakRate());
      case 4:
        return new GemAmount(t.getFullRate());
      case 5:
        return new GemAmount(t.getMax());
      case 6:
        return new GemAmount(t.getPassOffPeakPrice());
      case 7:
        return new GemAmount(t.getPassFullPrice());
    }
    return null;
  }

  @Override
  public void setValueAt(Object value, int ligne, int column) {

  }

}
