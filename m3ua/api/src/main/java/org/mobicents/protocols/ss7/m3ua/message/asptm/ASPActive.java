/*
 * JBoss, Home of Professional Open Source
 * Copyright ${year}, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */ 
package org.mobicents.protocols.ss7.m3ua.message.asptm;

import org.mobicents.protocols.ss7.m3ua.message.M3UAMessage;
import org.mobicents.protocols.ss7.m3ua.parameter.InfoString;
import org.mobicents.protocols.ss7.m3ua.parameter.RoutingContext;
import org.mobicents.protocols.ss7.m3ua.parameter.TrafficModeType;

/**
 * The ASP Active message is sent by an ASP to indicate to a remote M3UA peer
 * that it is ready to process signalling traffic for a particular Application
 * Server. The ASP Active message affects only the ASP state for the Routing
 * Keys identified by the Routing Contexts, if present.
 * 
 * @author amit bhayani
 * 
 */
public interface ASPActive extends M3UAMessage {

    /**
     * The Traffic Mode Type parameter identifies the traffic mode of operation
     * of the ASP within an AS. Optional
     * 
     * @return
     */
    public TrafficModeType getTrafficModeType();

    public void setTrafficModeType(TrafficModeType mode);

    /**
     * The optional Routing Context parameter contains (a list of) integers
     * indexing the Application Server traffic that the sending ASP is
     * configured/registered to receive.
     * 
     * @return
     */
    public RoutingContext getRoutingContext();

    public void setRoutingContext(RoutingContext rc);

    public InfoString getInfoString();

    public void setInfoString(InfoString str);

}
