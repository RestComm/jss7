/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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

package org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.cap.EsiBcsm.RouteSelectFailureSpecificInfoImpl;
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
			throw new CAPParsingComponentException("MAPParsingComponentException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
					CAPParsingComponentExceptionReason.MistypedParameter);
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
			throw new CAPParsingComponentException("MAPParsingComponentException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
					CAPParsingComponentExceptionReason.MistypedParameter);
		}
	}

	private void _decode(AsnInputStream ais, int length) throws CAPParsingComponentException, MAPParsingComponentException, IOException, AsnException {

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
				ais.advanceElementData(length);
				break;
			case _ID_oNoAnswerSpecificInfo:
				ais.advanceElementData(length);
				break;
			case _ID_oAnswerSpecificInfo:
				ais.advanceElementData(length);
				break;
			case _ID_oMidCallSpecificInfo:
				ais.advanceElementData(length);
				break;
			case _ID_oDisconnectSpecificInfo:
				ais.advanceElementData(length);
				break;
			case _ID_tBusySpecificInfo:
				ais.advanceElementData(length);
				break;
			case _ID_tNoAnswerSpecificInfo:
				ais.advanceElementData(length);
				break;
			case _ID_tAnswerSpecificInfo:
				ais.advanceElementData(length);
				break;
			case _ID_tMidCallSpecificInfo:
				ais.advanceElementData(length);
				break;
			case _ID_tDisconnectSpecificInfo:
				ais.advanceElementData(length);
				break;
			case _ID_oTermSeizedSpecificInfo:
				ais.advanceElementData(length);
				break;
			case _ID_callAcceptedSpecificInfo:
				ais.advanceElementData(length);
				break;
			case _ID_oAbandonSpecificInfo:
				ais.advanceElementData(length);
				break;
			case _ID_oChangeOfPositionSpecificInfo:
				ais.advanceElementData(length);
				break;
			case _ID_tChangeOfPositionSpecificInfo:
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
		// TODO Auto-generated method stub

	}

	@Override
	public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag) throws CAPException {
		// TODO Auto-generated method stub

	}

	@Override
	public void encodeData(AsnOutputStream asnOs) throws CAPException {
		// TODO Auto-generated method stub

	}
}
