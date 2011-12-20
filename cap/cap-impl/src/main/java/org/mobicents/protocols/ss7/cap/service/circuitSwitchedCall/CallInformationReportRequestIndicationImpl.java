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

package org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall;

import java.io.IOException;
import java.util.ArrayList;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.cap.api.CAPException;
import org.mobicents.protocols.ss7.cap.api.CAPParsingComponentException;
import org.mobicents.protocols.ss7.cap.api.CAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.cap.api.primitives.CAPExtensions;
import org.mobicents.protocols.ss7.cap.api.primitives.ReceivingSideID;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.CallInformationReportRequestIndication;
import org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive.RequestedInformation;
import org.mobicents.protocols.ss7.cap.primitives.ReceivingSideIDImpl;
import org.mobicents.protocols.ss7.cap.service.circuitSwitchedCall.primitive.RequestedInformationImpl;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class CallInformationReportRequestIndicationImpl extends CircuitSwitchedCallMessageImpl implements CallInformationReportRequestIndication {

	public static final int _ID_requestedInformationList = 0;
	public static final int _ID_extensions = 2;
	public static final int _ID_legID = 3;

	public static final String _PrimitiveName = "CallInformationReportRequestIndication";

	private ArrayList<RequestedInformation> requestedInformationList;
	private CAPExtensions extensions;
	private ReceivingSideID legID;

	
	@Override
	public ArrayList<RequestedInformation> getRequestedInformationList() {
		return requestedInformationList;
	}

	@Override
	public CAPExtensions getExtensions() {
		return extensions;
	}

	@Override
	public ReceivingSideID getLegID() {
		return legID;
	}

	
	@Override
	public int getTag() throws CAPException {
		return Tag.SEQUENCE;
	}

	@Override
	public int getTagClass() {
		return Tag.CLASS_UNIVERSAL;
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
		}
	}

	private void _decode(AsnInputStream ansIS, int length) throws CAPParsingComponentException, IOException, AsnException {
		
		this.requestedInformationList = null;
		this.extensions = null;
		this.legID = null;  // TODO: DEFAULT receivingSideID:leg2 

		AsnInputStream ais = ansIS.readSequenceStreamData(length);
		while (true) {
			if (ais.available() == 0)
				break;

			int tag = ais.readTag();
			
			if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
				switch (tag) {
				case _ID_requestedInformationList:
					this.requestedInformationList = new ArrayList<RequestedInformation>();
					AsnInputStream ais2 = ais.readSequenceStream();
					while (true) {
						if (ais2.available() == 0)
							break;

						int tag2 = ais2.readTag();
						if (tag2 != Tag.SEQUENCE || ais2.getTagClass() != Tag.CLASS_UNIVERSAL || ais2.isTagPrimitive())
							throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName
									+ ": bad RequestedInformation tag or tagClass or RequestedInformation is primitive ",
									CAPParsingComponentExceptionReason.MistypedParameter);

						RequestedInformationImpl el = new RequestedInformationImpl();
						el.decodeAll(ais2);
						this.requestedInformationList.add(el);
					}
					break;
				case _ID_extensions:
					ais.advanceElement(); // TODO: implement it
					break;
				case _ID_legID:
					ais2 = ais.readSequenceStream();
					ais2.readTag();
					this.legID = new ReceivingSideIDImpl();
					((ReceivingSideIDImpl) this.legID).decodeAll(ais2);
					break;

				default:
					ais.advanceElement();
					break;
				}
			} else {
				ais.advanceElement();
			}
		}

		if (this.requestedInformationList == null)
			throw new CAPParsingComponentException("Error while decoding " + _PrimitiveName + ": requestedInformationList is mandatory but not found ",
					CAPParsingComponentExceptionReason.MistypedParameter);
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
