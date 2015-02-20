/*
 * @(#)GroupIO.java	2.9.2.1 20/02/15
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
package net.algem.group;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import net.algem.config.Instrument;
import net.algem.config.InstrumentIO;
import net.algem.config.MusicStyle;
import net.algem.contact.Person;
import net.algem.contact.PersonIO;
import net.algem.util.DataCache;
import net.algem.util.DataConnection;
import net.algem.util.model.Cacheable;
import net.algem.util.model.Model;
import net.algem.util.model.TableIO;

/**
 * IO methods for class {@link net.algem.group.Group}.
 *
 * @author <a href="mailto:eric@musiques-tangentes.asso.fr">Eric</a>
 * @author <a href="mailto:jmg@musiques-tangentes.asso.fr">Jean-Marc Gobat</a>
 * @version 2.9.2.1
 * @since 1.0a 07/07/1999
 */
public class GroupIO
        extends TableIO
        implements Cacheable
{

  public static final String TABLE_DETAIL = "groupe_det";
  public static final String TABLE = "groupe";
  private static final String SEQUENCE = "groupe_id_seq";
  private DataConnection dc;

  public GroupIO(DataConnection dc) {
    this.dc = dc;
  }

  public void insert(Group g) throws SQLException {
    int n = nextId(SEQUENCE, dc);

    String query = "INSERT INTO " + TABLE + " VALUES(?,?,?,?,?,?)";
    PreparedStatement pst = dc.prepareStatement(query);
    pst.setInt(1, n);
    pst.setString(2, escape(g.getName()));
    pst.setInt(3, g.getStyle().getId());
    pst.setInt(4, g.getIdref());
    pst.setInt(5, g.getIdman());
    pst.setInt(6, g.getIdbook());

    pst.executeUpdate();
    pst.close();

    g.setId(n);
  }

  public void update(Group g) throws SQLException {
    String query = "UPDATE " + TABLE + " SET "
            + "nom = ?"
            + ",style = ?"
            + ",referent = ?"
            + ",manager = ?"
            + ",tourneur = ?";
    query += " WHERE id = " + g.getId();
    PreparedStatement pst = dc.prepareStatement(query);
    pst.setString(1, g.getName());
    pst.setInt(2, g.getStyle().getId());
    pst.setInt(3, g.getIdref());
    pst.setInt(4, g.getIdman());
    pst.setInt(5, g.getIdbook());
    pst.executeUpdate();
    pst.close();
  }

  public void delete(Group g) throws SQLException {
    String query = "DELETE FROM " + TABLE + " WHERE id = " + g.getId();
    dc.executeUpdate(query);
  }

  /** 
   * Gets the group with id {@code n}.
   * If the group is not in cache, it is searched in database.
   * @param n id of the group
   * @return a group instance or null if not found
   * @throws java.sql.SQLException
   */
  public Group findId(Integer n) throws SQLException {
    String query = "WHERE id = " + n;
    Vector<Group> v = find(query);
    if (v != null && v.size() > 0) {
      return v.elementAt(0);
    }
    return null;
  }

  /**
   * Finds a group by criterion {@code where}.
   * @param where 
   * @return a list of groups
   * @throws java.sql.SQLException
   */
  public Vector<Group> find(String where) throws SQLException {

    String query = "SELECT DISTINCT * FROM " + TABLE + " " + where;
    Vector<Group> v = new Vector<Group>();

    ResultSet rs = dc.executeQuery(query);
    while (rs.next()) {
      Group g = new Group();
      g.setId(rs.getInt(1));
      g.setName(unEscape(rs.getString(2).trim()));
      int idStyle = rs.getInt(3);
      MusicStyle style = findStyle(idStyle);
      if (style == null) {
        style = new MusicStyle(0, "Autre");
      }
      g.setStyle(style);
      g.setIdContact(rs.getInt(4), rs.getInt(5), rs.getInt(6));

      v.addElement(g);
    }
    rs.close();

    return v;
  }

  public Vector<Group> find(int idper) throws SQLException {
    String where = "WHERE id in(SELECT id FROM " + TABLE_DETAIL + " WHERE musicien = " + idper + ")";
    return find(where);
  }

  public Vector<Musician> findMusicians(Group g) throws SQLException {
    Vector<Musician> vm = findMusicians(g.getId());
    if (vm != null) {
      for (Musician m : vm) {
        m.setGroup(g);
      }
    }
    return vm;
  }

  /**
   * Finds the list of musicians of the group <code>id</code>.
   *
   * @param id group id
   * @return a list of musicians
   * @throws java.sql.SQLException
   */
  public Vector<Musician> findMusicians(int id) throws SQLException {
    Vector<Musician> v = new Vector<Musician>();
    String query = "SELECT musicien FROM " + TABLE_DETAIL + " WHERE id = " + id;
    ResultSet rs = dc.executeQuery(query);
    while (rs.next()) {
      Person p = ((PersonIO) DataCache.getDao(Model.Person)).findId(rs.getInt(1));//musicien
      if (p != null) {
        Musician m = new Musician(p);//personne, instrument
        List<Integer> li = InstrumentIO.find(p.getId(), Instrument.MUSICIAN, dc);
        if (li != null && !li.isEmpty()) {
          m.setInstrument(li.get(0));
        }
        v.addElement(m);
      }
    }
    rs.close();

    return v.isEmpty() ? null : v;
  }

  public void insert(int g, Musician p) throws SQLException {
    String query = "INSERT INTO " + TABLE_DETAIL + " VALUES(" + g + "," + p.getId() + ")";
    dc.executeUpdate(query);
    
    if (p.getInstrument() > 0) {
      List<Integer> li = new ArrayList<Integer>();
      li.add(p.getInstrument());
      InstrumentIO.insert(li, p.getId(), Instrument.MUSICIAN, dc);
    }
  }

  private MusicStyle findStyle(int id) throws SQLException {
    return (MusicStyle) DataCache.findId(id, Model.MusicStyle);
  }
  
  /**
   * Find by style.
   * @param styleId
   * @return the number of groups with this {@code styleId}.
   * @throws SQLException 
   */
  public int findByStyle(int styleId) throws SQLException {
    Vector<Group> vg = find("WHERE style = " + styleId);
    return vg.size();
  }

  @Override
  public List<Group> load() throws SQLException {
    return find("ORDER BY nom");
  }
}
