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

package org.mobicents.protocols.ss7.m3ua.message.ssnm;

import org.mobicents.protocols.ss7.m3ua.message.M3UAMessage;
import org.mobicents.protocols.ss7.m3ua.parameter.AffectedPointCode;
import org.mobicents.protocols.ss7.m3ua.parameter.InfoString;
import org.mobicents.protocols.ss7.m3ua.parameter.NetworkAppearance;
import org.mobicents.protocols.ss7.m3ua.parameter.RoutingContext;
import org.mobicents.protocols.ss7.m3ua.parameter.UserCause;

/**
 * Destination User Part Unavailable (DUPU) message is used by an SGP to inform
 * concerned ASPs that a remote peer MTP3-User Part (e.g., ISUP or SCCP) at an
 * SS7 node is unavailable.
 * 
 * @author amit bhayani
 * 
 */
public interface DestinationUPUnavailable extends M3UAMessage {

    public NetworkAppearance getNetworkAppearance();

    public void setNetworkAppearance(NetworkAppearance p);

    public RoutingContext getRoutingContext();

    public void setRoutingContext(RoutingContext routingCntx);

    public AffectedPointCode getAffectedPointCode();

    public void setAffectedPointCode(AffectedPointCode afpc);

    public UserCause getUserCause();

    public void setUserCause(UserCause usrCau);

    public InfoString getInfoString();

    public void setInfoString(InfoString str);

}