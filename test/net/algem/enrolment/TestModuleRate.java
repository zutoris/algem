/*
 * @(#)TestModuleRate.java	2.13.1 17/04/17
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
 *
 */
package net.algem.enrolment;

import net.algem.accounting.ModeOfPayment;
import net.algem.course.Module;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author <a href="mailto:jmg@musiques-tangentes.asso.fr">Jean-Marc Gobat</a>
 * @version 2.13.1
 * @since 2.8.w 16/07/14
 */
public class TestModuleRate
{

  public TestModuleRate() {
  }

  @BeforeClass
  public static void setUpClass() {
  }

  @AfterClass
  public static void tearDownClass() {
  }

  @Before
  public void setUp() {
  }

  @After
  public void tearDown() {
  }

  @Test
  public void testRateCalcul() {
    Module m = new Module();
    m.setBasePrice(600.0);
    m.setMonthReducRate(7.0);
    m.setQuarterReducRate(10.0);
    m.setYearReducRate(12.0);

    ModuleDlg dlg = new ModuleDlg();

    String check = ModeOfPayment.CHQ.toString();
    String ddebit = ModeOfPayment.PRL.toString();
    String cash = ModeOfPayment.ESP.toString();

    // rating periodicity : module rate is linked to this parameter (if YEAR, the module basic rate is based on year)
    PricingPeriod def = PricingPeriod.YEAR;

    double payment = dlg.calculatePayment(m, ddebit, PayFrequency.MONTH, def, null);
    double expected = 558.0 / 9;
    assertTrue(expected == payment);
    payment = dlg.calculatePayment(m, ddebit, PayFrequency.QUARTER, def, null);
    expected = 540 / 3;
    assertTrue(expected == payment);

    payment = dlg.calculatePayment(m, cash, PayFrequency.QUARTER, def, null);
    expected = 600 / 3;
    assertTrue(expected == payment);

    payment = dlg.calculatePayment(m, check, PayFrequency.QUARTER, def, null);
    expected = 600 / 3;
    assertTrue(expected == payment);

    payment = dlg.calculatePayment(m, ddebit, PayFrequency.YEAR, def, null);
    expected = m.getBasePrice() - (m.getBasePrice() * 12 /100);//
    System.out.println("base = " + m.getBasePrice() + " expected = " + expected);
    assertTrue(expected == payment);

    def = PricingPeriod.QTER;
    payment = dlg.calculatePayment(m, ddebit, PayFrequency.MONTH, def, null);
    expected = 558.0 / 3;
    assertTrue("payment == " + payment, expected == payment);

    payment = dlg.calculatePayment(m, ddebit, PayFrequency.QUARTER, def, null);
    expected = 540.0;
    assertTrue(expected == payment);

    payment = dlg.calculatePayment(m, check, PayFrequency.QUARTER, def, null);
    expected = 600.0;
    assertTrue(expected == payment);

    payment = dlg.calculatePayment(m, check, PayFrequency.YEAR, def, null);
    expected = (m.getBasePrice() - (m.getBasePrice() * 12 /100)) * 3;//
    System.out.println("base = " + m.getBasePrice() + " expected = " + expected);
    assertTrue("payment == " + payment, expected == payment);

    payment = dlg.calculatePayment(m, ddebit, PayFrequency.YEAR, def, null);
//    expected = (m.getBasePrice() - (m.getBasePrice() * 12 /100)) * 3;//
    System.out.println("base = " + m.getBasePrice() + " expected = " + expected);
    assertTrue("payment == " + payment, expected == payment);

    //
    def = PricingPeriod.BIAN;
    payment = dlg.calculatePayment(m, ddebit, PayFrequency.MONTH, def, null);
    expected = 558 / 6;
    assertTrue("payment == " + payment, expected == payment);

    payment = dlg.calculatePayment(m, ddebit, PayFrequency.QUARTER, def, null);
    expected = 540.0 / 2;
    assertTrue(expected == payment);

    payment = dlg.calculatePayment(m, check, PayFrequency.YEAR, def, null);
    expected = m.getBasePrice() - (m.getBasePrice() * 12 /100);//
    System.out.println("base = " + m.getBasePrice() + " expected = " + expected);
    assertTrue("payment == " + payment, expected == payment);

  }

}
