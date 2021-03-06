/*
 * @(#)PersonneListeCtrl.java	2.6.a 18/09/12
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
package net.algem.contact;

import java.awt.BorderLayout;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import net.algem.util.ui.ListCtrl;

/**
 * @author <a href="mailto:jmg@musiques-tangentes.asso.fr">Jean-Marc Gobat</a>
 * @version 2.6.a
 * @since 2.0ma
 */
public class PersonFileListCtrl
        extends ListCtrl
{

  public PersonFileListCtrl() {
    super(false);
    setLayout(new BorderLayout());
    tableModel = new PersonFileTableModel();

    table = new JTable(tableModel);
    table.setAutoCreateRowSorter(true);

    JScrollPane p = new JScrollPane(table);
    add(p, BorderLayout.SOUTH);
  }

  /**
   * Gets the person file corresponding to selection.
   *
   * @since 2.0ma
   * @return a person file
   */
  public PersonFile getPersonFile() {
    return ((PersonFileTableModel) tableModel).getPersonFile(table.getSelectedRow());
  }
}
