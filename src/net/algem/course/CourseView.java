/*
 * @(#)CourseView.java	2.16.0 05/03/19
 *
 * Copyright (c) 1999-2019 Musiques Tangentes. All Rights Reserved.
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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import javax.swing.JCheckBox;
import net.algem.config.GemParamChoice;
import net.algem.config.ParamChoice;
import net.algem.util.BundleUtil;
import net.algem.util.model.GemList;
import net.algem.util.ui.*;

/**
 * comment
 *
 * @author <a href="mailto:eric@musiques-tangentes.asso.fr">Eric</a>
 * @author <a href="mailto:jmg@musiques-tangentes.asso.fr">Jean-Marc Gobat</a>
 * @version 2.16.0
 */
public class CourseView
        extends GemPanel
{

  private GemNumericField no;
  private GemField title;
  private GemField label;
  private GemParamChoice code;

  /**@deprecated  As of release 2.15.12, replaced by {@link net.algem.planning.Action#places}.*/
  //private GemNumericField nplaces;

  /**@deprecated  As of release 2.15.12, replaced by {@link net.algem.config.Level}.*/
  private GemField level;
  private JCheckBox collective;
  private JCheckBox active;
  private ParamChoice schoolChoice;

  public CourseView(GemList<CourseCode> codes, GemList schools) {

    no = new GemNumericField(6);
    no.setEditable(false);
    no.setBackground(Color.lightGray);
    title = new GemField(24, 32);
    label = new GemField(24, 32);
    code = new GemParamChoice(new GemChoiceModel<CourseCode>(codes));
    Dimension comboDim = new Dimension(200, code.getPreferredSize().height);
    code.setPreferredSize(comboDim);
    //nplaces = new GemNumericField(5);
    level = new GemField(5);
    collective = new JCheckBox();
    collective.setBorder(null);
    collective.setSelected(true);
    active = new JCheckBox();
    active.setBorder(null);
    active.setSelected(true);
    schoolChoice = new ParamChoice(schools.getData());
    schoolChoice.setPreferredSize(comboDim);
    this.setLayout(new GridBagLayout());
    GridBagHelper gb = new GridBagHelper(this);

    gb.add(new GemLabel(BundleUtil.getLabel("Number.label")), 0, 0, 1, 1, GridBagHelper.WEST);
    gb.add(new GemLabel(BundleUtil.getLabel("Title.label")), 0, 1, 1, 1, GridBagHelper.WEST);
    gb.add(new GemLabel(BundleUtil.getLabel("Label.label")), 0, 2, 1, 1, GridBagHelper.WEST);
    gb.add(new GemLabel(BundleUtil.getLabel("Type.label")), 0, 3, 1, 1, GridBagHelper.WEST);
    //gb.add(new GemLabel(BundleUtil.getLabel("Place.number.label")), 0, 4, 1, 1, GridBagHelper.WEST);
    gb.add(new GemLabel(BundleUtil.getLabel("Level.label")), 0, 5, 1, 1, GridBagHelper.WEST);
    gb.add(new GemLabel(BundleUtil.getLabel("Collective.label")), 0, 6, 1, 1, GridBagHelper.WEST);
    gb.add(new GemLabel(BundleUtil.getLabel("Active.label")), 0, 7, 1, 1, GridBagHelper.WEST);
    gb.add(new GemLabel(BundleUtil.getLabel("School.label")), 0, 8, 1, 1, GridBagHelper.WEST);

    gb.add(no, 1, 0, 1, 1, GridBagHelper.WEST);
    gb.add(title, 1, 1, 3, 1, GridBagHelper.WEST);
    gb.add(label, 1, 2, 3, 1, GridBagHelper.WEST);
    gb.add(code, 1, 3, 3, 1, GridBagHelper.WEST);
    //gb.add(nplaces, 1, 4, 1, 1, GridBagHelper.WEST);
    gb.add(level, 1, 5, 1, 1, GridBagHelper.WEST);
    gb.add(collective, 1, 6, 1, 1, GridBagHelper.WEST);
    gb.add(active, 1, 7, 1, 1, GridBagHelper.WEST);
    gb.add(schoolChoice, 1, 8, 1, 1, GridBagHelper.WEST);
  }

  public int getId() {
    return Integer.parseInt(no.getText());
  }

  public void set(Course c) {
    no.setText(String.valueOf(c.getId()));
    title.setText(c.getTitle());
    label.setText(c.getLabel());
    //nplaces.setText(String.valueOf(c.getNPlaces()));
    level.setText(String.valueOf(c.getLevel()));
    collective.setSelected(c.isCollective());
    active.setSelected(c.isActive());
    code.setKey(c.getCode());
    schoolChoice.setKey(c.getSchool());
  }

  public Course get() {
    Course cr = new Course();

    try {
      cr.setId(Integer.parseInt(no.getText()));
    } catch (NumberFormatException e) {
      cr.setId(0);
    }

    cr.setTitle(title.getText());
    cr.setLabel(label.getText());
    /*try {
      cr.setNPlaces((short) Integer.parseInt(nplaces.getText()));
    } catch (NumberFormatException e) {
      cr.setNPlaces((short) 0);
    }*/
    try {
      cr.setLevel((short) Integer.parseInt(level.getText()));
    } catch (NumberFormatException e) {
      cr.setLevel((short) 0);
    }
    cr.setCollective(collective.isSelected());
    cr.setActive(active.isSelected());
    cr.setCode(code.getKey());
    cr.setSchool(schoolChoice.getKey());

    return cr;
  }

  public void clear() {
    no.setText("");
    title.setText("");
    label.setText("");
    //nplaces.setText("");
    level.setText("");
    collective.setSelected(true);
    active.setSelected(true);
    code.setSelectedIndex(0);
    schoolChoice.setSelectedIndex(0);
  }
}
