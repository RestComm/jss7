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

import org.mobicents.protocols.ss7.map.api.primitives.GSNAddress;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.mobility.MobilityMessage;

/**
 *
 MAP V3: updateGprsLocation OPERATION ::= { --Timer m ARGUMENT UpdateGprsLocationArg RESULT UpdateGprsLocationRes ERRORS {
 * systemFailure | unexpectedDataValue | unknownSubscriber | roamingNotAllowed} CODE local:23 }
 *
 *
 * UpdateGprsLocationArg ::= SEQUENCE { imsi IMSI, sgsn-Number ISDN-AddressString, sgsn-Address GSN-Address, extensionContainer
 * ExtensionContainer OPTIONAL, ... , sgsn-Capability [0] SGSN-Capability OPTIONAL, informPreviousNetworkEntity [1] NULL
 * OPTIONAL, ps-LCS-NotSupportedByUE [2] NULL OPTIONAL, v-gmlc-Address [3] GSN-Address OPTIONAL, add-info [4] ADD-Info OPTIONAL,
 * eps-info [5] EPS-Info OPTIONAL, servingNodeTypeIndicator [6] NULL OPTIONAL, skipSubscriberDataUpdate [7] NULL OPTIONAL,
 * usedRAT-Type [8] Used-RAT-Type OPTIONAL, gprsSubscriptionDataNotNeeded [9] NULL OPTIONAL, nodeTypeIndicator [10] NULL
 * OPTIONAL, areaRestricted [11] NULL OPTIONAL, ue-reachableIndicator [12] NULL OPTIONAL, epsSubscriptionDataNotNeeded [13] NULL
 * OPTIONAL, ue-srvcc-Capability [14] UE-SRVCC-Capability OPTIONAL }
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface UpdateGprsLocationRequest extends MobilityMessage {

    IMSI getImsi();

    ISDNAddressString getSgsnNumber();

    GSNAddress getSgsnAddress();

    MAPExtensionContainer getExtensionContainer();

    SGSNCapability getSGSNCapability();

    boolean getInformPreviousNetworkEntity();

    boolean getPsLCSNotSupportedByUE();

    GSNAddress getVGmlcAddress();

    ADDInfo getADDInfo();

    EPSInfo getEPSInfo();

    boolean getServingNodeTypeIndicator();

    boolean getSkipSubscriberDataUpdate();

    UsedRATType getUsedRATType();

    boolean getGprsSubscriptionDataNotNeeded();

    boolean getNodeTypeIndicator();

    boolean getAreaRestricted();

    boolean getUeReachableIndicator();

    boolean getEpsSubscriptionDataNotNeeded();

    UESRVCCCapability getUESRVCCCapability();

}
