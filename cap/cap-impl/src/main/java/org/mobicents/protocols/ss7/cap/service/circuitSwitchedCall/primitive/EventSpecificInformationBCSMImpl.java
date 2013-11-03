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

package org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive;

import java.io.IOException;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.cap.EsiBcsm.OAbandonSpecificInfoImpl;
import org.mobicents.protocols.ss7.cap.EsiBcsm.OAnswerSpecificInfoImpl;
import org.mobicents.protocols.ss7.cap.EsiBcsm.OCalledPartyBusySpecificInfoImpl;
import org.mobicents.protocols.ss7.cap.EsiBcsm.ODisconnectSpecificInfoImpl;
import org.mobicents.protocols.ss7.cap.EsiBcsm.ONoAnswerSpecificInfoImpl;
import org.mobicents.protocols.ss7.cap.EsiBcsm.RouteSelectFailureSpecificInfoImpl;
import org.mobicents.protocols.ss7.cap.EsiBcsm.TAnswerSpecificInfoImpl;
import org.mobicents.protocols.ss7.cap.EsiBcsm.TBusySpecificInfoImpl;
import org.mobicents.protocols.ss7.cap.EsiBcsm.TDisconnectSpecificInfoImpl;
import org.mobicents.protocols.ss7.cap.EsiBcsm.TNoAnswerSpecificInfoImpl;
import org.mobicents.protocols.ss7.cap.api.CAPException;
import org.mobicents.protocols.ss7.cap.api.CAPParsingComponentException;
import org.mobicents.protocols.ss7.cap.api.CAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.cap.api.EsiBcsm.CallAcceptedSpecificInfo;
import org.mobicents.protocols.ss7.cap.api.EsiBcsm.OAbandonSpecificInfo;
import org.mobicents.protocols.ss7.cap.api.EsiBcsm.OAnswerSpecificInfo;
import org.mobicents.protocols.ss7.cap.api.EsiBcsm.OCalledPartyBusySpecificInfo;
import org.mobicents.protocols.ss7.cap.api.EsiBcsm.OChangeOfPositionSpecificInfo;
import org.mobicents.protocols.ss7.cap.api.EsiBcsm.ODisconnectSpecificInfo;
import org.mobicents.protocols.ss7.cap.api.EsiBcsm.OMidCallSpecificInfo;
import org.mobicents.protocols.ss7.cap.api.EsiBcsm.ONoAnswerSpecificInfo;
import org.mobicents.protocols.ss7.cap.api.EsiBcsm.OTermSeizedSpecificInfo;
import org.mobicents.protocols.ss7.cap.api.EsiBcsm.RouteSelectFailureSpecificInfo;
import org.mobicents.protocols.ss7.cap.api.EsiBcsm.TAnswerSpecificInfo;
import org.mobicents.protocols.ss7.cap.api.EsiBcsm.TBusySpecificInfo;
import org.mobicents.protocols.ss7.cap.api.EsiBcsm.TChangeOfPositionSpecificInfo;
import org.mobicents.protocols.ss7.cap.api.EsiBcsm.TDisconnectSpecificInfo;
import org.mobicents.protocols.ss7.cap.api.EsiBcsm.TMidCallSpecificInfo;
import org.mobicents.protocols.ss7.cap.api.EsiBcsm.TNoAnswerSpecificInfo;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.EventSpecificInformationBCSM;
import org.mobicents.protocols.ss7.cap.primitives.CAPAsnPrimitive;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class EventSpecificInformationBCSMImpl implements EventSpecificInformationBCSM, CAPAsnPrimitive {

    private static final String ROUTE_SELECT_FAILURE_SPECIFIC_INFO = "routeSelectFailureSpecificInfo";
    private static final String O_CALLED_PARTY_BUSY_SPECIFIC_INFO = "oCalledPartyBusySpecificInfo";
    private static final String O_NO_ANSWER_SPECIFIC_INFO = "oNoAnswerSpecificInfo";
    private static final String O_ANSWER_SPECIFIC_INFO = "oAnswerSpecificInfo";
    private static final String O_MID_CALL_SPECIFIC_INFO = "oMidCallSpecificInfo";
    private static final String O_DISCONNECT_SPECIFIC_INFO = "oDisconnectSpecificInfo";
    private static final String T_BUSY_SPECIFIC_INFO = "tBusySpecificInfo";
    private static final String T_NO_ANSWER_SPECIFIC_INFO = "tNoAnswerSpecificInfo";
    private static final String T_ANSWER_SPECIFIC_INFO = "tAnswerSpecificInfo";
    private static final String T_MID_CALL_SPECIFIC_INFO = "tMidCallSpecificInfo";
    private static final String T_DISCONNECT_SPECIFIC_INFO = "tDisconnectSpecificInfo";
    private static final String O_TERM_SPECIFIC_INFO = "oTermSeizedSpecificInfo";
    private static final String CALL_ACCEPTED_SPECIFIC_INFO = "callAcceptedSpecificInfo";
    private static final String O_ABONDON_SPECIFIC_INFO = "oAbandonSpecificInfo";
    private static final String O_CHANGE_OF_POSITION_SPECIFIC_INFO = "oChangeOfPositionSpecificInfo";
    private static final String T_CHANGE_OF_POSITION_SPECIFIC_INFO = "tChangeOfPositionSpecificInfo";

    public static final int _ID_routeSelectFailureSpecificInfo = 2;
    public static final int _ID_oCalledPartyBusySpecificInfo = 3;
    public static final int _ID_oNoAnswerSpecificInfo = 4;
    public static final int _ID_oAnswerSpecificInfo = 5;
    public static final int _ID_oMidCallSpecificInfo = 6;
    public static final int _ID_oDisconnectSpecificInfo = 7;
    public static final int _ID_tBusySpecificInfo = 8;
    public static final int _ID_tNoAnswerSpecificInfo = 9;
    public static final int _ID_tAnswerSpecificInfo = 10;
    public static final int _ID_tMidCallSpecificInfo = 11;
    public static final int _ID_tDisconnectSpecificInfo = 12;
    public static final int _ID_oTermSeizedSpecificInfo = 13;
    public static final int _ID_callAcceptedSpecificInfo = 20;
    public static final int _ID_oAbandonSpecificInfo = 21;
    public static final int _ID_oChangeOfPositionSpecificInfo = 50;
    public static final int _ID_tChangeOfPositionSpecificInfo = 51;

    public static final String _PrimitiveName = "EventSpecificInformationBCSM";

    private RouteSelectFailureSpecificInfo routeSelectFailureSpecificInfo;
    private OCalledPartyBusySpecificInfo oCalledPartyBusySpecificInfo;
    private ONoAnswerSpecificInfo oNoAnswerSpecificInfo;
    private OAnswerSpecificInfo oAnswerSpecificInfo;
    private OMidCallSpecificInfo oMidCallSpecificInfo;
    private ODisconnectSpecificInfo oDisconnectSpecificInfo;
    private TBusySpecificInfo tBusySpecificInfo;
    private TNoAnswerSpecificInfo tNoAnswerSpecificInfo;
    private TAnswerSpecificInfo tAnswerSpecificInfo;
    private TMidCallSpecificInfo tMidCallSpecificInfo;
    private TDisconnectSpecificInfo tDisconnectSpecificInfo;
    private OTermSeizedSpecificInfo oTermSeizedSpecificInfo;
    private CallAcceptedSpecificInfo callAcceptedSpecificInfo;
    private OAbandonSpecificInfo oAbandonSpecificInfo;
    private OChangeOfPositionSpecificInfo oChangeOfPositionSpecificInfo;
    private TChangeOfPositionSpecificInfo tChangeOfPositionSpecificInfo;

    public EventSpecificInformationBCSMImpl() {
    }

    public EventSpecificInformationBCSMImpl(RouteSelectFailureSpecificInfo routeSelectFailureSpecificInfo) {
        this.routeSelectFailureSpecificInfo = routeSelectFailureSpecificInfo;
    }

    public EventSpecificInformationBCSMImpl(OCalledPartyBusySpecificInfo oCalledPartyBusySpecificInfo) {
        this.oCalledPartyBusySpecificInfo = oCalledPartyBusySpecificInfo;
    }

    public EventSpecificInformationBCSMImpl(ONoAnswerSpecificInfo oNoAnswerSpecificInfo) {
        this.oNoAnswerSpecificInfo = oNoAnswerSpecificInfo;
    }

    public EventSpecificInformationBCSMImpl(OAnswerSpecificInfo oAnswerSpecificInfo) {
        this.oAnswerSpecificInfo = oAnswerSpecificInfo;
    }

    public EventSpecificInformationBCSMImpl(OMidCallSpecificInfo oMidCallSpecificInfo) {
        this.oMidCallSpecificInfo = oMidCallSpecificInfo;
    }

    public EventSpecificInformationBCSMImpl(ODisconnectSpecificInfo oDisconnectSpecificInfo) {
        this.oDisconnectSpecificInfo = oDisconnectSpecificInfo;
    }

    public EventSpecificInformationBCSMImpl(TBusySpecificInfo tBusySpecificInfo) {
        this.tBusySpecificInfo = tBusySpecificInfo;
    }

    public EventSpecificInformationBCSMImpl(TNoAnswerSpecificInfo tNoAnswerSpecificInfo) {
        this.tNoAnswerSpecificInfo = tNoAnswerSpecificInfo;
    }

    public EventSpecificInformationBCSMImpl(TAnswerSpecificInfo tAnswerSpecificInfo) {
        this.tAnswerSpecificInfo = tAnswerSpecificInfo;
    }

    public EventSpecificInformationBCSMImpl(TMidCallSpecificInfo tMidCallSpecificInfo) {
        this.tMidCallSpecificInfo = tMidCallSpecificInfo;
    }

    public EventSpecificInformationBCSMImpl(TDisconnectSpecificInfo tDisconnectSpecificInfo) {
        this.tDisconnectSpecificInfo = tDisconnectSpecificInfo;
    }

    public EventSpecificInformationBCSMImpl(OTermSeizedSpecificInfo oTermSeizedSpecificInfo) {
        this.oTermSeizedSpecificInfo = oTermSeizedSpecificInfo;
    }

    public EventSpecificInformationBCSMImpl(CallAcceptedSpecificInfo callAcceptedSpecificInfo) {
        this.callAcceptedSpecificInfo = callAcceptedSpecificInfo;
    }

    public EventSpecificInformationBCSMImpl(OAbandonSpecificInfo oAbandonSpecificInfo) {
        this.oAbandonSpecificInfo = oAbandonSpecificInfo;
    }

    public EventSpecificInformationBCSMImpl(OChangeOfPositionSpecificInfo oChangeOfPositionSpecificInfo) {
        this.oChangeOfPositionSpecificInfo = oChangeOfPositionSpecificInfo;
    }

    public EventSpecificInformationBCSMImpl(TChangeOfPositionSpecificInfo tChangeOfPositionSpecificInfo) {
        this.tChangeOfPositionSpecificInfo = tChangeOfPositionSpecificInfo;
    }

    @Override
    public RouteSelectFailureSpecificInfo getRouteSelectFailureSpecificInfo() {
        return routeSelectFailureSpecificInfo;
    }

    @Override
    public OCalledPartyBusySpecificInfo getOCalledPartyBusySpecificInfo() {
        return oCalledPartyBusySpecificInfo;
    }

    @Override
    public ONoAnswerSpecificInfo getONoAnswerSpecificInfo() {
        return oNoAnswerSpecificInfo;
    }

    @Override
    public OAnswerSpecificInfo getOAnswerSpecificInfo() {
        return oAnswerSpecificInfo;
    }

    @Override
    public OMidCallSpecificInfo getOMidCallSpecificInfo() {
        return oMidCallSpecificInfo;
    }

    @Override
    public ODisconnectSpecificInfo getODisconnectSpecificInfo() {
        return oDisconnectSpecificInfo;
    }

    @Override
    public TBusySpecificInfo getTBusySpecificInfo() {
        return tBusySpecificInfo;
    }

    @Override
    public TNoAnswerSpecificInfo getTNoAnswerSpecificInfo() {
        return tNoAnswerSpecificInfo;
    }

    @Override
    public TAnswerSpecificInfo getTAnswerSpecificInfo() {
        return tAnswerSpecificInfo;
    }

    @Override
    public TMidCallSpecificInfo getTMidCallSpecificInfo() {
        return tMidCallSpecificInfo;
    }

    @Override
    public TDisconnectSpecificInfo getTDisconnectSpecificInfo() {
        return tDisconnectSpecificInfo;
    }

    @Override
    public OTermSeizedSpecificInfo getOTermSeizedSpecificInfo() {
        return oTermSeizedSpecificInfo;
    }

    @Override
    public CallAcceptedSpecificInfo getCallAcceptedSpecificInfo() {
        return callAcceptedSpecificInfo;
    }

    @Override
    public OAbandonSpecificInfo getOAbandonSpecificInfo() {
        return oAbandonSpecificInfo;
    }

    @Override
    public OChangeOfPositionSpecificInfo getOChangeOfPositionSpecificInfo() {
        return oChangeOfPositionSpecificInfo;
    }

    @Override
    public TChangeOfPositionSpecificInfo getTChangeOfPositionSpecificInfo() {
        return tChangeOfPositionSpecificInfo;
    }

    @Override
    public int getTag() throws CAPException {

        if (routeSelectFailureSpecificInfo != null) {
            return _ID_routeSelectFailureSpecificInfo;
        } else if (oCalledPartyBusySpecificInfo != null) {
            return _ID_oCalledPartyBusySpecificInfo;
        } else if (oNoAnswerSpecificInfo != null) {
            return _ID_oNoAnswerSpecificInfo;
        } else if (oAnswerSpecificInfo != null) {
            return _ID_oAnswerSpecificInfo;
        } else if (oMidCallSpecificInfo != null) {
            return _ID_oMidCallSpecificInfo;
        } else if (oDisconnectSpecificInfo != null) {
            return _ID_oDisconnectSpecificInfo;
        } else if (tBusySpecificInfo != null) {
            return _ID_tBusySpecificInfo;
        } else if (tNoAnswerSpecificInfo != null) {
            return _ID_tNoAnswerSpecificInfo;
        } else if (tAnswerSpecificInfo != null) {
            return _ID_tAnswerSpecificInfo;
        } else if (tMidCallSpecificInfo != null) {
            return _ID_tMidCallSpecificInfo;
        } else if (tDisconnectSpecificInfo != null) {
            return _ID_tDisconnectSpecificInfo;
        } else if (oTermSeizedSpecificInfo != null) {
            return _ID_oTermSeizedSpecificInfo;
        } else if (callAcceptedSpecificInfo != null) {
            return _ID_callAcceptedSpecificInfo;
        } else if (oAbandonSpecificInfo != null) {
            return _ID_oAbandonSpecificInfo;
        } else if (oChangeOfPositionSpecificInfo != null) {
            return _ID_oChangeOfPositionSpecificInfo;
        } else if (tChangeOfPositionSpecificInfo != null) {
            return _ID_tChangeOfPositionSpecificInfo;
        }

        throw new CAPException("Error while encoding " + _PrimitiveName + ": no choice is specified");
    }

    @Override
    public int getTagClass() {
        return Tag.CLASS_CONTEXT_SPECIFIC;
    }

    @Override
    public boolean getIsPrimitive() {
        return false;
    }

    @Override
    public void decodeAll(AsnInputStream ansIS) throws CAPParsingComponentException {

        try {
            int length = ansIS.readLength();
            this._decode(ansIS, length);
        } catch (IOException e) {
            throw new CAPParsingComponentException("IOException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
                    CAPParsingComponentExceptionReason.MistypedParameter);
        } catch (AsnException e) {
            throw new CAPParsingComponentException("AsnException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
                    CAPParsingComponentExceptionReason.MistypedParameter);
        } catch (MAPParsingComponentException e) {
            throw new CAPParsingComponentException("MAPParsingComponentException when decoding " + _PrimitiveName + ": "
                    + e.getMessage(), e, CAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    @Override
    public void decodeData(AsnInputStream ansIS, int length) throws CAPParsingComponentException {

        try {
            this._decode(ansIS, length);
        } catch (IOException e) {
            throw new CAPParsingComponentException("IOException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
                    CAPParsingComponentExceptionReason.MistypedParameter);
        } catch (AsnException e) {
            throw new CAPParsingComponentException("AsnException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
                    CAPParsingComponentExceptionReason.MistypedParameter);
        } catch (MAPParsingComponentException e) {
            throw new CAPParsingComponentException("MAPParsingComponentException when decoding " + _PrimitiveName + ": "
                    + e.getMessage(), e, CAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    private void _decode(AsnInputStream ais, int length) throws CAPParsingComponentException, MAPParsingComponentException,
            IOException, AsnException {

        this.routeSelectFailureSpecificInfo = null;
        this.oCalledPartyBusySpecificInfo = null;
        this.oNoAnswerSpecificInfo = null;
        this.oAnswerSpecificInfo = null;
        this.oMidCallSpecificInfo = null;
        this.oDisconnectSpecificInfo = null;
        this.tBusySpecificInfo = null;
        this.tNoAnswerSpecificInfo = null;
        this.tAnswerSpecificInfo = null;
        this.tMidCallSpecificInfo = null;
        this.tDisconnectSpecificInfo = null;
        this.oTermSeizedSpecificInfo = null;
        this.callAcceptedSpecificInfo = null;
        this.oAbandonSpecificInfo = null;
        this.oChangeOfPositionSpecificInfo = null;
        this.tChangeOfPositionSpecificInfo = null;

        int tag = ais.getTag();

        if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
            switch (tag) {
                case _ID_routeSelectFailureSpecificInfo:
                    this.routeSelectFailureSpecificInfo = new RouteSelectFailureSpecificInfoImpl();
                    ((RouteSelectFailureSpecificInfoImpl) this.routeSelectFailureSpecificInfo).decodeData(ais, length);
                    break;
                case _ID_oCalledPartyBusySpecificInfo:
                    this.oCalledPartyBusySpecificInfo = new OCalledPartyBusySpecificInfoImpl();
                    ((OCalledPartyBusySpecificInfoImpl) this.oCalledPartyBusySpecificInfo).decodeData(ais, length);
                    break;
                case _ID_oNoAnswerSpecificInfo:
                    this.oNoAnswerSpecificInfo = new ONoAnswerSpecificInfoImpl();
                    ((ONoAnswerSpecificInfoImpl) this.oNoAnswerSpecificInfo).decodeData(ais, length);
                    break;
                case _ID_oAnswerSpecificInfo:
                    this.oAnswerSpecificInfo = new OAnswerSpecificInfoImpl();
                    ((OAnswerSpecificInfoImpl) this.oAnswerSpecificInfo).decodeData(ais, length);
                    break;
                case _ID_oMidCallSpecificInfo:
                    // TODO: implement it
                    ais.advanceElementData(length);
                    break;
                case _ID_oDisconnectSpecificInfo:
                    this.oDisconnectSpecificInfo = new ODisconnectSpecificInfoImpl();
                    ((ODisconnectSpecificInfoImpl) this.oDisconnectSpecificInfo).decodeData(ais, length);
                    break;
                case _ID_tBusySpecificInfo:
                    this.tBusySpecificInfo = new TBusySpecificInfoImpl();
                    ((TBusySpecificInfoImpl) this.tBusySpecificInfo).decodeData(ais, length);
                    break;
                case _ID_tNoAnswerSpecificInfo:
                    this.tNoAnswerSpecificInfo = new TNoAnswerSpecificInfoImpl();
                    ((TNoAnswerSpecificInfoImpl) this.tNoAnswerSpecificInfo).decodeData(ais, length);
                    break;
                case _ID_tAnswerSpecificInfo:
                    this.tAnswerSpecificInfo = new TAnswerSpecificInfoImpl();
                    ((TAnswerSpecificInfoImpl) this.tAnswerSpecificInfo).decodeData(ais, length);
                    break;
                case _ID_tMidCallSpecificInfo:
                    // TODO: implement it
                    ais.advanceElementData(length);
                    break;
                case _ID_tDisconnectSpecificInfo:
                    this.tDisconnectSpecificInfo = new TDisconnectSpecificInfoImpl();
                    ((TDisconnectSpecificInfoImpl) this.tDisconnectSpecificInfo).decodeData(ais, length);
                    break;
                case _ID_oTermSeizedSpecificInfo:
                    // TODO: implement it
                    ais.advanceElementData(length);
                    break;
                case _ID_callAcceptedSpecificInfo:
                    // TODO: implement it
                    ais.advanceElementData(length);
                    break;
                case _ID_oAbandonSpecificInfo:
                    this.oAbandonSpecificInfo = new OAbandonSpecificInfoImpl();
                    ((OAbandonSpecificInfoImpl) this.oAbandonSpecificInfo).decodeData(ais, length);
                    break;
                case _ID_oChangeOfPositionSpecificInfo:
                    // TODO: implement it
                    ais.advanceElementData(length);
                    break;
                case _ID_tChangeOfPositionSpecificInfo:
                    // TODO: implement it
                    ais.advanceElementData(length);
                    break;

                default:
                    throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName + ": bad choice tag",
                            CAPParsingComponentExceptionReason.MistypedParameter);
            }
        } else {
            throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName + ": bad choice tagClass",
                    CAPParsingComponentExceptionReason.MistypedParameter);
        }
    }

    @Override
    public void encodeAll(AsnOutputStream asnOs) throws CAPException {
        this.encodeAll(asnOs, this.getTagClass(), this.getTag());
    }

    @Override
    public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag) throws CAPException {

        try {
            asnOs.writeTag(tagClass, this.getIsPrimitive(), tag);
            int pos = asnOs.StartContentDefiniteLength();
            this.encodeData(asnOs);
            asnOs.FinalizeContent(pos);
        } catch (AsnException e) {
            throw new CAPException("AsnException when encoding " + _PrimitiveName + ": " + e.getMessage(), e);
        }
    }

    @Override
    public void encodeData(AsnOutputStream asnOs) throws CAPException {

        if (routeSelectFailureSpecificInfo != null) {
            ((RouteSelectFailureSpecificInfoImpl) routeSelectFailureSpecificInfo).encodeData(asnOs);
            return;
        } else if (oCalledPartyBusySpecificInfo != null) {
            ((OCalledPartyBusySpecificInfoImpl) oCalledPartyBusySpecificInfo).encodeData(asnOs);
            return;
        } else if (oNoAnswerSpecificInfo != null) {
            ((ONoAnswerSpecificInfoImpl) oNoAnswerSpecificInfo).encodeData(asnOs);
            return;
        } else if (oAnswerSpecificInfo != null) {
            ((OAnswerSpecificInfoImpl) oAnswerSpecificInfo).encodeData(asnOs);
            return;
        } else if (oMidCallSpecificInfo != null) {
            // TODO: implement it
        } else if (oDisconnectSpecificInfo != null) {
            ((ODisconnectSpecificInfoImpl) oDisconnectSpecificInfo).encodeData(asnOs);
            return;
        } else if (tBusySpecificInfo != null) {
            ((TBusySpecificInfoImpl) tBusySpecificInfo).encodeData(asnOs);
            return;
        } else if (tNoAnswerSpecificInfo != null) {
            ((TNoAnswerSpecificInfoImpl) tNoAnswerSpecificInfo).encodeData(asnOs);
            return;
        } else if (tAnswerSpecificInfo != null) {
            ((TAnswerSpecificInfoImpl) tAnswerSpecificInfo).encodeData(asnOs);
            return;
        } else if (tMidCallSpecificInfo != null) {
            // TODO: implement it
        } else if (tDisconnectSpecificInfo != null) {
            ((TDisconnectSpecificInfoImpl) tDisconnectSpecificInfo).encodeData(asnOs);
            return;
        } else if (oTermSeizedSpecificInfo != null) {
            // TODO: implement it
        } else if (callAcceptedSpecificInfo != null) {
            // TODO: implement it
        } else if (oAbandonSpecificInfo != null) {
            ((OAbandonSpecificInfoImpl) oAbandonSpecificInfo).encodeData(asnOs);
            return;
        } else if (oChangeOfPositionSpecificInfo != null) {
            // TODO: implement it
        } else if (tChangeOfPositionSpecificInfo != null) {
            // TODO: implement it
        }

        throw new CAPException("Error while encoding " + _PrimitiveName + ": no choice is specified");
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append(_PrimitiveName);
        sb.append(" [");

        if (routeSelectFailureSpecificInfo != null) {
            sb.append("routeSelectFailureSpecificInfo=[");
            sb.append(routeSelectFailureSpecificInfo.toString());
            sb.append("]");
        } else if (oCalledPartyBusySpecificInfo != null) {
            sb.append("oCalledPartyBusySpecificInfo=[");
            sb.append(oCalledPartyBusySpecificInfo.toString());
            sb.append("]");
        } else if (oNoAnswerSpecificInfo != null) {
            sb.append("oNoAnswerSpecificInfo=[");
            sb.append(oNoAnswerSpecificInfo.toString());
            sb.append("]");
        } else if (oAnswerSpecificInfo != null) {
            sb.append("oAnswerSpecificInfo=[");
            sb.append(oAnswerSpecificInfo.toString());
            sb.append("]");
        } else if (oMidCallSpecificInfo != null) {
            sb.append("oMidCallSpecificInfo=[");
            sb.append(oMidCallSpecificInfo.toString());
            sb.append("]");
        } else if (oDisconnectSpecificInfo != null) {
            sb.append("oDisconnectSpecificInfo=[");
            sb.append(oDisconnectSpecificInfo.toString());
            sb.append("]");
        } else if (tBusySpecificInfo != null) {
            sb.append("tBusySpecificInfo=[");
            sb.append(tBusySpecificInfo.toString());
            sb.append("]");
        } else if (tNoAnswerSpecificInfo != null) {
            sb.append("tNoAnswerSpecificInfo=[");
            sb.append(tNoAnswerSpecificInfo.toString());
            sb.append("]");
        } else if (tAnswerSpecificInfo != null) {
            sb.append("tAnswerSpecificInfo=[");
            sb.append(tAnswerSpecificInfo.toString());
            sb.append("]");
        } else if (tMidCallSpecificInfo != null) {
            sb.append("tMidCallSpecificInfo=[");
            sb.append(tMidCallSpecificInfo.toString());
            sb.append("]");
        } else if (tDisconnectSpecificInfo != null) {
            sb.append("tDisconnectSpecificInfo=[");
            sb.append(tDisconnectSpecificInfo.toString());
            sb.append("]");
        } else if (oTermSeizedSpecificInfo != null) {
            sb.append("oTermSeizedSpecificInfo=[");
            sb.append(oTermSeizedSpecificInfo.toString());
            sb.append("]");
        } else if (callAcceptedSpecificInfo != null) {
            sb.append("callAcceptedSpecificInfo=[");
            sb.append(callAcceptedSpecificInfo.toString());
            sb.append("]");
        } else if (oAbandonSpecificInfo != null) {
            sb.append("oAbandonSpecificInfo=[");
            sb.append(oAbandonSpecificInfo.toString());
            sb.append("]");
        } else if (oChangeOfPositionSpecificInfo != null) {
            sb.append("oChangeOfPositionSpecificInfo=[");
            sb.append(oChangeOfPositionSpecificInfo.toString());
            sb.append("]");
        } else if (tChangeOfPositionSpecificInfo != null) {
            sb.append("tChangeOfPositionSpecificInfo=[");
            sb.append(tChangeOfPositionSpecificInfo.toString());
            sb.append("]");
        }

        sb.append("]");

        return sb.toString();
    }

    protected static final XMLFormat<EventSpecificInformationBCSMImpl> EVENT_SPECIFIC_INFORMATION_BCSM_XML = new XMLFormat<EventSpecificInformationBCSMImpl>(
            EventSpecificInformationBCSMImpl.class) {

        @Override
        public void read(javolution.xml.XMLFormat.InputElement xml,
                EventSpecificInformationBCSMImpl eventSpecificInformationBCSM) throws XMLStreamException {
            eventSpecificInformationBCSM.routeSelectFailureSpecificInfo = xml.get(ROUTE_SELECT_FAILURE_SPECIFIC_INFO,
                    RouteSelectFailureSpecificInfoImpl.class);

            eventSpecificInformationBCSM.oCalledPartyBusySpecificInfo = xml.get(O_CALLED_PARTY_BUSY_SPECIFIC_INFO,
                    OCalledPartyBusySpecificInfoImpl.class);

            eventSpecificInformationBCSM.oNoAnswerSpecificInfo = xml.get(O_NO_ANSWER_SPECIFIC_INFO,
                    ONoAnswerSpecificInfoImpl.class);

            eventSpecificInformationBCSM.oAnswerSpecificInfo = xml.get(O_ANSWER_SPECIFIC_INFO, OAnswerSpecificInfoImpl.class);

            // eventSpecificInformationBCSM.oMidCallSpecificInfo = xml.get(O_MID_CALL_SPECIFIC_INFO,
            // OMidCallSpecificInfoImpl.class);

            eventSpecificInformationBCSM.oDisconnectSpecificInfo = xml.get(O_DISCONNECT_SPECIFIC_INFO,
                    ODisconnectSpecificInfoImpl.class);

            eventSpecificInformationBCSM.tBusySpecificInfo = xml.get(T_BUSY_SPECIFIC_INFO, TBusySpecificInfoImpl.class);

            eventSpecificInformationBCSM.tNoAnswerSpecificInfo = xml.get(T_NO_ANSWER_SPECIFIC_INFO,
                    TNoAnswerSpecificInfoImpl.class);

            eventSpecificInformationBCSM.tAnswerSpecificInfo = xml.get(T_ANSWER_SPECIFIC_INFO, TAnswerSpecificInfoImpl.class);

            // eventSpecificInformationBCSM.tMidCallSpecificInfo = xml.get(T_MID_CALL_SPECIFIC_INFO,
            // TMidCallSpecificInfoImpl.class);

            eventSpecificInformationBCSM.tDisconnectSpecificInfo = xml.get(T_DISCONNECT_SPECIFIC_INFO,
                    TDisconnectSpecificInfoImpl.class);

            // eventSpecificInformationBCSM.oTermSeizedSpecificInfo = xml.get(O_TERM_SPECIFIC_INFO,
            // OTermSeizedSpecificInfoImpl.class);

            // eventSpecificInformationBCSM.callAcceptedSpecificInfo = xml.get(CALL_ACCEPTED_SPECIFIC_INFO,
            // CallAcceptedSpecificInfoImpl.class);

            eventSpecificInformationBCSM.oAbandonSpecificInfo = xml
                    .get(O_ABONDON_SPECIFIC_INFO, OAbandonSpecificInfoImpl.class);

            // eventSpecificInformationBCSM.oChangeOfPositionSpecificInfo = xml.get(O_CHANGE_OF_POSITION_SPECIFIC_INFO,
            // OChangeOfPositionSpecificInfoImpl.class);

            // eventSpecificInformationBCSM.tChangeOfPositionSpecificInfo = xml.get(T_CHANGE_OF_POSITION_SPECIFIC_INFO,
            // TChangeOfPositionSpecificInfoImpl.class);
        }

        @Override
        public void write(EventSpecificInformationBCSMImpl eventSpecificInformationBCSM,
                javolution.xml.XMLFormat.OutputElement xml) throws XMLStreamException {
            if (eventSpecificInformationBCSM.routeSelectFailureSpecificInfo != null) {
                xml.add((RouteSelectFailureSpecificInfoImpl) eventSpecificInformationBCSM.routeSelectFailureSpecificInfo,
                        ROUTE_SELECT_FAILURE_SPECIFIC_INFO, RouteSelectFailureSpecificInfoImpl.class);
            }

            if (eventSpecificInformationBCSM.oCalledPartyBusySpecificInfo != null) {
                xml.add((OCalledPartyBusySpecificInfoImpl) eventSpecificInformationBCSM.oCalledPartyBusySpecificInfo,
                        O_CALLED_PARTY_BUSY_SPECIFIC_INFO, OCalledPartyBusySpecificInfoImpl.class);
            }

            if (eventSpecificInformationBCSM.oNoAnswerSpecificInfo != null) {
                xml.add((ONoAnswerSpecificInfoImpl) eventSpecificInformationBCSM.oNoAnswerSpecificInfo,
                        O_NO_ANSWER_SPECIFIC_INFO, ONoAnswerSpecificInfoImpl.class);
            }

            if (eventSpecificInformationBCSM.oAnswerSpecificInfo != null) {
                xml.add((OAnswerSpecificInfoImpl) eventSpecificInformationBCSM.oAnswerSpecificInfo, O_ANSWER_SPECIFIC_INFO,
                        OAnswerSpecificInfoImpl.class);
            }

            // if (eventSpecificInformationBCSM.oMidCallSpecificInfo != null) {
            // xml.add((OMidCallSpecificInfoImpl) eventSpecificInformationBCSM.oMidCallSpecificInfo,
            // O_MID_CALL_SPECIFIC_INFO, OMidCallSpecificInfoImpl.class);
            // }

            if (eventSpecificInformationBCSM.oDisconnectSpecificInfo != null) {
                xml.add((ODisconnectSpecificInfoImpl) eventSpecificInformationBCSM.oDisconnectSpecificInfo,
                        O_DISCONNECT_SPECIFIC_INFO, ODisconnectSpecificInfoImpl.class);
            }

            if (eventSpecificInformationBCSM.tBusySpecificInfo != null) {
                xml.add((TBusySpecificInfoImpl) eventSpecificInformationBCSM.tBusySpecificInfo, T_BUSY_SPECIFIC_INFO,
                        TBusySpecificInfoImpl.class);
            }

            if (eventSpecificInformationBCSM.tNoAnswerSpecificInfo != null) {
                xml.add((TNoAnswerSpecificInfoImpl) eventSpecificInformationBCSM.tNoAnswerSpecificInfo,
                        T_NO_ANSWER_SPECIFIC_INFO, TNoAnswerSpecificInfoImpl.class);
            }

            if (eventSpecificInformationBCSM.tAnswerSpecificInfo != null) {
                xml.add((TAnswerSpecificInfoImpl) eventSpecificInformationBCSM.tAnswerSpecificInfo, T_ANSWER_SPECIFIC_INFO,
                        TAnswerSpecificInfoImpl.class);
            }

            // if (eventSpecificInformationBCSM.tMidCallSpecificInfo != null) {
            // xml.add((TMidCallSpecificInfoImpl) eventSpecificInformationBCSM.tMidCallSpecificInfo,
            // T_MID_CALL_SPECIFIC_INFO, TMidCallSpecificInfoImpl.class);
            // }

            if (eventSpecificInformationBCSM.tDisconnectSpecificInfo != null) {
                xml.add((TDisconnectSpecificInfoImpl) eventSpecificInformationBCSM.tDisconnectSpecificInfo,
                        T_DISCONNECT_SPECIFIC_INFO, TDisconnectSpecificInfoImpl.class);
            }

            // if (eventSpecificInformationBCSM.oTermSeizedSpecificInfo != null) {
            // xml.add((OTermSeizedSpecificInfoImpl) eventSpecificInformationBCSM.oTermSeizedSpecificInfo,
            // O_TERM_SPECIFIC_INFO, OTermSeizedSpecificInfoImpl.class);
            // }

            // if (eventSpecificInformationBCSM.callAcceptedSpecificInfo != null) {
            // xml.add((CallAcceptedSpecificInfoImpl) eventSpecificInformationBCSM.callAcceptedSpecificInfo,
            // CALL_ACCEPTED_SPECIFIC_INFO, CallAcceptedSpecificInfoImpl.class);
            // }

            if (eventSpecificInformationBCSM.oAbandonSpecificInfo != null) {
                xml.add((OAbandonSpecificInfoImpl) eventSpecificInformationBCSM.oAbandonSpecificInfo, O_ABONDON_SPECIFIC_INFO,
                        OAbandonSpecificInfoImpl.class);
            }

            // if (eventSpecificInformationBCSM.oChangeOfPositionSpecificInfo != null) {
            // xml.add((OChangeOfPositionSpecificInfo) eventSpecificInformationBCSM.oChangeOfPositionSpecificInfo,
            // O_CHANGE_OF_POSITION_SPECIFIC_INFO, OChangeOfPositionSpecificInfoImpl.class);
            // }

            // if (eventSpecificInformationBCSM.tChangeOfPositionSpecificInfo != null) {
            // xml.add((TChangeOfPositionSpecificInfoImpl) eventSpecificInformationBCSM.tChangeOfPositionSpecificInfo,
            // T_CHANGE_OF_POSITION_SPECIFIC_INFO, TChangeOfPositionSpecificInfoImpl.class);
            // }

        }
    };
}
