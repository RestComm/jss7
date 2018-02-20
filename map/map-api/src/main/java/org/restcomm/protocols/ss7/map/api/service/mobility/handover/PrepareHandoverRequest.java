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

package org.restcomm.protocols.ss7.map.api.service.mobility.handover;

import java.util.ArrayList;

import org.restcomm.protocols.ss7.map.api.primitives.ASCICallReference;
import org.restcomm.protocols.ss7.map.api.primitives.AccessNetworkSignalInfo;
import org.restcomm.protocols.ss7.map.api.primitives.ExternalSignalInfo;
import org.restcomm.protocols.ss7.map.api.primitives.GlobalCellId;
import org.restcomm.protocols.ss7.map.api.primitives.IMSI;
import org.restcomm.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.restcomm.protocols.ss7.map.api.service.mobility.MobilityMessage;
import org.restcomm.protocols.ss7.map.api.service.mobility.imei.UESBIIu;
import org.restcomm.protocols.ss7.map.api.service.mobility.subscriberManagement.ZoneCode;
import org.restcomm.protocols.ss7.map.api.service.oam.TracePropagationList;

/**
 *
 MAP V2-3:
 *
 * MAP V3: prepareHandover OPERATION ::= { --Timer m ARGUMENT PrepareHO-Arg RESULT PrepareHO-Res ERRORS { systemFailure |
 * dataMissing | unexpectedDataValue | noHandoverNumberAvailable | targetCellOutsideGroupCallArea } CODE local:68 }
 *
 * MAP V2: PrepareHandover ::= OPERATION --Timer m ARGUMENT prepareHO-Arg PrepareHO-Arg RESULT prepareHO-Res PrepareHO-Res
 * ERRORS { SystemFailure, DataMissing, UnexpectedDataValue, NoHandoverNumberAvailable}
 *
 * MAP V3: PrepareHO-Arg ::= [3] SEQUENCE { targetCellId [0] GlobalCellId OPTIONAL, ho-NumberNotRequired NULL OPTIONAL,
 * targetRNCId [1] RNCId OPTIONAL, an-APDU [2] AccessNetworkSignalInfo OPTIONAL, multipleBearerRequested [3] NULL OPTIONAL, imsi
 * [4] IMSI OPTIONAL, integrityProtectionInfo [5] IntegrityProtectionInformation OPTIONAL, encryptionInfo [6]
 * EncryptionInformation OPTIONAL, radioResourceInformation [7] RadioResourceInformation OPTIONAL, allowedGSM-Algorithms [9]
 * AllowedGSM-Algorithms OPTIONAL, allowedUMTS-Algorithms [10] AllowedUMTS-Algorithms OPTIONAL, radioResourceList [11]
 * RadioResourceList OPTIONAL, extensionContainer [8] ExtensionContainer OPTIONAL, ... , rab-Id [12] RAB-Id OPTIONAL,
 * bssmap-ServiceHandover [13] BSSMAP-ServiceHandover OPTIONAL, ranap-ServiceHandover [14] RANAP-ServiceHandover OPTIONAL,
 * bssmap-ServiceHandoverList [15] BSSMAP-ServiceHandoverList OPTIONAL, asciCallReference [20] ASCI-CallReference OPTIONAL,
 * geran-classmark [16] GERAN-Classmark OPTIONAL, iuCurrentlyUsedCodec [17] Codec OPTIONAL, iuSupportedCodecsList [18]
 * SupportedCodecsList OPTIONAL, rab-ConfigurationIndicator [19] NULL OPTIONAL, uesbi-Iu [21] UESBI-Iu OPTIONAL, imeisv [22]
 * IMEI OPTIONAL, alternativeChannelType [23] RadioResourceInformation OPTIONAL, tracePropagationList [25] TracePropagationList
 * OPTIONAL, aoipSupportedCodecsListAnchor [26] AoIPCodecsList OPTIONAL, regionalSubscriptionData [27] ZoneCodeList OPTIONAL,
 * globalCallReference [28] LCLS-GlobalCallReference OPTIONAL, lcls-Negotiation [29] LCLS-Negotiation OPTIONAL }
 *
 * MAP V2: PrepareHO-Arg ::= SEQUENCE { targetCellId GlobalCellId OPTIONAL, ho-NumberNotRequired NULL OPTIONAL, bss-APDU
 * ExternalSignalInfo OPTIONAL, ...}
 *
 * RadioResourceList ::= SEQUENCE SIZE (1.. 7) OF RadioResource
 *
 * RAB-Id ::= INTEGER (1..255)
 *
 * BSSMAP-ServiceHandoverList ::= SEQUENCE SIZE (1.. 7) OF BSSMAP-ServiceHandoverInfo
 *
 * ZoneCodeList ::= SEQUENCE SIZE (1..10) OF ZoneCode
 *
 *
 *
 * @author sergey vetyutnev
 *
 */
public interface PrepareHandoverRequest extends MobilityMessage {

    GlobalCellId getTargetCellId();

    boolean getHoNumberNotRequired();

    RNCId getTargetRNCId();

    AccessNetworkSignalInfo geTanAPDU();

    boolean getMultipleBearerRequested();

    IMSI getImsi();

    IntegrityProtectionInformation getIntegrityProtectionInfo();

    EncryptionInformation getEncryptionInfo();

    RadioResourceInformation getRadioResourceInformation();

    AllowedGSMAlgorithms getAllowedGSMAlgorithms();

    AllowedUMTSAlgorithms getAllowedUMTSAlgorithms();

    ArrayList<RadioResource> getRadioResourceList();

    MAPExtensionContainer getExtensionContainer();

    Integer getRABId();

    BSSMAPServiceHandover getBSSMAPServiceHandover();

    RANAPServiceHandover getRANAPServiceHandover();

    ArrayList<BSSMAPServiceHandoverInfo> getBSSMAPServiceHandoverList();

    ASCICallReference getASCICallReference();

    GERANClassmark getGERANClassmark();

    Codec getIuCurrentlyUsedCodec();

    SupportedCodecsList getIuSupportedCodecsList();

    boolean getRabConfigurationIndicator();

    UESBIIu getUESBIIu();

    RadioResourceInformation getAlternativeChannelType();

    TracePropagationList getTracePropagationList();

    AoIPCodecsList getAoipSupportedCodecsListAnchor();

    ArrayList<ZoneCode> getRegionalSubscriptionData();

    LCLSGlobalCallReference getGlobalCallReference();

    LCLSNegotiation getLCLSNegotiation();

    // this parameter is for MAP V2 only
    ExternalSignalInfo getBssAPDU();

}
