/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
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
