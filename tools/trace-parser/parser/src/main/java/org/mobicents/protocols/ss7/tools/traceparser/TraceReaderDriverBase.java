/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2013, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package org.mobicents.protocols.ss7.tools.traceparser;

import java.util.ArrayList;

import org.apache.log4j.Logger;

/**
 *
 * @author sergey vetyutnev
 *
 */
public abstract class TraceReaderDriverBase implements TraceReaderDriver {

    protected Logger loger = Logger.getLogger(TraceReaderDriverBase.class);

    protected ArrayList<TraceReaderListener> listeners = new ArrayList<TraceReaderListener>();
    protected boolean isStarted = false;

    protected ProcessControl processControl;
    protected String fileName;

    public TraceReaderDriverBase(ProcessControl processControl, String fileName) {
        this.processControl = processControl;
        this.fileName = fileName;
    }

    public void addTraceListener(TraceReaderListener listener) {
        this.listeners.add(listener);
    }

    public void removeTraceListener(TraceReaderListener listener) {
        this.listeners.remove(listener);
    }

    public void stop() {
        this.isStarted = false;
    }

}
