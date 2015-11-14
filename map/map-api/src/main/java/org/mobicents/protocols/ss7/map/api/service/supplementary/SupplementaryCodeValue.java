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

package org.mobicents.protocols.ss7.map.api.service.supplementary;

/**
 *
 * @author sergey vetyutnev
 *
 */
public enum SupplementaryCodeValue {

    /**
     * all SS
     */
    allSS(0),

    /**
     * all line identification SS
     */
    allLineIdentificationSS(0x10),
    /**
     * calling line identification presentation
     */
    clip(0x11),
    /**
     * calling line identification restriction
     */
    clir(0x12),
    /**
     * connected line identification presentation
     */
    colp(0x13),
    /**
     * connected line identification restriction
     */
    colr(0x14),
    /**
     * malicious call identification
     */
    mci(0x15),
    /**
     * all name identification SS
     */
    allNameIdentificationSS(0x18),
    /**
     * calling name presentation
     */
    cnap(0x19),

    /**
     * all forwarding SS
     */
    allForwardingSS(0x20),
    /**
     * call forwarding unconditional
     */
    cfu(0x21),
    /**
     * all conditional forwarding SS
     */
    allCondForwardingSS(0x28),
    /**
     * call forwarding busy
     */
    cfb(0x29),
    /**
     * call forwarding on no reply
     */
    cfnry(0x2a),
    /**
     * call forwarding on mobile subscriber not reachable
     */
    cfnrc(0x2b),
    /**
     * call deflection
     */
    cd(0x24),

    /**
     * all call offering SS includes also all forwarding SS
     */
    allCallOfferingSS(0x30),
    /**
     * explicit call transfer
     */
    ect(0x31),
    /**
     * mobile access hunting
     */
    mah(0x32),
    /**
     * all Call completion SS
     */
    allCallCompletionSS(0x40),
    /**
     * call waiting
     */
    cw(0x41),
    /**
     * call hold
     */
    hold(0x42),
    /**
     * completion of call to busy subscribers, originating side
     */
    ccbs_A(0x43),
    /**
     * completion of call to busy subscribers, destination side
     */
    ccbs_B(0x44),
    /**
     * multicall
     */
    mc(0x45),
    /**
     * all multiparty SS
     */
    allMultiPartySS(0x50),
    /**
     * multiparty
     */
    multiPTY(0x51),

    /**
     * all community of interest SS
     */
    allCommunityOfInterestSS(0x60),
    /**
     * closed user group
     */
    cug(0x61),

    /**
     * all charging SS
     */
    allChargingSS(0x70),
    /**
     * advice of charge information
     */
    aoci(0x71),
    /**
     * advice of charge charging
     */
    aocc(0x72),

    /**
     * all additional information transfer SS
     */
    allAdditionalInfoTransferSS(0x80),
    /**
     * UUS1 user-to-user signalling
     */
    uus1(0x81),
    /**
     * UUS2 user-to-user signalling
     */
    uus2(0x82),
    /**
     * UUS3 user-to-user signalling
     */
    uus3(0x83),

    /**
     * all Callrestriction SS
     */
    allCallRestrictionSS(0x90), barringOfOutgoingCalls(0x91),
    /**
     * barring of all outgoing calls
     */
    baoc(0x92),
    /**
     * barring of outgoing international calls
     */
    boic(0x93),
    /**
     * barring of outgoing international calls except those directed to the home
     * PLMN
     */
    boicExHC(0x94), barringOfIncomingCalls(0x99),
    /**
     * barring of all incoming calls
     */
    baic(0x9a),
    /**
     * barring of incoming calls when roaming outside home PLMN Country
     */
    bicRoam(0x9b), allPLMN_specificSS(0xf0),

    /**
     * all call priority SS
     */
    allCallPrioritySS(0xa0),
    /**
     * enhanced Multilevel Precedence Pre-emption (EMLPP) service
     */
    emlpp(0xa1),
    /**
     * all LCS Privacy Exception Classes
     */

    allLCSPrivacyException(0xb0),
    /**
     * allow location by any LCS client
     */
    universal(0xb1),
    /**
     * allow location by any value added LCS client to which a call is
     * established from the target MS
     */
    callrelated(0xb2),
    /**
     * allow location by designated external value added LCS clients
     */
    callunrelated(0xb3),
    /**
     * allow location by designated PLMN operator LCS clients
     */
    plmnoperator(0xb4),
    /**
     * allow location by LCS clients of a designated LCS service type
     */
    serviceType(0xb5),

