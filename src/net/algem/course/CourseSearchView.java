/*
 * @(#)CourseSearchView.java	2.8.t 15/04/14
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
package net.algem.course;

import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import javax.swing.JCheckBox;
import net.algem.config.GemParamChoice;
import net.algem.util.BundleUtil;
import net.algem.util.GemCommand;
import net.algem.util.model.GemList;
import net.algem.util.ui.*;

/**
 * Course search view.
 *
 * @author <a href="mailto:eric@musiques-tangentes.asso.fr">Eric</a>
 * @author <a href="mailto:jmg@musiques-tangentes.asso.fr">jean-marc gobat</a>
 * @version 2.8.t
 * @since 1.0a 07/07/1999
 */
public class CourseSearchView
        extends SearchView
{

  private GemNumericField number;
  private GemField title;
//  private GemField code;
  private GemParamChoice code;
  private JCheckBox collective;
  private GemPanel mask;
  private GemList<CourseCode> codes;

  public CourseSearchView() {
    super();
  }

	@Override
  public GemPanel init() {
    mask = new GemPanel();
    mask.setLayout(new GridBagLayout());

    number = new GemNumericField(6);
    number.addActionListener(this);
    title = new GemField(24);
    title.addActionListener(this);
    code = new GemParamChoice();
//    code = new GemField(25);
    code.addActionListener(this);
    collective = new JCheckBox();
    collective.setBorder(null);

    btCreate.setText(GemCommand.CREATE_CMD);
    btCreate.setEnabled(true);

    btErase = new GemButton(GemCommand.ERASE_CMD);
    btErase.addActionListener(this);

    GridBagHelper gb = new GridBagHelper(mask);
    gb.add(new GemLabel(BundleUtil.getLabel("Number.label")), 0, 0, 1, 1, GridBagHelper.WEST);
    gb.add(new GemLabel(BundleUtil.getLabel("Title.label")), 0, 1, 1, 1, GridBagHelper.WEST);
    gb.add(new GemLabel(BundleUtil.getLabel("Code.label")), 0, 2, 1, 1, GridBagHelper.WEST);
    gb.add(new GemLabel(BundleUtil.getLabel("Collective.label")), 0, 3, 1, 1, GridBagHelper.WEST);

    gb.add(number, 1, 0, 1, 1, GridBagHelper.WEST);
    gb.add(title, 1, 1, 1, 1, GridBagHelper.WEST);
    gb.add(code, 1, 2, 1, 1, GridBagHelper.WEST);
    gb.add(collective, 1, 3, 1, 1, GridBagHelper.WEST);

    return mask;
  }

  void setCode(GemList<CourseCode> codes){
    code.setModel(new GemChoiceModel<CourseCode>(codes));
  }

  @Override
  public void actionPerformed(ActionEvent evt) {
    if (actionListener == null) {
      return;
    }
    Object src = evt.getSource();
    if (src == number || src == title || src == code) {
      actionListener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, GemCommand.SEARCH_CMD));
    } else {
      actionListener.actionPerformed(evt);
    }
  }

	@Override
  public String getField(int n) {
    String s = null;
    switch (n) {
      case 0:
        s = number.getText();
        break;
      case 1:
        s = title.getText();
        break;
      case 2:
        s = String.valueOf(code.getKey());
        break;
      case 3:
        s = collective.isSelected() ? "t" : "f";
        break;
    }
    if (s != null && s.length() > 0) {
      return s;
    } else {
      return null;
    }
  }

	@Override
  public void clear() {
    number.setText("");
    title.setText("");
    code.setSelectedIndex(0);
    collective.setSelected(false);
  }
}
