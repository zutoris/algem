/*
 * @(#) TrainingContractIO.java Algem 2.15.0 05/09/17
 *
 * Copyright (c) 1999-2017 Musiques Tangentes. All Rights Reserved.
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
 */
package net.algem.enrolment;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import net.algem.course.Module;
import net.algem.course.ModuleIO;
import net.algem.planning.Hour;
import net.algem.planning.ScheduleIO;
import net.algem.planning.ScheduleRangeIO;
import net.algem.util.DataConnection;
import net.algem.util.GemLogger;
import net.algem.util.model.TableIO;

/**
 *
 * @author <a href="mailto:jmg@musiques-tangentes.asso.fr">Jean-Marc Gobat</a>
 * @version 2.15.0
 * @since 2.15.0 30/08/2017
 */
public class TrainingContractIO
        extends TableIO
{

  private final static String TABLE = "contratformation";
  private final static String COLUMNS = "id,ctype,idper,idcmd,libelle,saison,debut,fin,financement,total,montant,volumint,volumext,datesign";
  private final static String SEQUENCE = "contratformation_id_seq";

  private DataConnection dc;

  public TrainingContractIO(DataConnection dc) {
    this.dc = dc;
  }

  public void create(TrainingContract t) throws SQLException {

    int nextId = nextId(SEQUENCE, dc);

    String query = "INSERT INTO " + TABLE + "(" + COLUMNS + ") VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    try (PreparedStatement ps = dc.prepareStatement(query)) {
      ps.setInt(1, nextId);
      ps.setByte(2, t.getType());
      ps.setInt(3, t.getPersonId());
      ps.setInt(4, t.getOrderId());
      ps.setString(5, t.getLabel());
      ps.setString(6, t.getSeason());
      ps.setDate(7, new java.sql.Date(t.getStart().getTime()));
      ps.setDate(8, new java.sql.Date(t.getEnd().getTime()));
      ps.setString(9, t.getFunding());
      ps.setDouble(10, t.getTotal());
      ps.setDouble(11, t.getAmount());
      ps.setFloat(12, t.getInternalVolume());
      ps.setFloat(13, t.getExternalVolume());
      ps.setDate(14, new java.sql.Date(t.getSignDate().getTime()));
      GemLogger.info(ps.toString());

      ps.executeUpdate();
      t.setId(nextId);
    }
  }

  public void update(TrainingContract t) throws SQLException {
    String query = "UPDATE " + TABLE + " SET libelle=?,saison=?,debut=?,fin=?,financement=?,total=?,montant=?,volumint=?,volumext=?,datesign=? WHERE id = ?";
    try (PreparedStatement ps = dc.prepareStatement(query)) {

      ps.setString(1, t.getLabel());
      ps.setString(2,t.getSeason());
      ps.setDate(3, new java.sql.Date(t.getStart().getTime()));
      ps.setDate(4, new java.sql.Date(t.getEnd().getTime()));
      ps.setString(5, t.getFunding());
      ps.setDouble(6, t.getTotal());
      ps.setDouble(7, t.getAmount());
      ps.setFloat(8, t.getInternalVolume());
      ps.setFloat(9, t.getExternalVolume());
      ps.setDate(10, new java.sql.Date(t.getSignDate().getTime()));

      ps.setInt(11, t.getId());
      GemLogger.info(ps.toString());

      ps.executeUpdate();

    }
  }

  public void delete(int id) throws SQLException {
    String query = "DELETE FROM " + TABLE + " WHERE id = ?";
    try (PreparedStatement ps = dc.prepareStatement(query)) {
      ps.setInt(1, id);

      ps.executeUpdate();
    }
  }

  public TrainingContract find(int id) throws SQLException {
    String query = "SELECT * FROM " + TABLE + " WHERE id = ?";
    try (PreparedStatement ps = dc.prepareStatement(query)) {
      ps.setInt(1, id);
      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          return getFromRS(rs);
        }
      }
    }
    return null;
  }

  public List<TrainingContract> findAll(int idper) throws SQLException {
    String query = "SELECT * FROM " + TABLE + " WHERE idper = ?";
    List<TrainingContract> contracts = new ArrayList<>();
    try (PreparedStatement ps = dc.prepareStatement(query)) {
      ps.setInt(1, idper);
      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          contracts.add(getFromRS(rs));
        }
      }
    }
    return contracts;
  }

  /**
   * Converts a result row into a training contract.
   *
   * @param rs resultset instance
   * @return a training contract
   * @throws SQLException
   */
  private TrainingContract getFromRS(ResultSet rs) throws SQLException {
    TrainingContract t = new TrainingContract(rs.getInt(1));
    t.setType(rs.getByte(2));
    t.setPersonId(rs.getInt(3));
    t.setOrderId(4);
    t.setLabel(rs.getString(5));
    t.setSeason(rs.getString(6));
    t.setStart(rs.getDate(7));
    t.setEnd(rs.getDate(8));
    t.setFunding(rs.getString(9));
    t.setTotal(rs.getDouble(10));
    t.setAmount(rs.getDouble(11));
    t.setInternalVolume(rs.getShort(12));
    t.setExternalVolume(rs.getShort(13));
    t.setSignDate(rs.getDate(14));

    return t;
  }

  /**
   * Gets the total price and the main title of the training, based on subscribed modules.
   *
   * @param orderId last order id
   * @return a module instance
   * @throws SQLException
   */
  Module getModuleInfo(int orderId) throws SQLException {
    String query = "SELECT m.titre,m.prix_base,cm.tarification,cm.duree FROM " + ModuleIO.TABLE + " m JOIN " + ModuleOrderIO.TABLE + " cm ON m.id = cm.module JOIN " + OrderIO.TABLE + " c on cm.idcmd = c.id where c.id=?";

    try (PreparedStatement ps = dc.prepareStatement(query)) {
      ps.setInt(1, orderId);
      GemLogger.info(ps.toString());
      try (ResultSet rs = ps.executeQuery()) {
        double price = 0.0;
        String title = "";
        Module m = new Module();
        while (rs.next()) {
          title = rs.getString(1);
          double p = rs.getDouble(2);
          switch (rs.getString(3)) {
            case "MNTH":
              price += p * 9;
              break;
            case "QTER":
              price += p * 3;
              break;
            case "HOUR":
              price += p * (rs.getInt(4) / 60);
            default:
              price += p;
          }
        }
        m.setTitle(title);
        m.setBasePrice(price);

        return m;
      }

    }

  }

  /**
   * Gets the total hours done inside training center.
   *
   * @param orderId last order id
   * @return a number of hours, decimal-formatted
   * @throws SQLException
   */
  float getVolume(int orderId) throws SQLException {
    String query = "SELECT sum(duree) FROM (SELECT DISTINCT pl.fin-pl.debut AS duree,p.jour,pl.debut FROM "
            + ScheduleRangeIO.TABLE + " pl JOIN "
            + ScheduleIO.TABLE + " p ON pl.idplanning = p.id JOIN "
            + CourseOrderIO.TABLE + " cc ON p.action = cc.idaction JOIN "
            + OrderIO.TABLE + " c ON cc.idcmd = c.id WHERE c.id=?) AS th";
    try (PreparedStatement ps = dc.prepareStatement(query)) {
      ps.setInt(1, orderId);
      GemLogger.info(ps.toString());
      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          int min = Hour.getMinutesFromString(rs.getString(1));
          return min / 60f;
        }
      }
    }
    return 0.0f;
  }
}
