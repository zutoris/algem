/*
 * @(#)TrainingCourse.java 2.9.4.13 05/11/15
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

package net.algem.course;

/**
 * Training course entity.
 * 
 * @author <a href="mailto:jmg@musiques-tangentes.asso.fr">Jean-Marc Gobat</a>
 * @version 2.9.4.13
 */
public class TrainingCourse extends Course
{
  private static final long serialVersionUID = -6491663489181441385L;
  public final static int CODELABEL = 100;

  public TrainingCourse() {
    init();
  }

  public TrainingCourse(String s) {
    super(s);
    init();
  }

  private void init() {
    code = CODELABEL;
    collective = true;
  }

}
