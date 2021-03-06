/*
 * @(#)MemberExportDlg.java	2.15.0 26/07/2017
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
package net.algem.edition;

import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.sql.SQLException;
import javax.swing.JLabel;
import net.algem.accounting.*;
import net.algem.config.Param;
import net.algem.config.ParamChoice;
import net.algem.config.ParamTableIO;
import net.algem.config.SchoolCtrl;
import net.algem.planning.DateFr;
import net.algem.planning.DateRangePanel;
import net.algem.util.BundleUtil;
import net.algem.util.DataCache;
import net.algem.util.GemLogger;
import net.algem.util.MessageUtil;
import net.algem.util.module.GemDesktop;
import net.algem.util.ui.GemChoice;
import net.algem.util.ui.GemLabel;
import net.algem.util.ui.GemPanel;
import net.algem.util.ui.GridBagHelper;

/**
 * Export mailling members.
 * A member is defined by a specific account in order line view.
 *
 * @author <a href="mailto:eric@musiques-tangentes.asso.fr">Eric</a>
 * @author <a href="mailto:jmg@musiques-tangentes.asso.fr">Jean-Marc Gobat</a>
 * @version 2.15.0
 * @since 1.0a 14/12/1999
 */
public class MemberExportDlg
  extends ExportDlg {

  private static final String MEMBER_TITLE = BundleUtil.getLabel("Export.member.title");
  private GemPanel pCriterion;
  private GemChoice schoolChoice;
  private GemChoice account;
  private ParamChoice costAccount;
  private DateRangePanel dateRange;

  public MemberExportDlg(GemDesktop desktop) {
    super(desktop, MEMBER_TITLE);
  }

  public MemberExportDlg(Dialog _parent, DataCache dc) {
    super(_parent, MEMBER_TITLE);
  }

  @Override
  public GemPanel getCriterion() {

    pCriterion = new GemPanel();
    pCriterion.setLayout(new GridBagLayout());

    GridBagHelper gb = new GridBagHelper(pCriterion);

    schoolChoice = new ParamChoice(ParamTableIO.find(SchoolCtrl.TABLE, SchoolCtrl.SORT_COLUMN, dc));
    try {
      account = new AccountChoice(AccountIO.find(true, dc));
    } catch (SQLException ex) {
      GemLogger.logException(ex);
    }

    account.addItem(new Account(-1, "", MessageUtil.getMessage("export.criterium.any.account")));
    costAccount = new ParamChoice(ParamTableIO.find(CostAccountCtrl.tableName, CostAccountCtrl.columnName, dc));
    costAccount.addItem(new Param("", MessageUtil.getMessage("export.criterium.any.cost.account")));

    initDateRange();

    gb.add(new JLabel(BundleUtil.getLabel("Export.school.label")), 0, 0, 1, 1, GridBagHelper.WEST);
    gb.add(schoolChoice, 1, 0, 1, 1, GridBagHelper.WEST);
    gb.add(new JLabel(BundleUtil.getLabel("Export.account.label")), 0, 1, 1, 1, GridBagHelper.WEST);
    gb.add(account, 1, 1, 1, 1, GridBagHelper.WEST);
    gb.add(new JLabel(BundleUtil.getLabel("Export.cost.account.label")), 0, 2, 1, 1, GridBagHelper.WEST);
    gb.add(costAccount, 1, 2, 1, 1, GridBagHelper.WEST);
    gb.add(new GemLabel(BundleUtil.getLabel("Date.From.label")), 0, 3, 1, 1, GridBagHelper.WEST);
    gb.add(dateRange, 1, 3, 1, 1, GridBagHelper.WEST);

    schoolChoice.setPreferredSize(new Dimension(dateRange.getPreferredSize().width, schoolChoice.getPreferredSize().height));
    account.setPreferredSize(schoolChoice.getPreferredSize());
    costAccount.setPreferredSize(schoolChoice.getPreferredSize());
    return pCriterion;

  }

  @Override
  public String getRequest() {

    Account a = null;
    Account c = (Account) account.getSelectedItem();
    if (c == null) {
      c = new Account(0, "", "");
    }
    //c = (p == null) ? new Account() : new Account(p);

    Param n = (Param) costAccount.getSelectedItem();
    a = (n == null) ? new Account(0) : new Account(n);

    String query = "WHERE p.id IN ("
      + "SELECT DISTINCT p.id FROM personne p JOIN eleve e ON p.id = e.idper JOIN " + OrderLineIO.TABLE + " c ON p.id = c.adherent"
      + " WHERE c.echeance BETWEEN '" + dateRange.getStartFr().toString() + "' AND '" + dateRange.getEndFr().toString() + "'"
      + " AND c.ecole = '" + getSchool() + "'";
    query += c.getNumber().isEmpty() ? "" : " AND c.compte = '" + c.getId() + "'";
    query += a.getNumber().isEmpty() ? ")" : " AND c.analytique = '" + a.getNumber() + "')";

    return query;
  }

  private int getSchool() {
    return schoolChoice.getKey();
  }

  /**
   * Inits the period for search.
   * By default, the period stretches from the beginning of school year (first day in the month)
   * to the end of school year.
   */
  private void initDateRange() {
    DateFr b = new DateFr(desktop.getDataCache().getStartOfYear());
    b.setDay(1);
    dateRange = new DateRangePanel(b, desktop.getDataCache().getEndOfYear());
  }

  @Override
  protected String getFileName() {
    return BundleUtil.getLabel("Export.member.file");
  }
}
