/*
 * @(#)GemModule.java	2.7.i 26/02/13
 * 
 * Copyright (c) 1999-2012 Musiques Tangentes. All Rights Reserved.
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

package net.algem.util.module;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.ToolTipManager;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import net.algem.util.BundleUtil;
import net.algem.util.DataCache;
import net.algem.util.event.GemEvent;
import net.algem.util.event.GemEventListener;
import net.algem.util.model.GemCloseVetoException;

/**
 * Generic module.
 *
 * @author <a href="mailto:eric@musiques-tangentes.asso.fr">Eric</a>
 * @author <a href="mailto:jmg@musiques-tangentes.asso.fr">jean-marc gobat</a>
 * @version 2.7.i
 * @since 1.0a 06/07/2002
 */
public class GemModule
        implements GemEventListener, ActionListener
{

  public static final Dimension DEFAULT_SIZE = new Dimension(650, 450);
  public static final Dimension XXS_SIZE = new Dimension(240, 120);
  public static final Dimension XS_SIZE = new Dimension(320, 240);
  public static final Dimension S_SIZE = new Dimension(420, 400);
  public static final Dimension M_SIZE = new Dimension(750, 450);
  public static final Dimension L_SIZE = new Dimension(800, 500);
  public static final Dimension XL_SIZE = new Dimension(870, 500);
  public static final Dimension XXL_SIZE = new Dimension(905, 500);

  public static final Dimension POSTIT_SIZE = new Dimension(110, 500);
  public static final Dimension PLANNING_ITEM_SIZE = new Dimension(110, 500);
  public static final Dimension DAY_PLANNING_SIZE = new Dimension(920, 520);
  public static final Dimension MONTH_PLANNING_SIZE = new Dimension(700, 520);

  public static final String CONTACT_BROWSER_KEY = "Contact.browser";
  public static final String GROUP_BROWSER_KEY = "Group.browser";
  public static final String GROUPE_DOSSIER_KEY = "ModuleGroupe";
  public static final String GROUP_VIEW_KEY = "Group";
  public static final String ROOM_BROWSER_KEY = "Room.browser";
  public static final String ENROLMENT_BROWSER_KEY="Enrolment.browser";
  public static final String SALLE_DOSSIER_KEY = "ModuleSalle";
  public static final String ROOM_VIEW_KEY = "Room";
  public static final String GLOBAL_CONFIG_KEY = "Menu.configuration";
  public static final String CITY_KEY = "Menu.city";
  public static final String INVOICE_ITEM_BROWSER_KEY = BundleUtil.getLabel("Invoice.item.search.label");
  public static final String BOOKING_JOURNAL_KEY = "Menu.booking.journal";
  public static final String DEFAULT_ACCOUNT_KEY = "Menu.default.account";
  public static final String MODULE_BROWSER_KEY="Module.browser";
  public static final String MODULE_CREATE_KEY="Module.create";
  public static final String MODULE_DELETE_KEY="Module.delete";
  public static final String COURSE_BROWSER_KEY="Course.browser";
  public static final String COURSE_CREATE_KEY="Course.creation";
  public static final String COURSE_DELETE_KEY="Course.suppression";
  public static final String COURSE_SCHEDULING_KEY="Course.scheduling";
  public static final String WORKSHOP_BROWSER_KEY="Workshop.browser";
  public static final String WORKSHOP_SCHEDULING_KEY="Workshop.scheduling";
  public static final String POSTIT_CREATE_KEY="Postit.create";
  public static final String REPLACEMENT_KEY="Replacement";
  
	
	protected String label;
  protected GemView view;
  protected GemDesktop desktop;
  protected DataCache dataCache;
  // encapsulation panneau version V4
  Container container;

  public GemModule(String _label) {
    label = _label;
  }

  /**
  encapsulation panneau version V4
   */
  public GemModule(String _label, Container p) {
    this(_label);
    container = p;
  }

  /**
   * Default initialization to redefine in modules.
   */
  public void init() {
    view = new GemView(desktop, label);
    view.setSize(DEFAULT_SIZE);
    view.getContentPane().add("Center", container);
  }

  /**
   * Called by desktop when adding module.
   * Call init() in subtype classes for IHM creation.
   * @param _desktop
   * @see net.algem.util.module.GemDesktopCtrl#addModule(net.algem.util.module.GemModule) 
   * 
   */
  public void setDesktop(GemDesktop _desktop) {
    desktop = _desktop;
    dataCache = desktop.getDataCache();

    init();	// init module+IHM à redéfinir dans les modules

    view.addInternalFrameListener(new InternalFrameAdapter()
    {
      @Override
      public void internalFrameClosing(InternalFrameEvent evt) {
        try {
          close();
        } catch (GemCloseVetoException e) {
        }
      }
    });
  }

  public GemDesktop getDesktop() {
    return desktop;
  }

  public GemView getView() {
    return view;
  }

  public String getSID() {
    return null;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String _label) {
    label = _label;
  }

  @Override
  public String toString() {
    return label;
  }

  /**
   * Interface GemEventListener to redefine.
   * @param _evt
   */
  @Override
  public void postEvent(GemEvent _evt) {
  }

  /**
   * Interface ActionListener to redefine.
   * @param _evt
   */
  @Override
  public void actionPerformed(ActionEvent _evt) {
  }

  /**
   * To redefine.
   */
  public void close() throws GemCloseVetoException {
    view.close();
    desktop.removeModule(this);
  }

  //FIXME EG 08/2009 dans GemView
  public JMenuItem getMenuItem(String menu) {
    //JMenuItem m = new JMenuItem(dataCache.getLabel(menu+".label"));
    JMenuItem m = dataCache.getMenu2(menu);
    /*m.setMnemonic(dataCache.getLabel(menu+".mnemo").charAt(0));
    m.getAccessibleContext().setAccessibleDescription(dataCache.getLabel(menu+".info"));
    m.setToolTipText(dataCache.getLabel(menu+".bulle"));*/
    //int initialDelay = ToolTipManager.sharedInstance().getInitialDelay();
    ToolTipManager.sharedInstance().setInitialDelay(300);
    //m.setEnabled((dataCache.authorize(menu)));
    m.setActionCommand(menu);
    m.addActionListener(this);

    return m;
  }

  public JMenu createJMenu(String nom) {
    JMenu m = new JMenu(BundleUtil.getLabel(nom+".label"));
    m.setMnemonic(BundleUtil.getLabel(nom+".mnemo").charAt(0));

    return m;
  }

  public void setSize(Dimension d) {
    view.setSize(d);
  }

  public static String getClassName(Class c) {
    return c == null ? "" : c.getSimpleName();
  }
}
