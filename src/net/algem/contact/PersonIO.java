/*
 * @(#)PersonIO.java 2.9.1 04/11/14
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
package net.algem.contact;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import net.algem.billing.InvoiceIO;
import net.algem.util.DataConnection;
import net.algem.util.GemLogger;
import net.algem.util.model.Cacheable;
import net.algem.util.model.TableIO;

/**
 * IO methods for class {@link net.algem.contact.Person}.
 *
 * @author <a href="mailto:eric@musiques-tangentes.asso.fr">Eric</a>
 * @author <a href="mailto:jmg@musiques-tangentes.asso.fr">Jean-Marc Gobat</a>
 * @version 2.9.1
 */
public class PersonIO
        extends TableIO
        implements Cacheable
{

  public static final String TABLE = "personne";
  public static final String ALIAS = "p";
  public static final String COLUMNS = "p.id,p.ptype,p.nom,p.prenom,p.civilite,p.droit_img,p.organisation,p.partenaire,p.pseudo";
  public static final String SEQUENCE = "idper";
  
  /** Next column number in joined queries. */
  public static final int PERSON_COLUMNS_OFFSET = 10;

  private DataConnection dc;

  public PersonIO(DataConnection _dc) {
    this.dc = _dc;
  }

  public void insert(Person p) throws SQLException {
    int n = nextId(SEQUENCE, dc);
    // numero peut être remplacé par le mot clé DEFAULT à l'insertion
    // dans le cas où la colonne id est de type serial ou une valeur par défaut (nextval('idper') a été définie
    String query = "INSERT INTO " + TABLE + " VALUES(" + n
            + ", " + p.getType()
            + ",'" + escape(p.getName().toUpperCase())
            + "','" + escape(p.getFirstName())
            + "','" + p.getGender()
            + "','" + (p.hasImgRights() ? "t" : "f") // t pour non autorisation, f pour autorisation image
            + (p.getOrganization() == null || p.getOrganization().isEmpty() ? "',NULL" : "','" + escape(p.getOrganization()) + "'")
            + ",'" + (p.isPartnerInfo() ? "t" : "f") 
            + (p.getNickName() == null || p.getNickName().isEmpty() ? "',NULL" : "','" + escape(p.getNickName()) + "'") + ")";

    dc.executeUpdate(query);
    p.setId(n);
  }

  public void update(Person p) throws SQLException {
    String query;

    query = "UPDATE " + TABLE + " SET "
            + "nom = '" + escape(p.getName())
            + "',prenom = '" + escape(p.getFirstName())
            + "',civilite = '" + p.getGender() + "'"
            + ",droit_img = '" + (p.hasImgRights() ? "t" : "f")
            + "',organisation = " + (p.getOrganization() == null || p.getOrganization().isEmpty() ? "NULL" : "'" + escape(p.getOrganization()) + "'")
            + ",partenaire = '" + (p.isPartnerInfo() ? "t" : "f")
            + "',pseudo = " + (p.getNickName() == null || p.getNickName().isEmpty() ? "NULL" : "'" + escape(p.getNickName()) + "'")
            + " WHERE id = " + p.getId();

    dc.executeUpdate(query);
  }

  public void delete(Person p) throws SQLException {
    String query = "DELETE FROM " + TABLE + " WHERE id = " + p.getId() + " AND ptype = " + p.getType();
    dc.executeUpdate(query);
  }

  public Person findId(String n) {
    return findId(new Integer(n));
  }

  public Person findId(int n) {
    return findId(new Integer(n));
  }

  public Person findId(Integer n) {
    String query = "WHERE id = " + n;
    Vector<Person> v = find(query, dc);
    if (v != null && v.size() > 0) {
      return v.elementAt(0);
    }
    return null;
  }

  public static Person getFromRS(ResultSet rs) throws SQLException {
    Person p = new Person();
    p.setId(rs.getInt(1));
    p.setType(rs.getShort(2));
    String name = unEscape(rs.getString(3));
    p.setName(name != null ? name.trim() : null);
    String firstname = unEscape(rs.getString(4));
    p.setFirstName(firstname != null ? firstname.trim() : null);
    String cv = rs.getString(5);
    p.setGender(cv != null ? cv.trim() : null);
    p.setImgRights(rs.getBoolean(6));
    p.setOrganization(unEscape(rs.getString(7)));
    p.setPartnerInfo(rs.getBoolean(8));
    String nickname = rs.getString(9);
    p.setNickName(nickname != null ? unEscape(nickname.trim()) : null);

    return p;
  }

  public static Vector<Person> find(String where, DataConnection dc) {
    Vector<Person> v = new Vector<Person>();
    String query = "SELECT * FROM " + TABLE + " " + where + " ORDER BY nom";
    try {
      ResultSet rs = dc.executeQuery(query);
      while (rs.next()) {
        v.addElement(getFromRS(rs));
      }
    } catch (SQLException e) {
      GemLogger.logException(query, e);
    }
    return v;
  }

  public static int count(String where, DataConnection dc) {
    int cpt = 0;
    String query = "SELECT count(id) FROM " + TABLE + " " + where;
    try {
      ResultSet rs = dc.executeQuery(query);
      if (rs.next()) {
        cpt = rs.getInt(1);
      }
    } catch (SQLException e) {
      GemLogger.logException(query, e);
    }
    return cpt;
  }

  @Override
  public List<Person> load() throws SQLException {
    List<Person> lp = new ArrayList<Person>();
    String query = "SELECT " + COLUMNS + " FROM  " + TABLE + " p"
            + " WHERE p.id IN (SELECT debiteur FROM " + InvoiceIO.TABLE + ")"
            + " OR p.id IN (SELECT adherent FROM " + InvoiceIO.TABLE + ")";
    ResultSet rs = dc.executeQuery(query);
    while(rs.next()) {
      lp.add(getFromRS(rs));
    }
    return lp;
  }
  
}
