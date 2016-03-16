/*
 * @(#)DateFrField.java	2.9.6 16/03/16
 *
 * Copyright (c) 1999-2016 Musiques Tangentes. All Rights Reserved.
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

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JTextField;
import net.algem.util.BundleUtil;

/**
 * Date field for DateFr.
 *
 * @author <a href="mailto:eric@musiques-tangentes.asso.fr">Eric</a>
 * @author <a href="mailto:jmg@musiques-tangentes.asso.fr">jean-marc gobat</a>
 * @version 2.9.6
 * @since 1.0a 02/09/2001
 */
public class DateFrField extends JTextField
        implements KeyListener
{

  private int pos;
  private DateDocument buf;
  private final DateFormat mediumFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
  private final DateFormat simpleFormat = new SimpleDateFormat("EEEE dd MMM yyyy");
  private final DateFormat dayOfWeekFormat = new SimpleDateFormat("EEEE");

  public DateFrField(String s) {
    addKeyListener(this);
    buf = new DateDocument(this, s);
    setDocument(buf);
  }

  public DateFrField() {
    this(DateFr.NULLDATE);
  }

  public DateFrField(DateFr datefr) {
    this(datefr.toString());
  }

  public DateFrField(Date date) {
    this((new DateFr(date)).toString());
  }

  public DateFr get() {
    return buf.getDateFr();
  }

  public DateFr getDateFr() {
    return get();
  }

  public Date getDate() {
    return buf.getDate();
  }

  public void setDate(Date date) {
    set(date);
  }

  public void set(Date date) {
    buf = new DateDocument(this, (new DateFr(date)).toString());
    setDocument(buf);
    pos = 0;
  }

  public void setDate(DateFr datefr) {
    set(datefr);
  }

  public void set(DateFr datefr) {
    if (datefr != null) {
      buf = new DateDocument(this, datefr.toString());
      setDocument(buf);
      pos = 0;
    }
  }

  @Override
  public void keyTyped(KeyEvent ke) {
  }

  @Override
  public void keyReleased(KeyEvent ke) {
    char c = ke.getKeyChar();
    if (c == 'M' || c == 'm'
            || c == 'J' || c == 'j'
            || c == 'A' || c == 'a') {
      fireActionPerformed();
    }
  }

  @Override
  public void keyPressed(KeyEvent ke) {
    if (ke.isActionKey() && ke.getKeyCode() == KeyEvent.VK_F1) // 112)
    {
      CalendarDlg calendrierdlg = new CalendarDlg(this, BundleUtil.getLabel("Calendar.label"));
      String s = getText();
      DateFr datefr = new DateFr(s);
      if (!s.equals(DateFr.NULLDATE)) {
        calendrierdlg.setDate(datefr.getDate());
      }
      calendrierdlg.saisie();
      if (calendrierdlg.isValidate()) {
        DateFr datefr1 = new DateFr(calendrierdlg.getDate());
        buf.setDate(datefr1.toString());
        fireActionPerformed();
      }
      ke.consume();
    }
  }

  @Override
  public void setText(String s) {
    if (s != null) {
      buf = new DateDocument(this, s);
      setDocument(buf);
      pos = 0;
    }
  }

  @Override
  public Dimension getMinimumSize() {
    return new Dimension(90, 18);
  }

  @Override
  public String toString() {
    return buf.getDateFr().toString();
  }

  public String toSimpleString() {
    return simpleFormat.format(buf.getDate());
  }

  public String getDayOfWeek() {
    String dow = dayOfWeekFormat.format(buf.getDate());
    if (dow.length() < 8) {
      StringBuilder fill = new StringBuilder();
      for (int i = 8; i > dow.length(); i--) {
        fill.append(' ');
      }
      dow = fill.toString() + dow;
    }
    return dow;
  }

}
