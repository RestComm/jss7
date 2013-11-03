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

package org.mobicents.protocols.ss7.m3ua.message.mgmt;

import org.mobicents.protocols.ss7.m3ua.message.M3UAMessage;
import org.mobicents.protocols.ss7.m3ua.parameter.AffectedPointCode;
import org.mobicents.protocols.ss7.m3ua.parameter.DiagnosticInfo;
import org.mobicents.protocols.ss7.m3ua.parameter.ErrorCode;
import org.mobicents.protocols.ss7.m3ua.parameter.NetworkAppearance;
import org.mobicents.protocols.ss7.m3ua.parameter.RoutingContext;

/**
 * The Error message is used to notify a peer of an error event associated with an incoming message. For example, the message
 * type might be unexpected given the current state, or a parameter value might be invalid. Error messages MUST NOT be generated
 * in response to other Error messages.
 *
 * @author amit bhayani
 *
 */
public interface Error extends M3UAMessage {

    ErrorCode getErrorCode();

    void setErrorCode(ErrorCode code);

    RoutingContext getRoutingContext();

    void setRoutingContext(RoutingContext rc);

    NetworkAppearance getNetworkAppearance();

    void setNetworkAppearance(NetworkAppearance netApp);

    AffectedPointCode getAffectedPointCode();

    void setAffectedPointCode(AffectedPointCode affPc);

    DiagnosticInfo getDiagnosticInfo();

    void setDiagnosticInfo(DiagnosticInfo diagInfo);

}
