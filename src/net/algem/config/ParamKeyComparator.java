/*
 * @(#)ParamKeyComparator  2.6.a 06/08/2012
 *
 * Copyright (c) 2010 Musiques Tangentes All Rights Reserved.
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

import java.util.Comparator;

/**
 *
 * @author <a href="mailto:jmg@musiques-tangentes.asso.fr">Jean-Marc Gobat</a>
 * @version 2.6.a
 */
public class ParamKeyComparator
        implements Comparator
{

  @Override
  public int compare(Object o1, Object o2) {

    String regex = ".*[A-Za-z].*";

    String s1 = (String) o1;
    String s2 = (String) o2;
    if (s1.matches(regex) || s2.matches(regex)) {
      return s1.compareTo(s2);
    } else {
      Long g1 = Long.valueOf((String) o1);
      Long g2 = Long.valueOf((String) o2);
      return g1.compareTo(g2);
    }
  }
}
