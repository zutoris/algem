/*
 * @(#)MemberPassRehearsalView.java	2.9.4.0 06/04/15
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
package net.algem.contact.member;

import java.awt.GridBagLayout;
import java.util.Date;
import net.algem.planning.DateFr;
import net.algem.planning.DateRangePanel;
import net.algem.planning.Hour;
import net.algem.planning.HourRangePanel;
import net.algem.planning.day.DayChoice;
import net.algem.room.RoomChoice;
import net.algem.util.BundleUtil;
import net.algem.util.DataCache;
import net.algem.util.model.Model;
import net.algem.util.ui.GemField;
import net.algem.util.ui.GemLabel;
import net.algem.util.ui.GemPanel;
import net.algem.util.ui.GridBagHelper;

/**
 * comment
 *
 * @author <a href="mailto:eric@musiques-tangentes.asso.fr">Eric</a>
 * @author <a href="mailto:jmg@musiques-tangentes.asso.fr">Jean-Marc Gobat</a>
 * @version 2.9.4.0
 */
public class MemberPassRehearsalView
        extends GemPanel
{

  private GemField memberField;
  private DateRangePanel datePanel;
  private HourRangePanel hourPanel;
  private RoomChoice roomChoice;
  private DayChoice dayChoice;

  public MemberPassRehearsalView(DataCache dataCache) {

    memberField = new GemField(35);
    memberField.setEditable(false);
    datePanel = new DateRangePanel(DateRangePanel.RANGE_DATE, null);
    hourPanel = new HourRangePanel(60 * 8);
    roomChoice = new RoomChoice(dataCache.getList(Model.Room));
    dayChoice = new DayChoice();

    this.setLayout(new GridBagLayout());
    GridBagHelper gb = new GridBagHelper(this);

    gb.add(new GemLabel(BundleUtil.getLabel("Member.label")), 0, 0, 1, 1, GridBagHelper.WEST);
    gb.add(new GemLabel(BundleUtil.getLabel("Date.label")), 0, 1, 1, 1, GridBagHelper.WEST);
    gb.add(new GemLabel(BundleUtil.getLabel("Day.label")), 0, 2, 1, 1, GridBagHelper.WEST);
    gb.add(new GemLabel(BundleUtil.getLabel("Hour.label")), 0, 3, 1, 1, GridBagHelper.WEST);
    gb.add(new GemLabel(BundleUtil.getLabel("Room.label")), 0, 4, 1, 1, GridBagHelper.WEST);

    gb.add(memberField, 1, 0, 3, 1, GridBagHelper.WEST);
    gb.add(datePanel, 1, 1, 1, 1, GridBagHelper.WEST);
    gb.add(dayChoice, 1, 2, 1, 1, GridBagHelper.WEST);
    gb.add(hourPanel, 1, 3, 1, 1, GridBagHelper.WEST);
    gb.add(roomChoice, 1, 4, 1, 1, GridBagHelper.WEST);
  }

  void setMember(String s) {
    memberField.setText(s);
  }

  int getRoom() {
    return roomChoice.getKey();
  }

  int getDay() {
    return dayChoice.getKey();
  }

  DateFr getDateStart() {
    return datePanel.getStartFr();
  }

  DateFr getDateEnd() {
    return datePanel.getEndFr();
  }

  Hour getHourStart() {
    return hourPanel.getStart();
  }

  Hour getHourEnd() {
    return hourPanel.getEnd();
  }

  public void clear() {
    memberField.setText("");
    datePanel.setStart(new Date());
    datePanel.setEnd(new Date());
    hourPanel.clear();
    roomChoice.setSelectedIndex(0);
    dayChoice.setSelectedIndex(0);
  }
}
