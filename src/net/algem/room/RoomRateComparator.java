/*
 * @(#)RoomRateComparator.java	2.9.4.13 05/10/2015
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

package net.algem.room;

import java.util.Comparator;

/**
 * Room rate comparator.
 * 
 * @author <a href="mailto:jmg@musiques-tangentes.asso.fr">Jean-Marc Gobat</a>
 * @version 2.9.4.13
 * @since 2.6.a 03/10/12
 */
public class RoomRateComparator 
implements Comparator<RoomRate> {

  @Override
  public int compare(RoomRate o1, RoomRate o2) {
//    return o1.getLabel().compareTo(o2.getLabel());
    Double d1 = o1.getFullRate();
    Double d2 = o2.getFullRate();
    return d1.compareTo(d2);
  }

}
