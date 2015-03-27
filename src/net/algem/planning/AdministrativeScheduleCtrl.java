/*
 * @(#)AdministrativeScheduleCtrl.java	2.9.4.0 24/03/15
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

package net.algem.planning;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.JTextField;
import net.algem.config.ParamChoice;
import net.algem.contact.EmployeeIO;
import net.algem.contact.EmployeeSelector;
import net.algem.contact.EmployeeType;
import net.algem.contact.Person;
import net.algem.contact.PersonIO;
import net.algem.room.EstabChoice;
import net.algem.room.Room;
import net.algem.util.BundleUtil;
import net.algem.util.DataCache;
import net.algem.util.GemCommand;
import net.algem.util.GemLogger;
import net.algem.util.MessageUtil;
import net.algem.util.model.GemList;
import net.algem.util.model.Model;
import net.algem.util.module.GemDesktop;
import net.algem.util.ui.GemButton;
import net.algem.util.ui.GemChoice;
import net.algem.util.ui.GemLabel;
import net.algem.util.ui.GemPanel;
import net.algem.util.ui.GridBagHelper;
import net.algem.util.ui.MessagePopup;

/**
 *
 * @author <a href="mailto:jmg@musiques-tangentes.asso.fr">Jean-Marc Gobat</a>
 * @version 2.9.4.0
 * @since 2.9.4.0 18/03/15
 */
public class AdministrativeScheduleCtrl
  extends GemPanel
  implements ActionListener
{

  private DataCache dataCache;
  private GemDesktop desktop;
  private DateRangePanel datePanel;
  private AdministrativeTableView tableView;
  private GemChoice employee;
  private GemButton btCancel;
  private GemButton btValidation;
  private PlanningService service;
  private GemList<Room> roomList;
  
  public AdministrativeScheduleCtrl(GemDesktop desktop) {
    
    GemPanel mainPanel = new GemPanel();
    this.desktop = desktop;
    this.dataCache = desktop.getDataCache();
    service = new PlanningService(DataCache.getDataConnection());

    final EstabChoice estab = new EstabChoice(dataCache.getList(Model.Establishment));
    estab.setSelectedIndex(0);
    roomList = dataCache.getList(Model.Room);
    datePanel = new DateRangePanel(dataCache.getStartOfYear(), dataCache.getEndOfYear());
    tableView = new AdministrativeTableView(roomList, estab.getKey());
    tableView.load();
    estab.addItemListener(new ItemListener() {
      @Override
      public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
          tableView.setEstab(roomList, estab.getKey());
        }
      }
    });
    employee = new EmployeeSelector(service.getEmployees(EmployeeType.ADMINISTRATOR));
    ParamChoice vacancy = new ParamChoice(dataCache.getVacancyCat());
    vacancy.setPreferredSize(employee.getPreferredSize());
    
    JTextField helpLabel = new JTextField();
    helpLabel.setText(MessageUtil.getMessage("help.administrative.schedule.ctrl"));
    helpLabel.setEditable(false);
    helpLabel.setBorder(BorderFactory.createCompoundBorder(helpLabel.getBorder(), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
    
    mainPanel.setLayout(new GridBagLayout());
    GridBagHelper gb = new GridBagHelper(mainPanel);
    gb.insets = GridBagHelper.SMALL_INSETS;
    gb.add(new GemLabel(BundleUtil.getLabel("Establishment.label")), 0, 0, 1, 1, GridBagHelper.WEST);
    gb.add(new GemLabel(BundleUtil.getLabel("Date.label")), 0, 1, 1, 1, GridBagHelper.WEST);
    gb.add(new GemLabel(BundleUtil.getLabel("Person.label")), 0, 3, 1, 1, GridBagHelper.WEST);
    gb.add(new GemLabel(BundleUtil.getLabel("Menu.holidays.label")), 0, 4, 1, 1, GridBagHelper.WEST);
    
    gb.add(estab, 1, 0, 1, 1, GridBagHelper.WEST);
    gb.add(datePanel, 1, 1, 1, 1, GridBagHelper.WEST);
    gb.add(tableView, 1, 2, 1, 1, GridBagHelper.BOTH, GridBagHelper.WEST);
    gb.add(employee, 1, 3, 1, 1, GridBagHelper.WEST);
    gb.add(vacancy, 1, 4, 1, 1, GridBagHelper.WEST);
    gb.add(helpLabel, 0, 5, 2, 1, GridBagHelper.HORIZONTAL, GridBagHelper.WEST);
    
    btCancel = new GemButton(GemCommand.CANCEL_CMD);
    btCancel.addActionListener(this);
    btValidation = new GemButton(GemCommand.VALIDATE_CMD);
    btValidation.addActionListener(this);
    
    GemPanel buttons = new GemPanel(new GridLayout(1,2));
    buttons.add(btCancel);
    buttons.add(btValidation);
    
    setLayout(new BorderLayout());
    add(mainPanel, BorderLayout.CENTER);
    add(buttons, BorderLayout.SOUTH);
  }
  
  private List<AdministrativeActionModel> getEdited() {
    return tableView.getRows();
  }
  
  private String checked() {
    for (AdministrativeActionModel a : getEdited()) {
      if (a.getStart().equals(a.getEnd())) {
        continue;
      }
      if (a.getRoom() != null && a.getRoom().getId() == 0) {
        return "room.invalid.choice";
      }
    }
    return employee.getKey() > 0 ? null : "invalid.employee.warning";
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    Object src = e.getSource();
    if (src == btCancel) {
      desktop.removeModule("Administrative.scheduling");
    } else if (src == btValidation) {
      String notOk = checked();
      List<Action> actions;
      if (notOk == null) {
        actions = createActions(getEdited(), employee.getKey(), datePanel.getStartFr(), datePanel.getEndFr());
      } else {
        MessagePopup.warning(this, MessageUtil.getMessage(notOk));
        return;
      }
      save(actions);
    }
  }
  
  static List<Action> createActions(List<AdministrativeActionModel> result, int idper, DateFr start, DateFr end) {
    List<Action> actions = new ArrayList<Action>();
    Set<AdministrativeActionModel> set = new LinkedHashSet<>(result); // remove duplicates but preserve order
    for (AdministrativeActionModel aa : set) {
      Action a = new Action();
      if (aa.getEnd().le(aa.getStart())) {
        continue;
      }
      a.setHourStart(aa.getStart());
      a.setHourEnd(aa.getEnd());
      a.setDateStart(start);
      a.setDateEnd(end);
      a.setIdper(idper);

      a.setDay(aa.getDay().getIndex() -1);
      a.setPeriodicity(Periodicity.WEEK);
      a.setRoom(aa.getRoom().getId());
      a.setNSessions((short) 52);
      actions.add(a);
    }
    return actions;
  }
  
  private void save(final List<Action> actions) {
    try {
      String log = service.planAdministrative(actions);
      if (log.length() > 0) {
        log = "Séances non planifiées en raison de\nl'occupation de la personne ou de la salle :\n\n" + log;
        MessagePopup.information(this, log);
      }
    } catch (PlanningException e) {
      GemLogger.log(e.getMessage());
    }
  }

}
