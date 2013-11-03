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

package org.mobicents.protocols.ss7.m3ua.message.ssnm;

import org.mobicents.protocols.ss7.m3ua.message.M3UAMessage;
import org.mobicents.protocols.ss7.m3ua.parameter.AffectedPointCode;
import org.mobicents.protocols.ss7.m3ua.parameter.InfoString;
import org.mobicents.protocols.ss7.m3ua.parameter.NetworkAppearance;
import org.mobicents.protocols.ss7.m3ua.parameter.RoutingContext;

/**
 * Destination Restricted (DRST) message is optionally sent from the SGP to all concerned ASPs to indicate that the SG has
 * determined that one or more SS7 destinations are now restricted from the point of view of the SG, or in response to a DAUD
 * message, if appropriate. The M3UA layer at the ASP is expected to send traffic to the affected destination via an alternate
 * SG with a route of equal priority, but only if such an alternate route exists and is available. If the affected destination
 * is currently considered unavailable by the ASP, The MTP3-User should be informed that traffic to the affected destination can
 * be resumed. In this case, the M3UA layer should route the traffic through the SG initiating the DRST message.
 *
 * @author amit bhayani
 *
 */
public interface DestinationRestricted extends M3UAMessage {

    NetworkAppearance getNetworkAppearance();

    void setNetworkAppearance(NetworkAppearance p);

    RoutingContext getRoutingContexts();

    void setRoutingContexts(RoutingContext routingCntx);

    AffectedPointCode getAffectedPointCodes();

    void setAffectedPointCodes(AffectedPointCode afpcs);

    InfoString getInfoString();

    void setInfoString(InfoString str);

}
