/*
 * @(#)PlanificationUtil.java	2.8.v 29/05/14
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

package net.algem.planning;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author <a href="mailto:jmg@musiques-tangentes.asso.fr">Jean-Marc Gobat</a>
 * @version 2.8.v
 * @since 2.8.v 29/05/14
 */
public class PlanificationUtil
{

  static boolean hasOverlapping(List<GemDateTime> orig) {
    List<GemDateTime> dup = new ArrayList<GemDateTime>(orig);
    for (int i = 0; i < orig.size(); i++) {
      DateFr d = orig.get(i).getDate();
      HourRange h = orig.get(i).getTimeRange();
      for(int j = 0; j < dup.size(); j++) {
        if (j != i && dup.get(j).getDate().equals(d)) {
          if (dup.get(j).getTimeRange().overlap(h.getStart(), h.getEnd())) {
            return true;
          }
        }
      }
    }
    return false;
  }

}