    /**
     * all Mobile Originating Location Request Classes
     */
    allMOLR_SS(0xC0),
    /**
     * allow an MS to request its own location
     */
    basicSelfLocation(0xC1),
    /**
     * allow an MS to perform self location without interaction with the PLMN
     * for a predetermined period of time
     */
    autonomousSelfLocation(0xC2),
    /**
     * allow an MS to request transfer of its location to another LCS client
     */
    transferToThirdParty(0xc3),

    /**
     * plmn-specificSS-1
     */
    plmn_specificSS_1(0xf1),
    /**
     * plmn-specificSS-2
     */
    plmn_specificSS_2(0xf2),
    /**
     * plmn-specificSS-3
     */
    plmn_specificSS_3(0xf3),
    /**
     * plmn-specificSS-4
     */
    plmn_specificSS_4(0xf4),
    /**
     * plmn-specificSS-5
     */
    plmn_specificSS_5(0xf5),
    /**
     * plmn-specificSS-6
     */
    plmn_specificSS_6(0xf6),
    /**
     * plmn-specificSS-7
     */
    plmn_specificSS_7(0xf7),
    /**
     * plmn-specificSS-8
     */
    plmn_specificSS_8(0xf8),
    /**
     * plmn-specificSS-9
     */
    plmn_specificSS_9(0xf9),
    /**
     * plmn-specificSS-a
     */
    plmn_specificSS_a(0xfa),
    /**
     * plmn-specificSS-b
     */
    plmn_specificSS_b(0xfb),
    /**
     * plmn-specificSS-c
     */
    plmn_specificSS_c(0xfc),
    /**
     * plmn-specificSS-d
     */
    plmn_specificSS_d(0xfd),
    /**
     * plmn-specificSS-e
     */
    plmn_specificSS_e(0xfe),
    /**
     * plmn-specificSS-f
     */
    plmn_specificSS_f(0xff);

//    allSpeechTransmissionServices(0x10), telephony(0x11), emergencyCalls(0x12),
//    allShortMessageServices(0x20), shortMessageMT_PP(0x21), shortMessageMO_PP(0x22), cellBroadcast(0x23),
//    allFacsimileTransmissionServices(0x60), facsimileGroup3AndAlterSpeech(0x61), automaticFacsimileGroup3(0x62),
//    allVoiceGroupCallServices(0xC0), voiceGroupCall(0xC1), voiceBroadcastCall(0xC2),
//    AsynchronousGeneralBearerService(0x70),
//    SynchronousGeneralBearerService(0x80),
//    GPRS(0xD0);

    private int code;

