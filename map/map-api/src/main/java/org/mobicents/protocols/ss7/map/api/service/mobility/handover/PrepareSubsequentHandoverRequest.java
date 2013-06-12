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

package org.mobicents.protocols.ss7.map.api.service.mobility.handover;

import org.mobicents.protocols.ss7.map.api.primitives.AccessNetworkSignalInfo;
import org.mobicents.protocols.ss7.map.api.primitives.ExternalSignalInfo;
import org.mobicents.protocols.ss7.map.api.primitives.GlobalCellId;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.mobility.MobilityMessage;

/**
 *
 MAP V2-3:
 *
 * MAP V3: prepareSubsequentHandover OPERATION ::= { --Timer m ARGUMENT PrepareSubsequentHO-Arg RESULT PrepareSubsequentHO-Res
 * ERRORS { unexpectedDataValue | dataMissing | unknownMSC | subsequentHandoverFailure} CODE local:69 }
 *
 * MAP V2: PrepareSubsequentHandover ::= OPERATION --Timer m ARGUMENT prepareSubsequentHO-Arg PrepareSubsequentHO-Arg RESULT
 * bss-APDU ExternalSignalInfo 191 ERRORS { UnexpectedDataValue, DataMissing, UnknownMSC, SubsequentHandoverFailure}
 *
 * MAP V3: PrepareSubsequentHO-Arg ::= [3] SEQUENCE { targetCellId [0] GlobalCellId OPTIONAL, targetMSC-Number [1]
 * ISDN-AddressString, targetRNCId [2] RNCId OPTIONAL, an-APDU [3] AccessNetworkSignalInfo OPTIONAL, selectedRab-Id [4] RAB-Id
 * OPTIONAL, extensionContainer [5] ExtensionContainer OPTIONAL, ..., geran-classmark [6] GERAN-Classmark OPTIONAL,
 * rab-ConfigurationIndicator [7] NULL OPTIONAL }
 *
 * MAP V2: PrepareSubsequentHO-Arg ::= SEQUENCE { targetCellId GlobalCellId, targetMSC-Number ISDN-AddressString, bss-APDU
 * ExternalSignalInfo, ...}
 *
 * RAB-Id ::= INTEGER (1..255)
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface PrepareSubsequentHandoverRequest extends MobilityMessage {

    GlobalCellId getTargetCellId();

    ISDNAddressString getTargetMSCNumber();

    RNCId getTargetRNCId();

    AccessNetworkSignalInfo getAnAPDU();

    Integer getSelectedRabId();

    MAPExtensionContainer getExtensionContainer();

    GERANClassmark getGERANClassmark();

    boolean getRabConfigurationIndicator();

    // MAP V2
    ExternalSignalInfo getBssAPDU();

}
