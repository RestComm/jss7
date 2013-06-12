/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
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

package org.mobicents.protocols.ss7.map.api.service.mobility.locationManagement;

import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.OfferedCamel4CSIs;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.SupportedCamelPhases;

/**
 *
 SGSN-Capability ::= SEQUENCE{ solsaSupportIndicator NULL OPTIONAL, extensionContainer [1] ExtensionContainer OPTIONAL, ... ,
 * superChargerSupportedInServingNetworkEntity [2] SuperChargerInfo OPTIONAL , gprsEnhancementsSupportIndicator [3] NULL
 * OPTIONAL, supportedCamelPhases [4] SupportedCamelPhases OPTIONAL, supportedLCS-CapabilitySets [5] SupportedLCS-CapabilitySets
 * OPTIONAL, offeredCamel4CSIs [6] OfferedCamel4CSIs OPTIONAL, smsCallBarringSupportIndicator [7] NULL OPTIONAL,
 * supportedRAT-TypesIndicator [8] SupportedRAT-Types OPTIONAL, supportedFeatures [9] SupportedFeatures OPTIONAL,
 * t-adsDataRetrieval [10] NULL OPTIONAL, homogeneousSupportOfIMSVoiceOverPSSessions [11] BOOLEAN OPTIONAL -- "true" indicates
 * homogeneous support, "false" indicates homogeneous non-support -- in the complete SGSN area }
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface SGSNCapability {

    boolean getSolsaSupportIndicator();

    MAPExtensionContainer getExtensionContainer();

    SuperChargerInfo getSuperChargerSupportedInServingNetworkEntity();

    boolean getGprsEnhancementsSupportIndicator();

    SupportedCamelPhases getSupportedCamelPhases();

    SupportedLCSCapabilitySets getSupportedLCSCapabilitySets();

    OfferedCamel4CSIs getOfferedCamel4CSIs();

    boolean getSmsCallBarringSupportIndicator();

    SupportedRATTypes getSupportedRATTypesIndicator();

    SupportedFeatures getSupportedFeatures();

    boolean getTAdsDataRetrieval();

    Boolean getHomogeneousSupportOfIMSVoiceOverPSSessions();

}
