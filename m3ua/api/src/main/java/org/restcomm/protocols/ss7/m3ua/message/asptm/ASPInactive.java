/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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

package org.restcomm.protocols.ss7.m3ua.message.asptm;

import org.restcomm.protocols.ss7.m3ua.message.M3UAMessage;
import org.restcomm.protocols.ss7.m3ua.parameter.InfoString;
import org.restcomm.protocols.ss7.m3ua.parameter.RoutingContext;

/**
 * The ASP Inactive message is sent by an ASP to indicate to a remote M3UA peer that it is no longer an active ASP to be used
 * from within a list of ASPs. The ASP Inactive message affects only the ASP state in the Routing Keys identified by the Routing
 * Contexts, if present.
 *
 * @author amit bhayani
 *
 */
public interface ASPInactive extends M3UAMessage {

    /**
     * The optional Routing Context parameter contains (a list of) integers indexing the Application Server traffic that the
     * sending ASP is configured/registered to receive.
     *
     * @return
     */
    RoutingContext getRoutingContext();

    void setRoutingContext(RoutingContext rc);

    InfoString getInfoString();

    void setInfoString(InfoString str);
}
