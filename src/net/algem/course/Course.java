/*
 * @(#)Course.java	2.7.h 25/02/13
 * 
 * Copyright (c) 1999-2012 Musiques Tangentes. All Rights Reserved.
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

import net.algem.util.model.GemModel;

/**
 * Course entity.
 *
 * @author <a href="mailto:eric@musiques-tangentes.asso.fr">Eric</a>
 * @author <a href="mailto:jmg@musiques-tangentes.asso.fr">Jean-Marc Gobat</a>
 * @version 2.7.h
 * @since 1.0a 07/07/1999
 */
public class Course
        implements GemModel
{

  public static final int BREAK = 0;
  
  /** Single workshop code. */
  public static final String ATP_CODE = "ATP";
 
  public static final String PRIVATE_INSTRUMENT_CODE = "Inst";
  protected int id;
  protected String title;
  protected String label;
  protected String code;
  protected short nplaces;
  protected short level;
  protected boolean collective;
  protected short school;
  protected boolean active;

  public Course() {
  }

  public Course(int i) {
    id = i;
    title = "";
  }

  public Course(String s) {
    id = 0;
    title = s;
  }

  public Course(Workshop a) {
    id = a.getId();
    title = a.getName();
    collective = true;
    label = title.length() > 15 ? title.substring(0, 15) : title;
    code = ATP_CODE;
  }

  @Override
  public String toString() {
    return title;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final Course other = (Course) obj;
    if (this.id != other.id) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 29 * hash + this.id;
    return hash;
  }

  @Override
  public int getId() {
    return id;
  }

  @Override
  public void setId(int i) {
    id = i;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String t) {
    title = t;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String l) {
    label = l;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String c) {
    code = c;
  }

  public short getLevel() {
    return level;
  }

  public void setLevel(short n) {
    level = n;
  }

  public short getNPlaces() {
    return nplaces;
  }

  public void setNPlaces(short n) {
    nplaces = n;
  }

  public boolean isCollective() {
    return collective;
  }

  public void setCollective(boolean n) {
    collective = n;
  }

  /**
   * Checks if the course is active.
   *
   * @return true if active
   * @version 1.1d
   * @see net.algem.course.CourseChoiceActiveModel
   */
  public boolean isActive() {
    return active;
  }

  /**
   * Sets the state (active or inactive) of a course.
   *
   * @param a 
   * @version 1.1d
   */
  public void setActive(boolean a) {
    active = a;
  }

  public int getSchool() {
    return (int) school;
  }

  public void setSchool(int j) {
    school = (short) j;
  }

  /**
   * Checks if the course is undefined.
   * Course which names match "A DEFINIR" or "NON DEFINI" or "UNDEFINED" are considered as templates.
   *
   * @return true if undefined
   */
  public boolean isUndefined() {
    String t = title.toLowerCase();
    return t.matches("^.*d[ée]finir.*$")
            || t.matches("^.*n[/\\\\][ad].*$")
            || t.matches("^.*non d[eé]fini.*$")
            || t.matches("^.*undefined.*$");
  }

  /**
   * Checks if the course is a single workshop (discovery workshop).
   *
   * @return true if code equals {@code ATP_CODE }
   * @since 2.4.a 04/05/12
   */
  public boolean isATP() {
    return ATP_CODE.equals(code.trim());
  }

  /**
   * Checks if course's code is of instrument type.
   *
   * @return true if code begins with {@code PRIVATE_INSTRUMENT_CODE}
   */
  public boolean isInstCode() {
    return PRIVATE_INSTRUMENT_CODE.equals(getCode());
  }

  public boolean isCourseCoInst() {
    return isCollective() && isInstCode();
  }

  /**
   * Checks if course has a valid length in its code.
   * Some courses may not include length in their codes (eg F.M. code)
   * 
   * @return true if code ends with 3 digits
   */
  public boolean hasValidCodeLength() {
    if (code == null || code.isEmpty() || code.length() < 3) {
      return false;
    }
    return code.substring(code.length() - 3).matches("[0-9]{3}");
  }

  /**
   * Gets the course's length.
   * If 0 is returned, the course's length is free.
   *
   * @return a length in minutes
   */
  public int getLength() {
    String c = getCode();
    if (isCollective() && !isATP()) {
      if ("AT".equals(c) || "F.M.".equals(c)) {
        return 60;
      } else {
        try {
          if (c.length() >= 3) {
            return Integer.parseInt(c.substring(c.length() - 3));
          }
        } catch (NumberFormatException nfe) {
          return 0;
        }
      }
    }
    return 0;
  }
}
