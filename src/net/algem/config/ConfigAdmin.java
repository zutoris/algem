/*
 * @(#)ConfigAdmin.java 2.15.6 01/12/17
 *
 * Copyright (c) 1999-2016 Musiques Tangentes. All Rights Reserved.
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
package net.algem.config;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.border.Border;
import net.algem.enrolment.PricingPeriod;
import net.algem.room.EstabChoice;
import net.algem.room.RoomActiveChoiceModel;
import net.algem.room.RoomChoice;
import net.algem.util.BundleUtil;
import net.algem.util.DataCache;
import net.algem.util.model.Model;
import net.algem.util.ui.GemLabel;
import net.algem.util.ui.GemPanel;

/**
 * Panel for config and administrative tasks.
 *
 * @author <a href="mailto:jmg@musiques-tangentes.asso.fr">Jean-Marc Gobat</a>
 * @version 2.15.6
 */
public class ConfigAdmin
        extends ConfigPanel
{

  private Config c1, c2, c3, c4, c5, c6, c7,c8,c9;
  private JCheckBox jc1, jc2, jc3, jc4, jc5;
  private ParamChoice school;
  private EstabChoice estab;
  private RoomChoice studio;
  private JComboBox pricingPeriod;
  //private JRadioButton jr1, jr2;

  public ConfigAdmin(String title, Map<String, Config> cm) {
    super(title, cm);
  }

  public void init(DataCache dataCache) {

    c1 = confs.get(ConfigKey.TEACHER_MANAGEMENT.getKey());
    c2 = confs.get(ConfigKey.COURSE_MANAGEMENT.getKey());
    c3 = confs.get(ConfigKey.ADMINISTRATIVE_MANAGEMENT.getKey());
    c4 = confs.get(ConfigKey.DEFAULT_SCHOOL.getKey());
    c5 = confs.get(ConfigKey.DEFAULT_ESTABLISHMENT.getKey());
    c6 = confs.get(ConfigKey.DEFAULT_STUDIO.getKey());
    c7 = confs.get(ConfigKey.DEFAULT_PRICING_PERIOD.getKey());
    c8 = confs.get(ConfigKey.TRAINING_CONTRACT_MANAGEMENT.getKey());
    c9 = confs.get(ConfigKey.INTERNSHIP_AGREEMENT_MANAGEMENT.getKey());
    //c8 = confs.get(ConfigKey.ESTABLISHEMENT_ACTIVATION_TYPE.getKey());

    content = new GemPanel();

    Border checkBorder = BorderFactory.createEmptyBorder(0,0,5,5);
    jc1 = new JCheckBox(ConfigKey.TEACHER_MANAGEMENT.getLabel());
    jc1.setBorder(checkBorder);
    jc2 = new JCheckBox(ConfigKey.COURSE_MANAGEMENT.getLabel());
    jc2.setBorder(checkBorder);
    jc3 = new JCheckBox(ConfigKey.ADMINISTRATIVE_MANAGEMENT.getLabel());
    jc3.setBorder(checkBorder);
    jc4 = new JCheckBox(ConfigKey.TRAINING_CONTRACT_MANAGEMENT.getLabel());
    jc4.setBorder(checkBorder);
    jc5 = new JCheckBox(ConfigKey.INTERNSHIP_AGREEMENT_MANAGEMENT.getLabel());
    jc5.setBorder(checkBorder);

    school = new ParamChoice(dataCache.getList(Model.School).getData());
    school.setKey(Integer.parseInt(c4.getValue()));

    estab = new EstabChoice(dataCache.getList(Model.Establishment));
    estab.setKey(Integer.parseInt(c5.getValue()));

    studio = new RoomChoice(new RoomActiveChoiceModel(dataCache.getList(Model.Room), true));
    studio.setKey(Integer.parseInt(c6.getValue()));

    jc1.setSelected(isSelected(c1.getValue()));
    jc2.setSelected(isSelected(c2.getValue()));
    jc3.setSelected(isSelected(c3.getValue()));
    jc4.setSelected(isSelected(c8.getValue()));
    jc5.setSelected(isSelected(c9.getValue()));

    pricingPeriod = new JComboBox(PricingPeriod.values());
    String frequencyTip = BundleUtil.getLabel("ConfEditor.default.module.pricing.tip");
    pricingPeriod.setToolTipText(frequencyTip);
    String period = ConfigUtil.getConf(ConfigKey.DEFAULT_PRICING_PERIOD.getKey());
    pricingPeriod.setSelectedItem(PricingPeriod.valueOf(period));

    Box box1 = Box.createHorizontalBox();
    box1.add(jc1);
    box1.add(Box.createHorizontalGlue());
    Box box2 = Box.createHorizontalBox();
    box2.add(jc2);
    box2.add(Box.createHorizontalGlue());

    Box box3 = Box.createHorizontalBox();
    box3.add(jc3);
    box3.add(Box.createHorizontalGlue());

    Box box4 = Box.createHorizontalBox();
    box4.add(jc4);
    box4.add(Box.createHorizontalGlue());

    Box box5 = Box.createHorizontalBox();
    box5.add(jc5);
    box5.add(Box.createHorizontalGlue());

    content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
    content.add(box1);
    content.add(box2);
    content.add(box3);
    content.add(box4);
    content.add(box5);

    GemPanel defs = new GemPanel(new GridLayout(4,2,0,5));

    defs.add(new GemLabel(ConfigKey.DEFAULT_SCHOOL.getLabel()));
    defs.add(school);

    defs.add(new GemLabel(ConfigKey.DEFAULT_ESTABLISHMENT.getLabel()));
    defs.add(estab);

    defs.add(new GemLabel(ConfigKey.DEFAULT_STUDIO.getLabel()));
    defs.add(studio);
    GemLabel frequencyLabel = new GemLabel(ConfigKey.DEFAULT_PRICING_PERIOD.getLabel());
    frequencyLabel.setToolTipText(frequencyTip);
    defs.add(frequencyLabel);
    defs.add(pricingPeriod);

    content.add(defs);
    Box box6 = Box.createHorizontalBox();
    box6.add(Box.createVerticalStrut(10));
    content.add(box6);
    /*
    jr1 = new JRadioButton("Globale");
    jr1.setToolTipText("<html>L'activation ou la désactivation d'un établissement est commune<br />à tous les utilisateurs d'Algem dans la structure.</html>");
    jr2 = new JRadioButton("Individuelle");
    jr2.setToolTipText("<html>L'activation ou la désactivation d'un établissement est spécifique<br />à chaque utilisateur d'Algem dans la structure.<br />Un établissement marqué \"Actif\" pour l'un ne le sera pas forcément pour l'autre.</html>");
    ButtonGroup jrgroup = new ButtonGroup();
    jrgroup.add(jr1);
    jrgroup.add(jr2);
    if ("0".equals(c8.getValue())) {
      jr1.setSelected(true);
    } else {
      jr2.setSelected(true);
    }
    GemPanel p2 = new GemPanel(new GridBagLayout());
    GridBagHelper gb = new GridBagHelper(p2);
    GemLabel p2label = new GemLabel("Activation/Désactivation d'un établissement : ");
    gb.add(p2label, 0,0,1,1, GridBagHelper.WEST);
    gb.add(jr1,1,0,1,1);
    gb.add(jr2,2,0,1,1);
    content.add(p2);*/

    add(content);
  }

  @Override
  public List<Config> get() {
    List<Config> conf = new ArrayList<Config>();
    c1.setValue(getValue(jc1));
    c2.setValue(getValue(jc2));
    c3.setValue(getValue(jc3));
    c4.setValue(String.valueOf(school.getKey()));
    c5.setValue(String.valueOf(estab.getKey()));
    c6.setValue(String.valueOf(studio.getKey()));
    c7.setValue(((PricingPeriod) pricingPeriod.getSelectedItem()).name());
    c8.setValue(getValue(jc4));
    c9.setValue(getValue(jc5));
    //c10.setValue(jr1.isSelected() ? "0" : "1");

    conf.add(c1);
    conf.add(c2);
    conf.add(c3);
    conf.add(c4);
    conf.add(c5);
    conf.add(c6);
    conf.add(c7);
    conf.add(c8);
    conf.add(c9);

    return conf;
  }

  private boolean isSelected(String conf) {
    return conf.startsWith("t");
  }

  private String getValue(JCheckBox box) {
    return box.isSelected() ? "t" : "f";
  }

}
