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
package org.czerocase.log.listener.console.activation;

import org.czerocase.core.logging.LogReaderService;
import org.czerocase.log.listener.console.ConsoleLogImpl;

import java.util.Iterator;
import java.util.LinkedList;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.util.tracker.ServiceTracker;

public class StdOutActivator implements BundleActivator
{
    private ConsoleLogImpl m_console = new ConsoleLogImpl();
    private LinkedList<LogReaderService> m_readers = new LinkedList<LogReaderService>();

    //  We use a ServiceListener to dynamically keep track of all the LogReaderService service being
    //  registered or unregistered
    private ServiceListener m_servlistener = new ServiceListener() {
        public void serviceChanged(ServiceEvent event)
        {
            BundleContext context = event.getServiceReference().getBundle().getBundleContext();
            LogReaderService logReaderService = (LogReaderService)context.getService(event.getServiceReference());
            if (logReaderService != null)
            {
                if (event.getType() == ServiceEvent.REGISTERED)
                {
                    m_readers.add(logReaderService);
                    logReaderService.addLogListener(m_console);
                } else if (event.getType() == ServiceEvent.UNREGISTERING)
                {
                    logReaderService.removeLogListener(m_console);
                    m_readers.remove(logReaderService);
                }
            }
        }
    };

    public void start(BundleContext context) throws Exception
    {
        // Get a list of all the registered LogReaderService, and add the console listener
        ServiceTracker logReaderTracker = new ServiceTracker(context, LogReaderService.class.getName(), null);
        logReaderTracker.open();
        Object[] readers = logReaderTracker.getServices();
        if (readers != null)
        {
            for (int i=0; i<readers.length; i++)
            {
                LogReaderService lrs = (LogReaderService)readers[i];
                m_readers.add(lrs);
                lrs.addLogListener(m_console);
            }
        }

        logReaderTracker.close();
       
        // Add the ServiceListener, but with a filter so that we only receive events related to LogReaderService
        String filter = "(objectclass=" + LogReaderService.class.getName() + ")";
        try {
            context.addServiceListener(m_servlistener, filter);
        } catch (InvalidSyntaxException e) {
            e.printStackTrace();
        }
    }

    public void stop(BundleContext context) throws Exception
    {
        for (Iterator<LogReaderService> i = m_readers.iterator(); i.hasNext(); )
        {
            LogReaderService lrs = i.next();
            lrs.removeLogListener(m_console);
            i.remove();
        }
    }

}
