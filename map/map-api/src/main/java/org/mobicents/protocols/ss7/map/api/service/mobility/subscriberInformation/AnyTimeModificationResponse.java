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

package org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation;

import org.mobicents.protocols.ss7.map.api.primitives.AddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.mobility.MobilityMessage;

/**
 *
 AnyTimeModificationRes ::= SEQUENCE { ss-InfoFor-CSE [0] Ext-SS-InfoFor-CSE OPTIONAL, camel-SubscriptionInfo [1]
 * CAMEL-SubscriptionInfo OPTIONAL, extensionContainer [2] ExtensionContainer OPTIONAL, ..., odb-Info [3] ODB-Info OPTIONAL,
 * cw-Data [4] CallWaitingData OPTIONAL, ch-Data [5] CallHoldData OPTIONAL, clip-Data [6] ClipData OPTIONAL, clir-Data [7]
 * ClirData OPTIONAL, ect-data [8] EctData OPTIONAL, serviceCentreAddress [9] AddressString OPTIONAL }
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface AnyTimeModificationResponse extends MobilityMessage {

    ExtSSInfoForCSE getSsInfoForCSE();

    CAMELSubscriptionInfo getCamelSubscriptionInfo();

    MAPExtensionContainer getExtensionContainer();

    ODBInfo getOdbInfo();

    CallWaitingData getCwData();

    CallHoldData getChData();

    ClipData getClipData();

    ClirData getClirData();

    EctData getEctData();

    AddressString getServiceCentreAddress();

}
