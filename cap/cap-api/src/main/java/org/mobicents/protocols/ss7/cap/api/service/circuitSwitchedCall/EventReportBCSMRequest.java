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

package org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall;

import org.mobicents.protocols.ss7.cap.api.primitives.CAPExtensions;
import org.mobicents.protocols.ss7.cap.api.primitives.EventTypeBCSM;
import org.mobicents.protocols.ss7.cap.api.primitives.ReceivingSideID;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.EventSpecificInformationBCSM;
import org.mobicents.protocols.ss7.inap.api.primitives.MiscCallInfo;

/**
 * <p>
 * The event report BCSM (ERB) operation is used in combination with CAP Request Report BCSM (RRB). It is used to report the
 * occurrence of an event, in the case that the event was previously armed. The mode in which the event is reported (EDP-N,
 * EDP-R) depends on the arming state of the event. See also the description of request report BCSM
 * {@link RequestReportBCSMEventRequest}
 *
 * </p>
 *
 * eventReportBCSM {PARAMETERS-BOUND : bound} OPERATION ::= { ARGUMENT EventReportBCSMArg {bound} RETURN RESULT FALSE ALWAYS
 * RESPONDS FALSE CODE opcode-eventReportBCSM} -- Direction: gsmSSF -> gsmSCF, Timer: Terb -- This operation is used to notify
 * the gsmSCF of a call-related event (e.g. BCSM -- events such as O_Busy or O_No_Answer) previously requested by the gsmSCF in
 * a -- RequestReportBCSMEvent operation.
 *
 * EventReportBCSMArg {PARAMETERS-BOUND : bound} ::= SEQUENCE { eventTypeBCSM [0] EventTypeBCSM, eventSpecificInformationBCSM
 * [2] EventSpecificInformationBCSM {bound} OPTIONAL, legID [3] ReceivingSideID OPTIONAL, miscCallInfo [4] MiscCallInfo DEFAULT
 * {messageType request}, extensions [5] Extensions {bound} OPTIONAL, ... }
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface EventReportBCSMRequest extends CircuitSwitchedCallMessage {

    EventTypeBCSM getEventTypeBCSM();

    EventSpecificInformationBCSM getEventSpecificInformationBCSM();

    ReceivingSideID getLegID();

    MiscCallInfo getMiscCallInfo();

    CAPExtensions getExtensions();

}