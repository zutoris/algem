/*
 * @(#)ConflictException.java 2.6.a 19/09/12
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
package net.algem.planning;

/**
 *
 * @author <a href="mailto:jmg@musiques-tangentes.asso.fr">Jean-Marc Gobat</a>
 * @version 2.6.a
 * @since 2.1.p
 */
public class ConflictException extends Exception
{

  /**
   * Creates a new instance of <code>ConflictException</code> without detail message.
   */
  public ConflictException() {
  }

  /**
   * Constructs an instance of <code>ConflictException</code> with the specified detail message.
   * @param msg the detail message.
   */
  public ConflictException(String msg) {
    super(msg);
  }
}
