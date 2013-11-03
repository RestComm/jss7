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
