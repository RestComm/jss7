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

package org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.CUGInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.EMLPPInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtCallBarInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtForwInfo;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtSSData;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberManagement.ExtSSInfo;
import org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.CUGInfoImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.EMLPPInfoImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.ExtCallBarInfoImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.ExtForwInfoImpl;
import org.mobicents.protocols.ss7.map.service.mobility.subscriberManagement.ExtSSDataImpl;

/**
 * @author daniel bichara
 * 
 */
public class ExtSSInfoImpl implements ExtSSInfo, MAPAsnPrimitive {

	public static final String _PrimitiveName = "ExtSSInfo";

	private static final int _TAG_forwardingInfo = 0;
	private static final int _TAG_callBarringInfo = 1;
	private static final int _TAG_cugInfo = 2;
	private static final int _TAG_ssData = 3;
	private static final int _TAG_emlppInfo = 4;

	private ExtForwInfo forwardingInfo = null;
	private ExtCallBarInfo callBarringInfo = null;
	private CUGInfo cugInfo = null;
	private ExtSSData ssData = null;
	private EMLPPInfo emlppInfo = null;

	public ExtSSInfoImpl() {
		
	}

	/**
	 * 
	 */
	public ExtSSInfoImpl(ExtForwInfo forwardingInfo, ExtCallBarInfo callBarringInfo,
			CUGInfo cugInfo, ExtSSData ssData, EMLPPInfo emlppInfo) {

		this.forwardingInfo = forwardingInfo;
		this.callBarringInfo = callBarringInfo;
		this.cugInfo = cugInfo;
		this.ssData = ssData;
		this.emlppInfo = emlppInfo;
	}

	public ExtForwInfo getForwardingInfo() {
		return this.forwardingInfo;
	}

	public ExtCallBarInfo getCallBarringInfo() {
		return this.callBarringInfo;
	}

	public CUGInfo getCugInfo() {
		return this.cugInfo;
	}

	public ExtSSData getSsData() {
		return this.ssData;
	}

