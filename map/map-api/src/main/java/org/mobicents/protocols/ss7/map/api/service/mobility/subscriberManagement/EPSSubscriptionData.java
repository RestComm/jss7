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

package org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement;

import java.io.Serializable;

import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;

/**
 *
 EPS-SubscriptionData ::= SEQUENCE { apn-oi-Replacement [0] APN-OI-Replacement OPTIONAL, -- this apn-oi-Replacement refers to
 * the UE level apn-oi-Replacement. rfsp-id [2] RFSP-ID OPTIONAL, ambr [3] AMBR OPTIONAL, apn-ConfigurationProfile [4]
 * APN-ConfigurationProfile OPTIONAL, stn-sr [6] ISDN-AddressString OPTIONAL, extensionContainer [5] ExtensionContainer
 * OPTIONAL, ..., mps-CSPriority [7] NULL OPTIONAL, mps-EPSPriority [8] NULL OPTIONAL } -- mps-CSPriority by its presence
 * indicates that the UE is subscribed to the eMLPP in -- the CS domain, referring to the 3GPP TS 29.272 [144] for details. --
 * mps-EPSPriority by its presence indicates that the UE is subscribed to the MPS in -- the EPS domain, referring to the 3GPP TS
 * 29.272 [144] for details.
 *
 * RFSP-ID ::= INTEGER (1..256)
 *
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface EPSSubscriptionData extends Serializable {

    APNOIReplacement getApnOiReplacement();

    Integer getRfspId();

    AMBR getAmbr();

    APNConfigurationProfile getAPNConfigurationProfile();

    ISDNAddressString getStnSr();

    MAPExtensionContainer getExtensionContainer();

    boolean getMpsCSPriority();

    boolean getMpsEPSPriority();

}
