/*
 *  Czero Case is the Open Source Platform, realized by ImagoItalia Srl,
 *  to quickly develop and deploy innovative Case Management solutions.
 *  Czero Case framework, based on Java environment, enables designer
 *  and developers to build advanced solutions for document and process
 *  management ensuring compliance with government regulations
 *  and industry standards.
 * 
 *  Copyright (C) 2012 ImagoItalia srl <http://www.imagoitalia.com>
 *  
 *  This file is part of Czero Case.
 *  
 *  Czero Case is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *   
 *  Czero Case is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *   
 *  You should have received a copy of the GNU General Public License
 *  along with Czero Case.  If not, see <http://www.gnu.org/licenses/>.
*/
package org.czerocase.log.listener.console;

import org.czerocase.core.logging.LogEntry;
import org.czerocase.core.logging.LogListener;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ConsoleLogImpl implements LogListener {

	private static Map<Integer, String> levelMap;
	private static DateFormat dateFormat;
	

	static {
		levelMap = new HashMap<Integer, String>();
		levelMap.put(1, "ERROR");
		levelMap.put(2, "WARN");
		levelMap.put(3, "INFO");
		levelMap.put(4, "DEBUG");

		dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss,S");

	}

	public void logged(LogEntry logEntry) {
		
		if (logEntry.getMessage() != null) {

			String className = logEntry.getOrginalClass() != null ? 
					"[" + logEntry.getOrginalClass().getName() + "]" : "";
			
			System.out.println(dateFormat.format(new Date(logEntry.getTime()))
					+ "[" + logEntry.getBundle().getSymbolicName() + "]" 
					+ className
					+ "[" + levelMap.get(logEntry.getLevel()) + "]"
					+ " " + logEntry.getMessage());
		}

	}
}