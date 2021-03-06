/*
 * @(#)StatusCtrl.java 2.9.4.13 15/10/15
 * 
 * Copyright (c) 1999-2015 Musiques Tangentes. All Rights Reserved.
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

package net.algem.config;

import java.sql.SQLException;
import net.algem.planning.ActionIO;
import net.algem.planning.ActionService;
import net.algem.util.DataCache;
import net.algem.util.GemLogger;
import net.algem.util.MessageUtil;
import net.algem.util.event.GemEvent;
import net.algem.util.model.Model;
import net.algem.util.module.GemDesktop;
import net.algem.util.ui.MessagePopup;

/**
 * Status management (Leisure, Professional, etc.).
 * 
 * @author <a href="mailto:jmg@musiques-tangentes.asso.fr">Jean-Marc Gobat</a>
 * @version 2.9.4.13
 * @since 2.5.a 06/07/12
 */
public class StatusCtrl 
  extends GemParamCtrl 
{
  
  public StatusCtrl(GemDesktop desktop, String title) {
    super(desktop, title);
  }

  @Override
  public void load() {
    service = new ActionService(desktop.getDataCache());
    try {
      load(service.getStatusAll().elements());
    } catch (SQLException ex) {
      GemLogger.logException(ex);
    }
  }

  @Override
  public void modification(Param current, Param p) throws SQLException, ParamException {
    if (p instanceof GemParam) {
      Status status = new Status((GemParam) p);
      if (isValidUpdate(status) && status.getId() > 0) {
        service.updateStatus(status);  
        desktop.getDataCache().update(status);
        desktop.postEvent(new GemEvent(this, GemEvent.MODIFICATION, GemEvent.STATUS, status));
      }
    }
  }

  @Override
  public void insertion(Param p) throws SQLException, ParamException {
    if (p instanceof GemParam) {
      Status status = new Status((GemParam) p);
      if (isValidInsert(status)) {
        service.insertStatus(status);
        p.setId(status.getId());// important
        desktop.getDataCache().add(status);
        desktop.postEvent(new GemEvent(this, GemEvent.CREATION, GemEvent.STATUS, status));
      }
    }
  }

  @Override
  public void suppression(Param p) throws Exception {
    if (p instanceof GemParam) {
      Status status = new Status((GemParam) p);
      if (status.getId() == 0) {
        throw new ParamException(MessageUtil.getMessage("status.default.delete.exception"));
      }
      int used = ((ActionIO) DataCache.getDao(Model.Action)).haveStatus(status.getId());
      if (used > 0) {
        throw new ParamException(MessageUtil.getMessage("status.delete.exception", used));
      }
      if (MessagePopup.confirm(this, MessageUtil.getMessage("param.delete.confirmation"))) {
        service.deleteStatus(status);
        desktop.getDataCache().remove(status);
        desktop.postEvent(new GemEvent(this, GemEvent.SUPPRESSION, GemEvent.STATUS, status));
      } else {
        throw new ParamException();
      }
    }
  }
  
  @Override
  protected boolean isValid(GemParam n) throws ParamException, SQLException {
    String msg = service.verifyStatus(n);
    if (msg != null) {
      throw new ParamException(msg);
    }
    return true;
  }

}
