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

package org.mobicents.protocols.ss7.map.api.service.lsm;

import java.io.Serializable;

import org.mobicents.protocols.ss7.map.api.primitives.DiameterIdentity;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.LMSI;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement.SupportedLCSCapabilitySets;

/**
 *
 LCSLocationInfo ::= SEQUENCE { networkNode-Number ISDN-AddressString, -- NetworkNode-number can be msc-number, sgsn-number or
 * a dummy value of "0" lmsi [0] LMSI OPTIONAL, extensionContainer [1] ExtensionContainer OPTIONAL, ... , gprsNodeIndicator [2]
 * NULL OPTIONAL, -- gprsNodeIndicator is set only if the SGSN number is sent as the Network Node Number additional-Number [3]
 * Additional-Number OPTIONAL, supportedLCS-CapabilitySets [4] SupportedLCS-CapabilitySets OPTIONAL,
 * additional-LCS-CapabilitySets [5] SupportedLCS-CapabilitySets OPTIONAL, mme-Name [6] DiameterIdentity OPTIONAL,
 * aaa-Server-Name [8] DiameterIdentity OPTIONAL }
 *
 *
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
public interface LCSLocationInfo extends Serializable {

    ISDNAddressString getNetworkNodeNumber();

    LMSI getLMSI();

    MAPExtensionContainer getExtensionContainer();

    boolean getGprsNodeIndicator();

    AdditionalNumber getAdditionalNumber();

    SupportedLCSCapabilitySets getSupportedLCSCapabilitySets();

    SupportedLCSCapabilitySets getAdditionalLCSCapabilitySets();

    DiameterIdentity getMmeName();

    DiameterIdentity getAaaServerName();

}
