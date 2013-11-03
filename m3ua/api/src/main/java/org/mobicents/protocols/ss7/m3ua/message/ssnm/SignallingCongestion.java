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
import org.mobicents.protocols.ss7.m3ua.parameter.ConcernedDPC;
import org.mobicents.protocols.ss7.m3ua.parameter.CongestedIndication;
import org.mobicents.protocols.ss7.m3ua.parameter.InfoString;
import org.mobicents.protocols.ss7.m3ua.parameter.NetworkAppearance;
import org.mobicents.protocols.ss7.m3ua.parameter.RoutingContext;

/**
 * <p>
 * The Signalling Congestion SCON message can be sent from an SGP to all concerned ASPs to indicate that an SG has determined
 * that there is congestion in the SS7 network to one or more destinations, or to an ASP in response to a DATA or DAUD message,
 * as appropriate. For some MTP protocol variants (e.g., ANSI MTP) the SCON message may be sent when the SS7 congestion level
 * changes. The SCON message MAY also be sent from the M3UA layer of an ASP to an M3UA peer, indicating that the congestion
 * level of the M3UA layer or the ASP has changed.
 * </p>
 *
 * @author amit bhayani
 *
 */
public interface SignallingCongestion extends M3UAMessage {

    NetworkAppearance getNetworkAppearance();

    void setNetworkAppearance(NetworkAppearance p);

    RoutingContext getRoutingContexts();

    void setRoutingContexts(RoutingContext routingCntx);

    AffectedPointCode getAffectedPointCodes();

    void setAffectedPointCodes(AffectedPointCode afpcs);

    ConcernedDPC getConcernedDPC();

    void setConcernedDPC(ConcernedDPC dpc);

    CongestedIndication getCongestedIndication();

    void setCongestedIndication(CongestedIndication congInd);

    InfoString getInfoString();

    void setInfoString(InfoString str);

}
