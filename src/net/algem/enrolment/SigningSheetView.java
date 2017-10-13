/*
 * @(#) SigningSheetView.java Algem 2.10.0 02/06/2016
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
 */
package net.algem.enrolment;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import net.algem.enrolment.SigningSheetCtrl.SigningSheetType;
import net.algem.planning.DateFr;
import net.algem.planning.DateRange;
import net.algem.planning.DateRangePanel;
import net.algem.util.BundleUtil;
import net.algem.util.GemCommand;
import net.algem.util.MessageUtil;
import net.algem.util.module.GemModule;
import net.algem.util.ui.GemButton;
import net.algem.util.ui.GemLabel;
import net.algem.util.ui.GemPanel;
import net.algem.util.ui.GridBagHelper;

/**
 *
 * @author <a href="mailto:jmg@musiques-tangentes.asso.fr">Jean-Marc Gobat</a>
 * @version 2.10.0
 * @since 2.10.0 02/06/2016
 */
public class SigningSheetView
        extends JDialog
        implements ActionListener

{

  private DateRangePanel datePanel;
  private GemButton btOk;
  private GemButton btCancel;
  private boolean validation;
  private Component parent;
  private JRadioButton rStandard, rDetailed;

  public SigningSheetView() {
  }

  public SigningSheetView(Frame owner, boolean modal) {
    super(owner, modal);
    this.parent = owner;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    Object src = e.getSource();
    validation = (btOk == src);
    setVisible(false);
  }

  public boolean isValidation() {
    return validation;
  }
  
  SigningSheetType getPageModel() {
    if (rStandard.isSelected()) {
      return SigningSheetType.STANDARD;
    } else if (rDetailed.isSelected()) {
      return SigningSheetType.DETAILED;
    }
    return SigningSheetType.STANDARD;
  }

  void createUI() {
    setTitle(BundleUtil.getLabel("Signing.sheet.label"));
    Calendar cal = Calendar.getInstance();
    cal.set(Calendar.DAY_OF_MONTH, 1);
    DateFr start = new DateFr(cal.getTime());
    cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
    DateFr end = new DateFr(cal.getTime());
    datePanel = new DateRangePanel(start, end);
    
    GemPanel buttons = new GemPanel(new GridLayout(1, 2));
    btOk = new GemButton(GemCommand.VALIDATION_CMD);
    btOk.addActionListener(this);
    btCancel = new GemButton(GemCommand.CANCEL_CMD);
    btCancel.addActionListener(this);
    buttons.add(btOk);
    buttons.add(btCancel);

    ButtonGroup stype = new ButtonGroup();
    stype.add(rStandard = new JRadioButton(BundleUtil.getLabel("Signing.sheet.standard.type.label")));
    stype.add(rDetailed = new JRadioButton(BundleUtil.getLabel("Signing.sheet.detailed.type.label")));
    final JTextArea radioHelp = new JTextArea(5, 30);

    JPanel p = new JPanel(new GridBagLayout());
    radioHelp.setEditable(false);
    radioHelp.setLineWrap(true);
    radioHelp.setWrapStyleWord(true);
    radioHelp.setBackground(p.getBackground());
    radioHelp.setPreferredSize(new Dimension(300, 100));
    ActionListener typeListener = new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == rStandard) {
          radioHelp.setText(MessageUtil.getMessage("signing.sheet.standard.type.info"));
        } else if (src == rDetailed) {
          radioHelp.setText(MessageUtil.getMessage("signing.sheet.detailed.type.info"));
        } else {
          radioHelp.setText(null);
        }

      }
    };
    rStandard.addActionListener(typeListener);
    rDetailed.addActionListener(typeListener);

    GridBagHelper gb = new GridBagHelper(p);
    gb.add(new GemLabel(BundleUtil.getLabel("Period.label")), 0, 0, 1, 1, GridBagHelper.WEST);
    gb.add(datePanel, 1, 0, 2, 1, GridBagHelper.WEST);
    gb.add(rStandard, 1, 1, 1, 1, GridBagHelper.WEST);
    gb.add(rDetailed, 2, 1, 1, 1, GridBagHelper.WEST);
    gb.add(radioHelp, 1, 3, 2, 1, GridBagHelper.WEST);

    setLayout(new BorderLayout());
    add(p, BorderLayout.CENTER);
    add(buttons, BorderLayout.SOUTH);

    setSize(440, 320);
    setLocationRelativeTo(parent);

    setVisible(true);

  }

  DateRange getPeriod() {
    return new DateRange(datePanel.getStartFr(), datePanel.getEndFr());
  }

}
