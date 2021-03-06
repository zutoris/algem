/*
 * @(#)ContactIO.java	2.15.8 21/03/18
 *
 * Copyright (c) 1999-2018 Musiques Tangentes. All Rights Reserved.
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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;
import net.algem.accounting.DirectDebitIO;
import net.algem.accounting.OrderLineIO;
import net.algem.bank.RibIO;
import net.algem.billing.InvoiceIO;
import net.algem.contact.member.MemberIO;
import net.algem.contact.member.PersonSubscriptionCard;
import net.algem.contact.member.PersonSubscriptionCardIO;
import net.algem.enrolment.TrainingAgreementIO;
import net.algem.planning.Schedule;
import net.algem.planning.ScheduleIO;
import net.algem.planning.ScheduleRange;
import net.algem.planning.ScheduleRangeIO;
import net.algem.room.RoomIO;
import net.algem.util.DataCache;
import net.algem.util.DataConnection;
import net.algem.util.GemLogger;
import net.algem.util.MessageUtil;
import net.algem.util.model.Model;
import net.algem.util.model.TableIO;

/**
 * IO methods for class {@link net.algem.contact.Contact}.
 *
 * @author <a href="mailto:eric@musiques-tangentes.asso.fr">Eric</a>
 * @author <a href="mailto:jmg@musiques-tangentes.asso.fr">Jean-Marc Gobat</a>
 * @version 2.15.8
 * @since 1.0a 07/07/1999
 */
