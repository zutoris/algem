/*
 * @(#)MenuPlanning.java	2.9.4.13 11/11/15
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
package net.algem.util.menu;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import javax.swing.JMenuItem;
import net.algem.config.ConfigKey;
import net.algem.config.ConfigUtil;
import net.algem.contact.teacher.SubstituteTeacherCtrl;
import net.algem.edition.AttendanceSheetDlg;
import net.algem.planning.AdministrativeScheduleCtrl;
import net.algem.planning.CourseScheduleCtrl;
import net.algem.planning.StudioScheduleCtrl;
import net.algem.planning.TrainingScheduleCtrl;
import net.algem.planning.WorkhopScheduleCtrl;
import net.algem.planning.day.DayScheduleCtrl;
import net.algem.planning.month.MonthScheduleCtrl;
import net.algem.util.BundleUtil;
import net.algem.util.GemCommand;
import net.algem.util.module.GemDesktop;
import net.algem.util.module.GemModule;

/**
 * Planning menu.
 *
 * @author <a href="mailto:eric@musiques-tangentes.asso.fr">Eric</a>
 * @author <a href="mailto:jmg@musiques-tangentes.asso.fr">Jean-Marc Gobat</a>
 * @version 2.9.4.13
 * @since 1.0a 07/07/1999
 */
public class MenuPlanning
        extends GemMenu
{

  private JMenuItem miDay;
  private JMenuItem miMonth;
  private JMenuItem miAttendanceSheet;
  private JMenuItem miCourse;
  private JMenuItem miWorkshop;
  private JMenuItem miTraining;
  private JMenuItem miStudio;
  private JMenuItem miReplacement;
  private JMenuItem miAdministrative;

  public MenuPlanning(GemDesktop desktop) {

    super(BundleUtil.getLabel("Menu.schedule.label"), desktop);

    add(miDay = new JMenuItem(BundleUtil.getLabel("Menu.day.schedule.label")));
    add(miMonth = new JMenuItem(BundleUtil.getLabel("Menu.month.schedule.label")));

    String manage = ConfigUtil.getConf(ConfigKey.COURSE_MANAGEMENT.getKey()).toLowerCase();
    if (manage != null && manage.startsWith("t")) {
      addSeparator();
      add(miCourse = new JMenuItem(BundleUtil.getLabel("Course.scheduling.label")));
      add(miWorkshop = new JMenuItem(BundleUtil.getLabel("Workshop.scheduling.label")));
      add(miTraining = new JMenuItem(BundleUtil.getLabel("Training.course.scheduling.label")));
      add(miStudio = new JMenuItem(BundleUtil.getLabel("Studio.scheduling.label")));
    }

    manage = ConfigUtil.getConf(ConfigKey.TEACHER_MANAGEMENT.getKey()).toLowerCase();
    if (manage != null && manage.startsWith("t")) {
      addSeparator();
      add(miAttendanceSheet = new JMenuItem(BundleUtil.getLabel("Menu.presence.file.label")));
      add(miReplacement = dataCache.getMenu2("Menu.replacement", true));
    }
    addSeparator();
    add(miAdministrative = new JMenuItem(BundleUtil.getLabel("Administrative.scheduling.label")));
    manage = ConfigUtil.getConf(ConfigKey.ADMINISTRATIVE_MANAGEMENT.getKey()).toLowerCase();
    if (manage == null || !manage.startsWith("t")) {
      miAdministrative.setEnabled(false);
    }

    setListener(this);
  }

  @Override
  public void actionPerformed(ActionEvent evt) {
    String arg = evt.getActionCommand();
    Object src = evt.getSource();

    desktop.setWaitCursor();

    if (src == miDay) {
      DayScheduleCtrl dsCtrl = new DayScheduleCtrl();
      desktop.addModule(dsCtrl);
      dsCtrl.mayBeMaximize();
    } else if (src == miMonth) {
      desktop.addModule(new MonthScheduleCtrl());
    } else if (src == miCourse) {// planification d'un cours
      CourseScheduleCtrl csCtrl = new CourseScheduleCtrl(desktop);
      csCtrl.addActionListener(this);
      csCtrl.init();
      desktop.addPanel(CourseScheduleCtrl.COURSE_SCHEDULING_KEY, csCtrl);
    } else if (src == miWorkshop) {
      WorkhopScheduleCtrl wsCtrl = new WorkhopScheduleCtrl(desktop);
      wsCtrl.addActionListener(this);
      wsCtrl.init();
      desktop.addPanel(WorkhopScheduleCtrl.WORKSHOP_SCHEDULING_KEY, wsCtrl);
    } else if (src == miTraining) {
      TrainingScheduleCtrl tsCtrl = new TrainingScheduleCtrl(desktop);
      tsCtrl.addActionListener(this);
      tsCtrl.init();
      desktop.addPanel(TrainingScheduleCtrl.TRAINING_SCHEDULING_KEY, tsCtrl);
    } else if (src == miStudio) {
      StudioScheduleCtrl studioCtrl = new StudioScheduleCtrl(desktop);
      studioCtrl.addActionListener(this);
      studioCtrl.init();
      desktop.addPanel(StudioScheduleCtrl.STUDIO_SCHEDULING_KEY, studioCtrl, new Dimension(650, 480));
    } else if (src == miAttendanceSheet) {
       new AttendanceSheetDlg(desktop.getFrame(), dataCache);
    } else if (src == miReplacement) {
      SubstituteTeacherCtrl rCtrl = new SubstituteTeacherCtrl(desktop);
      desktop.addPanel("Replacement", rCtrl);
      desktop.getSelectedModule().setSize(GemModule.L_SIZE);
    } else if (src == miAdministrative) {
      AdministrativeScheduleCtrl adminScheduleCtrl = new AdministrativeScheduleCtrl(desktop);
      desktop.addPanel("Administrative.scheduling", adminScheduleCtrl);
    } else if (GemCommand.CANCEL_CMD.equals(arg)) {
      desktop.removeCurrentModule();
    }
    desktop.setDefaultCursor();
  }
}
