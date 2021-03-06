/*
 * @(#)PostponeScheduleDlg.java	2.9.4.0 26/03/2015
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

package net.algem.planning.editing;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import net.algem.course.Course;
import net.algem.planning.CourseSchedule;
import net.algem.planning.Hour;
import net.algem.planning.PlanningService;
import net.algem.planning.Schedule;
import net.algem.planning.ScheduleObject;
import net.algem.planning.TrainingCourseSchedule;
import net.algem.planning.WorkshopSchedule;
import net.algem.room.Room;
import net.algem.room.RoomIO;
import net.algem.util.DataCache;
import net.algem.util.MessageUtil;
import net.algem.util.model.Model;
import net.algem.util.module.GemDesktop;

/**
 * Dialog for course time modification.
 * 
 * @author <a href="mailto:jmg@musiques-tangentes.asso.fr">Jean-Marc Gobat</a>
 * @version 2.9.4.0
 * @since 2.9.4.0 26/03/2015
 *
 */
public class PostponeScheduleDlg
        extends ModifPlanDlg
{

  private PostponeScheduleView pv;
  private ScheduleObject schedule;

  public PostponeScheduleDlg(GemDesktop desktop, ScheduleObject _plan, PlanningService service, String titleKey) {
    super(desktop.getFrame());
    schedule = _plan;
    pv = new PostponeScheduleView(desktop.getDataCache().getList(Model.Room), service);
    boolean noRange = titleKey.equals("Schedule.course.copy.title")
            || (schedule instanceof CourseSchedule && ((Course) schedule.getActivity()).isCollective())
            || schedule instanceof WorkshopSchedule 
            || schedule instanceof TrainingCourseSchedule;
    pv.set(schedule, noRange);
    validation = false;
    dlg = new JDialog(desktop.getFrame(), true);
    dlg.setSize(400,300);
    addContent(pv, titleKey);

  }

  @Override
  public void show() {
    dlg.setVisible(true);
  }

  @Override
  public boolean isEntryValid() {
    ScheduleObject ns = pv.getSchedule();
    String error = MessageUtil.getMessage("invalid.time.slot");
    if (!ns.getEnd().after(ns.getStart())) {
      JOptionPane.showMessageDialog(dlg, MessageUtil.getMessage("hour.range.error"), error, JOptionPane.ERROR_MESSAGE);
      return false;
    }
    /* Condition annulée car on peut différer un cours par anticipation à une date antérieure */
    /*if (pv.getNewDate().before(pv.getDate()))
    {
    JOptionPane.showMessageDialog(dlg,  "Date de end invalide", "Plage de date invalide", JOptionPane.ERROR_MESSAGE);
    return false;
    }*/
    // > versus != because range time may be only one part of the original range
    if (ns.getStart().getLength(ns.getEnd()) > schedule.getStart().getLength(schedule.getEnd())) {
      JOptionPane.showMessageDialog(dlg, MessageUtil.getMessage("invalid.duration"), error, JOptionPane.ERROR_MESSAGE);
      return false;
    }
    
    if (ns.getEnd().le(ns.getStart())) {
       JOptionPane.showMessageDialog(dlg, MessageUtil.getMessage("hour.range.error"), error, JOptionPane.ERROR_MESSAGE);
      return false;
    }

    int room = ns.getIdRoom();
    RoomIO roomIO = (RoomIO) DataCache.getDao(Model.Room);
    Room r = roomIO.findId(schedule.getIdRoom()); // room to change
    Room n = roomIO.findId(room); // new room
    // 1.1c IGNORE this block : SEULEMENT POUR MUSIQUES TANGENTES
    if (Schedule.COURSE == ns.getType() && r.getEstab() > 13000 && n.getName().toLowerCase().startsWith("rattrap")) {
      JOptionPane.showMessageDialog(dlg,
                                    "La salle de rattrapage n'est pas valide pour les cours à l'extérieur.",
                                    MessageUtil.getMessage("room.invalid.choice"),
                                    JOptionPane.ERROR_MESSAGE);
      return false;

    }
    return true;
  }

  @Override
  public boolean isValidate() {
    return validation;
  }

  ScheduleObject getSchedule() {
    return pv.getSchedule();
  }

  Hour[] getRange() {
    return new Hour[] {pv.getRange().getStart(), pv.getRange().getEnd()};
  }
 
}

