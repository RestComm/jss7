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
import org.mobicents.protocols.ss7.m3ua.parameter.UserCause;

/**
 * Destination User Part Unavailable (DUPU) message is used by an SGP to inform concerned ASPs that a remote peer MTP3-User Part
 * (e.g., ISUP or SCCP) at an SS7 node is unavailable.
 *
 * @author amit bhayani
 *
 */
public interface DestinationUPUnavailable extends M3UAMessage {

    NetworkAppearance getNetworkAppearance();

    void setNetworkAppearance(NetworkAppearance p);

    RoutingContext getRoutingContext();

    void setRoutingContext(RoutingContext routingCntx);

    AffectedPointCode getAffectedPointCode();

    void setAffectedPointCode(AffectedPointCode afpc);

    UserCause getUserCause();

    void setUserCause(UserCause usrCau);

    InfoString getInfoString();

    void setInfoString(InfoString str);

}