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
package org.mobicents.protocols.ss7.m3ua.message.rkm;

import org.mobicents.protocols.ss7.m3ua.message.M3UAMessage;
import org.mobicents.protocols.ss7.m3ua.parameter.RoutingContext;

/**
 * The DEREG REQ message is sent by an ASP to indicate to a remote M3UA peer
 * that it wishes to deregister a given Routing Key. Typically, an ASP would
 * send this message to an SGP and expects to receive a DEREG RSP message in
 * return with the associated Routing Context value.
 * 
 * @author amit bhayani
 * 
 */
public interface DeregistrationRequest extends M3UAMessage {

    /**
     * The Routing Context parameter contains (a list of) integers indexing the
     * Application Server traffic that the sending ASP is currently registered
     * to receive from the SGP but now wishes to deregister.
     * 
     * @return
     */
    public RoutingContext getRoutingContext();

    public void setRoutingContext(RoutingContext rc);
}
