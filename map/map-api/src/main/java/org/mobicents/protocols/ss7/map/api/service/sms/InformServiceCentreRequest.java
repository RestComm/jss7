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

import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;

/**
 *
 MAP V2-3:
 *
 * MAP V3: informServiceCentre OPERATION ::= { --Timer s ARGUMENT InformServiceCentreArg CODE local:63 }
 *
 * MAP V2: InformServiceCentre ::= OPERATION --Timer s ARGUMENT informServiceCentreArg InformServiceCentreArg
 *
 * MAP V3: InformServiceCentreArg ::= SEQUENCE { storedMSISDN ISDN-AddressString OPTIONAL, mw-Status MW-Status OPTIONAL,
 * extensionContainer ExtensionContainer OPTIONAL, ... , absentSubscriberDiagnosticSM AbsentSubscriberDiagnosticSM OPTIONAL,
 * additionalAbsentSubscriberDiagnosticSM [0] AbsentSubscriberDiagnosticSM OPTIONAL } -- additionalAbsentSubscriberDiagnosticSM
 * may be present only if -- absentSubscriberDiagnosticSM is present. -- if included, additionalAbsentSubscriberDiagnosticSM is
 * for GPRS and -- absentSubscriberDiagnosticSM is for non-GPRS
 *
 * MAP V2: InformServiceCentreArg ::= SEQUENCE { storedMSISDN ISDN-AddressString OPTIONAL, mw-Status MW-Status OPTIONAL, ...}
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface InformServiceCentreRequest extends SmsMessage {

    ISDNAddressString getStoredMSISDN();

    MWStatus getMwStatus();

    MAPExtensionContainer getExtensionContainer();

    Integer getAbsentSubscriberDiagnosticSM();

    Integer getAdditionalAbsentSubscriberDiagnosticSM();

}
