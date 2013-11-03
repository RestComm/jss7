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

import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.LAIFixedLength;
import org.mobicents.protocols.ss7.map.api.primitives.LMSI;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.primitives.TMSI;
import org.mobicents.protocols.ss7.map.api.service.mobility.MobilityMessage;

/**
 *
 MAP V2-3:
 *
 * MAP V3: sendIdentification OPERATION ::= { --Timer s ARGUMENT SendIdentificationArg RESULT SendIdentificationRes ERRORS {
 * dataMissing | unidentifiedSubscriber} CODE local:55 }
 *
 * MAP V2: SendIdentification::= OPERATION --Timer s ARGUMENT tmsi TMSI RESULT sendIdentificationRes SendIdentificationRes
 * ERRORS { DataMissing, UnidentifiedSubscriber}
 *
 *
 * MAP V3: SendIdentificationArg ::= SEQUENCE { tmsi TMSI, numberOfRequestedVectors NumberOfRequestedVectors OPTIONAL, -- within
 * a dialogue numberOfRequestedVectors shall be present in -- the first service request and shall not be present in subsequent
 * service requests. -- If received in a subsequent service request it shall be discarded. segmentationProhibited NULL OPTIONAL,
 * extensionContainer ExtensionContainer OPTIONAL, ..., msc-Number ISDN-AddressString OPTIONAL, previous-LAI [0] LAIFixedLength
 * OPTIONAL, hopCounter [1] HopCounter OPTIONAL, mtRoamingForwardingSupported [2] NULL OPTIONAL, newVLR-Number [3]
 * ISDN-AddressString OPTIONAL, new-lmsi [4] LMSI OPTIONAL }
 *
 * NumberOfRequestedVectors ::= INTEGER (1..5) HopCounter ::= INTEGER (0..3)
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface SendIdentificationRequest extends MobilityMessage {

    TMSI getTmsi();

    Integer getNumberOfRequestedVectors();

    boolean getSegmentationProhibited();

    MAPExtensionContainer getExtensionContainer();

    ISDNAddressString getMscNumber();

    LAIFixedLength getPreviousLAI();

    Integer getHopCounter();

    boolean getMtRoamingForwardingSupported();

    ISDNAddressString getNewVLRNumber();

    LMSI getNewLmsi();

}
