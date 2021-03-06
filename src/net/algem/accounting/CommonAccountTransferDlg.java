/*
 * @(#)CommonAccountTransferDlg.java	2.15.9 07/06/18
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
package net.algem.accounting;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Frame;
import java.awt.GridBagLayout;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import net.algem.planning.DateFr;
import net.algem.util.BundleUtil;
import net.algem.util.DataCache;
import net.algem.util.GemLogger;
import net.algem.util.MessageUtil;
import net.algem.util.ui.GemPanel;
import net.algem.util.ui.GridBagHelper;
import net.algem.util.ui.MessagePopup;

/**
 * Common transfer dialog.
 *
 * @author <a href="mailto:eric@musiques-tangentes.asso.fr">Eric</a>
 * @author <a href="mailto:jmg@musiques-tangentes.asso.fr">Jean-Marc Gobat</a>
 * @version 2.15.9
 * @since 2.8.r 13/12/13
 */
public class CommonAccountTransferDlg
        extends AccountTransferDlg
{
  private AccountTransferView transferView;

  /**
   * Empty constructor used for testing.
   */
  public CommonAccountTransferDlg() {
  }

  public CommonAccountTransferDlg(Frame parent, DataCache dataCache, AccountExportService exportService) {
    super(parent, dataCache, exportService);
    setDisplay();
  }

  private void setDisplay() {

    transferView = new AccountTransferView(dataCache);

    setLayout(new BorderLayout());
    setTitle(BundleUtil.getLabel("Menu.schedule.payment.transfer.label"));

    GemPanel p = new GemPanel();
    p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

    GemPanel header = new GemPanel();
    header.setLayout(new GridBagLayout());
    GridBagHelper gb = new GridBagHelper(header);

    gb.add(new JLabel(BundleUtil.getLabel("Menu.file.label")), 0, 0, 1, 1, GridBagHelper.EAST);
    gb.add(filePath, 1, 0, 1, 1, GridBagHelper.HORIZONTAL, GridBagHelper.WEST);
    gb.add(chooser, 2, 0, 1, 1, GridBagHelper.WEST);

    p.add(header);
    p.add(transferView);

    add(p, BorderLayout.CENTER);
    add(buttons, BorderLayout.SOUTH);
    setLocation(200, 100);
    setSize(460,300);
    //pack();
  }

  @Override
  void transfer() {
    // mode of payment selected in dialog
    String modeOfPayment = transferView.getModeOfPayment();

    Vector<OrderLine> orderLines = getOrderLines(
      modeOfPayment,
      transferView.getDateStart(),
      transferView.getDateEnd(),
      transferView.getSchool(),
      transferView.withUnpaid()
    );
    if (orderLines.size() <= 0) {
      MessagePopup.information(this, MessageUtil.getMessage("payment.transfer.empty.collection"));
      return;
    }

    int errors = 0;
    setCursor(new Cursor(Cursor.WAIT_CURSOR));
    try {
      String codeJournal = "";
      Account documentAccount = exportService.getDocumentAccount(modeOfPayment);
      if (documentAccount != null) {
        codeJournal = exportService.getCodeJournal(documentAccount.getId());
      }
      String path = filePath.getText();

      if (transferView.withCSV()) {
        path = path.replace(".txt", ".csv");
        List<String> errorsCSV = exportService.exportCSV(path, orderLines);
        if (errorsCSV.size() > 0) {
          writeErrorLog(errorsCSV, path + ".log");
          MessagePopup.warning(this, MessageUtil.getMessage("payment.transfer.error.log.warning", new Object[] {errorsCSV.size(), path + ".log"}));
        }
      } else {
        if (path.toLowerCase().endsWith(".txt")) {
          path = path.replaceAll("(?i)\\.txt", exportService.getFileExtension());
        }
        if (ModeOfPayment.FAC.toString().equalsIgnoreCase(modeOfPayment)) {
          errors = exportService.tiersExport(path, orderLines);
        } else {
          // if transfer is native, filter payment orderlines
          orderLines = filter(orderLines);
          exportService.export(path, orderLines, codeJournal, documentAccount);
        }
        // maj transfer echeances
        updateTransfer(orderLines);
      }
      int transfered = orderLines.size() - errors;
      String msgKey = transfered > 1 ? "payment.transfer.info" : "payment.single.transfer.info";
      MessagePopup.information(this, MessageUtil.getMessage(msgKey, new Object[]{transfered, path}));
    } catch (IOException ioe) {
      GemLogger.log(ioe.getMessage());
    } catch (SQLException sqe) {
      GemLogger.logException(MessageUtil.getMessage("payment.transfer.exception"), sqe, this);
    } finally {
      setCursor(Cursor.getDefaultCursor());
    }
  }

  protected Vector<OrderLine> getOrderLines(String modeOfPayment, DateFr start, DateFr end, int school, boolean withUnpaid) {

    String query = "WHERE echeance >= '" + start + "' AND echeance <= '" + end + "' AND ecole = '" + school + "'";
    if (!withUnpaid) {
      query += " AND paye = 't'";
    }
    query += " AND transfert = 'f' AND reglement = '" + modeOfPayment + "'";
    // DO NOT export if no invoice is present
    if (ModeOfPayment.FAC.name().equals(modeOfPayment)) {
      query += " AND facture IS NOT NULL AND facture != ''";
    }

    return OrderLineIO.find(query, dc);
  }


}
