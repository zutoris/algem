/*
 * @(#)AccountingService.java	2.8.v 10/06/14
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

package net.algem.accounting;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import net.algem.planning.*;
import net.algem.util.DataConnection;

/**
 * Service class for accounting.
 *
 * @author <a href="mailto:jmg@musiques-tangentes.asso.fr">Jean-Marc Gobat</a>
 * @version 2.8.v
 * @since 2.4.a 21/05/12
 */
public class AccountingService {

  private DataConnection dc;

  public AccountingService(DataConnection dc) {
    this.dc = dc;
  }

  public Vector<PlanningLib> getPlanningLib(String start, String end, int school, boolean catchup) throws SQLException {
    String where = "WHERE jour >= '" + start + "' AND jour <= '" + end + "' AND ecole = " + school;
    if(!catchup) {
      where += " AND salle !~* 'rattrap'";
    }
    where  += " ORDER BY nomprof, jour, debut, coursid";
    return PlanningLibIO.find(where, dc);
  }

  public Vector<PlanningLib> getPlanningLib(String start, String end, int school, int teacherId, boolean catchup) throws SQLException {
    String where = "WHERE jour >= '" + start + "' AND jour <= '" + end + "' AND ecole =" + school + " AND profid = " + teacherId;
    if(!catchup) {
      where += " AND salle !~* 'rattrap'";
    }
    where += " ORDER BY jour, debut, coursid";
    return PlanningLibIO.find(where, dc);
  }

  public Vector<ScheduleRange> getCourseScheduleRange(int idplanning) throws SQLException {
    // on ne comptabilise pas les plages de pause (adherent = 0)
    return ScheduleRangeIO.find("pg WHERE pg.idplanning = " + idplanning + " AND pg.adherent != 0 ORDER BY pg.debut", dc);
  }

  public ResultSet getDetailEmployee(String start, String end, int type) throws SQLException {
    String query = "SELECT p.jour, p.idper, p.debut, p.fin, (p.fin - p.debut) AS duree, pg.adherent"
            + " FROM " + ScheduleIO.TABLE + " p, " + ScheduleRangeIO.TABLE + " pg"
            + " WHERE pg.idplanning = p.id"
            + " AND p.jour BETWEEN '" + start + "' and '" + end + "'"
            + " AND p.ptype = " + type
            + " ORDER BY pg.adherent,p.jour,p.debut";
    return dc.executeQuery(query);
  }
}