public class ContactIO
  extends TableIO {

  private DataConnection dc;
  private PersonIO personIO;

  public ContactIO(DataConnection dc) {
    this.dc = dc;
    personIO = (PersonIO) DataCache.getDao(Model.Person);
  }

  public ContactIO(DataConnection dc, PersonIO personIO) {
    this.dc = dc;
    this.personIO = personIO;
  }

  /**
   *
   * @param c
   * @throws SQLException
   */
  public void insert(Contact c) throws SQLException {
    personIO.insert(c);
    Address a = c.getAddress();
    if (a != null) {
      a.setId(c.getId());
      addCity(a, dc);
      AddressIO.insert(a, dc);
    }
    Vector<Telephone> v = c.getTele();
    for (int i = 0; v != null && i < v.size(); i++) {
      Telephone tel = v.elementAt(i);
      tel.setIdper(c.getId());
      if (tel.getNumber().length() > 0) {
        TeleIO.insert(tel, i, dc);
      }
    }
    Vector<Email> ve = c.getEmail();
    if (ve != null) {
      for (int j = 0; j < ve.size(); j++) {
        Email em = ve.elementAt(j);
        em.setIdper(c.getId());
        if (em.getEmail() != null && em.getEmail().trim().length() > 0) {
          EmailIO.insert(ve.elementAt(j), j, dc);
        }
      }
    }

    Vector<WebSite> vsw = c.getSites();
    if (vsw != null) {
      for (int j = 0; j < vsw.size(); j++) {
        WebSite site = vsw.elementAt(j);
        site.setIdper(c.getId());
        site.setPtype(c.getType());
        WebSiteIO.insert(site, j, dc);
      }
    }
  }

  /**
   * Updates a contact.
   *
   * @param o old contact
   * @param n new contact
   * @throws java.sql.SQLException
   */
  public void update(Contact o, Contact n) throws SQLException {
    System.out.println("ContactIO.update " + o + " --> " + n);

    n.setId(o.getId());
    personIO.update(n);

    Address ao = o.getAddress();
    Address an = n.getAddress();
    if (ao == null) {
      if (an != null) {
        an.setId(o.getId());
        AddressIO.insert(an, dc);
        addCity(an, dc);
      }
    } else if (an != null && !ao.equals(an)) {
      an.setId(o.getId());
      AddressIO.update(an, dc);
      addCity(an, dc);
    } else if (an == null) {
      AddressIO.delete(ao, dc);
    }

    Vector<Telephone> oldtels = o.getTele();
    Vector<Telephone> newtels = n.getTele();

    int i = 0;
    for (; newtels != null && i < newtels.size(); i++) {
      Telephone nt = newtels.elementAt(i);
      if (oldtels != null && i < oldtels.size()) {
        if (!nt.equals(oldtels.elementAt(i))) {
          nt.setIdper(o.getId());
          TeleIO.update(nt, i, dc);
        }
      } else {
        nt.setIdper(o.getId());
        TeleIO.insert(nt, i, dc);
      }
    }
    // si le nombre d'anciens numéros > nombre nouveaux numéros
    for (; oldtels != null && i < oldtels.size(); i++) {
      TeleIO.delete(o.getId(), i, dc);
    }

    i = 0;
    Vector<Email> oldmails = o.getEmail();
    Vector<Email> newmails = n.getEmail();
    for (; newmails != null && i < newmails.size(); i++) {
      Email ne = newmails.elementAt(i);
      if (oldmails != null && i < oldmails.size()) {
        if (!ne.equals(oldmails.elementAt(i))) {
          EmailIO.update(ne, i, dc);
        }
      } else {
        EmailIO.insert(ne, i, dc);
      }
    }

    for (; oldmails != null && i < oldmails.size(); i++) {
      EmailIO.delete(o.getId(), i, dc);
    }
    updateSites(o.getSites(), n.getSites(), o.getId(), o.getType());

  }

  public void updateSites(Vector<WebSite> oldsites, Vector<WebSite> newsites, int idper, short ptype) throws SQLException {

    int i = 0;
    for (; newsites != null && i < newsites.size(); i++) {
      WebSite ns = newsites.elementAt(i);
      ns.setPtype(ptype);
      if (ns.getUrl().length() == 0) {
        continue;
      }
      if (oldsites != null && i < oldsites.size()) {
        if (!ns.equiv(oldsites.elementAt(i))) {
          ns.setIdper(idper);
          WebSiteIO.update(ns, i, dc);
        }
      } else {
        ns.setIdper(idper);
        WebSiteIO.insert(ns, i, dc);
      }
    }
    // si le nombre d'anciens sites > nombre nouveaux sites
    for (; oldsites != null && i < oldsites.size(); i++) {
      WebSite s = oldsites.elementAt(i);
      WebSiteIO.delete(s, i, dc);
    }
  }

  /**
   * Suppression of contact. Address, telephone, emails are also deleted.
   *
   * @param c
   * @throws java.sql.SQLException
   * @throws net.algem.contact.ContactDeleteException
   */
  public void delete(final Contact c) throws SQLException, ContactDeleteException {
    try {
      checkDelete(c, dc); // may throw ContactDeleteException
      dc.withTransaction(new DataConnection.SQLRunnable<Void>() {
        @Override
        public Void run(DataConnection conn) throws Exception {
          personIO.delete(c);

          AddressIO.delete(c.getId(), dc);

          if (c.getType() != Person.BANK) {
            TeleIO.delete(c.getId(), dc);
            EmailIO.delete(c.getId(), dc);
            WebSiteIO.delete(c.getId(), Person.PERSON, dc);
          }
          // member and rib might be deleted if exists
          if (c.getType() == Person.PERSON) {
            ((MemberIO) DataCache.getDao(Model.Member)).delete(c.getId());
            RibIO.delete(c.getId(), dc);
          }
          return null;
        }
      });

    } catch (Exception ex) {
      throw new ContactDeleteException(ex.getMessage());
    }
  }

  /**
   * Finds a contact by id.
   *
   * @param n
   * @param dc
   * @return a contact or null
   */
  public static Contact findId(int n, DataConnection dc) {
    String query = "WHERE p.id = " + n;
    Vector<Contact> v = find(query, true, dc);
    if (v.size() > 0) {
      return v.elementAt(0);
    }
    return null;
  }

  /**
   * Finds a contact.
   *
   * @param dc
   * @param query
   * @return e contact or null
   */
  public static Contact findId(String query, DataConnection dc) {
    Vector<Contact> v = find(query, true, dc);
    if (v.size() > 0) {
      return v.elementAt(0);
    }
    return null;
  }

  /**
   * Finds a contact by name and firstname.
   *
   * @param n name
   * @param p firstname
   * @param dc
   * @return a contact or null
   */
  public static Contact findId(String n, String p, DataConnection dc) {
    String query = "WHERE nom = '" + n + "' AND prenom = '" + p + "'";
    Vector<Contact> v = find(query, true, dc);
    if (v.size() > 0) {
      return v.elementAt(0);
    }
    return null;
  }

  /**
   * Finds a list of contacts.
   *
   * @param where
   * @param complete with email, address and telephone infos
   * @param dc
   * @return a list of contacts
   */
  public static Vector<Contact> find(String where, boolean complete, DataConnection dc) {

    Vector<Contact> v = new Vector<Contact>();
    Vector<Person> pl = PersonIO.find(where, dc);
    if (pl.isEmpty()) {
      return v;
    }
    Enumeration<Person> enu = pl.elements();
    while (enu.hasMoreElements()) {
      Person p = enu.nextElement();
      Contact c = new Contact(p);
      if (complete) {
        complete(c, dc);
      }
      v.addElement(c);
    }
    return v;
  }

  /**
   * Gets the number of persons responding to request.
   *
   * @param where
   * @param dc
   * @return an integer
   */
  public static int count(String where, DataConnection dc) {
    int cpt = 0;
    String query = "SELECT count(*) FROM " + PersonIO.TABLE + " p " + where;
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

  /**
   * Fills the contact data with address, telephones and emails.
   *
   * @param dc
   * @param c
   */
  public static void complete(Contact c, DataConnection dc) {
    try {
      c.setAddress(AddressIO.findId(c.getId(), dc));
      c.setTele(TeleIO.findId(c.getId(), dc));
      c.setEmail(EmailIO.find(c.getId(), dc));
      c.setWebSites(WebSiteIO.find(c.getId(), Person.PERSON, dc));
    } catch (SQLException ex) {
      GemLogger.logException(ex);
    }
  }

  public static String findOrgName(int orgId, DataConnection dc) {
    String query = "SELECT nom FROM " + OrganizationIO.TABLE + " WHERE idper=?";
    try (PreparedStatement ps = dc.prepareStatement(query)) {
      ps.setInt(1, orgId);
      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          return rs.getString(1);
        }
      }
    } catch (SQLException ex) {
      GemLogger.logException(ex);
    }
    return null;
  }

  public static String findCompanyName(int orgId, DataConnection dc) {
    String query = "SELECT coalesce(raison,nom) FROM " + OrganizationIO.TABLE + " WHERE idper=?";
    try (PreparedStatement ps = dc.prepareStatement(query)) {
      ps.setInt(1, orgId);
      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          return rs.getString(1);
        }
      }
    } catch (SQLException ex) {
      GemLogger.logException(ex);
    }
    return null;
  }

  /**
   * Automatic insertion of new city if postal code is not already registered.
   *
   * @since 2.0q
   * @author jm
   * @param dc
   * @param a
   */
  private static void addCity(Address a, DataConnection dc) {
    List<City> v = CityIO.findCity(a.getCdp(), dc);
    if (v == null || v.isEmpty()) {
      if (a.getCdp() != null && a.getCity() != null) {
        try {
          CityIO.insert(new City(a.getCdp(), a.getCity()), dc);
        } catch (SQLException se) {
          GemLogger.logException(se);
        }
      }
    }
  }

  /**
   * Checks contact data before suppression.
   *
   * @param dc dataCache
   * @param c the contact to delete
   * @throws SQLException
   * @throws ContactDeleteException if data exists for this contact
   */
  private static void checkDelete(Contact c, DataConnection dc) throws SQLException, ContactDeleteException {

    String msg = MessageUtil.getMessage("delete.exception");
    if (c.getType() == 0) {
      msg += MessageUtil.getMessage("contact.delete.company.warning");
      throw new ContactDeleteException(msg);
    }
    if (c.getType() == Person.PERSON || c.getType() == Person.ROOM) {
      // vérifier qu'il n'existe plus d'échéances pour ce contact (en tant que payeur)
      String check = "SELECT count(payeur) FROM " + OrderLineIO.TABLE + " WHERE payeur = " + c.getId();
      ResultSet rs = dc.executeQuery(check);
      if (rs.next()) {
        int count = rs.getInt(1);
        if (count > 0) {
          msg += MessageUtil.getMessage("contact.delete.payer.warning1", count);
          throw new ContactDeleteException(msg);
        }
      }
      // checks if contact is payer
      check = "SELECT idper FROM eleve WHERE payeur = " + c.getId() + " AND payeur != idper";
      rs = dc.executeQuery(check);
      if (rs.next()) {
        int a = rs.getInt(1);
        msg += MessageUtil.getMessage("contact.delete.payer.warning2", a);
        throw new ContactDeleteException(msg);
      }
      // checks if contact belongs to a group
      check = "SELECT id FROM groupe_det WHERE musicien = " + c.getId();
      rs = dc.executeQuery(check);
      if (rs.next()) {
        int g = rs.getInt(1);
        msg += MessageUtil.getMessage("contact.delete.musician.warning", g);
        throw new ContactDeleteException(msg);
      }
      // checks if some invoice exists with this contact
      check = "SELECT numero FROM " + InvoiceIO.TABLE + " WHERE debiteur = " + c.getId() + " OR adherent = " + c.getId();
      rs = dc.executeQuery(check);
      if (rs.next()) {
        String g = rs.getString(1);
        msg += MessageUtil.getMessage("contact.delete.invoice.warning", g);
        throw new ContactDeleteException(msg);
      }
      /* // checks if some rib exists for this contact (DO NOT USE) */
 /* check = "SELECT idper FROM " + RibIO.TABLE + " WHERE idper = " + c.getId();
       * rs = dc.executeQuery(check);
       * if (rs.next()) {
       * int g = rs.getInt(1);
       * msg += MessageUtil.getMessage("contact.delete.rib.warning", g);
       * throw new ContactDeleteException(msg);
       * } */
      // checks if some mandate exists for this contact
      check = "SELECT id FROM " + DirectDebitIO.TABLE + " WHERE payeur = " + c.getId();
      rs = dc.executeQuery(check);
      if (rs.next()) {
        int g = rs.getInt(1);
        msg += MessageUtil.getMessage("contact.delete.mandate.warning", g);
        throw new ContactDeleteException(msg);
      }
      // checks if some room is associated with this contact
      check = "SELECT id FROM " + RoomIO.TABLE + " WHERE payeur = " + c.getId() + " OR idper = " + c.getId();
      rs = dc.executeQuery(check);
      if (rs.next()) {
        int g = rs.getInt(1);
        msg += MessageUtil.getMessage("contact.delete.room.warning", g);
        throw new ContactDeleteException(msg);
      }
      // checks if contact is scheduled
      Vector<ScheduleRange> vp = ScheduleRangeIO.find("pg WHERE pg.adherent = " + c.getId(), dc);
      if (vp != null && vp.size() > 0) {
        msg += MessageUtil.getMessage("contact.delete.range.warning", vp.size());
        throw new ContactDeleteException(msg);
      }
      // checks if contact is scheduled (teacher or rehearsal)
      Vector<Schedule> vpl = ScheduleIO.find(" WHERE p.idper = " + c.getId(), dc);
      if (vpl != null && vpl.size() > 0) {
        msg += MessageUtil.getMessage("contact.delete.schedule.warning", vpl.size());
        throw new ContactDeleteException(msg);
      }
      // checks if contact has subscription cards
      List<PersonSubscriptionCard> cards = new PersonSubscriptionCardIO(dc).find(c.getId(), null, false, 0);
      if (cards.size() > 0) {
        msg += MessageUtil.getMessage("contact.delete.subscription.warning", cards.size());
        throw new ContactDeleteException(msg);
      }

      if (c.getOrganization() != null && c.getOrganization().getId() > 0) {
        // checks if organization is used in contracts/agreements
        int agreements = usedInAgreements(c.getId(), dc);
        if (agreements > 0) {
          msg += MessageUtil.getMessage("contact.delete.organization.agreements.warning", agreements);
          throw new ContactDeleteException(msg);
        }
         // checks organization members
        int orgMembers = countOrganizationMembers(c.getId(), dc);
        if (orgMembers > 0) {
          String msgKey = orgMembers == 1 ? "contact.delete.organization.member.warning" : "contact.delete.organization.members.warning";
          msg += MessageUtil.getMessage(msgKey, orgMembers);
          throw new ContactDeleteException(msg);
        }
      }
    } else if (c.getType() == Person.BANK) {
      String check = "SELECT count(guichetid) FROM " + RibIO.TABLE + " WHERE guichetid = " + c.getId();
      ResultSet rs = dc.executeQuery(check);
      if (rs.next()) {
        int g = rs.getInt(1);
        msg += MessageUtil.getMessage("contact.delete.bank.branch.warning", g);
        throw new ContactDeleteException(msg);
      }
    }

  }

  private static int countOrganizationMembers(int idper, DataConnection dc) throws SQLException {
    int found = 0;
    List<Person> members = new OrganizationIO(dc).findMembers(idper);
    for (Person p : members) {
      if (p.getId() != idper) {
        found++;
      }
    }
    return found;
  }

  private static int usedInAgreements(int idper,  DataConnection dc) throws SQLException {
    String query = "SELECT count(id) FROM " + TrainingAgreementIO.TABLE + " WHERE idorg = ?";

    try(PreparedStatement ps = dc.prepareStatement(query)) {
      ps.setInt(1, idper);
      try(ResultSet rs = ps.executeQuery()) {
        while(rs.next()) {
          return rs.getInt(1);
        }
      }
    }
    return 0;
  }
}
