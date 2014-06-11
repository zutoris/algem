/*
 * @(#)TestDateFr.java 2.8.v 11/06/14
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
package net.algem.planning;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import static org.junit.Assert.*;
import org.junit.*;

/**
 *
 * @author <a href="mailto:jmg@musiques-tangentes.asso.fr">Jean-Marc Gobat</a>
 * @version 2.8.v
 */
public class TestDateFr

{

  private Calendar cal;

  public TestDateFr() {
  }

  @BeforeClass
  public static void setUpClass() throws Exception {
  }

  @AfterClass
  public static void tearDownClass() throws Exception {
  }

  @Before
  public void setUp() {
    cal = Calendar.getInstance(Locale.FRANCE);
  }

  @After
  public void tearDown() {
  }

  @Test
  public void testEqualsDateFr() {
    DateFr d1 = new DateFr("10-10-2010");
    DateFr d2 = new DateFr("10-10-2010");

    assertTrue("time not equals ?", d1.getTime() == d2.getTime());
    assertEquals("datefr not equals ?", d1, d2);

    d2.decYear(1);
    assertEquals("10-10-2009", d2.toString());
    assertFalse("datefr equals ?", d1.equals(d2));

  }

  @Test
  public void testDateFormat() {
    Format f1 = new SimpleDateFormat("MMM yyyy");
    Format f2 = new SimpleDateFormat("EEEE dd-MM-yyyy");
    DateFr d1 = new DateFr("11-06-2014");
    String formatted = f2.format(d1.getDate());

    assertTrue(formatted, formatted.equals("mercredi 11-06-2014"));
    f2 = new SimpleDateFormat("EEE dd/MM/yyyy");
    formatted = f2.format(d1.getDate());
    assertTrue(formatted, formatted.equals("mer. 11/06/2014"));
    f2 = new SimpleDateFormat("EEEE dd MMM yyyy");
    formatted = f2.format(d1.getDate());
    assertTrue(formatted, formatted.equals("mercredi 11 juin 2014"));
  }
}
