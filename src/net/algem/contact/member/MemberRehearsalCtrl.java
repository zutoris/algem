/*
 * @(#)MemberRehearsalCtrl.java	2.15.8 26/03/18
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
package net.algem.contact.member;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import net.algem.contact.PersonFile;
import net.algem.contact.PersonFileEditor;
import net.algem.contact.PersonFileEvent;
import net.algem.planning.*;
import net.algem.planning.editing.ModifPlanEvent;
import net.algem.room.Room;
import net.algem.room.RoomIO;
import net.algem.room.RoomService;
import net.algem.util.BundleUtil;
import net.algem.util.DataCache;
import net.algem.util.GemLogger;
import net.algem.util.MessageUtil;
import net.algem.util.model.Model;
import net.algem.util.module.GemDesktop;
import net.algem.util.ui.FileTabDialog;
import net.algem.util.ui.MessagePopup;
import net.algem.util.ui.PopupDlg;

/**
 * Single rehearsal controller for a member.
 *
 * @author <a href="mailto:eric@musiques-tangentes.asso.fr">Eric</a>
 * @author <a href="mailto:jmg@musiques-tangentes.asso.fr">Jean-Marc Gobat</a>
 * @version 2.15.8
 * @since 1.0a 12/12/2001
 */
public class MemberRehearsalCtrl
        extends FileTabDialog
{

  private PersonFile personFile;
  private MemberRehearsalView view;
  private ActionListener actionListener;
  private MemberService memberService;

  public MemberRehearsalCtrl(GemDesktop desktop) {
    super(desktop);
    memberService = new MemberService(dc);
  }

  public MemberRehearsalCtrl(GemDesktop desktop, ActionListener listener, PersonFile dossier) {
    this(desktop);
    personFile = dossier;
    actionListener = listener;

    view = new MemberRehearsalView(dataCache.getList(Model.Room));
    view.set(personFile.getContact());

    setLayout(new BorderLayout());
    add(view, BorderLayout.CENTER);
    add(buttons, BorderLayout.SOUTH);
  }

  public void clear() {
    view.clear();
  }

  @Override
  public void load() {
    view.set(personFile.getContact());
  }

  @Override
  public boolean isLoaded() {
    return personFile != null;
  }

  @Override
  public void cancel() {
    actionListener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "AdherentRepetitionPonctuelle.Abandon"));
  }

  @Override
  public void validation() {

    if (!isEntryValid(view.getDate())) {
      return;
    }
    try {
      if (!save()) {
        return;
      }
      JOptionPane.showMessageDialog(this,
              MessageUtil.getMessage("planning.update.info"),
              MessageUtil.getMessage("rehearsal.member.entry"),
              JOptionPane.INFORMATION_MESSAGE);
      desktop.postEvent(new ModifPlanEvent(this, view.getDate(), view.getDate()));
      actionListener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "AdherentRepetitionPonctuelle.Validation"));
    } catch (MemberException ex) {
      GemLogger.logException(MessageUtil.getMessage("rehearsal.create.exception"), ex, this);
    }
  }

  /**
   * Updates member's card for single rehearsal.
   * If card doesn't exist, a new one is created.
   * Else, the remaining time is updated.
   * If there is not enough remaining time, a new card is created.
   *
   * @param pFile member's file
   * @date date of rehearsal
   * @param length rehearsal length
   * @param dlg dialog for selecting a subscription
   *
   * @return an amount
   * @throws SQLException
   */
  PersonSubscriptionCard updatePersonalCard(PersonFile pFile, RehearsalPass pass, Schedule dto) throws SQLException, MemberException {

    PersonSubscriptionCard last = pFile.getSubscriptionCard();//XXX le pass peut être différent du pass courant
    PersonSubscriptionCard nc = null;

    int timeLength = new Hour(dto.getStart()).getLength(new Hour(dto.getEnd()));
    if (last == null) {//aucune carte n'existe pour cette personne
      nc = createNewCard(pass, timeLength, pFile.getId(), new DateFr(new Date()), dto);//XXX choix peut etre null
      pFile.setSubscriptionCard(nc);
    } else {
      last.setSessions(memberService.getSessions(last.getId()));//lasy loading
      int remainder = last.getRest() - timeLength;
      if (remainder < 0) { // plus de place sur la carte
        last.setRest(0);
        Hour start = new Hour(dto.getStart());
        Hour offset = new Hour(start);
        //offset.incMinute(Math.abs(remainder));
        offset.incMinute(timeLength - Math.abs(remainder));
        Hour end = new Hour(dto.getEnd());
        dto.setStart(offset);
        dto.setEnd(end);
        nc = createNewCard(pass, Math.abs(remainder), last.getIdper(), new DateFr(new Date()), dto);
        pFile.setSubscriptionCard(nc);
        //current card session offset
        dto.setStart(start);
        dto.setEnd(offset);
      } else {
        RehearsalPass currentPass = (RehearsalPass) DataCache.findId(last.getPassId(), Model.PassCard);
        if (last.getRest() > currentPass.getTotalTime()) {// XXX current card pass
          // excedent généré par ex. par une annulation (ou un retrait d'heures)
          Hour offset = new Hour(dto.getStart());
          offset.incMinute(last.getRest() - currentPass.getTotalTime());
          dto.setStart(offset);// on ampute la durée de la session de la partie positive
        }
        last.setRest(remainder);
      }
      // update last card
      if (dto.getStart().lt(dto.getEnd())) {
        last.addSession(dto);
      }
      memberService.update(last);
    }
    return nc;
  }

  /**
   *
   * @param max max time
   * @param length time length of a session
   * @return 0 if length is longer than the total length
   */
  int calcRemainder(int max, int length) {
    if (max > length) {
      return max - length;
    }
    return 0;
  }

  /**
   * Creates a new subscription card.
   *
   * @param pass selected pass
   * @param length rehearsal length
   * @param idper person's id
   * @throws SQLException
   */
  PersonSubscriptionCard createNewCard(RehearsalPass pass, int length, int idper, DateFr date, Schedule dto) throws SQLException {

    PersonSubscriptionCard c = new PersonSubscriptionCard();
    c.setIdper(idper);
    c.setPassId(pass.getId());
    c.setPurchaseDate(date);
    c.setRest(calcRemainder(pass.getTotalTime(), length));
    c.addSession(dto);

    memberService.create(c);
    return c;
  }

  /**
   * Selects a subscription.
   *
   * @param dlg dialog
   * @return a rehearsal pass
   */
  private RehearsalPass choosePass(PopupDlg dlg) {
    dlg.show();
    if (dlg.isValidation()) {
      return ((RehearsalPassDlg) dlg).get();
    }
    return null;
  }

  private boolean isEntryValid(DateFr date) {

    String dateError = MessageUtil.getMessage("date.entry.error");//date incorrecte
    String entryError = MessageUtil.getMessage("entry.error");

    if (date.bufferEquals(DateFr.NULLDATE)) {
      MessagePopup.error(view, dateError, entryError);
      return false;
    }
    if (date.before(dataCache.getStartOfPeriod())
            || date.after(dataCache.getEndOfPeriod())) {
      MessagePopup.error(view, MessageUtil.getMessage("date.out.of.period"), entryError);
      return false;
    }

    Hour hStart = view.getHourStart();
    Hour hEnd = view.getHourEnd();

    if (hStart.toString().equals("00:00")
            || hEnd.toString().equals("00:00")
            || !(hEnd.after(hStart))) {
      MessagePopup.error(view, MessageUtil.getMessage("hour.range.error"), entryError);
      return false;
    }

    if (!RoomService.isOpened(view.getRoom(), date, hStart, hEnd)) {
      return false;
    }

    return true;
  }

  private boolean save() throws MemberException {

    ScheduleObject so = new MemberRehearsalSchedule();
    so.setDate(view.getDate());
    so.setIdPerson(personFile.getId());
    so.setStart(view.getHourStart());
    so.setEnd(view.getHourEnd());
    so.setIdRoom(view.getRoom());

    if (!isFree(so)) {
      return false;
    }

    boolean subscription = view.withCard();

    so.setType(Schedule.MEMBER);
    so.setNote(0);
    try {
      memberService.saveRehearsal(so);
      //ajout échéance et mise à jour choix abonnement
      if (subscription) {
        // recherche d'une choix d'abonnement pour cet adhérent
        RehearsalPass pass = null;
        List<RehearsalPass> passList = memberService.getPassList();
        if (passList.size() > 1) {
          PopupDlg dlg = new RehearsalPassDlg(view, passList);
          pass = choosePass(dlg);
        } else if (passList.size() == 1) {
          pass = passList.get(0);
        } else {
          MessagePopup.warning(this, MessageUtil.getMessage("no.subscription.pass.warning"));
          saveSinglePayment(view.getRoom(),0);
          return true;
        }

        PersonSubscriptionCard newCard = updatePersonalCard(personFile, pass, so);
        PersonFileEvent event = null;
        if (newCard != null) {
          memberService.saveRehearsalOrderLine(personFile, view.getDate(), pass.getAmount(), newCard.getId());
          event = new PersonFileEvent(newCard, PersonFileEvent.SUBSCRIPTION_CARD_CHANGED);
          MessagePopup.information(this, MessageUtil.getMessage("subscription.card.create.info"));
        } else {
          event = new PersonFileEvent(personFile.getSubscriptionCard(), PersonFileEvent.SUBSCRIPTION_CARD_CHANGED);
        }
        if (actionListener != null) {
          ((PersonFileEditor) actionListener).contentsChanged(event);
        }
      } else {
        saveSinglePayment(view.getRoom(), so.getId());
      }
    } catch (MemberException e) {
      throw e;
    } catch (SQLException sqe) {
      throw new MemberException(sqe.getMessage());
    }
    return true;
  }

  /**
   *
   * @param subscription subscription card selected
   * @param schedule the schedule to book
   * @throws MemberException
   */
  public void order(boolean subscription, Schedule schedule) throws MemberException {

    try {
      PersonFile pf = (PersonFile) DataCache.findId(schedule.getIdPerson(), Model.PersonFile);
      if (pf == null) {
        return;
      }
      // don't forget to set the card before searching a new one
      pf.setSubscriptionCard(memberService.getLastSubscription(pf.getId(), false));
      if (subscription) {
        // auto-select a pass
        int passId = pf.getSubscriptionCard() == null ? 0 : pf.getSubscriptionCard().getPassId();
        RehearsalPass pass = getPassFromList(memberService.getPassList(), passId);
        if (pass == null) {
          MessagePopup.warning(this, MessageUtil.getMessage("no.subscription.pass.warning"));
          saveSinglePayment(schedule, pf);
          return;
        }

        PersonSubscriptionCard newCard = updatePersonalCard(pf, pass, schedule);
        PersonFileEvent event = null;
        if (newCard != null) {
          memberService.saveRehearsalOrderLine(pf, schedule.getDate(), pass.getAmount(), newCard.getId());
          event = new PersonFileEvent(newCard, PersonFileEvent.SUBSCRIPTION_CARD_CHANGED);
          MessagePopup.information(this, MessageUtil.getMessage("subscription.card.create.info"));
        } else {
          event = new PersonFileEvent(pf.getSubscriptionCard(), PersonFileEvent.SUBSCRIPTION_CARD_CHANGED);
        }
        if (actionListener != null) {
          ((PersonFileEditor) actionListener).contentsChanged(event);
        }
      } else {
        saveSinglePayment(schedule, pf);
      }
    } catch (SQLException sqe) {
      throw new MemberException(sqe.getMessage());
    }
  }

  private RehearsalPass getPassFromList(List<RehearsalPass> passList, int passId) {
    if (passList == null || passList.isEmpty()) {
      return null;
    }
    for (RehearsalPass p : passList) {
      if (p.getId() == passId) {
        return p;
      }
    }
    return passList.get(0);
  }

  /**
   * Calculates the price of the session for this specific room {@literal roomId}
   * and possibly save it.
   * @throws SQLException
   * @param roomId room id
   * @param scheduleId schedule id (this id is used in order line for linking)
   * @throws SQLException
   */
  private void saveSinglePayment(int roomId, int scheduleId) throws SQLException {
    Room s = ((RoomIO) DataCache.getDao(Model.Room)).findId(roomId);
    double amount = RehearsalUtil.calcSingleRehearsalAmount(view.getHourStart(), view.getHourEnd(), s.getRate(), 1, dc);
    if (amount > 0.0) {
      memberService.saveRehearsalOrderLine(personFile, view.getDate(), amount, scheduleId);
    }
  }

  /**
   *
   * @param schedule schedule to book
   * @param pf person file
   * @throws SQLException
   */
  private void saveSinglePayment(Schedule schedule, PersonFile pf) throws SQLException {
    Room room = ((RoomIO) DataCache.getDao(Model.Room)).findId(schedule.getIdRoom());
    double amount = RehearsalUtil.calcSingleRehearsalAmount(schedule.getStart(), schedule.getEnd(), room.getRate(), 1, dc);
    if (amount > 0.0) {
      memberService.saveRehearsalOrderLine(pf, schedule.getDate(), amount, schedule.getId());
    }
  }

  private boolean isFree(ScheduleObject p) {
    // room checking
    String query = ConflictQueries.getRoomConflictSelection(p.getDate().toString(), p.getStart().toString(), p.getEnd().toString(), p.getIdRoom());
    if (ScheduleIO.count(query, dc) > 0) {
      MessagePopup.error(view, BundleUtil.getLabel("Room.conflict.label"), BundleUtil.getLabel("Conflit.label"));
      return false;
    }
    // rehearsal member checking
    query = ConflictQueries.getMemberRehearsalSelection(p.getDate().toString(), p.getStart().toString(), p.getEnd().toString(), p.getIdPerson());
    if (ScheduleIO.count(query, dc) > 0) {
      MessagePopup.error(view, BundleUtil.getLabel("Member.conflict.label"), BundleUtil.getLabel("Conflit.label"));
      return false;
    }

    // course member checking
    query = ConflictQueries.getCourseMemberSelection(p.getDate().toString(), p.getStart().toString(), p.getEnd().toString(), p.getIdPerson());
    if (ScheduleIO.count(query, dc) > 0) {
      MessagePopup.error(view, BundleUtil.getLabel("Member.conflict.label"), BundleUtil.getLabel("Conflit.label"));
      return false;
    }
    return true;
  }
}
