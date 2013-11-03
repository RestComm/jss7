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

package org.mobicents.protocols.ss7.map.api.service.sms;

import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;

/**
 *
 MAP V2-3:
 *
 * MAP V3: readyForSM OPERATION ::= { --Timer m ARGUMENT ReadyForSM-Arg RESULT ReadyForSM-Res -- optional ERRORS { dataMissing |
 * unexpectedDataValue | facilityNotSupported | unknownSubscriber} CODE local:66 }
 *
 * MAP V2: ReadyForSM ::= OPERATION --Timer m ARGUMENT readyForSM-Arg ReadyForSM-Arg RESULT ERRORS { DataMissing,
 * UnexpectedDataValue, FacilityNotSupported, UnknownSubscriber}
 *
 *
 * MAP V3: ReadyForSM-Arg ::= SEQUENCE { imsi [0] IMSI, alertReason AlertReason, alertReasonIndicator NULL OPTIONAL, --
 * alertReasonIndicator is set only when the alertReason -- sent to HLR is for GPRS extensionContainer ExtensionContainer
 * OPTIONAL, ..., additionalAlertReasonIndicator [1] NULL OPTIONAL -- additionalAlertReasonIndicator is set only when the
 * alertReason -- sent to HLR is for IP-SM-GW }
 *
 * MAP V2: ReadyForSM-Arg ::= SEQUENCE { imsi [0] IMSI, alertReason AlertReason, ...}
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface ReadyForSMRequest extends SmsMessage {

    IMSI getImsi();

    AlertReason getAlertReason();

    boolean getAlertReasonIndicator();

    MAPExtensionContainer getExtensionContainer();

    boolean getAdditionalAlertReasonIndicator();

}
