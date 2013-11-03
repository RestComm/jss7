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

package org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement;

import java.io.Serializable;

import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.OfferedCamel4CSIs;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.SupportedCamelPhases;

/**
 *
 * VLR-Capability ::= SEQUENCE{ supportedCamelPhases [0] SupportedCamelPhases OPTIONAL, extensionContainer ExtensionContainer
 * OPTIONAL, ... , solsaSupportIndicator [2] NULL OPTIONAL, istSupportIndicator [1] IST-SupportIndicator OPTIONAL,
 * superChargerSupportedInServingNetworkEntity [3] SuperChargerInfo OPTIONAL, longFTN-Supported [4] NULL OPTIONAL,
 * supportedLCS-CapabilitySets [5] SupportedLCS-CapabilitySets OPTIONAL, offeredCamel4CSIs [6] OfferedCamel4CSIs OPTIONAL,
 * supportedRAT-TypesIndicator [7] SupportedRAT-Types OPTIONAL, longGroupID-Supported [8] NULL OPTIONAL,
 * mtRoamingForwardingSupported [9] NULL OPTIONAL }
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface VLRCapability extends Serializable {

    SupportedCamelPhases getSupportedCamelPhases();

    MAPExtensionContainer getExtensionContainer();

    boolean getSolsaSupportIndicator();

    ISTSupportIndicator getIstSupportIndicator();

    SuperChargerInfo getSuperChargerSupportedInServingNetworkEntity();

    boolean getLongFtnSupported();

    SupportedLCSCapabilitySets getSupportedLCSCapabilitySets();

    OfferedCamel4CSIs getOfferedCamel4CSIs();

    SupportedRATTypes getSupportedRATTypesIndicator();

    boolean getLongGroupIDSupported();

    boolean getMtRoamingForwardingSupported();

}
