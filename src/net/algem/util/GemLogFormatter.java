/*
 * @(#)GemLogFormatter.java	2.13.2 10/05/17
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
package net.algem.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 * Formatter for logging.
 * @author <a href="mailto:jmg@musiques-tangentes.asso.fr">Jean-Marc Gobat</a>
 * @version 2.13.2
 * @since 2.6.a 30/07/2012
 */
public class GemLogFormatter
	extends java.util.logging.Formatter
{
  private static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy H:mm:ss");

	@Override
	public String format(LogRecord record) {
        String nl = TextUtil.LINE_SEPARATOR;
		StringBuilder s = new StringBuilder(1000);

		Date d = new Date(record.getMillis());

		s.append(record.getLevel()).append(':').append(DATE_FORMAT.format(d)).append(" ").append(System.getProperty("user.name")).append(nl);
		s.append(record.getMessage()).append(nl).append(nl);
		return s.toString();
	}

	@Override
	public String getHead(Handler h) {
		return "============================================================================" + TextUtil.LINE_SEPARATOR;
	}

}

