/*
 * @(#)MemberRentalView.java	2.17.1 28/08/2019
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
package net.algem.rental;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.util.Date;
import net.algem.contact.Person;
import net.algem.planning.DateFr;
import net.algem.planning.DateRangePanel;
import net.algem.util.BundleUtil;
import net.algem.util.GemLogger;
import net.algem.util.model.GemList;
import net.algem.util.ui.GemField;
import net.algem.util.ui.GemLabel;
import net.algem.util.ui.GemPanel;
import net.algem.util.ui.GridBagHelper;

/**
 * Member single rental panel entry.
 * 
 * @author <a href="mailto:eric@musiques-tangentes.asso.fr">Eric</a>
 * @version 2.17.1
 * @since 2.17.1  28/09/2019
 */
public class MemberRentalView
        extends GemPanel
{

  private GemField memberField;
  private RentableChoice rentableChoice;
  private DateRangePanel datePanel;
  private GemField amount;
  private GemField description;

  public MemberRentalView(GemList<RentableObject> rentableList) {

    memberField = new GemField(35);
    memberField.setEditable(false);
    memberField.setMinimumSize(new Dimension(400, memberField.getPreferredSize().height));
    rentableChoice = new RentableChoice(rentableList);
    datePanel = new DateRangePanel(DateRangePanel.SIMPLE_DATE, null);
    amount = new GemField(8);
    amount.setMinimumSize(new Dimension(60, amount.getPreferredSize().height));
    description = new GemField(64);
    description.setMinimumSize(new Dimension(500, description.getPreferredSize().height));
    this.setLayout(new GridBagLayout());
    GridBagHelper gb = new GridBagHelper(this);
    
    gb.add(new GemLabel(BundleUtil.getLabel("Member.label")), 0, 0, 1, 1, GridBagHelper.WEST);
    gb.add(new GemLabel(BundleUtil.getLabel("Rentable.label")), 0, 1, 1, 1, GridBagHelper.WEST);
    gb.add(new GemLabel(BundleUtil.getLabel("Date.label")), 0, 2, 1, 1, GridBagHelper.WEST);
    gb.add(new GemLabel(BundleUtil.getLabel("Amount.label")), 0, 3, 1, 1, GridBagHelper.WEST);
    gb.add(new GemLabel(BundleUtil.getLabel("Rental.info.label")), 0, 4, 1, 1, GridBagHelper.WEST);
    
    gb.add(memberField, 1, 0, 1, 1, GridBagHelper.WEST);
    gb.add(rentableChoice, 1, 1, 1, 1, GridBagHelper.WEST);
    gb.add(datePanel, 1, 2, 1, 1, GridBagHelper.WEST);
    gb.add(amount, 1, 3, 1, 1, GridBagHelper.WEST);
    gb.add(description, 1, 4, 1, 1, GridBagHelper.WEST);

  }

  int getMemberId() {
    int n = 0;
    try {
      n = Integer.parseInt(memberField.getText());
    } catch (NumberFormatException ex) {
      GemLogger.logException(ex);
    }
    return n;
  }

  int getRentableId() {
    return rentableChoice.getKey();
  }

  RentableObject getRentable() {
    return (RentableObject)rentableChoice.getSelectedItem();
      
  }
  DateFr getDate() {
    return datePanel.get();
  }

  String getAmount() {
    return amount.getText();
  }

  String getDescription() {
    return description.getText();
  }

  void set(Person per) {
    memberField.setText(per.getId() + " " + per.getFirstName() + " " + per.getName());
  }

  void clear() {
    rentableChoice.setSelectedIndex(0);
    datePanel.setDate(new Date());
    amount.setText("");
    description.setText("");
  }
}
