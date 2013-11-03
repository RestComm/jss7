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

package org.mobicents.protocols.ss7.cap.api.service.gprs;

import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.GPRSEventSpecificInformation;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.GPRSEventType;
import org.mobicents.protocols.ss7.cap.api.service.gprs.primitive.PDPID;
import org.mobicents.protocols.ss7.inap.api.primitives.MiscCallInfo;

/**
 *
 eventReportGPRS {PARAMETERS-BOUND : bound} OPERATION ::= { ARGUMENT EventReportGPRSArg {bound} RETURN RESULT TRUE ERRORS
 * {unknownPDPID} CODE opcode-eventReportGPRS} -- Direction gprsSSF -> gsmSCF,Timer Tereg -- This operation is used to notify
 * the gsmSCF of a GPRS session or PDP context related -- events (e.g. PDP context activation) previously requested by the
 * gsmSCF in a -- RequestReportGPRSEventoperation.
 *
 * EventReportGPRSArg {PARAMETERS-BOUND : bound}::= SEQUENCE { gPRSEventType [0] GPRSEventType, miscGPRSInfo [1] MiscCallInfo
 * DEFAULT {messageType request}, gPRSEventSpecificInformation [2] GPRSEventSpecificInformation {bound} OPTIONAL, pDPID [3]
 * PDPID OPTIONAL, ... }
 *
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface EventReportGPRSRequest extends GprsMessage {

    GPRSEventType getGPRSEventType();

    MiscCallInfo getMiscGPRSInfo();

    GPRSEventSpecificInformation getGPRSEventSpecificInformation();

    PDPID getPDPID();

}