	public EMLPPInfo getEmlppInfo() {
		return this.emlppInfo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#getTag()
	 */
	public int getTag() throws MAPException {
		return Tag.SEQUENCE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#getTagClass()
	 */
	public int getTagClass() {
		return Tag.CLASS_UNIVERSAL;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#getIsPrimitive
	 * ()
	 */
	public boolean getIsPrimitive() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#decodeAll(
	 * org.mobicents.protocols.asn.AsnInputStream)
	 */
	public void decodeAll(AsnInputStream ansIS) throws MAPParsingComponentException {
		try {
			// It is a CHOICE, ignore length
			this._decode(ansIS, 0);
		} catch (IOException e) {
			throw new MAPParsingComponentException("IOException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		} catch (AsnException e) {
			throw new MAPParsingComponentException("AsnException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#decodeData
	 * (org.mobicents.protocols.asn.AsnInputStream, int)
	 */
	public void decodeData(AsnInputStream ansIS, int length) throws MAPParsingComponentException {
		try {
			// It is a CHOICE, ignore length
			this._decode(ansIS, 0);
		} catch (IOException e) {
			throw new MAPParsingComponentException("IOException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		} catch (AsnException e) {
			throw new MAPParsingComponentException("AsnException when decoding " + _PrimitiveName + ": " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		}
	}

	private void _decode(AsnInputStream ais, int l) throws MAPParsingComponentException, IOException, AsnException {
		this.forwardingInfo = null;
		this.callBarringInfo = null;
		this.cugInfo = null;
		this.ssData = null;
		this.emlppInfo = null;

		int tag = ais.readTag();
		int length = ais.readLength();

		switch (tag) {
		case _TAG_forwardingInfo:
			if (!ais.isTagPrimitive())
				throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName + ".forwardingInfo: bad tag or tag class or not primitive", MAPParsingComponentExceptionReason.MistypedParameter);
			this.forwardingInfo = new ExtForwInfoImpl();
			((ExtForwInfoImpl) this.forwardingInfo).decodeData(ais, length);
			return;
		case _TAG_callBarringInfo:
			if (!ais.isTagPrimitive())
				throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName + ".callBarringInfo: bad tag or tag class or not primitive", MAPParsingComponentExceptionReason.MistypedParameter);
			this.callBarringInfo = new ExtCallBarInfoImpl();
			((ExtCallBarInfoImpl) this.callBarringInfo).decodeData(ais, length);
			return;
		case _TAG_cugInfo:
			if (!ais.isTagPrimitive())
				throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName + ".cugInfo: bad tag or tag class or not primitive", MAPParsingComponentExceptionReason.MistypedParameter);
			this.cugInfo = new CUGInfoImpl();
			((CUGInfoImpl) this.cugInfo).decodeData(ais, length);
			return;
		case _TAG_ssData:
			if (!ais.isTagPrimitive())
				throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName + ".ssData: bad tag or tag class or not primitive", MAPParsingComponentExceptionReason.MistypedParameter);
			this.ssData = new ExtSSDataImpl();
			((ExtSSDataImpl) this.ssData).decodeData(ais, length);
			return;
		case _TAG_emlppInfo:
			if (!ais.isTagPrimitive())
				throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName + ".emlppInfo: bad tag or tag class or not primitive", MAPParsingComponentExceptionReason.MistypedParameter);
			this.emlppInfo = new EMLPPInfoImpl();
			((EMLPPInfoImpl) this.emlppInfo).decodeData(ais, length);
			return;
		}

		// Failed.
		throw new MAPParsingComponentException("Error while decoding " + _PrimitiveName + ": one sequence is required.", MAPParsingComponentExceptionReason.MistypedParameter);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#encodeAll(
	 * org.mobicents.protocols.asn.AsnOutputStream)
	 */
	public void encodeAll(AsnOutputStream asnOs) throws MAPException {
		this.encodeAll(asnOs, this.getTagClass(), this.getTag());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#encodeAll(
	 * org.mobicents.protocols.asn.AsnOutputStream, int, int)
	 */
	public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag) throws MAPException {
		// It is a CHOICE, ignore tagClass and tag
		this.encodeData(asnOs);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mobicents.protocols.ss7.map.primitives.MAPAsnPrimitive#encodeData
	 * (org.mobicents.protocols.asn.AsnOutputStream)
	 */
	public void encodeData(AsnOutputStream asnOs) throws MAPException {

		if (this.forwardingInfo == null && this.callBarringInfo == null  && this.cugInfo == null
				 && this.ssData == null && this.emlppInfo == null)
			throw new MAPException("Error while encoding " + _PrimitiveName + ": one sequence is required.");

		if (this.forwardingInfo != null) {
			((ExtForwInfoImpl) this.forwardingInfo).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_forwardingInfo);
			return;
		}
			
		if (this.callBarringInfo != null) {
			((ExtCallBarInfoImpl) this.callBarringInfo).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_callBarringInfo);
			return;
		}

		if (this.cugInfo != null) {
			((CUGInfoImpl) this.cugInfo).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_cugInfo);
			return;
		}

		if (this.ssData != null) {
			((ExtSSDataImpl) this.ssData).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_ssData);
			return;
		}

		if (this.emlppInfo != null) {
			((EMLPPInfoImpl) this.emlppInfo).encodeAll(asnOs, Tag.CLASS_CONTEXT_SPECIFIC, _TAG_emlppInfo);
			return;
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(_PrimitiveName + " [");

		if (this.forwardingInfo != null) {
			sb.append("forwardingInfo=");
			sb.append(this.forwardingInfo.toString());
			sb.append(", ");
		}

		if (this.callBarringInfo != null) {
			sb.append("callBarringInfo=");
			sb.append(this.callBarringInfo.toString());
			sb.append(", ");
		}

		if (this.cugInfo != null) {
			sb.append("cugInfo=");
			sb.append(this.cugInfo.toString());
			sb.append(", ");
		}

		if (this.ssData != null) {
			sb.append("ssData=");
			sb.append(this.ssData.toString());
			sb.append(", ");
		}

		if (this.emlppInfo != null) {
			sb.append("emlppInfo=");
			sb.append(this.emlppInfo.toString());
		}

		sb.append("]");

		return sb.toString();
	}

}