    private SupplementaryCodeValue(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public static SupplementaryCodeValue getInstance(int code) {
        switch (code) {
            case 0x00:
                return SupplementaryCodeValue.allSS;

            case 0x10:
                return SupplementaryCodeValue.allLineIdentificationSS;
            case 0x11:
                return SupplementaryCodeValue.clip;
            case 0x12:
                return SupplementaryCodeValue.clir;
            case 0x13:
                return SupplementaryCodeValue.colp;
            case 0x14:
                return SupplementaryCodeValue.colr;
            case 0x15:
                return SupplementaryCodeValue.mci;
            case 0x18:
                return SupplementaryCodeValue.allNameIdentificationSS;
            case 0x19:
                return SupplementaryCodeValue.cnap;

            case 0x20:
                return SupplementaryCodeValue.allForwardingSS;
            case 0x21:
                return SupplementaryCodeValue.cfu;
            case 0x28:
                return SupplementaryCodeValue.allCondForwardingSS;
           case 0x29:
                return SupplementaryCodeValue.cfb;
            case 0x2a:
                return SupplementaryCodeValue.cfnry;
            case 0x2b:
                return SupplementaryCodeValue.cfnrc;
            case 0x24:
                return SupplementaryCodeValue.cd;

            case 0x30:
                return SupplementaryCodeValue.allCallOfferingSS;
            case 0x31:
                return SupplementaryCodeValue.ect;
            case 0x32:
                return SupplementaryCodeValue.mah;

            case 0x40:
                return SupplementaryCodeValue.allCallCompletionSS;
            case 0x41:
                return SupplementaryCodeValue.cw;
            case 0x42:
                return SupplementaryCodeValue.hold;
            case 0x43:
                return SupplementaryCodeValue.ccbs_A;
            case 0x44:
                return SupplementaryCodeValue.ccbs_B;
            case 0x45:
                return SupplementaryCodeValue.mc;

            case 0x50:
                return SupplementaryCodeValue.allMultiPartySS;
            case 0x51:
                return SupplementaryCodeValue.multiPTY;

            case 0x60:
                return SupplementaryCodeValue.allCommunityOfInterestSS;
            case 0x61:
                return SupplementaryCodeValue.cug;

            case 0x70:
                return SupplementaryCodeValue.allChargingSS;
            case 0x71:
                return SupplementaryCodeValue.aoci;
            case 0x72:
                return SupplementaryCodeValue.aocc;

            case 0x80:
                return SupplementaryCodeValue.allAdditionalInfoTransferSS;
            case 0x81:
                return SupplementaryCodeValue.uus1;
            case 0x82:
                return SupplementaryCodeValue.uus2;
            case 0x83:
                return SupplementaryCodeValue.uus3;

            case 0x90:
                return SupplementaryCodeValue.allCallRestrictionSS;
            case 0x91:
                return SupplementaryCodeValue.barringOfOutgoingCalls;
            case 0x92:
                return SupplementaryCodeValue.baoc;
            case 0x93:
                return SupplementaryCodeValue.boic;
            case 0x94:
                return SupplementaryCodeValue.boicExHC;
            case 0x99:
                return SupplementaryCodeValue.barringOfIncomingCalls;
            case 0x9a:
                return SupplementaryCodeValue.baic;
            case 0x9b:
                return SupplementaryCodeValue.bicRoam;
            case 0xf0:
                return SupplementaryCodeValue.allPLMN_specificSS;

            case 0xa0:
                return SupplementaryCodeValue.allCallPrioritySS;
            case 0xa1:
                return SupplementaryCodeValue.emlpp;

            case 0xb0:
                return SupplementaryCodeValue.allLCSPrivacyException;
            case 0xb1:
                return SupplementaryCodeValue.universal;
            case 0xb2:
                return SupplementaryCodeValue.callrelated;
            case 0xb3:
                return SupplementaryCodeValue.callunrelated;
            case 0xb4:
                return SupplementaryCodeValue.plmnoperator;
            case 0xb5:
                return SupplementaryCodeValue.serviceType;

            case 0xC0:
                return SupplementaryCodeValue.allMOLR_SS;
            case 0xC1:
                return SupplementaryCodeValue.basicSelfLocation;
            case 0xC2:
                return SupplementaryCodeValue.autonomousSelfLocation;
            case 0xC3:
                return SupplementaryCodeValue.transferToThirdParty;

            case 0xf1:
                return SupplementaryCodeValue.plmn_specificSS_1;
            case 0xf2:
                return SupplementaryCodeValue.plmn_specificSS_2;
            case 0xf3:
                return SupplementaryCodeValue.plmn_specificSS_3;
            case 0xf4:
                return SupplementaryCodeValue.plmn_specificSS_4;
            case 0xf5:
                return SupplementaryCodeValue.plmn_specificSS_5;
            case 0xf6:
                return SupplementaryCodeValue.plmn_specificSS_6;
            case 0xf7:
                return SupplementaryCodeValue.plmn_specificSS_7;
            case 0xf8:
                return SupplementaryCodeValue.plmn_specificSS_8;
            case 0xf9:
                return SupplementaryCodeValue.plmn_specificSS_9;
            case 0xfa:
                return SupplementaryCodeValue.plmn_specificSS_a;
            case 0xfb:
                return SupplementaryCodeValue.plmn_specificSS_b;
            case 0xfc:
                return SupplementaryCodeValue.plmn_specificSS_c;
            case 0xfd:
                return SupplementaryCodeValue.plmn_specificSS_d;
            case 0xfe:
                return SupplementaryCodeValue.plmn_specificSS_e;
            case 0xff:
                return SupplementaryCodeValue.plmn_specificSS_f;

            default:
                return null;
        }
    }

}