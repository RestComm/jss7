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

import java.io.Serializable;

import org.mobicents.protocols.ss7.map.api.primitives.AdditionalNumberType;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.LMSI;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;

/**
 *
 * LocationInfoWithLMSI ::= SEQUENCE { networkNode-Number [1] ISDN-AddressString, lmsi LMSI OPTIONAL, extensionContainer
 * ExtensionContainer OPTIONAL, ..., gprsNodeIndicator [5] NULL OPTIONAL, -- gprsNodeIndicator is set only if the SGSN number is
 * sent as the -- Network Node Number additional-Number [6] Additional-Number OPTIONAL -- NetworkNode-number can be either
 * msc-number or sgsn-number }
 *
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface LocationInfoWithLMSI extends Serializable {

    ISDNAddressString getNetworkNodeNumber();

    LMSI getLMSI();

    MAPExtensionContainer getExtensionContainer();

    AdditionalNumberType getAdditionalNumberType();

    ISDNAddressString getAdditionalNumber();

}
